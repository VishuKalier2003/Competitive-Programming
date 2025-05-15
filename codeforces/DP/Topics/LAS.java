package DP.Topics;
import java.util.Scanner;

public class LAS {      // IMP- Longest Alternating Subsequence
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final int n = sc.nextInt();
        final int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = sc.nextInt();
        System.out.println("----- Pure Recursion -----");
        measurePerformance(() -> System.out.println("LCS Length (Recursion): "+Math.max(helper(0, n, nums, true), helper(0, n, nums, false))));
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

    public static int helper(int i, final int n, final int nums[], boolean positive) {
        if(i == n)      // When array is completely traversed
            return 0;
        // IMP- Check greedily whether the current element to choose is positive or negative and check the index as well
        if(positive && nums[i] > 0)
            return 1 + helper(i+1, n, nums, !positive);
        else if(!positive && nums[i] < 0)
            return 1 + helper(i+1, n, nums, !positive);
        else
            return helper(i+1, n, nums, positive);
    }
}
