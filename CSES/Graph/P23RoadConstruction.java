import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P23RoadConstruction {
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
        }, "Road-Construction",
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
        int edges[][] = new int[m][2];
        for(int i = 0; i < m; i++) {
            edges[i][0] = fr.nextInt();
            edges[i][1] = fr.nextInt();
        }
        fw.attachOutput(solve(n, m, edges));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int m, final int edges[][]) {
        UnionFind uf = new UnionFind(n);
        final StringBuilder output = new StringBuilder();
        for(int e[] : edges) {
            uf.union(e[0], e[1]);       // Union the two different components
            output.append(uf.components).append(" ").append(uf.maxSize).append("\n");
        }
        return new StringBuilder().append(output);
    }

    private static class UnionFind {
        private final int n;
        private int maxSize, components;        // variables to store max size and components count
        private final int parent[], rank[];

        public UnionFind(int n) {
            this.n = n;
            // Creating arrays for 1 based indexing
            this.parent = new int[n+1];
            this.rank = new int[n+1];
            Arrays.fill(rank, 1);       // Initially each component is of size 1
            for(int i = 1; i <= n; i++)         // Info: Marking the parents with themselves
                parent[i] = i;
            this.maxSize = 1;
            this.components = this.n;
        }

        // Info: If the index does not correspond to parent, then it is not root node of the component
        public int find(int x) {        // Note: Time Complexity - O(f(n)), Ackkermann function
            if(parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);       // finding nodes via recursion
            // If two different components are being merged
            if(rootX != rootY) {
                // Info: The lower rank (subtree) component is merged with the larger rank (subtree) component
                if(rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];     // Adding the rank
                    this.maxSize = Math.max(maxSize, rank[rootX]);      // Finding max component size
                } else if(rank[rootX] <= rank[rootY]) {
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];
                    this.maxSize = Math.max(maxSize, rank[rootY]);
                }
                components--;       // When different components are merged, component count is reduced by 1
            }
        }
    }
}
