import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P2StringMatching {
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
        }, "String-Matching-(https://cses.fi/problemset/task/1753)", 1 << 26);
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
        final String text = fr.next(), pattern = fr.next();
        solve(text.length(), text, pattern.length(), pattern);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int t, final String text, final int m, final String pattern) {
        int lps[] = lps(m, pattern);
        // i is the index for text and j for pattern
        int i = 0, j = 0, count = 0;
        while(i < t) {
            if(text.charAt(i) == pattern.charAt(j)) {       // If pattern matches
                j++; i++;
            }
            if(j == m) {    // If the pattern string is traversed completely, pattern matched
                count++;
                j = lps[j-1];       // shift j to the prefix suffix that are valid using lps (for pattern)
            }
            else if(i < t && text.charAt(i) != pattern.charAt(j)) {     // If not match
                    if(j != 0)      // If j is not 0
                        j = lps[j-1];       // shift j to smaller segment of prefix suffix that is valid
                    else
                        i++;
                }
        }
        output.append(count);
    }

    public static int[] lps(final int n, final String s) {
        int lps[] = new int[n];
        // i is index and j is the length of prefix suffix matched
        int j = 0, i = 1;
        while(i < n) {
            if(s.charAt(i) == s.charAt(j)) {        // If matched
                j++;
                lps[i] = j;     // update lps[i] with j
                i++;
            }
            else {
                if(j != 0)      // If prefix suffix length is not 0 the lps[j-1] characters are valid so move back to lps[j-1]
                    j = lps[j-1];
                else {
                    lps[i] = 0;     // Otherwise mark lps[i] as 0 and move forward
                    i++;
                }
            }
        }
        return lps;
    }
}
