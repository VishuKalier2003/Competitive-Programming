package DP.Topics;

import java.util.Arrays;
import java.util.Scanner;

public class LIS {      // IMP- Longest Increasing Subsequence
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final int n = sc.nextInt();
        final int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = sc.nextInt();
        System.out.println("----- Pure Recursion -----");
        measurePerformance(() -> System.out.println("LIS Length (Recursion): " + helper(0, n, -1, nums)));
        System.out.println("\n----- Memoization -----");
        // IMP- dp[i][j] stores the max LIS size of array till ith index and jth index element is the last chosen
        final Integer memo[][] = new Integer[n+1][n+1];     // The Dp table is of size n+1 and n+1
        measurePerformance(() -> System.out.println("LIS Length (Memoization): " + memonize(0, n, -1, nums, memo)));
        System.out.println("\n----- Tabulation -----");
        measurePerformance(() -> System.out.println("LIS Length (Tabulation): " + tabulate(n, nums)));
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


    public static int helper(int i, final int n, int prev, final int nums[]) {
        if(i == n)      // Base case, when we have traversed the entire array
            return 0;
        int notTake = helper(i+1, n, prev, nums);       // Either we do not take the element
        int take = 0;
        // If previous element is not yet chosen, or is smaller than current element
        if(prev == -1 || nums[i] > nums[prev])
            take = 1 + helper(i+1, n, i, nums);     // Take it and update the prev by i here
        return Math.max(take, notTake);     // Take the max size of the element
    }

    public static int memonize(int i, final int n, int prev, final int nums[], final Integer dp[][]) {
        if(i == n)      // Base case, when we have traversed the entire array
            return 0;
        // We check the state prev+1, because in initialization we take prev as -1, so we do +1 here as to not get caught by exception
        if(dp[i][prev+1] != null)
            return dp[i][prev+1];
        int notTake = memonize(i+1, n, prev, nums, dp);     // Either we do not take the element
        int take = 0;
        // If previous element is not yet chosen, or is smaller than current element
        if(prev == -1 || nums[i] > nums[prev])
            take = 1 + memonize(i+1, n, i, nums, dp);       // Take it and update the prev by i
        return dp[i][prev+1] = Math.max(take, notTake);
    }

    public static int tabulate(final int n, final int nums[]) {
        int maxLIS = 0;
        int dp[] = new int[n];
        // Each element itself can be a Longest Increasing Subsequence
        Arrays.fill(dp, 1);
        for(int i = 0; i < n; i++) {        // For each element
            for(int j = 0; j < i; j++)
                // We check if any previous element is smaller than the current
                if(nums[j] < nums[i])
                    dp[i] = Math.max(dp[i], 1+dp[j]);   // IMP- Update the dp by max of current and 1 + previous smaller element found
            maxLIS = Math.max(maxLIS, dp[i]);       // For each i, update the LIS variable
        }
        return maxLIS;
    }
}
