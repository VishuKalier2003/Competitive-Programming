import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class DynamicMin {
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

    public static StringBuilder solve(final int n, final int nums[], List<int[]> queries) {
        final StringBuilder ans = new StringBuilder();
        SegmentTree segmentTree = new SegmentTree(n);
        segmentTree.buildQuery(1, 0, n-1, nums);
        for(int query[] : queries) {
            if(query[0] == 1)
                segmentTree.updateQuery(1, 0, n-1, query[1]-1, query[2]);
            else
                ans.append(segmentTree.rangeMinQuery(1, 0, n-1, query[1]-1, query[2]-1)).append("\n");
        }
        return ans;
    }

    public static class SegmentTree {
        public int tree[];

        public SegmentTree(int n) {
            this.tree = new int[4*n];
            Arrays.fill(tree, Integer.MAX_VALUE);
        }

        public void buildQuery(int index, int left, int right, int nums[]) {
            if(left == right) {
                this.tree[index] = nums[left];
                return;
            }
            int mid = left + ((right - left) >> 1);
            buildQuery(2*index, left, mid, nums);
            buildQuery(2*index+1, mid+1, right, nums);
            this.tree[index] = Math.min(this.tree[2*index], this.tree[2*index+1]);
        }

        public void updateQuery(int index, int left, int right, int pos, int value) {
            if(left == right) {
                this.tree[index] = value;
                return;
            }
            int mid = left + ((right - left) >> 1);
            if(pos <= mid)
                updateQuery(2*index, left, mid, pos, value);
            else
                updateQuery(2*index+1, mid+1, right, pos, value);
            this.tree[index] = Math.min(this.tree[2*index], this.tree[2*index+1]);
        }

        public int rangeMinQuery(int index, int left, int right, int queryLeft, int queryRight) {
            if(queryLeft > right || queryRight < left)
                return Integer.MAX_VALUE;
            if(queryLeft <= left && queryRight >= right)
                return this.tree[index];
            int mid = left + ((right - left) >> 1);
            return Math.min(rangeMinQuery(2*index, left, mid, queryLeft, queryRight), rangeMinQuery(2*index+1, mid+1, right, queryLeft, queryRight));
        }
    }
}
