import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewRoadQueries {
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
        }, "CSES-New-Road-Queries",
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
        final int n =fr.nextInt(), m = fr.nextInt(), q = fr.nextInt();
        edges = new ArrayList<>();
        queries = new ArrayList<>();
        for (int i = 0; i < m; i++)
            edges.add(new int[] { fr.nextInt(), fr.nextInt() });
        for (int j = 0; j < q; j++)
            queries.add(new int[] { fr.nextInt(), fr.nextInt() , j});
        fw.attachOutput(solve(n, m, q, edges, queries));
        fw.printOutput();
    }

    private static List<int[]> edges, queries;

    private static StringBuilder solve(final int n, final int m, final int q, final List<int[]> edges, final List<int[]> queries) {
        final int high[] = new int[q], low[] = new int[q];
        Set<Integer> s = new HashSet<>();
        for(int i = 0; i < q; i++)
            s.add(i);
        Arrays.fill(high, m);
        Arrays.fill(low, 0);
        while(!s.isEmpty()) {
            Map<Integer, Set<Integer>> bucket = new HashMap<>();
            for(int idx : s) {
                int mid = (low[idx] + high[idx]) >>> 1;
                bucket.putIfAbsent(mid, new HashSet<>());
                bucket.get(mid).add(idx);
            }
            UnionFind uf = new UnionFind(n);
            for(int day = 0; day < m; day++) {
                uf.union(edges.get(day)[0], edges.get(day)[1]);
                if(bucket.containsKey(day)) {
                    for(int idx : bucket.get(day)) {
                        int mid = (low[idx] + high[idx]) >>> 1;
                        if(uf.sameComponent(queries.get(idx)[0], queries.get(idx)[1])) {
                            high[idx] = mid;
                        } else {
                            low[idx] = mid + 1;
                        }
                    }
                }
            }
            s.clear();
            for(int i = 0; i < q; i++) {
                if(low[i] < high[i])
                    s.add(i);
            }
        }
        final int res[] = new int[q];
        for(int i = 0; i < q; i++)
            res[i] = low[i] == m ? -1 : low[i] + 1;
        final StringBuilder output = new StringBuilder();
        for(int r : res)
            output.append(r).append("\n");
        return output;
    }

    public static class UnionFind {
        private final int size;
        private final int parent[], rank[];

        public UnionFind(int n) {
            this.size = n + 1;
            this.parent = new int[size];
            this.rank = new int[size];
            for (int i = 1; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public boolean sameComponent(int x, int y) {
            return find(x) == find(y);
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if(rootX != rootY) {
                if(rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];
                } else {
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];
                }
            }
        }
    }
}
