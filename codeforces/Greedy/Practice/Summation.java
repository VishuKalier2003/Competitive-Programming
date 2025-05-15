// IMP- Sliding Window and Prefix Sums Sorting (Greedy)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Summation {
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
    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            final int k = fast.nextInt();
            final int x = fast.nextInt();
            int[] nums = new int[n];
            int sum = 0;
            for (int i = 0; i < n; i++) {
                nums[i] = fast.nextInt();
                sum += nums[i];
            }
            builder.append(optimalScore(n, k, x, nums, sum)).append("\n");
        }
        System.out.print(builder);
    }

    public static int optimalScore(final int n, int k, final int x, int[] nums, final int sum) {
        Arrays.sort(nums);
        // IMP- Build prefix sum array: prefix[i] is sum of first i elements.
        int[] prefix = new int[n+1];
        prefix[0] = 0;
        for (int i = 0; i < n; i++)
            prefix[i+1] = prefix[i] + nums[i];
        int optimalScore = Integer.MIN_VALUE;
        // r is the number of removals by Alice (from 0 to k, but cannot exceed n)
        for (int r = 0; r <= k && r <= n; r++) {
            int remaining = n - r; // number of elements left after removals
            // IMP- Sum of removed elements is the sum of the r largest elements.
            int removalSum = prefix[n] - prefix[remaining];
            int currentScore;
            if (remaining >= x) {
                // Bob flips the x largest among the remaining.
                int bobSum = prefix[remaining] - prefix[remaining - x];
                currentScore = (sum - removalSum) - 2 * bobSum;
            } else
                // If fewer than x remain, Bob flips them all.
                currentScore = -(sum - removalSum);
            optimalScore = Math.max(optimalScore, currentScore);
        }
        // IMP- In case Alice can remove all elements, she can guarantee 0.
        if (n == k)
            optimalScore = Math.max(optimalScore, 0);
        return optimalScore;
    }
}
