// https://codeforces.com/problemset/problem/1360/D

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class BuyingShovel {
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
            int c, x = 0;
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

        public long nextLong() throws IOException {
            long c, x = 0L;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1L;
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
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
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
        }, "Shovels", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static List<Long> factors;

    public static void callMain(String args[]) throws IOException {
        final FastReader fr = new FastReader();
        int t = fr.nextInt();
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final long n = fr.nextLong(), k = fr.nextLong();
            out.append(solve(n, k)).append("\n");
        }
        wr.write(out.toString());
        wr.flush();
    }

    public static long solve(long n, long k) {
        long minPackages = n; 
        for (long i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                long d1 = i;
                long d2 = n / i;
                if (d1 <= k)
                    minPackages = Math.min(minPackages, n / d1);
                if (d2 <= k)
                    minPackages = Math.min(minPackages, n / d2);
            }
        }
        return minPackages;
    }
}
