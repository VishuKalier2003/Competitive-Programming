/*
 * Ques - Long Jumps - https://codeforces.com/problemset/problem/1472/C
 * Tags - Left Shift DP, Graph
 * Rating - 1100
 */

import java.util.Scanner;

public class LongJumps {
    // Record created for storing each test case data, allows quick operations on immutable data...
    public record TestCase(int n, String str, long[] nums) {}   // Record fields are final...
    public static void main(String[] args) {
        TestCase tests[];
        // Input block segment...
        input: {
            Scanner sc = new Scanner(System.in);
            int t = sc.nextInt();
            tests = new TestCase[t];        // Array defined...
            for(int i = 0; i < t; i++) {
                int n = sc.nextInt();
                sc.nextLine();
                String s = sc.nextLine();
                // Since fields are final, we need to process data before passing as it cannot be modified later...
                tests[i] = new TestCase(n, s, convertStringToNums(n, s));
            }
            sc.close();
            break input;
        }
        // Output block segment...
        output: {
            for(TestCase test : tests)      // Function call for each test case...
                System.out.println(maxScore(test.n, test.nums));    // Passing the final fields...
            break output;
        }
    }

    public static long[] convertStringToNums(int n, String s) {
        // Converting String to an array of long data type...
        long nums[] = new long[n];
        String str[] = s.split(" ");
        for(int i = 0; i < n; i++)
            nums[i] = Long.parseLong(str[i]);
        return nums;
    }

    public static long maxScore(int n, long nums[]) {
        // Creating a dp array...
        long dp[] = new long[n];
        dp[n-1] = nums[n-1];        // Base case initialized...
        for(int i = n-2; i > -1; i--)
            // If the pointer crosses the boundary set it to current, else increment as per the forward state value...
            dp[i] = i+nums[i] >= n ? nums[i] : nums[i]+dp[(int)(i+nums[i])];
        long max = 0l;
        for(long node : dp)     // Find the max score...
            max = Math.max(node, max);
        return max;
    }
}
