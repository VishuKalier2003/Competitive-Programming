import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P3AreaOfPolygon {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;
        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }
        public void attachOutput(StringBuilder s) { sb.append(s); }
        public void printOutput() { pw.write(sb.toString()); pw.flush(); }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try { callMain(args); } catch (IOException e) { e.getLocalizedMessage(); }
        }, "Area-Of-Polygon-(https://cses.fi/problemset/task/2191)", 1 << 26);
        t.start();
        try { t.join(); } catch (InterruptedException ignored) {}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        final int n = fr.nextInt();
        long[] x = new long[n];
        long[] y = new long[n];
        for (int i = 0; i < n; i++) {
            x[i] = fr.nextInt();
            y[i] = fr.nextInt();
        }

        long twiceArea = 0L;
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            twiceArea += x[i] * y[j] - x[j] * y[i];
        }
        twiceArea = Math.abs(twiceArea);

        fw.attachOutput(new StringBuilder().append(twiceArea).append('\n'));
        fw.printOutput();
    }
}
