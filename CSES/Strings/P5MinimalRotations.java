import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P5MinimalRotations {
    public static class FastReader {        // Note: Fastest reading data
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

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {}
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t2 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "minimal-string", 1 << 26);
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.next());
    }

    public static void solve(final String s) {
        int n = s.length();
        String ss = s + s;
        int[] f = new int[2 * n];
        for (int i = 0; i < 2 * n; i++) f[i] = -1;
        int k = 0; // least rotation of string found so far
        for (int j = 1; j < 2 * n; ++j) {
            int i = f[j - k - 1];
            while (i != -1 && ss.charAt(j) != ss.charAt(k + i + 1)) {
                if (ss.charAt(j) < ss.charAt(k + i + 1)) {
                    k = j - i - 1;
                }
                i = f[i];
            }
            if (i == -1 && ss.charAt(j) != ss.charAt(k)) {
                if (ss.charAt(j) < ss.charAt(k)) {
                    k = j;
                }
                f[j - k] = -1;
            } else {
                f[j - k] = i + 1;
            }
        }
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(ss.substring(k, k+n));
        writer.write(output.toString());
        writer.flush();
    }
}
