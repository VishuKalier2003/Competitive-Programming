import java.util.*;

public class CoinProblem {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int t = sc.nextInt();
            long nums[] = new long[t];
            for (int i = 0; i < t; i++)
                nums[i] = sc.nextLong();
            int denomination[] = new int[]{1, 3, 6, 10, 15};
            // Precompute DP results for all values up to the max `n` in input
            long maxN = Arrays.stream(nums).max().getAsLong();
            long[] dp = computeMinCoins((int) Math.min(maxN, 1000000), denomination);     // Compute up to 10⁶
            // Process each test case efficiently
            Arrays.stream(nums).map(n -> {
                if (n <= 1_000_000) return dp[(int) n]; // Use precomputed DP
                return greedySolve(n, denomination); // Greedy approach for large n
            }).forEach(System.out::println);
        }
    }

    public static long[] computeMinCoins(int maxN, int[] denomination) {
        long[] dp = new long[maxN + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; // Base case: 0 coins for amount 0
        for (int i = 1; i <= maxN; i++)
            for (int coin : denomination)
                if (i >= coin)
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
        return dp;
    }

    public static long greedySolve(long amount, int[] denomination) {
        long count = 0;
        for (int i = denomination.length - 1; i >= 0; i--) { // Start from largest coin
            count += amount / denomination[i];  // Use as many large coins as possible
            amount %= denomination[i];  // Reduce remaining amount
        }
        return count;
    }
}
