// Note- 1st AI-assist
/*
 * Got MLE, due to using Sieve of Erasthosthenes on large input 10^9, which consumes around 4GB of memory
 * Got TLE, since using dp was an overkill, as it had a more direct mathematical approach
 */
// Note- 2nd AI-assist
/*
 * Dp was required but only till 15, since mathematically it can be computed that after 15, the best possible split is always divide by 4
 */

// Ques - Maximum Splitting - https://codeforces.com/problemset/problem/870/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MaxSplitting {

    // Precomputed answers (dp) for numbers 1 to 15:
    // Index:      0   1   2   3   4  5   6   7   8   9   10  11  12  13  14  15
    // Answer:     0, -1, -1, -1, 1, -1, 1, -1, 2, 1, 2, -1, 3, 2, 3, 2
    // (dp[0] is unused in our queries)
    private static final int[] dpArr = {
        0,  -1, -1, -1,
        1,  -1,  1, -1,
        2,   1,  2, -1,
        3,   2,  3,  2
    };

    // Threshold: We precompute values for numbers up to M.
    private static final int M = 15;

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        StringBuilder builder = new StringBuilder();

        // Number of queries
        int q = fast.nextInt();
        for (int i = 0; i < q; i++) {
            long n = fast.nextLong();
            builder.append(solve(n)).append("\n");
        }
        System.out.print(builder);
    }

    /**
     * For a given n, returns the maximum number of composite summands in its splitting,
     * or -1 if no such splitting exists.
     *
     * For n <= M, we return the precomputed value.
     * For n > M, we subtract k copies of 4 (the minimal composite) such that the remainder r = n - 4*k
     * becomes a small number (≤ M). Then, the answer is k + dpArr[r].
     */
    public static long solve(long n) {
        // For small numbers, directly return the precomputed answer.
        if (n <= M)
            return dpArr[(int)n];
        // Calculate minimal k such that the remainder r = n - 4*k is <= M.
        // Solve for k using ceiling division: k = ceil((n - M) / 4)
        long k = (n - M + 3) / 4;  // +3 handles ceiling for integer division
        // Compute remainder after subtracting 4*k.
        int r = (int)(n - 4 * k);
        // According to the theory, r will be between 4 and 15 and dpArr[r] must be valid.
        if (r < 4 || dpArr[r] == -1)
            // This safeguard theoretically should never be needed if n > 15.
            return -1;
        return k + dpArr[r];
    }

    // FastReader implementation for fast input
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;
        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }
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
        public int nextInt() { return Integer.parseInt(next()); }
        public long nextLong() { return Long.parseLong(next()); }
    }
}
