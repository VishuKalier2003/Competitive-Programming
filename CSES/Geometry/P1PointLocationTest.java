
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P1PointLocationTest {
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
        }, "Point-Location-Test-(https://cses.fi/problemset/task/2189)", 1 << 26);
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
        int t = fr.nextInt();
        while(t-- > 0) {
            final int x1 = fr.nextInt(), y1 = fr.nextInt(), x2 = fr.nextInt(), y2 = fr.nextInt(), x3 = fr.nextInt(), y3 = fr.nextInt();
            fw.attachOutput(solve(x1, y1, x2, y2, x3, y3));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final long x1, final long y1, final int x2, final int y2, final int x3, final int y3) {
        double D = pointLocationSignedArea(x1, y1, x2, y2, x3, y3);
        final StringBuilder output = new StringBuilder();
        if(D > 0)
            output.append("LEFT");
        else if(D < 0)
            output.append("RIGHT");
        else
            output.append("TOUCH");
        return output.append("\n");
    }

    // Info: Provides exact signed area of triangle formed by the three points
    public static double pointLocationSignedArea(final long x1, final long y1, final long x2, final long y2, final long x3, final long y3) {
        return 0.5d * ((x1 * (y2 - y3)) + (x2 * (y3 - y1)) + (x3 * (y1 - y2)));
    }

    // Info: Provides the orientation which is twice the value of the signed area of the triangle
    public static double pointLocationSignedOrientation(final long x1, final long y1, final long x2, final long y2, final long x3, final long y3) {
        return pointLocationSignedArea(x1, y1, x2, y2, x3, y3) * 2.0d;
    }
}
