import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class kTree {
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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt(), d = fast.nextInt();
        // IMP- dp table stores the number of ways to reach nth number (sum) by either choosing a branch >= d or not
        final int dp[][] = new int[n+1][2];
        for(int row[] : dp)
            Arrays.fill(row, -1);
        System.out.print(countWays(n, k, d, dp));
    }

    public static final int MOD = 1_000_000_007;        // Modular arithmetic

    public static int countWays(final int n, final int k, final int d, final int dp[][]) {
        return memonize(k, d, n, 0, dp);
    }

    public static int helper(final int k, final int d, int sum, boolean branch) {
        if(sum == 0)        // Checking if required sum is reached, and at least one of the edge is >= d
            return branch ? 1 : 0;
        if(sum < 0)
            return 0;
        int ways = 0;
        for(int i = 1; i <= k; i++)     // For each k
            ways = (ways + (helper(k, d, sum-i, branch || i >= d))) % MOD;
        return ways;
    }

    public static int memonize(final int k, final int d, int sum, int usedEdge, int dp[][]) {
        if(sum == 0)        // Checking if required sum is reached, and at least one of the edge is >= d
            return usedEdge == 1 ? 1 : 0;
        if(sum < 0)
            return 0;
        if(dp[sum][usedEdge] != -1)     // If state is precomputed
            return dp[sum][usedEdge];
        int ways = 0;
        for(int i = 1; i <= k; i++) {       // IMP- Loop for each k
            int usedBigEdge = usedEdge;
            if(i >= d)
                usedBigEdge = 1;        // update used edge if current k >= d
            ways = (ways + (memonize(k, d, sum-i, usedBigEdge, dp))) % MOD;
        }
        return dp[sum][usedEdge] = ways;
    }
}
