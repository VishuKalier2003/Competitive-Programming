package DP.Topics;

import java.util.Arrays;
import java.util.Scanner;

public class LCS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final String s1 = sc.next(), s2 = sc.next();
        final int n = s1.length(), m = s2.length();
        System.out.println("----- Pure Recursion -----");
        measurePerformance(() -> System.out.println("LCS Length (Recursion): " + helper(0, 0, n, m, s1, s2)));
        System.out.println("\n----- Memoization -----");
        // IMP- dp[i][j] stores the max LCS size when we have check string s1 to index i and s2 to index j
        final int memo[][] = new int[n+1][n+1];     // The Dp table is of size n+1 and n+1
        for(int row[] : memo)
            Arrays.fill(row, -1);
        measurePerformance(() -> System.out.println("LCS Length (Memoization): " + memonize(0, 0, n, m, s1, s2, memo)));
        System.out.println("\n----- Tabulation -----");
        measurePerformance(() -> System.out.println("LCS Length (Tabulation): " + tabulate(n, m, s1, s2)));
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

    public static int helper(int i, int j, final int n, final int m, final String s1, final String s2) {
        if(i == n || j == m)        // If any of the string is completely traversed, we cannot increase the LCS further
            return 0;
        if(s1.charAt(i) == s2.charAt(j))        // When characters match, increase LCS by 1
            return 1 + helper(i+1, j+1, n, m, s1, s2);
        else
            // Else, either move pointer either for s1 or s2
            return Math.max(helper(i+1, j, n, m, s1, s2), helper(i, j+1, n, m, s1, s2));
    }

    public static int memonize(int i, int j, final int n, final int m, final String s1, final String s2, final int dp[][]) {
        if(i == n || j == m)       // If any of the string is completely traversed, we cannot increase the LCS further
            return 0;
        if(dp[i][j] != -1)      // If subproblem not yet computed
            return dp[i][j];
        if(s1.charAt(i) == s2.charAt(j))    // When characters match, increase LCS by 1
            return dp[i][j] = 1 + memonize(i+1, j+1, n, m, s1, s2, dp);
        // Else, either move pointer either for s1 or s2
        return dp[i][j] = Math.max(memonize(i+1, j, n, m, s1, s2, dp), memonize(i, j+1, n, m, s1, s2, dp));
    }

    public static int tabulate(final int n, final int m, final String s1, final String s2) {
        int dp[][] = new int[n+1][m+1];     // Tabulate dp of size n+1 and m+1
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= m; j++) {
                // If characters match at indices i-1 and j-1, we do this to convert from 1 based to 0 based
                if(s1.charAt(i-1) == s2.charAt(j-1))
                    dp[i][j] = 1 + dp[i-1][j-1];
                else        // Only take the max of the previous states (either for s1 or s2)
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        return dp[n][m];
    }
}
