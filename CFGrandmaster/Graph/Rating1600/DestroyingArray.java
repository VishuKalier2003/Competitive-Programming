import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class DestroyingArray {
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
        }, "https://codeforces.com/problemset/problem/722/C",
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
        final int n = fr.nextInt();
        int cost[] = new int[n+1], perm[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            cost[i] = fr.nextInt();
        for(int i = 1; i <= n; i++)
            perm[i] = fr.nextInt();
        fw.attachOutput(solve(n, cost, perm));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int cost[], final int perm[]) {
        final StringBuilder output = new StringBuilder();
        UnionFind uf = new UnionFind(n, cost);
        boolean active[] = new boolean[n+1];
        long ans[] = new long[n+1];
        // Activation of nodes is done in reverse to prevent unnecessary rollbacks
        for(int j = n; j > 1; j--) {
            int node = perm[j];
            active[node] = true;        // activate nodes
            if(node < n && node > 1) {
                // If in middle check both left and right
                if(active[node-1])
                    uf.union(node, node-1);
                if(active[node+1])
                    uf.union(node, node+1);
            } else if(node == n && active[node-1])      // check only left
                uf.union(node, node-1);
            else if(node == 1 && active[node+1])        // check only right
                uf.union(node, node+1);
            ans[j] = uf.getMax(node);       // storing result in answer
        }
        for(int j = 2; j <= n; j++)
            output.append(ans[j]).append("\n");
        return output.append("0");
    }

    public static class UnionFind {
        private final int[] parent, rank;
        private final long[] sum;       // Sum metadata to store the sum of component
        private final int sz;
        private long maxSum;        // max sum among all components

        public UnionFind(int n, int cost[]) {
            this.sz = n+1;
            this.parent = new int[sz];
            this.rank = new int[sz];
            this.sum = new long[sz];
            // initialize each component (single node) with its own sum
            for(int i = 1; i < sz; i++) {
                parent[i] = i;
                sum[i] = cost[i];       
                rank[i] = 1;
            }
            this.maxSum = 0;
        }

        public int find(int x) {        // path compression
            if(parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        // finding the maxSum as the max of single component and the maxSum parameter
        public long getMax(int idx) {
            maxSum = Math.max(maxSum, sum[idx]);
            return maxSum;
        } 

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if(rootX != rootY) {        // Union by size technique
                if(rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];     // union by component size (smaller merged into larger)
                    sum[rootY] += sum[rootX];       // sum of the component values
                } else {
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];     // union by component size (smaller merged into larger)
                    sum[rootX] += sum[rootY];       // sum of the component values
                }
                maxSum = Math.max(maxSum, Math.max(sum[rootX], sum[rootY]));
            }
        }
    }
}
