import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class ProblemG {
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
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "problem-G",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        while (t-- > 0) {
            output.append(solve(fr.readInt(), fr.readString())).append("\n");
        }
        PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(output.toString());
        wr.flush();
    }

    public static long solve(final int n, final String s) {
        long totalLen = 0;
        for (long len = 1; len <= n; len++)
            totalLen += len * (n - len + 1);
        long[] D = new long[n + 1];
        D[0] = 0;
        long diff = 0;
        for (int i = 1; i <= n; i++) {
            diff += (s.charAt(i - 1) == '0' ? 1 : -1);
            D[i] = diff;
        }
        long[] comp = D.clone();
        Arrays.sort(comp);
        int m = 0;
        for (int i = 0; i <= n; i++) {
            if (i == 0 || comp[i] != comp[i - 1]) {
                comp[m++] = comp[i];
            }
        }
        Fenwick cnt = new Fenwick(m), sum = new Fenwick(m);
        long sumAbs = 0;
        for (int j = 0; j <= n; j++) {
            // find compressed index of D[j]
            int idx = Arrays.binarySearch(comp, 0, m, D[j]);
            long cLow = cnt.sum(idx);
            long sLow = sum.sum(idx);
            long cHigh = j - cLow;
            long sHigh = sum.sum(m - 1) - sLow;
            sumAbs += D[j] * cLow - sLow;
            sumAbs += sHigh - D[j] * cHigh;
            cnt.add(idx, 1);
            sum.add(idx, D[j]);
        }
        return (totalLen + sumAbs) / 2;
    }

    static class Fenwick {
        private final long[] f;

        Fenwick(int n) {
            f = new long[n];
        }

        // add v at position i
        void add(int i, long v) {
            for (; i < f.length; i |= i + 1) {
                f[i] += v;
            }
        }

        // sum f[0..i] inclusive
        long sum(int i) {
            long s = 0;
            for (; i >= 0; i = (i & (i + 1)) - 1) {
                s += f[i];
            }
            return s;
        }
    }
}
