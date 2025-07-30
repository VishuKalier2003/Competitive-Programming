import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GCDSequence {
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
        }, "GCD-Sequence",
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
        int t = fr.readInt();
        while (t-- > 0) {
            final int n = fr.readInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fr.readInt();
            fw.attachOutput(solve(n, nums));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, int[] a) {
        // 1. Build GCD‐sequence
        int m = n - 1;
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = gcd(a[i], a[i + 1]);
        }
        // 2. Find all drops
        List<Integer> drops = new ArrayList<>();
        for (int i = 0; i < m - 1; i++) {
            if (b[i] > b[i + 1]) {
                drops.add(i);
                if (drops.size() > 1) {
                    return new StringBuilder("NO\n");
                }
            }
        }
        if (drops.isEmpty()) {
            return new StringBuilder("YES\n");
        }
        int i = drops.get(0);

        // 3. Try removing a[i+1]
        int g1 = gcd(a[i], a[i + 2]);
        boolean ok1 = (i == 0 || b[i - 1] <= g1) &&
                (i + 2 >= m || g1 <= b[i + 2]);

        // 4. Try removing a[i+2]
        boolean ok2;
        if (i + 3 < n) {
            int g2 = gcd(a[i + 1], a[i + 3]);
            ok2 = (i == 0 || b[i - 1] <= b[i]) && // the left side drop is at b[i] ≤ new b[i+1]
                    (g2 >= b[i] && g2 <= (i + 3 >= m ? Integer.MAX_VALUE : b[i + 2]));
        } else {
            // if i+3 == n, removal of last element only affects left side
            ok2 = (i == 0 || b[i - 1] <= b[i]);
        }

        return new StringBuilder((ok1 || ok2) ? "YES\n" : "NO\n");
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
