import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MedianSplit {
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

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while (t-- > 0) {
            int n = fast.nextInt();
            int k = fast.nextInt();
            int[] a = new int[n];

            for (int i = 0; i < n; i++) {
                a[i] = fast.nextInt();
            }

            output.append(solve(n, a, k)).append("\n");
        }
        System.out.print(output);
    }

    public static String solve(int n, int[] a, int k) {
        // You need at least three elements to make three non-empty segments
        if (n < 3) return "NO";

        // 1) Build prefix-sums P[i] = sum_{j=1..i} b_j, where b_j = +1 if a[j]<=k else -1
        int[] P = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            P[i] = P[i - 1] + (a[i - 1] <= k ? 1 : -1);
        }
        int total = P[n];

        // 2) Fenwick (BIT) over P[1..r-1], but P ranges in [-n..n], so offset by n+1
        int size = 2 * n + 5;
        int offset = n + 2;
        Fenwick bit = new Fenwick(size);

        // initially insert P[1]
        bit.update(P[1] + offset, +1);

        // 3) For each r=2..n-1, test the three cases
        for (int r = 2; r <= n - 1; r++) {
            int cur = P[r];

            // --- Case 1: segments 1&2 both ≥0  ⇔  ∃ l<r with P[l]∈[0,cur]
            int cntIn_0_to_cur = bit.sum(cur + offset) - bit.sum(offset - 1);
            if (cntIn_0_to_cur > 0) {
                return "YES";
            }

            // --- If suffix C = P[n] - P[r] ≥ 0, we can also try cases 2 & 3
            if (total - cur >= 0) {
                // Case 2: segments 1&3  ⇔  ∃ l<r with P[l] ≥ 0
                int cntGe0 = bit.sum(size - 1) - bit.sum(offset - 1);
                if (cntGe0 > 0) {
                    return "YES";
                }
                // Case 3: segments 2&3  ⇔  ∃ l<r with P[l] ≤ cur
                int cntLeCur = bit.sum(cur + offset);
                if (cntLeCur > 0) {
                    return "YES";
                }
            }

            // insert P[r] for the next iteration
            bit.update(cur + offset, +1);
        }
        return "NO";
    }

    // Fenwick / BIT for point updates and prefix sums
    static class Fenwick {
        private final int[] f;
        Fenwick(int n) { f = new int[n]; }
        void update(int i, int v) {
            for (; i < f.length; i += i & -i) f[i] += v;
        }
        int sum(int i) {
            int s = 0;
            for (; i > 0; i -= i & -i) s += f[i];
            return s;
        }
    }

}
