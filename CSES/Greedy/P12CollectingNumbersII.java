import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P12CollectingNumbersII {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }
    }

    public static void main(String[] args) throws IOException {
        Thread th1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "collecting-numbers-II", 1 << 26);
        th1.start();
        try {
            th1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), m = fast.nextInt();
        int[] arr = new int[n + 1]; // 1-based for easier management
        int[] pos = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = fast.nextInt();
            pos[arr[i]] = i;
        }

        int rounds = 1;
        for (int i = 2; i <= n; i++) {
            if (pos[i] < pos[i - 1]) rounds++;
        }

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < m; i++) {
            int a = fast.nextInt(), b = fast.nextInt();

            if (a == b) {
                sb.append(rounds).append('\n');
                continue;
            }

            int va = arr[a], vb = arr[b];

            // Remove the effect of both values before swap
            rounds -= getDelta(pos, va, n);
            rounds -= getDelta(pos, vb, n);
            if (va + 1 == vb || vb + 1 == va) {
                // If adjacent, we must avoid double subtracting shared boundary
                rounds += checkAdjacentOverlap(pos, va, vb);
            }

            // Swap
            arr[a] = vb;
            arr[b] = va;
            pos[va] = b;
            pos[vb] = a;

            // Recompute the affected values after swap
            rounds += getDelta(pos, va, n);
            rounds += getDelta(pos, vb, n);
            if (va + 1 == vb || vb + 1 == va) {
                // Adjust for adjacent again
                rounds -= checkAdjacentOverlap(pos, va, vb);
            }

            sb.append(rounds).append('\n');
        }

        writer.write(sb.toString());
        writer.flush();
    }

    // Count +1 for each place where pos[x] < pos[x-1]
    private static int getDelta(int[] pos, int x, int n) {
        int delta = 0;
        if (x > 1 && pos[x] < pos[x - 1]) delta++;
        if (x < n && pos[x + 1] < pos[x]) delta++;
        return delta;
    }

    // Special case: if x and y are adjacent (e.g., 3 and 4), we might double-count the boundary
    private static int checkAdjacentOverlap(int[] pos, int a, int b) {
        int min = Math.min(a, b);
        if (pos[min + 1] < pos[min]) return 1;
        return 0;
    }
}
