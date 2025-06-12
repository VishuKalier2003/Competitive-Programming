// https://codeforces.com/problemset/problem/371/B

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FoxCheese {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single
        // System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
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

        public long readLong() throws IOException {
            long x = 0l, c;
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

        public String readString() throws IOException {
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String readLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread math1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "fox-cheese", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(solve(fr.readInt(), fr.readInt()));
        wr.write(output.toString()); // printwriter to print output
        wr.flush();
    }

    public static long solve(int a, int b) {
        // Step 1: Extract exponents of 2, 3 and 5 from a
        int a2 = 0, a3 = 0, a5 = 0;
        int coreA = a;
        while (coreA % 2 == 0) {
            coreA /= 2;
            a2++;
        }
        while (coreA % 3 == 0) {
            coreA /= 3;
            a3++;
        }
        while (coreA % 5 == 0) {
            coreA /= 5;
            a5++;
        }

        // Step 2: Extract exponents of 2, 3 and 5 from b
        int b2 = 0, b3 = 0, b5 = 0;
        int coreB = b;
        while (coreB % 2 == 0) {
            coreB /= 2;
            b2++;
        }
        while (coreB % 3 == 0) {
            coreB /= 3;
            b3++;
        }
        while (coreB % 5 == 0) {
            coreB /= 5;
            b5++;
        }

        // Step 3: If residual cores differ, no sequence of allowed operations can
        // equalize them
        if (coreA != coreB) {
            return -1L;
        }

        // Step 4: The minimal number of eats is the total absolute difference of
        // exponents
        // |a2 - b2| operations to align factors of 2
        // + |a3 - b3| operations to align factors of 3
        // + |a5 - b5| operations to align factors of 5
        return (long) Math.abs(a2 - b2)
                + Math.abs(a3 - b3)
                + Math.abs(a5 - b5);
    }

}
