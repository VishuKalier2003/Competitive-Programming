import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Bipartite graph
public class UnionFindLevel1 {
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
        }, "Union-Find-Bipartite",
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
        g = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++)
            edges.add(new int[] { fr.nextInt(), fr.nextInt() });
        int color[] = new int[n + 1];
        for (int i = 1; i <= n; i++)
            color[i] = fr.nextInt();
        fw.attachOutput(solve(n, m, edges, color));
        fw.printOutput();
    }

    private static List<List<Integer>> g;

    public static StringBuilder solve(final int n, final int m, final List<int[]> edges, int color[]) {
        UnionFind uf = new UnionFind(n, color); // Union find operation
        for (int e[] : edges) {
            if (uf.union(e[0], e[1])) {
                System.out.println("Weak edge or duplicate edge");
            } else
                System.out.println("Strong edge");
        }
        return new StringBuilder();
    }

    private static class UnionFind {
        private final int[] parent, rank, parity;
        private final int size;

        public UnionFind(int n, int color[]) { // Parametrized constructor
            this.size = n + 1;
            this.parent = new int[size];
            this.parity = new int[size];
            this.rank = new int[size];
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 1;
                parity[i] = color[i];
            }
        }

        public int find(int x) { // find with path compression
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        // Note: can use this logic to connect k color nodes, by checking parity of (1
        // << k) - 1, meaning k bits 1
        public boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            // The parity ensures that the connected nodes are of different color
            if (rootX != rootY && ((parity[rootX] ^ parity[rootY]) == 1)) {
                // Union by rank technique
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootX] = rootY;
                    rank[rootY]++;
                }
                return true; // When strong edge
            }
            return false; // When weak edge
        }
    }
}
