import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathQueries {
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
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nexLong() throws IOException { // reading long
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
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
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
        }, "https://codeforces.com/problemset/problem/1213/G",
                1 << 26);
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
        final int n = fr.nextInt(), m = fr.nextInt();
        List<int[]> edges = new ArrayList<>(), queries = new ArrayList<>();
        for (int i = 0; i < n - 1; i++)
            edges.add(new int[] { fr.nextInt(), fr.nextInt(), fr.nextInt() });
        for (int i = 0; i < m; i++)
            queries.add(new int[] { fr.nextInt(), i });
        fw.attachOutput(solve(n, m, edges, queries));
        fw.printOutput();
    }

    public static StringBuilder solve(int n, final int m, final List<int[]> edges, final List<int[]> queries) {
        UnionFind uf = new UnionFind(n);
        Collections.sort(queries, (a, b) -> Integer.compare(a[0], b[0])); // sort by query value (not index)
        Collections.sort(edges, (a, b) -> Integer.compare(a[2], b[2])); // sort by weights
        int ei = 0, E = edges.size();
        final long res[] = new long[m];
        for (int[] q : queries) {
            int limit = q[0], qIdx = q[1];
            while (ei < E && edges.get(ei)[2] <= limit) {
                uf.union(edges.get(ei)[0], edges.get(ei)[1]);
                ei++;
            }
            res[qIdx] = uf.countPairs();
        }
        final StringBuilder output = new StringBuilder();
        for (long r : res)
            output.append(r).append(" ");
        return output;
    }

    private static class UnionFind {
        private final int[] parent;
        private final long[] size;
        private long totalPairs;

        public UnionFind(int n) {
            parent = new int[n + 1];
            size = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
            totalPairs = 0L;
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry)
                return;
            if (size[rx] < size[ry]) {
                int tmp = rx;
                rx = ry;
                ry = tmp;
            }
            // remove old contributions
            totalPairs -= (size[rx] * (size[rx] - 1)) / 2;
            totalPairs -= (size[ry] * (size[ry] - 1)) / 2;

            // merge
            parent[ry] = rx;
            size[rx] += size[ry];

            // add new contribution
            totalPairs += (size[rx] * (size[rx] - 1)) / 2;
        }

        public long countPairs() {
            return totalPairs;
        }
    }

}
