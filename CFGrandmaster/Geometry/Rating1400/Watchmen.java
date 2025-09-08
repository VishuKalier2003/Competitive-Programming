import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Watchmen {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Main wrapper thread
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Watchmen-(https://codeforces.com/problemset/problem/651/C)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Core logic
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt();

        Map<Integer, Long> countX = new HashMap<>();
        Map<Integer, Long> countY = new HashMap<>();
        Map<String, Long> countXY = new HashMap<>();

        for (int i = 0; i < n; i++) {
            final int x = fr.nextInt();
            final int y = fr.nextInt();

            countX.put(x, countX.getOrDefault(x, 0L) + 1);
            countY.put(y, countY.getOrDefault(y, 0L) + 1);
            String key = x + "#" + y;
            countXY.put(key, countXY.getOrDefault(key, 0L) + 1);
        }

        long ans = 0;

        for (long c : countX.values()) {
            ans += c * (c - 1) / 2;
        }
        for (long c : countY.values()) {
            ans += c * (c - 1) / 2;
        }
        for (long c : countXY.values()) {
            ans -= c * (c - 1) / 2;
        }

        output.append(ans).append("\n");
        fw.attachOutput(output);
        fw.printOutput();
    }

    public static final StringBuilder output = new StringBuilder();
}
