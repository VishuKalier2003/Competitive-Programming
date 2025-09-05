import java.io.*;
import java.util.*;

public class P6EuclideanDistance {
    // =================== FastReader ===================
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20]; // 1MB buffer
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
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            long x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    // =================== FastWriter ===================
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

    // =================== Point ===================
    static class Point {
        long x, y;
        Point(long x, long y) { this.x = x; this.y = y; }
    }

    // =================== Main ===================
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Euclidean-Distance-(https://cses.fi/problemset/task/2194)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final StringBuilder output = new StringBuilder();

    public static void callMain(String[] args) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        int n = fr.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(fr.nextLong(), fr.nextLong());
        }

        Arrays.sort(points, Comparator.comparingLong(p -> p.x));

        TreeSet<Point> active = new TreeSet<>((a, b) -> {
            if (a.y == b.y) return Long.compare(a.x, b.x);
            return Long.compare(a.y, b.y);
        });

        long best = dist2(points[0], points[1]); // start with first pair
        active.add(points[0]);
        active.add(points[1]);
        int left = 0;

        for (int i = 2; i < n; i++) {
            Point p = points[i];

            // Remove points too far in x
            while (left < i && (p.x - points[left].x) * (p.x - points[left].x) > best) {
                active.remove(points[left]);
                left++;
            }

            long d = (long) Math.ceil(Math.sqrt(best));
            Point low = new Point(Long.MIN_VALUE, p.y - d);
            Point high = new Point(Long.MAX_VALUE, p.y + d);

            for (Point q : active.subSet(low, true, high, true)) {
                long dist2 = dist2(p, q);
                if (dist2 < best) best = dist2;
            }

            active.add(p);
        }

        output.append(best).append("\n");
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static long dist2(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        return dx * dx + dy * dy;
    }
}
