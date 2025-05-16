
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P9CountingPaths {
    public static class FastReader {        // Note: Fastest Input reading technique in Java
        private static final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;        // Converting to 0 to 255 (ASCII codes)
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
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), m = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        queries = new ArrayList<>();
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        for(int i = 0; i < m; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n);
    }

    public static int lmos[], paths[];

    public static void solve(final int n) {
        SparseTable sparseTable = new SparseTable(n);
        lmos = new int[n+1];
        paths = new int[n+1];
        for(int query[] : queries) {
            int n1 = query[0], n2 = query[1];
            int lca = sparseTable.lca(n1, n2);
            lmos[n1]++;
            lmos[n2]++;
            lmos[lca]--;        // Info: Remove the double addition (+2) from n1 and n2, since a node in path should be counted only once
            if(sparseTable.lift[0][lca] != -1 && sparseTable.lift[0][lca] != lca)
                // Hack: We do not want path to extend above from the lca, so we subtract the remaining addition left (+1) from the parent of lca
                lmos[sparseTable.lift[0][lca]]--;   // When parent exists and it is not lca
        }
        postDfs(sparseTable);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for(int i = 1; i <= n; i++)
            output.append(paths[i]).append(" ");
        writer.write(output.toString());
        writer.flush();
    }

    public static void postDfs(SparseTable sparseTable) {
        Deque<Integer> stack = new ArrayDeque<>(), post = new ArrayDeque<>();
        stack.push(1);
        while(!stack.isEmpty()) {
            int node = stack.pop();
            post.push(node);
            for(int child : tree.get(node))
                if(child != sparseTable.lift[0][node])
                    stack.push(child);
        }
        while(!post.isEmpty()) {
            int node = post.pop();
            int parent = sparseTable.lift[0][node];     // Info: Extract parent post order wise
            if(parent != node && parent > 0)
                lmos[parent] += lmos[node];     // Note: We first propogate lmos values from node to parent
            paths[node] = lmos[node];       // Note: Now we just assign the lmos values to the paths array
        }
    }

    // Note: Rest is the same Sparse Table technique for Binary Lifting
    public static final class SparseTable {
        public int lift[][];
        public int depth[];
        public int maxLog;

        public SparseTable(final int n) {
            this.maxLog = 32 - Integer.numberOfLeadingZeros(n);
            this.lift = new int[maxLog+1][n+1];
            this.depth = new int[n+1];
            for(int row[] : this.lift)
                Arrays.fill(row, -1);
            binaryLiftingIterativeOptimised(n);
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
                } else {
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

        public void binaryLiftingIterativeOptimised(final int n) {
            Deque<Integer> queue = new ArrayDeque<>();
            queue.add(1);
            this.depth[1] = 0;
            this.lift[0][1] = -1;
            while(!queue.isEmpty()) {
                int node = queue.poll();
                for(int child : tree.get(node)) {
                    if(child == this.lift[0][node])
                        continue;
                    this.depth[child] = this.depth[node] + 1;
                    this.lift[0][child] = node;
                    queue.add(child);
                }
            }
            for(int k = 1; k <= this.maxLog; k++)
                for(int node = 1; node <= n; node++) {
                    if(this.lift[k-1][node] != -1)
                        this.lift[k][node] = this.lift[k-1][this.lift[k-1][node]];
                    else
                        this.lift[k][node] = -1;
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
            else if(this.depth[x] < this.depth[y])
                y = lifting(y, this.depth[y] - this.depth[x]);
            if(x == y)
                return x;
            for(int k = this.maxLog; k >= 0; k--)
                // Hack: We will lift both nodes from same level to an above level where the 0 power parent is the lca
                if(this.lift[k][x] != -1 && this.lift[k][x] != this.lift[k][y]) {
                    x = this.lift[k][x];
                    y = this.lift[k][y];
                }
            return this.lift[0][x];
        }
    }
}
