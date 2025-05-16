
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P7CompanyQueriesII {
    public static class FastReader {
        private final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
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
                x = x * 10 + (c - '0');
            } while((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c <= 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c - '0');
            } while((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        tree = new ArrayList<>();
        queries = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 2; i <= n; i++) {
            int boss = fast.nextInt();
            tree.get(boss).add(i);
            tree.get(i).add(boss);
        }
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n);
    }

    public static void solve(final int n) {
        SparseTable sparseTable = new SparseTable(n);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for(int query[] : queries)
            output.append(sparseTable.lca(query[0], query[1])).append("\n");
        writer.write(output.toString());
        writer.flush();
    }

    public static final class SparseTable {
        public int lift[][];
        public int depth[];
        public int maxLog;

        public SparseTable(final int n) {
            this.maxLog = 32 - Integer.numberOfLeadingZeros(n);
            this.lift = new int[maxLog+1][n+1];
            this.depth = new int[n+1];
            for(int row[] : lift)
                Arrays.fill(row, -1);
            binaryLiftingIterative();
        }

        public void binaryLifting(int root, int parent) {
            this.lift[0][root] = parent;
            for(int k = 1; k <= this.maxLog; k++) {
                if(this.lift[k-1][root] != -1)
                    this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];
                else
                    this.lift[k][root] = -1;
            }
            for(int child : tree.get(root))
                if(child != parent) {
                    this.depth[child] = this.depth[root] + 1;
                    binaryLifting(child, root);
                }
        }

        public void binaryLiftingIterative() {
            Deque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{1, -1});
            while(!stack.isEmpty()) {
                int data[] = stack.pop();
                int root = data[0], parent = data[1];
                if(parent == -1) {
                    this.depth[root] = 0;
                    this.lift[0][root] = root;
                }
                else {
                    this.depth[root] = this.depth[parent] + 1;
                    this.lift[0][root] = parent;
                }
                for(int k = 1; k <= this.maxLog; k++) {
                    if(this.lift[k-1][root] != -1)
                        this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];
                    else
                        this.lift[k][root] = -1;
                }
                for(int child : tree.get(root))
                    if(child != parent)
                        stack.push(new int[]{child, root});
            }
        }

        public int lifting(int root, int k) {
            int ancestor = root;
            for(int i = 0; i <= this.maxLog; i++)
                if((k & (1 << i)) != 0) {
                    ancestor = this.lift[i][ancestor];
                    if(ancestor == -1)
                        break;
                }
            return ancestor;
        }

        public int lca(int x, int y) {
            if(this.depth[x] > this.depth[y])
                x = lifting(x, this.depth[x] - this.depth[y]);
            else if(this.depth[y] > this.depth[x])
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
    }
}
