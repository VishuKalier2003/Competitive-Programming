import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CfSlothNaptime {
    public static class FastReader {            // Note: Fastest Input reader for Java (working on byte and not String)
        private final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;        // Note: Converting to ASCII character between 0 to 255 (Hexadecimal code 0xff)
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) >= '0' && c <= '9');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) >= '0' && c <= '9');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String[] args) {
        Thread threadIII = new Thread(
            null, () -> {
                try {
                    callMain(args);
                } catch(IOException e) {
                    e.getLocalizedMessage();
                }
            }, "sloth-queries",
            1 << 26);
        threadIII.start();
        try {
            threadIII.join();
        } catch(InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String[] args) throws IOException {     // Input reading main function
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        final int q = fast.nextInt();
        queries = new ArrayList<>();
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt(), fast.nextInt()});
        solve(n);
    }

    public static void solve(final int n) {
        BinaryLifting bLifting = new BinaryLifting(n);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for(int query[] : queries) {
            int n1 = query[0], n2 = query[1], c = query[2];
            int lca = bLifting.lca(n1, n2);
            int dist = bLifting.distance(n1, n2, lca);
            int delta1 = Math.abs(bLifting.depth[n1] - bLifting.depth[lca]);
            if(dist <= c)
                output.append(n2);
            else {
                if(delta1 >= c)
                    output.append(bLifting.lifting(n1, c));
                else
                    output.append(bLifting.lifting(n2, dist-c)); 
            }
            output.append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static final class BinaryLifting {
        private final int lift[][];
        private final int depth[];
        private final int n, maxLog;

        public BinaryLifting(final int n) {
            this.n = n;
            this.maxLog = 32 - Integer.numberOfLeadingZeros(this.n);
            this.lift = new int[this.maxLog+1][this.n+1];
            this.depth = new int[this.n+1];
            for(int row[] : this.lift)
                Arrays.fill(row, -1);
            binaryLiftTable(1, -1);
        }

        public void binaryLiftTable(int root, int parent) {
            if(parent == -1)
                this.depth[root] = 0;
            else
                this.depth[root] = this.depth[parent] + 1;
            this.lift[0][root] = parent;
            for(int k = 1; k <= this.maxLog; k++) {
                if(this.lift[k-1][root] != -1)
                    this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];
                else
                    this.lift[k][root] = -1;
            }
            for(int child : tree.get(root))
                if(child != parent)
                    binaryLiftTable(child, root);
        }

        public int lifting(int root, int k) {
            int ancestor = root;
            for(int i = 0; i <= this.maxLog; i++)
                if((k & (1 << i)) != 0) {
                    if(ancestor == -1)
                        return -1;
                    ancestor = this.lift[i][ancestor];
                }
            return ancestor;
        }

        public int lca(int x, int y) {
            if(this.depth[x] > this.depth[y])
                x = lifting(x, this.depth[x] - this.depth[y]);
            else if(this.depth[x] < this.depth[y])
                y = lifting(y, this.depth[y] - this.depth[x]);
            if(x == y)
                return x;
            for(int k = this.maxLog; k >= 0; k--) {
                if(this.lift[k][x] != -1 && this.lift[k][x] != this.lift[k][y]) {
                    x = this.lift[k][x];
                    y = this.lift[k][y];
                }
            }
            return this.lift[0][x];
        }

        public int distance(int x, int y, int lca) {
            return this.depth[x] + this.depth[y] - (2 * this.depth[lca]);
        }
    }
}