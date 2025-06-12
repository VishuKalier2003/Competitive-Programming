import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SaveTheCity {
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
        }, "colorful-table", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final int a = fr.readInt(), b = fr.readInt();
            output.append(solve(a, b, fr.readString())).append("\n");
        }
        wr.write(output.toString()); // printwriter to print output
        wr.flush();
    }

    public static long solve(int a, int b, String s) {
        int n = s.length();
        int i = 0;
        // 1) Skip any leading '0's — they cost nothing.
        while (i < n && s.charAt(i) == '0')
            i++;
        // If the whole string is zero, no mines => no cost.
        if (i == n)
            return 0L;
        // Activate the first segment of '1's.
        long cost = a;
        while (i < n) {
            // Skip current segment of '1's.
            while (i < n && s.charAt(i) == '1')
                i++;
            // Count the next run of '0's (the gap).
            int startZero = i;
            while (i < n && s.charAt(i) == '0')
                i++;
            int gapLength = i - startZero;
            // If we reached the end, there's no further '1' to activate.
            if (i >= n)
                break;
            cost += Math.min((long) gapLength * b, a);
        }

        return cost;
    }

}
