import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Constanze {
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
        System.out.print(solve(fast.next()));
    }

    public static final int MOD = 1_000_000_007;
    public static boolean defect = false;

    public static int solve(final String s) {
        final int n = s.length();
        final int dp[] = new int[n+1];      // Dp array for memonization
        Arrays.fill(dp, -1);
        return helper(0, n, s, dp);
    }

    public static int helper(int i, final int n, final String s, final int dp[]) {
        if(i >= n)      // Imp- base case when the entire string is traversed, we have found a valid way
            return 1;
        if(s.charAt(i) == 'm' || s.charAt(i) == 'w') {      // When the string cannot be inscribed
            defect = true;      // using a global variable
            return 0;
        }
        if(dp[i] != -1)     // Memonization case
            return dp[i];
        int ways = 0;
        if(!defect) {       // If the string is possible to inscribe
            ways = (ways + helper(i+1, n, s, dp)) % MOD;        // Normal traversal
            if(i > 0 && s.charAt(i) == 'n' && s.charAt(i-1) == 'n')
                ways = (ways + helper(i+2, n, s, dp)) % MOD;    // When found two consecutive n
            if(i > 0 && s.charAt(i) == 'u' && s.charAt(i-1) == 'u')
                ways = (ways + helper(i+2, n, s, dp)) % MOD;    // When found two consecutive u
        }
        return dp[i] = ways % MOD;
    }
}
