import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class P21ForestQueriesII {
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
        }, "https://cses.fi/problemset/task/1739/", 1 << 26);
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
        FenwickTree fenwick = new FenwickTree(n, n);
        int[][] grid = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            String s = fr.next();
            for (int j = 1; j <= n; j++) {
                grid[i][j] = s.charAt(j - 1) == '*' ? 1 : 0;
                if (grid[i][j] == 1)
                    fenwick.pointUpdate(i, j, 1);
            }
        }
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            final int type = fr.nextInt();
            if (type == 1)
                queries.add(new int[] { type, fr.nextInt(), fr.nextInt() });
            else
                queries.add(new int[] { type, fr.nextInt(), fr.nextInt(), fr.nextInt(), fr.nextInt() });
        }
        fw.attachOutput(solve(grid, fenwick, queries));
        fw.printOutput();
    }

    private static StringBuilder solve(final int[][] grid, final FenwickTree fenwick, final List<int[]> queries) {
        final StringBuilder output = new StringBuilder();
        for (int q[] : queries) {
            if (q[0] == 1) {
                final int x = q[1], y = q[2];
                if (grid[x][y] == 1) {
                    fenwick.pointUpdate(x, y, -1);
                    grid[x][y] = 0;
                } else {
                    fenwick.pointUpdate(x, y, 1);
                    grid[x][y] = 1;
                }
            } else
                output.append(fenwick.coordinateQuery(q[1], q[2], q[3], q[4])).append("\n");
        }
        return output;
    }

    private static class FenwickTree {
        private final int tree[][];
        private final int n, m;

        public FenwickTree(int a, int b) {
            this.n = a;
            this.m = b;
            this.tree = new int[n + 1][m + 1];
        }

        public void pointUpdate(int x, int y, int delta) {
            for (int i = x; i <= n; i += i & -i)
                for (int j = y; j <= n; j += j & -j)
                    tree[i][j] += delta;
        }

        public int pointQuery(int x, int y) {
            int sum = 0;
            for (int i = x; i > 0; i -= i & -i)
                for (int j = y; j > 0; j -= j & -j)
                    sum += tree[i][j];
            return sum;
        }

        public int coordinateQuery(int x1, int y1, int x2, int y2) {
            return pointQuery(x2, y2) - pointQuery(x2, y1 - 1) - pointQuery(x1 - 1, y2) + pointQuery(x1 - 1, y1 - 1);
        }
    }
}
