import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BoringDay {
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

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        StringBuilder builder = new StringBuilder();
        while (t-- > 0) {
            int n = fast.nextInt();
            long l = fast.nextInt();
            long r = fast.nextInt();
            long[] nums = new long[n];
            for (int i = 0; i < n; i++) {
                nums[i] = fast.nextLong();
            }
            builder.append(solve(n, nums, l, r)).append("\n");
        }
        System.out.print(builder);
    }

    // solve method: builds prefix sums and launches recursion with memoization
    public static int solve(int n, long[] nums, long l, long r) {
        // Build prefix sum array.
        long[] prefix = new long[n + 1];
        prefix[0] = 0;
        for (int i = 1; i <= n; i++)
            prefix[i] = prefix[i - 1] + nums[i - 1];
        // memo[i] stores the maximum rounds that can be achieved starting from index i.
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return rec(0, n, prefix, l, r, memo);
    }

    // IMP- rec(i) returns the maximum wins when starting from ith segment
    public static int rec(int i, int n, long[] prefix, long l, long r, int[] memo) {
        if (i == n) return 0;
        if (memo[i] != -1) return memo[i];
        // Option 1: Skip the i-th card.
        int best = rec(i + 1, n, prefix, l, r, memo);
        // Option 2: Start a segment at index i.
        // We need to find the smallest index j in the range [i+1, n]
        // such that prefix[j] - prefix[i] >= l.
        long target = prefix[i] + l;
        // Use binary search over prefix[i+1...n] since prefix is monotonic.
        int j = Arrays.binarySearch(prefix, i + 1, n + 1, target);
        if (j < 0)
            j = -j - 1; // insertion point: first index with prefix[j] >= target
        // Check if this segment is valid:
        if (j <= n && (prefix[j] - prefix[i] <= r))
            best = Math.max(best, 1 + rec(j, n, prefix, l, r, memo));
        memo[i] = best;
        return best;
    }
}
