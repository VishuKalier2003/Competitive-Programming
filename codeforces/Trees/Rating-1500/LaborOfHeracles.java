import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.PriorityQueue;

public class LaborOfHeracles {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {
            }
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(
                null, () -> {
                    try {
                        callMain(args);
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                },
                "13th-labor-of-heracles",
                1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException IE) {
            IE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fast.nextInt();
        while (t-- > 0) {
            final int n = fast.nextInt();
            long sum = 0l;
            int w[] = new int[n + 1], deg[] = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                w[i] = fast.nextInt();
                sum += w[i];
            }
            for (int i = 1; i < n; i++) {
                int u = fast.nextInt(), v = fast.nextInt();
                deg[u]++;
                deg[v]++;
            }
            output.append(solve(n, w, deg, sum)).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static StringBuilder solve(final int n, final int w[], final int deg[], long sum) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 1; i <= n; i++)
            if (deg[i] > 1)
                while (deg[i] > 1) {
                    maxHeap.add(w[i]);
                    deg[i]--;
                }
        final StringBuilder kColor = new StringBuilder();
        kColor.append(sum).append(" ");
        for (int k = 2; k < n; k++) {
            sum += maxHeap.poll();
            kColor.append(sum).append(" ");
        }
        return kColor;
    }
}
