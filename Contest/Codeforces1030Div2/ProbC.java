import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

public class ProbC {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;
        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0; len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }
        public int readInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do { x = 10*x + (c - '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
        public long readLong() throws IOException {
            long x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do { x = 10*x + (c - '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fr.readInt();
        while (t-- > 0) {
            int n = fr.readInt();
            long k = fr.readLong();
            long[] a = new long[n];
            for (int i = 0; i < n; i++) a[i] = fr.readLong();
            out.println(solve(n, k, a));
        }
        out.flush();
    }

    public static long solve(int n, long K, long[] nums) {
        // base beauty
        long base = 0;
        for (long x: nums) base += Long.bitCount(x);

        // gather all "upgrade" options per index
        List<List<long[]>> groups = new ArrayList<>(n);
        int maxGainSum = 0;
        for (int i = 0; i < n; i++) {
            long ai = nums[i];
            long orig = Long.bitCount(ai);
            long limit = ai + K;
            int hb = 63 - Long.numberOfLeadingZeros(limit);
            long scanMax = Math.min(limit, (1L << (hb+1)) - 1 - ai);
            long curB = orig;
            int stale = 0;
            List<long[]> opts = new ArrayList<>();
            for (long d = 1; d <= scanMax && stale < 2*(hb+1); d++) {
                long nb = Long.bitCount(ai + d);
                if (nb > curB) {
                    int gain = (int)(nb - orig);
                    opts.add(new long[]{d, gain});
                    maxGainSum += gain;
                    curB = nb;
                    stale = 0;
                } else {
                    stale++;
                }
            }
            groups.add(opts);
        }

        // dp[g] = min cost to achieve total gain = g
        final long INF = Long.MAX_VALUE/4;
        long[] dp = new long[maxGainSum + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (List<long[]> opts : groups) {
            long[] next = new long[maxGainSum + 1];
            Arrays.fill(next, INF);
            // option: pick none
            for (int g = 0; g <= maxGainSum; g++) {
                next[g] = dp[g];
            }
            // option: pick exactly one upgrade
            for (long[] opt : opts) {
                long cost = opt[0];
                int gain = (int)opt[1];
                for (int g = 0; g + gain <= maxGainSum; g++) {
                    if (dp[g] != INF) {
                        long c = dp[g] + cost;
                        if (c < next[g + gain]) {
                            next[g + gain] = c;
                        }
                    }
                }
            }
            dp = next;
        }

        // find best gain within budget K
        long best = 0;
        for (int g = 0; g <= maxGainSum; g++) {
            if (dp[g] <= K && g > best) {
                best = g;
            }
        }
        return base + best;
    }
}
