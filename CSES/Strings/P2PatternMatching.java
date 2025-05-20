import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P2PatternMatching {
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
        Thread t1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "pattern-matching", 1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.next(), fast.next());
    }

    public static final int BASE = 26;
    public static final int MOD = 1_000_000_007;

    public static void solve(final String text, final String pattern) {
        int t = text.length(), p = pattern.length();
        if (p > t) {
            System.out.println(0);
            return;
        }
        long highestPower = 1L;
        for(int i = 0; i < p-1; i++)
            highestPower = (highestPower * BASE) % MOD;
        int count = 0;
        long hashP = 0, hashT = 0;
        // Compute initial hashes for pattern and first window of text
        for (int i = 0; i < p; i++) {
            hashP = (hashP * BASE + pattern.charAt(i) - 'a') % MOD;
            hashT = (hashT * BASE + text.charAt(i) - 'a') % MOD;
        }
        // Check first window
        if (hashP == hashT && check(text, pattern, 0) == 1)
            count++;
        // Slide window and update hash
        for (int i = 1; i <= t - p; i++) {
            // Remove leading character
            hashT = (hashT - (text.charAt(i - 1) - 'a') * highestPower % MOD + MOD) % MOD;
            // Shift left and add trailing character
            hashT = (hashT * BASE + (text.charAt(i + p - 1) - 'a')) % MOD;
            if (hashP == hashT && check(text, pattern, i) == 1)
                count++;
        }
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(count);
        writer.write(output.toString());
        writer.flush();
    }

    public static int check(String text, String pattern, int tIdx) {
        for (int i = 0; i < pattern.length(); i++) {
            if (text.charAt(tIdx + i) != pattern.charAt(i))
                return 0;
        }
        return 1;
    }
}