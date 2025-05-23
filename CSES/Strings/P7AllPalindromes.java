import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P7AllPalindromes {
    public static class FastReader {        // Note: Fastest reading data
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
        Thread t3 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "all-palindrome",
        1 << 26);
        t3.start();
        try {
            t3.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.next());
    }

    public static int p[];

    public static void solve(final String s) {
        manacher(s);
        int n = s.length();
        int output[] = new int[n];
        Arrays.fill(output, 1);
        // Hack: We will the values in intermediate between center to end
        for(int i = 1; i < p.length; i += 2) {      // For odd length (odd indices in p)
            int center = i/2, radius = p[i]/2;
            // Note: For odd lengths we will fill the values in between
            for(int len = 1; len <= radius && center + len - 1 < n; len++) {
                int end = center + len - 1;     // Info: end position for odd length palindromes
                int palinLength = 2 * len - 1;
                output[end] = Math.max(output[end], palinLength);
            }
        }
        for(int i = 2; i < p.length; i += 2) {      // For even length (even indices in p)
            int leftCenter = (i - 2)/2, radius = p[i]/2;
            // Note: For even lengths we will fill the values in between
            for(int len = 1; len <= radius && leftCenter + len < n; len++) {
                int end = leftCenter + len;
                int palinLength = 2 * len;
                output[end] = Math.max(output[end], palinLength);
            }
        }
        final StringBuilder result = new StringBuilder();
        for(int value : output)
            result.append(value).append(" ");
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(result.toString());
        writer.flush();
    }

    public static void manacher(final String s) {
        int n = s.length();
        char seq[] = new char[2*n+1];
        int len = seq.length;
        for(int i = 1, j = 0; j < n; i += 2, j++)
            seq[i] = s.charAt(j);
        for(int i = 0; i < seq.length; i += 2)
            seq[i] = '#';
        p = new int[len];
        int l = 1, r = 1;
        for(int i = 1; i < len; i++) {
            if(i < r) {
                int mirror = l+r-1;
                p[i] = Math.max(0, Math.min(r-i, p[mirror]));
            }
            while(i+p[i] < len && i-p[i] >= 0 && seq[i-p[i]] == seq[i+p[i]])
                p[i]++;
            if(l + p[i] > r) {
                l = i - p[i];
                r = i + p[i];
            }
        }
        System.out.println(Arrays.toString(p));
    }
}
