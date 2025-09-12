import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P6LongestPalindrome {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
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

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException { // reading string (whitespace exclusive)
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

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
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
        }
    }

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Longest-Palindrome-(https://cses.fi/problemset/task/1110/)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        solve(fr.next());
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final String s) {
        int p[] = manacher(s, s.length());
        System.out.println(Arrays.toString(p));
        int max = 0, center = 0;
        for(int i = 0; i < p.length; i++) {
            if(p[i] > max) {
                max = p[i];
                center = i;
            }
        }
        int start = (center - max + 1) / 2;
        int len = max - 1;
        output.append(s.substring(start, start+len));
    }

    public static int[] manacher(String s, int len) {
        final int p[] = new int[2 * len + 1];       // p array
        char seq[] = new char[2 * len + 1];
        Arrays.fill(seq, '#');
        for (int i = 1, j = 0; j < len; i += 2, j++)
            seq[i] = s.charAt(j);
        int n = p.length;
        int center = 0;     // index of rightmost palindrome center found so far
        int right = 0;      // right boundary (exclusive)
        for (int i = 0; i < n; i++) {
            int mirror = 2 * center - i;        // Info: mirror pointer (symmetric position of i around center)
            if (i < right)  // If within bounding box, radius clipping
                p[i] = Math.min(right - i, p[mirror]);
            // expand around i until bounds
            while (i+1+p[i] < n && i-1-p[i] >= 0 && seq[i+1+p[i]] == seq[i-1-p[i]])
                p[i]++;
            // If bounding box is crossed
            if (i + p[i] > right) {     // Info: update bounding box range
                center = i;
                right = i + p[i];
            }
        }
        return p;
    }
}
