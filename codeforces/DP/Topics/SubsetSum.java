package DP.Topics;
import java.util.Scanner;

public class SubsetSum {        // Subset Sum exists or not
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final int n = sc.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = sc.nextInt();
        final int target = sc.nextInt();
        System.out.println("----- Pure Recursion -----");
        measurePerformance(() -> System.out.println("Subset Sum Length (Recursion): " + helper(0, n, nums, 0, target)));
        System.out.println("\n----- Memoization -----");
        // IMP- dp[i][j] stores the possibility whether any subset till ith array index can form a sum of jth index value
        final Boolean memo[][] = new Boolean[n][target+1];     // The Dp table is of size n and target+1
        measurePerformance(() -> System.out.println("Subset Sum Length (Memoization): " + memonize(0, n, nums, 0, target, memo)));
        System.out.println("\n----- Tabulation -----");
        measurePerformance(() -> System.out.println("Subset Sum Length (Tabulation): " + tabulate(n, nums, target)));
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

    public static boolean helper(int i, final int n, int nums[], int sum, final int target) {
        if(i == n)      // When entire array is traversed
            return sum == target;       // If found any sum equal to target
        // IMP- If we find sum equal to target earlier than no need to explore further (pruning)
        if(target == sum)
            return true;
        boolean take = helper(i+1, n, nums, sum+nums[i], target);       // Add current value to the sum
        boolean leave = helper(i+1, n, nums, sum, target);          // Or leave it
        return take || leave;
    }

    public static boolean memonize(int i, final int n, int nums[], int sum, final int target, final Boolean dp[][]) {
        if(i == n)          // When the entire array is traversed
            return sum == target;       // If found any sum equal to the target
        // IMP- If we find the sum equal to target earlier, than no need to explore further (pruning)
        if(target == sum)
            return true;
        if(dp[i][sum] != null)      // IMP- If dp state already computed once
            return dp[i][sum];
        boolean take = memonize(i+1, n, nums, sum+nums[i], target, dp);     // Take (add value to the sum)
        boolean leave = memonize(i+1, n, nums, sum, target, dp);            // Leave (do not add value to the sum)
        return dp[i][sum] = take || leave;
    }

    public static boolean tabulate(final int n, final int nums[], final int target) {
        boolean dp[][] = new boolean[n+1][target+1];        // Tabulation approach
        for(int i = 0; i <= n; i++)
            dp[i][0] = true;
        for(int i = 1; i <= n; i++)
            for(int sum = 1; sum <= target; sum++) {
                if(nums[i-1] > sum)     // If nums is lesser than sum
                    dp[i][sum] = dp[i-1][sum];          // Update the state as per the previous value
                else
                    // Otherwise check the possibility of sum getting equal to the target value
                    dp[i][sum] = dp[i-1][sum] || dp[i-1][sum - nums[i-1]];
            }
        return dp[n][target];
    }
}
