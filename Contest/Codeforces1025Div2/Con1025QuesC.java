import java.io.IOException;
import java.io.PrintWriter;

public class Con1025QuesC {
    // Hack: Fastest Input Output reading in Java
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
            // Skip whitespace and blank lines
            while ((c = read()) != -1 && c <= ' ');
            if (c == -1) return -1;
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0L;
            // Skip whitespace and blank lines
            while ((c = read()) != -1 && c <= ' ');
            if (c == -1) return -1L;
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static void main(String[] args) {
        Thread extendThread2 = new Thread(null,
            () -> {
                try {
                    callMain(args);
                } catch (IOException e) {
                    // handle exception if needed
                }
            },
            "quesC",
            1 << 26);
        extendThread2.start();
        try {
            extendThread2.join();
        } catch (InterruptedException ignored) {}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        PrintWriter out = new PrintWriter(System.out, true);

        int t = fr.nextInt();
        while (t-- > 0) {
            long n = fr.nextLong();
            long x = fr.nextLong();

            // If x already equals n, simply answer
            if (x == n) {
                out.println("!");
            } else {
                // One-step conversion: add (n - x) to x
                long delta = n - x;
                out.println("add " + delta);
                out.println("!");
            }
            // Separate outputs by an empty line
            out.println();
        }

        out.flush();
    }
}
