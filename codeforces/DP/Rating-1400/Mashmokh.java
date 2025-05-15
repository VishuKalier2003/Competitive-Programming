import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Mashmokh {
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
        System.out.print(solve(fast.nextInt(), fast.nextInt()));
    }

    public static final int MOD = 1_000_000_007;

    public static int solve(final int n, final int k) {
        final int dp[][] = new int[n+1][k+1];       // Initialize dp to n+1, k+1
        for(int row[] : dp)
            Arrays.fill(row, -1);
        return helper(1, k, n, dp);
    }

    public static int helper(int i, int k, final int n, final int dp[][]) {
        if(k == 0)      // Imp- When a valid way is found at the end (return 1)
            return 1;
        if(dp[i][k] != -1)
            return dp[i][k];
        int ways = 0;
        for(int num = i; num <= n; num += i)      // Imp- Iterate only with multiples of i (to not get TLE)
            // Counting ways with the modulus operation
            ways = (ways + helper(num, k-1, n, dp)) % MOD;      // Add all the valid ways recursively
        return dp[i][k] = ways % MOD;
    }
}
