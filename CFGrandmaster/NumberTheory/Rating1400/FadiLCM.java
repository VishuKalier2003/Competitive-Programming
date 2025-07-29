import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FadiLCM {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
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

    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
            sb.setLength(0);
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Fadi-LCM",
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
        FastWriter fw = new FastWriter();
        fw.attachOutput(solve(fr.readLong()));
        fw.printOutput();
    }

    public static List<Long> divisors;      // list of divisors

    public static StringBuilder solve(long x) {
        long temp = x;
        // Prime Factorization to extract prime numbers with their powers
        Map<Long, Integer> fMap = new HashMap<>();
        for(long i = 2; i*i <= x; i++) {
            while(x % i == 0) {
                fMap.put(i, fMap.getOrDefault(i, 0) +1);
                x /= i;
            }
        }
        if(x > 1)
            fMap.put(x, 1);
        divisors = new ArrayList<>();
        extractDivisors(0, 1L, new ArrayList<>(fMap.keySet()), fMap);
        long min = Long.MAX_VALUE;
        long x1 = -1, y1 = -1;
        for(long a : divisors) {
            long b = temp / a;
            if(Math.max(a, b) < min && lcm(a, b) == temp) {
                x1 = a;
                y1 = b;
                min = Math.max(a, b);
            }
        }
        return new StringBuilder().append(x1).append(" ").append(y1);
    }

    public static void extractDivisors(int i, long curr, List<Long> primes, Map<Long, Integer> fMap) {
        if(i == primes.size()) {
            divisors.add(curr);
            return;
        }
        long prime = primes.get(i), product = 1L;
        int p = fMap.get(prime);
        for(int exp = 0; exp <= p; exp++) {
            extractDivisors(i+1, curr * product, primes, fMap);
            product *= prime;
        }
    }

    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }
}
