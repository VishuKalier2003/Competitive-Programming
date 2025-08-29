import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;

public class RoadsInBerland {
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
        }, "https://codeforces.com/problemset/problem/25/D", 1 << 26);
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
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n - 1; i++)
            edges.add(new int[] { fr.nextInt(), fr.nextInt() });
        fw.attachOutput(solve(n, edges));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final List<int[]> edges) {
        UnionFind uf = new UnionFind(n);
        for (int e[] : edges)
            uf.union(e[0], e[1]);
        int center = uf.roots.getFirst();
        final StringBuilder output = new StringBuilder();
        output.append(uf.roots.size()-1).append("\n");
        for (int root : uf.roots) {
            if (root != center) {
                int edge[] = uf.extraEdges.poll();
                output.append(edge[0]).append(" ").append(edge[1]).append(" ");
                output.append(center).append(" ").append(root).append("\n");
            }
        }
        return output;
    }

    public static class UnionFind {
        private final int[] parent, rank;
        private final int size;
        private final Deque<int[]> extraEdges;
        private final LinkedHashSet<Integer> roots;

        public UnionFind(int n) {
            this.size = n + 1;
            this.parent = new int[this.size];
            this.rank = new int[this.size];
            roots = new LinkedHashSet<>();
            this.extraEdges = new ArrayDeque<>();
            for (int i = 0; i < this.size; i++) {
                parent[i] = i;
                rank[i] = 1;
                if (i != 0)
                    roots.add(i);
            }
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rX = find(x), rY = find(y);
            if (rX == rY) {
                extraEdges.offer(new int[] { x, y });
            }
            if (rX != rY) {
                if (rank[rX] < rank[rY]) {
                    roots.remove(rX);
                    rank[rY] += rank[rX];
                    parent[rX] = rY;
                } else {
                    roots.remove(rY);
                    rank[rX] += rank[rY];
                    parent[rY] = rX;
                }
            }
        }
    }
}
