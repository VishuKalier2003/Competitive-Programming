// https://codeforces.com/problemset/problem/1203/C

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CommonDivisor {
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
        }, "common-divisors", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        final int n = fr.readInt();
        long nums[] = new long[n];
        for(int i = 0; i < n; i++)
            nums[i] = fr.readLong();
        out.append(solve(n, nums)); 
        wr.write(out.toString());
        wr.flush();
    }

    public static long solve(final int n, final long nums[]) {
        long gcd = nums[0];
        for(long num : nums)
            gcd = gcd(gcd, num);    // Evaluating the biggest divisor of all numbers
        // Performing prime factorization
        Map<Long, Integer> fMap = new HashMap<>();
        for(long i = 2; i*i <= gcd; i++) {
            while(gcd % i == 0) {
                fMap.put(i, fMap.getOrDefault(i, 0)+1);
                gcd /= i;
            }
        }
        if(gcd > 1)
            fMap.put(gcd, 1);
        // Cacluating tau, the number of factors via prime factors
        long count = 1L;
        for(int f : fMap.values())
            count *= (f+1);
        return count;
    }

    public static long gcd(long a, long b) {       // gcd function
        return b == 0 ? a : gcd(b, a % b);
    }
}
