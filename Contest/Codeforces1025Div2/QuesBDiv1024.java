import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class QuesBDiv1024 {
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
            return buffer[ptr++] & 0xff; // Reading data between 0 to 255 characters
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
                x = x * 10 + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
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
                x = x * 10 + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static void main(String[] args) {
        Thread extendThread2 = new Thread(null,
                () -> {
                    try {
                        callMain(args);
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                },
                "quesB",
                1 << 26);
        extendThread2.start();
        try {
            extendThread2.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while (t-- > 0) {
            output.append(solve(fast.nextInt(), fast.nextInt(), fast.nextInt(), fast.nextInt())).append("\n");
        }
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static int solve(final int n, final int m, final int a, final int b) {
        final java.util.function.IntUnaryOperator bitLength = x -> {
            if (x <= 0)
                return 0;
            int len = 0;
            while ((1 << len) <= x) {
                len++;
            }
            return len;
        };
        int leftInterval = Math.min(a, n - a + 1) - 1;
        int rightInterval = Math.min(b, m - b + 1) - 1;
        int candidate1 = bitLength.applyAsInt(leftInterval) + bitLength.applyAsInt(m - 1) + 1;
        int candidate2 = bitLength.applyAsInt(n - 1) + bitLength.applyAsInt(rightInterval) + 1;
        return Math.min(candidate1, candidate2);
    }
}
