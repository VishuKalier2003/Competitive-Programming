import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DynamicRangeSum {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public static void main(String[] args) {
        FastReader fastReader = new FastReader();
        final int n = fastReader.nextInt(), k = fastReader.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fastReader.nextInt();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < k; i++)
            queries.add(new int[]{fastReader.nextInt(), fastReader.nextInt(), fastReader.nextInt()});
        System.out.print(solve(n, nums, queries));
    }

    public static StringBuilder solve(final int n, final int nums[], final List<int[]> queries) {
        final StringBuilder ans = new StringBuilder();
        FenwickTree fenwickTree = new FenwickTree(n);       // Creating fenwick tree object
        for(int i = 1; i <= n; i++)
            fenwickTree.updateQuery(i, nums[i-1]);
        for(int query[] : queries) {
            if(query[0] == 1) {
                long delta = query[2] - nums[query[1]-1];        // Imp- We always pass the delta in update (the change that happened not the value)
                fenwickTree.updateQuery(query[1], delta);
                nums[query[1]-1] = query[2];        // Imp- update the nums as well
            }
            else
                ans.append(fenwickTree.rangeSumQuery(query[1], query[2])).append("\n");     // Perform range sum on [l,r]
        }
        return ans;
    }

    public static class FenwickTree {
        public int size;
        public long tree[];

        public FenwickTree(int n) {
            this.size = n;
            this.tree = new long[n+1];       // Imp- Create fenwick tree of size n+1 for 1 based indexing and accomodate size n
        }

        public void updateQuery(int index, long value) {
            while(index <= size) {
                tree[index] += value;
                index += lowBit(index);     // shift higher
            }
        }

        public long sumQuery(int index) {
            long sum = 0;
            while(index > 0) {
                sum += tree[index];
                index -= lowBit(index);     // shift lower
            }
            return sum;
        }

        public long rangeSumQuery(int left, int right) {return sumQuery(right) - sumQuery(left-1);}

        public int lowBit(int index) {return index & -index;}       // Imp- extracting rightmost set bit
    }
}
