import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P4MinimalRotations {
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
        Thread t2 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "border-finding", 1 << 26);
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

    public static final long BASE = 57, MOD = (long)1e9 + 7;

    public static void solve(final String text) {
        String nonCyclic = text.repeat(2);
        int n = text.length();
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        int i = 0, j = 1, k = 0;
        while(i < n && j < n && k < n) {
            if(nonCyclic.charAt(i+k) == nonCyclic.charAt(j+k)) {
                k++;
                continue;
            }
            if(nonCyclic.charAt(i+k) > nonCyclic.charAt(j+k))
                i = i+k+1;
            else
                j = j+k+1;
            if(i == j)
                j++;
            k = 0;
        }
        output.append(nonCyclic.substring(i, j));
        writer.write(output.toString());
        writer.flush();
    }

    public static long compute(final String s) {
        long hash = 0l;
        int n = s.length();
        for(int i = 0; i < n; i++) {
            int value = s.charAt(i)-'a'+1;
            hash = (hash * BASE + value) % MOD;
        }
        return hash;
    }
}
