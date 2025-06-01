// Question - https://cses.fi/problemset/task/1633

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class P1DiceCombinations {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        System.out.print(solve(fast.nextInt()));
    }

    public static final int MOD = 1_000_000_007;
    public static int dp[];

    // Imp- When writing bottom up, we write opposite of recursion
    public static int solve(int num) {
        dp = new int[num + 1];
        dp[0] = 1; // Initialise
        for (int i = 1; i <= num; i++) {
            int ways = 0;
            for (int face = 1; face <= 6; face++) {
                if (i - face >= 0) // Recursive condition check
                    ways += dp[i - face];
                if (ways >= MOD) // Imp- better than MOD (performs subtraction instead of division)
                    ways -= MOD;
            }
            dp[i] = ways; // Assign the dp
        }
        return dp[num];
    }

    // Imp Memonization gave TLE (on three cases)
    public static int helper(int num) {
        if (num == 0)
            return 1;
        if (num < 0)
            return 0;
        if (dp[num] != -1)
            return dp[num];
        int count = 0;
        for (int diceFace = 1; diceFace <= 6; diceFace++)
            count = (count + helper(num - diceFace)) % MOD;
        return dp[num] = count;
    }
}
