
/**
 * Problem - https://codeforces.com/problemset/problem/977/E?csrf_token=37fc59658fb9c686afdfe022d2c3b954&__cf_chl_tk=NYRiTkH.9cZ0fEfyVJk2kt1DnTXcyAsSfaAuxxlU._U-1754584573-1.0.1.1-P6mmyodB8DWG8Px3nYxxblby4i0_L5FxnKMkMvViKhY
 * Algo - Union FInd
 */

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CyclicComponents {
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
        }, "Cyclic-Components-(977E)",
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
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            final int u = fr.nextInt(), v = fr.nextInt();
            dsu.union(u, v);
            g.get(u).add(v);
            g.get(v).add(u);
        }
        fw.attachOutput(solve(n, dsu));
        fw.printOutput();
    }

    private static List<List<Integer>> g;

    public static StringBuilder solve(final int n, final DSU dsu) {
        boolean vis[] = new boolean[n + 1];
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (!vis[i]) {
                if (isCycleComponent(i, vis)) {
                    count++;
                }
            }
        }
        return new StringBuilder().append(count);
    }

    public static boolean isCycleComponent(int source, boolean[] visited) {
        // Info: travel the entire component, do not return in between, else the lefover nodes will be calculated again
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(source);
        visited[source] = true;
        int nodes = 0, edges = 0;
        boolean allDegTwo = true;   
        while (!q.isEmpty()) {
            int node = q.poll();
            nodes++;
            int deg = g.get(node).size();       // Add all the edges into edge count (will be added twice)
            edges += deg;
            if (deg != 2)       // The degree of each node should be 2
                allDegTwo = false;
            for (int neighbor : g.get(node)) {
                // Micro-optimisation: Mark visited, so that it is not traversed again
                if (!visited[neighbor]) {
                    visited[neighbor] = true;       
                    q.add(neighbor);
                }
            }
        }
        edges /= 2; // because undirected, reduce by half
        return allDegTwo && edges == nodes;     // And number of nodes should be number of edges
    }

    public static class DSU {
        public int n;
        private final int rank[], parent[];

        public DSU(int n) {
            this.n = n;
            this.parent = new int[n + 1];
            this.rank = new int[n + 1];
            for (int i = 1; i <= n; i++)
                this.parent[i] = i;
            Arrays.fill(rank, 1);
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];
                } else if (rank[rootY] <= rank[rootX]) {
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];
                }
            }
        }
    }
}
