// https://cses.fi/problemset/task/1634

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class P2MinimizeCoins {
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
        final int n = fast.nextInt(), money = fast.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(n, money, nums));
    }

    public static int dp[];

    public static int solve(final int n, int money, final int nums[]) {
        dp = new int[money + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; // No way to create 0 money
        for (int i = 1; i <= money; i++) {
            for (int num : nums)
                // Check the sentinel Integer.MAX_VALUE so that you don't get -1 when adding 1
                // to Integer.MAX_VALUE
                if (i - num >= 0 && dp[i - num] != Integer.MAX_VALUE)
                    dp[i] = Math.min(dp[i], 1 + dp[i - num]);
        }
        // Imp- Since the range is till 1_000_000 so the ways can be impossible only
        // when the count is more than this
        if (dp[money] > 1_000_000)
            return -1;
        return dp[money];
    }

    // Imp- Memonization gave TLE here
    public static int helper(int num, int coins[]) {
        if (num == 0)
            return 0;
        if (num < 0)
            return 1_000_000;
        if (dp[num] != -1)
            return dp[num];
        int minCoins = Integer.MAX_VALUE;
        for (int coin : coins)
            minCoins = Math.min(minCoins, 1 + helper(num - coin, coins));
        return dp[num] = minCoins;
    }
}
