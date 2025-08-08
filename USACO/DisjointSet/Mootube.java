import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Mootube {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {this.buffer = new BufferedReader(new FileReader("mootube.in"));}

        @SuppressWarnings("CallToPrintStackTrace")
        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() throws IOException {
            this.pw = new PrintWriter(new FileWriter("mootube.out"));
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
        }, "Mootube",
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
        final int n = fr.nextInt(), q = fr.nextInt();
        edges = new ArrayList<>();
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < n - 1; i++)
            edges.add(new Edge(fr.nextInt(), fr.nextInt(), fr.nextInt()));
        for (int i = 0; i < q; i++)
            queries.add(new int[] { fr.nextInt(), fr.nextInt(), i });
        fw.attachOutput(solve(n, q, queries));
        fw.printOutput();
    }

    private static List<Edge> edges;

    public static class Edge {
        private final int node1, node2, relevance;

        public Edge(int n1, int n2, int r) {
            this.node1 = n1;
            this.node2 = n2;
            this.relevance = r;
        }

        public int node1() {
            return node1;
        }

        public int node2() {
            return node2;
        }

        public int relevance() {
            return relevance;
        }
    }

    public static StringBuilder solve(final int n, final int q, final List<int[]> queries) {
        Collections.sort(edges, (a, b) -> Integer.compare(b.relevance, a.relevance));
        Collections.sort(queries, (a, b) -> Integer.compare(b[0], a[0]));

        final StringBuilder output = new StringBuilder();
        int edgePtr = 0;
        DSU dsu = new DSU(n);
        final int ans[] = new int[q];

        for (int[] query : queries) {
            int k = query[0], node = query[1], index = query[2];
            while (edgePtr < edges.size() && edges.get(edgePtr).relevance >= k) {
                Edge e = edges.get(edgePtr++);
                dsu.union(e.node1, e.node2);
            }
            ans[index] = dsu.rank[dsu.find(node)] - 1; // exclude itself
        }

        for (int i = 0; i < q; i++)
            output.append(ans[i]).append("\n");
        return output;
    }

    public static class DSU {
        private final int size;
        private final int parent[], rank[];

        public DSU(int n) {
            this.size = n + 1;
            this.parent = new int[this.size];
            this.rank = new int[this.size];
            for (int i = 1; i < this.size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public int union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];
                } else {
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];
                }
            }
            return Math.max(rank[rootX], rank[rootY]);
        }
    }
}