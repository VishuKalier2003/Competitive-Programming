import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ProblemE {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single System.in.read()
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
        }, "problem-E",
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
            output.append(solve(fr.readLong(), fr.readLong())).append("\n");
        }
        PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(output.toString());
        wr.flush();
    }

    public static final long INF = (long) 1e18;

    public static long solve(long l, long r) {
        String L = String.valueOf(l), R = String.valueOf(r);
        int n = L.length();
        // dp[tightL][tightR] = minimal cost up to current pos
        long[][] dp = new long[2][2], next = new long[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                dp[i][j] = INF;
        dp[1][1] = 0;
        for (int pos = 0; pos < n; pos++) {
            // reset next
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    next[i][j] = INF;
            int lowDigit = L.charAt(pos) - '0';
            int highDigit = R.charAt(pos) - '0';
            for (int tL = 0; tL < 2; tL++) {
                for (int tR = 0; tR < 2; tR++) {
                    long cur = dp[tL][tR];
                    if (cur >= INF)
                        continue;
                    // determine allowed d range
                    int lo = tL == 1 ? lowDigit : 0;
                    int hi = tR == 1 ? highDigit : 9;
                    for (int d = lo; d <= hi; d++) {
                        int cost = (d == lowDigit ? 1 : 0)
                                + (d == highDigit ? 1 : 0);
                        int nL = (tL == 1 && d == lowDigit) ? 1 : 0;
                        int nR = (tR == 1 && d == highDigit) ? 1 : 0;
                        long cand = cur + cost;
                        if (cand < next[nL][nR]) {
                            next[nL][nR] = cand;
                        }
                    }
                }
            }
            // swap dp, next
            long[][] tmp = dp;
            dp = next;
            next = tmp;
        }
        // answer is min over all final tight states
        long ans = INF;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                ans = Math.min(ans, dp[i][j]);
        return ans;
    }
}
