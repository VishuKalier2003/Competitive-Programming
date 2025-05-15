package DP.Topics;

import java.util.Arrays;
import java.util.Scanner;

public class ASS {      // IMP- Alternating Subsequence Sum
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final int n = sc.nextInt();
        final int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = sc.nextInt();
        System.out.println("----- Pure Recursion -----");
        // First element we will always tend to take positive, hence started with false
        measurePerformance(() -> System.out.println("ASS Length (Recursion): " + helper(0, n, nums, true)));
        System.out.println("\n----- Memoization -----");
        // IMP- dp[i][j] stores the max AAS when the array has reached i, and the last chosen element was either added (0) or subtracted (1)
        final int memo[][] = new int[n][2];     // The Dp table is of size n and 2
        for(int row[] : memo)
            Arrays.fill(row, -1);
        measurePerformance(() -> System.out.println("LCS Length (Memoization): " + memonize(0, n, nums, true, memo)));
        System.out.println("\n----- Tabulation -----");
        measurePerformance(() -> System.out.println("LCS Length (Tabulation): " + tabulate(n, nums)));
        sc.close();
    }

    // Measure runtime and memory usage
    public static void measurePerformance(Runnable function) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Suggest GC to minimize prior memory effects
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        function.run();
        long endTime = System.nanoTime();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        double timeInMillis = (endTime - startTime) / 1_000_000.0; // milliseconds
        System.out.printf("Time taken: %.6f ms\n", timeInMillis);
        System.out.println("Memory used: " + (endMemory - startMemory) + " bytes");
    }

    public static int helper(int i, final int n, final int nums[], boolean currentPositive) {
        if(i == n)      // Base case, when the array is completely traversed
            return 0;
        // When taking current element, we check that whether the last chosen element was positive or negative, and hence according reverse the number sign and flag while adding
        int takenSum = (currentPositive ? nums[i] : -nums[i]) + helper(i+1, n, nums, !currentPositive);     // Flag reversed
        int notTakenSum = helper(i+1, n, nums, currentPositive);        // Otherwise skip
        return Math.max(takenSum, notTakenSum);     // Take the max sum
    }

    public static int memonize(int i, final int n, final int nums[], boolean currentPositive, int dp[][]) {
        if(i == n)
            return 0;
        int stateIndex = currentPositive ? 0 : 1;
        if(dp[i][stateIndex] != -1)
            return dp[i][stateIndex];
        // When taking current element, we check that whether the last chosen element was positive or negative, and hence according reverse the number sign and flag while adding
        int takenSum = (currentPositive ? nums[i] : -nums[i]) + helper(i+1, n, nums, !currentPositive);     // Flag reversed
        int notTakenSum = memonize(i+1, n, nums, currentPositive, dp);      // Otherwise skip
        return dp[i][stateIndex] = Math.max(takenSum, notTakenSum);     // Take the max sum
    }

    public static int tabulate(final int n, final int nums[]) {
        int dp[][] = new int[n][2];
        // Base cases
        dp[0][0] = nums[0];
        dp[0][1] = Integer.MIN_VALUE;
        for(int i = 1; i < n; i++) {
            // When positive, can remain unchanged, can add (need to add to negative, since when adding positive last number should be negative) or can start afresh
            dp[i][0] = Math.max(Math.max(dp[i-1][0], dp[i-1][1]+nums[i]), nums[i]);
            // When negative, can leave it unchanged or subtract (add negative that means last number was positive)
            dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0]-nums[i]);
        }
        return dp[n-1][0];      // Return the last positive answer
    }
}
