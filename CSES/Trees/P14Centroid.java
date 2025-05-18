import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class P14Centroid {
    // Hack: Fastest Input Output reading in Java
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;
        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }
        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static List<List<Integer>> tree;

    public static void main(String[] args) {
        @SuppressWarnings("CallToPrintStackTrace")
        Thread t = new Thread(null, () -> {
            try { callMain(args); }
            catch (IOException e) { e.printStackTrace(); }
        }, 
        "centroid", 
        1<<26);
        t.start();
        try { t.join(); }
        catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        int n = fast.nextInt();
        tree = new ArrayList<>(n+1);
        for (int i = 0; i <= n; i++) 
            tree.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int u = fast.nextInt(), v = fast.nextInt();
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        solve(n);
    }

    public static void solve(int n) {
        CentroidDecomposition cd = new CentroidDecomposition(n);        // Object created
        final StringBuilder output = new StringBuilder();
        output.append(cd.getCentroid());
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    // Hack: Centroid Decomposition is used to find the centroid and build centroid tree in O(n log n) where the n x n edges are turned into n x log n edges in centroid tree
    public static final class CentroidDecomposition {
        private final int n;
        private final int subtree[];        // Info: Counting the subtree sizes for each node
        private int centroid;       // stores the centroid (at least 1)

        public CentroidDecomposition(final int n) {
            this.n = n;
            this.subtree = new int[n+1];
            this.centroid = -1;
            dfsIterative();     // Perform Iterative dfs
            this.centroid = centroid(1, 0);
        }

        public void dfs(int root, int parent) {     // Note: Post Order O(n) dfs to fill the subtree sizes
            this.subtree[root] = 1;
            for(int child : tree.get(root))
                if(child != parent) {
                    dfs(child, root);
                    this.subtree[root] += this.subtree[child];
                }
        }

        public void dfsIterative() {
            ArrayDeque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{1, 0, 0});
            while(!stack.isEmpty()) {
                int data[] = stack.pop();
                int root = data[0], parent = data[1], phase = data[2];
                // Info: Pre order
                if(phase == 0) {
                    this.subtree[root] = 1;
                    stack.push(new int[]{root, parent, 1});     // Note: put this pre order processed data into the stack with phase as 1 (post order)
                    for(int child : tree.get(root))
                        if(child != parent)
                            stack.push(new int[]{child, root, 0});      // Note: Marking child nodes for pre order
                // Info: Post order
                } else if(phase == 1) {     // Info: The processed pre order nodes enters for post order computation
                    if(parent != 0)
                        this.subtree[parent] += this.subtree[root];
                }
            }
        }

        public int centroid(int root, int parent) {
            for(int child : tree.get(root))
                // Hack: If any one child node contains subtree size more than n/2, we shift towards there to balance the tree (finding centroid)
                if(child != parent && this.subtree[child] > n/2)
                    return centroid(child, root);
            return root;        // Otherwise it is the node itself
        }

        public int getCentroid() {return this.centroid;}
    }
}
