import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P4PointInPolygon {
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
        }, "Point-in-Polygon-(https://cses.fi/problemset/task/2192)", 1 << 26);
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
        final int n = fr.nextInt(), q = fr.nextInt();
        Point points[] = new Point[n];
        for(int i = 0; i < n; i++) 
            points[i] = new Point(fr.nextInt(), fr.nextInt());
        long queries[][] = new long[q][2];
        for(int i = 0; i < q; i++) {
            queries[i][0] = fr.nextInt();
            queries[i][1] = fr.nextInt();
        }
        solve(n, q, points, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int n, final int q, final Point point[], final long queries[][]) {
        for(long query[] : queries) {
            Point p = new Point(query[0], query[1]);
            if(onBoundary(point, p))
                output.append("BOUNDARY");
            else if(inside(point, p))
                output.append("INSIDE");
            else
                output.append("OUTSIDE");
            output.append("\n");
        }
    }

    public static boolean onBoundary(Point poly[], Point p) {
        int n = poly.length;
        for(int i = 0; i < n; i++) {
            Point a = poly[i], b = poly[(i + 1) % n];
            if(cross(a, b, p) == 0L && within(a, b, p))
                return true;
        }
        return false;
    }

    public static boolean inside(Point poly[], Point p) {
        int n = poly.length;
        boolean inside = false;
        for(int i = 0; i < n; i++) {
            Point a = poly[i], b = poly[(i + 1) % n];
            // checks two points must not be both above or both below the ray, then the ray cannot pass horizontally through the edge of the polygon
            if((a.y > p.y) != (b.y > p.y)) {
                long s = b.y - a.y;
                long lhs = (p.x - a.x) * s;
                long rhs = (b.x - a.x) * (p.y - a.y);
                boolean hit = (s > 0) ? (lhs < rhs) : (lhs > rhs);
                if(hit)
                    inside = !inside;
            }
        }
        return inside;
    }

    public static long cross(Point a, Point b, Point c) {
        return ((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x));
    }

    public static boolean within(Point a, Point b, Point p) {
        return Math.min(a.x, b.x) <= p.x && p.x <= Math.max(a.x, b.x) && Math.min(a.y, b.y) <= p.y && p.y <= Math.max(a.y, b.y);
    }

    public static class Point {
        long x, y;

        public Point(long a, long b) {
            this.x = a; this.y = b;
        }
    }
}
