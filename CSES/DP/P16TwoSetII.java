import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P16TwoSetII {
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

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "minimal-grid-path", 1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        solve(n);
    }

    public static final int MOD = 1_000_000_007;

    public static void solve(final int n) {
        int sum = (n * (n + 1)) / 2;
        if (sum % 2 != 0) {
            System.out.println(0);
            return;
        }
        int t = sum / 2;
        final int dp[] = new int[t + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++)
            for (int s = t; s >= i; s--)
                dp[s] = (dp[s] + dp[s - i]) % MOD;
        long ways = dp[t]; // The ways to split in equal halves
        long inv2 = (MOD + 1L) / 2;
        // The ans we have is divided by 2 and then MOD, so we use fernet's theorem
        long ans = (ways * inv2) % MOD;
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(ans);
        writer.write(output.toString());
        writer.flush();
    }
}
