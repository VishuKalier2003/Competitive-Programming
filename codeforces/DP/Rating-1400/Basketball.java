import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Basketball {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        long row1[] = new long[n], row2[] = new long[n];
        final Long dp[][] = new Long[n+1][3];
        for(int i = 0; i < n; i++)
            row1[i] = fast.nextLong();
        for(int i = 0; i < n; i++)
            row2[i] = fast.nextLong();
        System.out.print(solve(n, row1, row2, dp));
    }

    public static long solve(final int n, final long nums1[], final long nums2[], final Long dp[][]) {
        // Imp- using memonization to solve
        return helper(0, 0, n, nums1, nums2, dp);
    }

    public static long helper(int i, int prev, final int n, final long nums1[], final long nums2[], final Long dp[][]) {
        if(i == n)      // When the rows are traversed (base case)
            return 0l;
        if(dp[i][prev] != null)     // If a state is precomputed
            return dp[i][prev];
        long leave = helper(i+1, prev, n, nums1, nums2, dp);        // Leave the ball
        long take = 0;
        if(prev == 0)       // Imp- If we have to choose the first element (we can choose from any row), need to be the max of both
            take = Math.max(nums1[i] + helper(i+1, 2, n, nums1, nums2, dp), nums2[i] + helper(i+1, 1, n, nums1, nums2, dp));
        if(prev == 1)       // When taking from first row, next has to be second
            take = Math.max(take, nums1[i] + helper(i+1, 2, n, nums1, nums2, dp));
        if(prev == 2)       // When taking from second row, next has to be first
            take = Math.max(take, nums2[i] + helper(i+1, 1, n, nums1, nums2, dp));
        return dp[i][prev] = Math.max(take, leave);     // Max state evaluation and storing in the dp table
    }
}
