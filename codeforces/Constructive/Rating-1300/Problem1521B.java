import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem1521B {
    public static class FastReader {
        private final BufferedReader buffer;
        private StringTokenizer tokenizer;
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
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        StringBuilder out = new StringBuilder();

        int t = in.nextInt();
        while (t-- > 0) {
            int n = in.nextInt();
            int[] a = new int[n];
            int minVal = Integer.MAX_VALUE, minIdx = -1;

            // 1) Read input and determine global minimum
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
                if (a[i] < minVal) {
                    minVal = a[i];
                    minIdx = i;
                }
            }

            // 2) We will execute exactly (n - 1) operations
            out.append(n - 1).append('\n');

            // 3) For each index != minIdx, swap (minIdx, i) → (m, m + |i - minIdx|)
            //    This guarantees gcd(m, m + d) = 1 for any 1 <= d < n.
            for (int i = 0; i < n; i++) {
                if (i == minIdx) continue;
                int distance = Math.abs(i - minIdx);
                int newValue = minVal + distance;
                // operation: indices are 1‑based in output
                out.append(minIdx + 1)
                   .append(' ')
                   .append(i + 1)
                   .append(' ')
                   .append(minVal)
                   .append(' ')
                   .append(newValue)
                   .append('\n');
            }
        }

        // 4) Emit the consolidated output
        System.out.print(out);
    }
}
