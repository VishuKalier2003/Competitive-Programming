import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P7ConvexHull {
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
        }, "Convex-Hull-(https://cses.fi/problemset/task/2192)", 1 << 26);
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
        final int n = fr.nextInt();
        Point polygon[] = new Point[n];
        for (int i = 0; i < n; i++)
            polygon[i] = new Point(fr.nextInt(), fr.nextInt());
        solve(n, polygon);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    // Note: Andrew's Monotone Chain Algorithm (Convex Hull)
    public static void solve(final int n, final Point points[]) {
        // Sort on basis of first x-coordinate then on basis of y-coordinate (ensures we can build lower and upper half safely)
        Arrays.sort(points, (a,b) -> a.x != b.x ? Long.compare(a.x, b.x) : Long.compare(a.y, b.y));
        List<Point> hull = new ArrayList<>();
        // Info: Builds lower part of Cnvex hull from left to right
        for(Point p : points) {
            // Convexity check - When orientation is -ve, turns right (not convex), so out of orientation(A, B, C) we pop the node B from the list
            while(hull.size() >= 2 && orientation(hull.get(hull.size()-2), hull.get(hull.size()-1), p) < 0)
                hull.remove(hull.size()-1);
            hull.add(p);
        }
        // Info: Builds upper part of Convex Hull from right to left
        int t = hull.size();
        for(int i = points.length-2; i >= 0; i--) {
            Point p = points[i];    // extract current point
            // Convexity check - When orientation is -ve, turns right (not convex), so out of orientation (A, B, C) we pop the node B from the list
            while(hull.size() > t && orientation(hull.get(hull.size()-2), hull.get(hull.size()-1), p) < 0)
                // Size not lower than t, to ensure that lower hull does not gets discarded
                hull.remove(hull.size()-1);
            hull.add(p);
        }
        hull.remove(hull.size()-1);     // Info: remove the last element that is duplication of the first
        print(hull);
    }

    private static void print(List<Point> hull) {
        output.append(hull.size()).append("\n");
        for(Point vertex : hull)
            output.append(vertex.x).append(" ").append(vertex.y).append("\n");
    }

    private static long orientation(Point p1, Point p2, Point p3) {
        return ((p2.x - p1.x) * (p3.y - p1.y)) - ((p2.y - p1.y) * (p3.x - p1.x));
    }

    public static final class Point {       // Point class
        final long x, y;

        public Point(long a, long b) {
            this.x = a;
            this.y = b;
        }

        public long X() {
            return x;
        }

        public long Y() {
            return y;
        }
    }
}
