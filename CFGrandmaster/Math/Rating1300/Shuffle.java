// https://codeforces.com/problemset/problem/1366/B

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Shuffle {
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
        }, "shuffle", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final int n = fr.readInt(), x = fr.readInt(), m = fr.readInt();
            List<int[]> seg = new ArrayList<>();
            for (int i = 0; i < m; i++)
                seg.add(new int[] { fr.readInt(), fr.readInt() });
            out.append(solve(n, x, m, seg)).append("\n");
        }
        wr.write(out.toString());
        wr.flush();
    }

    // Math with two pointers
    public static int solve(final int n, final int x, final int m, final List<int[]> seg) {
        int l = x, r = x;
        for(int[] q : seg) {
            int ql = q[0], qr = q[1];
            // Find the partial and complete overlaps and expand the range with each query
            if((ql >= l && ql <= r) || (qr >= l && qr <= r) || (ql <= l && qr >= r)) {
                l = Math.min(ql, l);
                r = Math.max(qr, r);
            }
        }
        return r-l+1;
    }
}
