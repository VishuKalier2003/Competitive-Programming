import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StaticMin {

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
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n, q, nums, queries);
    }

    public static void solve(final int n, final int q, final int nums[], final List<int[]> queries) {
        ParseTable parseTable = new ParseTable(nums);
        final StringBuilder output = new StringBuilder();
        for(int query[] : queries)
            output.append(parseTable.minQuery(query[0]-1, query[1]-1)).append("\n");        // 1based indexing
        System.out.print(output);
    }

    public static class ParseTable {
        public int table[][];
        public int logTable[];

        public ParseTable(int nums[]) {
            int n = nums.length;
            int maxLog = 32 - Integer.numberOfLeadingZeros(n);   // finding the floor(log(n))
            this.table = new int[maxLog][n];
            this.logTable = new int[n+1];
            this.logTable[1] = 0;       // Computing log(i) for each value uptil n for faster computation
            for(int i = 2; i <= n; i++)
                this.logTable[i] = this.logTable[i/2]+1;
            // Just copy the array into the first row itself
            System.arraycopy(nums, 0, this.table[0], 0, n);
            for(int i = 1; i < maxLog; i++) {
                // For each i, row representing 2^i
                int span = 1 << (i-1);      // Imp- Segment span of previous row
                // While the segment does not jumps out of the table
                for(int j = 0; j + (1 << i) <= n; j++)
                    // Take the operation of previous row jth column and previous row, j + 2^(i-1) th column (l and r of the segment)
                    this.table[i][j] = Math.min(this.table[i-1][j], this.table[i-1][j + span]);
            }
        }

        // Imp- Takes O(1) time
        public int minQuery(int left, int right) {      // For non-overlapping Idempotent functions
            int length = right - left + 1;
            int p = this.logTable[length];
            return Math.min(this.table[p][left], this.table[p][right - (1 << p) + 1]);
        }

        // Imp- Takes O(log n) time
        public int minLogQuery(int left, int right) {   // For overlapping associative functions
            int min = Integer.MAX_VALUE;
            while(left <= right) {      // Reduce overlapping function to non-overlapping intervals in O(log n) time
                int p = this.logTable[right - left + 1];
                min = Math.min(min, this.table[p][left]);
                left += (1 << p);
            }
            return min;
        }
    }
}
