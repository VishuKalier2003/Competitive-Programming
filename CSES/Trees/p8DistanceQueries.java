
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class p8DistanceQueries {
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

    public static void main(String[] args) throws IOException {     // Input reading main function
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 0; i < n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        queries = new ArrayList<>();
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n);
    }

    public static void solve(final int n) {
        SparseTable sparseTable = new SparseTable(n);       // Sparse Table defined for Binary Lifting
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        int n1, n2;
        for(int query[] : queries) {
            n1 = query[0];
            n2 = query[1];
            int lca = sparseTable.lca(n1, n2);
            output.append(sparseTable.depth[n1] + sparseTable.depth[n2] - (2 * sparseTable.depth[lca])).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static final class SparseTable {     // Note: Sparse Table defined for Binary Lifting
        public int lift[][];
        public int depth[];
        public int maxLog;

        public SparseTable(final int n) {
            this.maxLog = 32 - Integer.numberOfLeadingZeros(n);
            this.lift = new int[maxLog+1][n+1];
            this.depth = new int[n+1];
            for(int row[] : this.lift)
                Arrays.fill(row, -1);
            binaryLiftingIterativeOptimized(n);     // Info: Running the most optimized iterative function for binary lifting
        }

        public void binaryLifting(int root, int parent) {       // Info: O(log n) per node + recursion overheads
            this.lift[0][root] = parent;        
            for(int k = 1; k <= this.maxLog; k++) {
                if(this.lift[k-1][root] != -1)
                    this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];      // Jumping from k-1 power twice to reach k
                else
                    this.lift[k][root] = -1;
            }
            for(int child : tree.get(root))
                if(child != parent) {
                    this.depth[child] = this.depth[root] + 1;
                    binaryLifting(child, root);     // Recursive call
                }
        }

        public void binaryLiftingIterative() {      // Info: O(n log n) with stack p/p overhead
            Deque<int[]> stack = new ArrayDeque<>();        // Using like iterative stack
            stack.push(new int[]{1, -1});
            while(!stack.isEmpty()) {
                int data[] = stack.pop();
                int root = data[0], parent = data[1];
                if(parent == -1) {      // root case
                    this.depth[root] = 0;
                    this.lift[0][root] = -1;
                } else {        // other node case
                    this.depth[root] = this.depth[parent] + 1;
                    this.lift[0][root] = parent;
                }   // Bug: We might iterate a cell more than once
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

        public void binaryLiftingIterativeOptimized(final int n) {      // Info: 1st phase O(n) + 2nd phase O(n log n)
            Deque<Integer> queue = new ArrayDeque<>();
            this.depth[1] = 0;
            this.lift[0][1] = -1;
            queue.add(1);       // Use queue to fill the sparse table with 0 power parent and the depths
            while(!queue.isEmpty()) {       // Note: Array deque is the best for both (stack and queue)
                int node = queue.poll();
                for(int child : tree.get(node)) {
                    if(child == this.lift[0][node])
                        continue;
                    this.depth[child] = this.depth[node] + 1;
                    this.lift[0][child] = node;
                    queue.add(child);
                }
            }
            // Hack: We iterate a cell exactly once by using dp, since we have already filled the 0 power parent of each node
            for(int k = 1; k <= this.maxLog; k++)
                for(int node = 1; node <= n; node++) {
                    if(this.lift[k-1][node] != -1)
                        this.lift[k][node] = this.lift[k-1][this.lift[k-1][node]];
                    else
                        this.lift[k][node] = -1;
                }
        }

        public int lift(int root, int k) {      // Info: O(log n)
            int ancestor = root;
            for(int i = 0; i <= this.maxLog; i++)
                if((k & (1 << i)) != 0) {
                    ancestor = this.lift[i][ancestor];
                    if(ancestor == -1)      // Early termination
                        break;
                }
            return ancestor;
        }

        public int lca(int node1, int node2) {
            // Make the nodes at same level
            if(this.depth[node1] > this.depth[node2])
                node1 = lift(node1, this.depth[node1] - this.depth[node2]);
            else if(this.depth[node2] > this.depth[node1])
                node2 = lift(node2, this.depth[node2] - this.depth[node1]);
            if(node1 == node2)      // If the two nodes become same at one level, then one of the node from start were lca
                return node1;
            for(int k = this.maxLog; k >= 0; k--)
                // Note: Lifting the node upto the necesary ancestor
                if(this.lift[k][node1] != -1 && this.lift[k][node1] != this.lift[k][node2]) {
                    node1 = this.lift[k][node1];
                    node2 = this.lift[k][node2];
                }
            return this.lift[0][node1];
        }

        public int path(int node1, int node2, int lca) {        // Hack: formula for calculating distance when lca is provided
            return this.depth[node1] + this.depth[node2] - (2 * this.depth[lca]);
        }
    }
}
