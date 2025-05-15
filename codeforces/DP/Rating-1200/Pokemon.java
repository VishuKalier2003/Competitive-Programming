import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Pokemon {
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
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), q = fast.nextInt();
            int nums[] = new int[n];
            for(int i = q; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(createPokemonArmy(n, nums));
        }
        System.out.print(builder);
    }

    public static String createPokemonArmy(final int n, final int nums[]) {
        // IMP- dp matrix to store that ith dimension the max sum and the jth dimension whether the last number taken is +ve or -ve
        long dp[][] = new long[n][2];
        dp[0][0] = nums[0];             // Base cases
        dp[0][1] = Integer.MIN_VALUE;
        for(int i = 1; i < n; i++) {
            // When positive, can remain unchanged, can add (need to add to negative, since when adding positive last number should be negative) or can start afresh
            dp[i][0] = Math.max(Math.max(dp[i-1][0], dp[i-1][1]+nums[i]), nums[i]);
            // When negative, can leave it unchanged or subtract (add negative that means last number was positive)
            dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0]-nums[i]);
        }
        return dp[n-1][0]+"\n";     // Return the last positive answer
    }
}
