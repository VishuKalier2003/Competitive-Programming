import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SocialNetwork {
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
        }, "https://codeforces.com/problemset/problem/1609/D",
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
        final int edges[][] = new int[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = fr.nextInt();
            edges[i][1] = fr.nextInt();
        }
        fw.attachOutput(solve(n, m, edges));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int m, final int edges[][]) {
        UnionFind uf = new UnionFind(n);
        final StringBuilder output = new StringBuilder();
        for (int e[] : edges) {
            uf.union(e[0], e[1]);
            output.append(uf.findMaxConnection()).append("\n");
        }
        return output;
    }

    public static class UnionFind {
        private final int[] parent, rank;
        private int maxComponent, extraEdges;
        private final int size;
        private final SortedMap<Integer, Integer> fMap;

        public UnionFind(int n) {
            this.size = n + 1;
            this.parent = new int[size];
            this.rank = new int[size];
            this.fMap = new TreeMap<>((a, b) -> Integer.compare(b, a));
            for (int i = 1; i <= n; i++) {
                this.parent[i] = i;
                this.rank[i] = 1;
                this.fMap.put(1, fMap.getOrDefault(1, 0) + 1);
            }
            this.maxComponent = 1;
            this.extraEdges = 0;
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rX = find(x), rY = find(y);
            if (rX != rY) {
                remove(rank[rX]);
                remove(rank[rY]);
                if (rank[rX] < rank[rY]) {
                    rank[rY] += rank[rX];
                    push(rank[rY]);
                    parent[rX] = rY;
                } else {
                    rank[rX] += rank[rY];
                    push(rank[rX]);
                    parent[rY] = rX;
                }
                maxComponent = Math.max(maxComponent, Math.max(rank[rX], rank[rY]));
            } else
                extraEdges++;
        }

        public int findMaxConnection() {
            int toTake = extraEdges + 1;
            int sum = 0;
            for (Map.Entry<Integer, Integer> e : fMap.entrySet()) {
                int sz = e.getKey();
                int cnt = e.getValue();
                while (cnt > 0 && toTake > 0) {
                    sum += sz;
                    toTake--;
                    cnt--;
                }
                if (toTake == 0)
                    break;
            }
            return sum - 1;
        }

        private void remove(int key) {
            fMap.put(key, fMap.get(key) - 1);
            if (fMap.get(key) == 0)
                fMap.remove(key);
        }

        private void push(int key) {
            fMap.put(key, fMap.getOrDefault(key, 0) + 1);
        }
    }
}
