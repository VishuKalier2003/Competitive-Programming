
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class BinaryLifting {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static List<List<Integer>> tree;

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 0; i < n-1; i++) {
            int node1 = fast.nextInt(), node2 = fast.nextInt();
            tree.get(node1).add(node2);
            tree.get(node2).add(node1);
        }
    }

    public static class TreeTable {
    public int LOG;                  // Maximum power of 2 needed for n nodes (i.e., log2(n))
    public int lift[][];            // lift[k][v] stores the 2^k-th ancestor of node v
    public int depth[];             // depth[v] stores depth of node v from root (root has depth 0)

    public TreeTable(final int n) {
        // Calculate the maximum power of 2 required (ceil(log2(n)))
        this.LOG = 32 - Integer.numberOfLeadingZeros(n);

        // Allocate memory for binary lifting table and initialize
        // Size: (LOG+1) rows for 0 to LOG powers, n+1 columns for 1-based nodes
        this.lift = new int[LOG+1][n+1];
        this.depth = new int[n+1];

        // Initialize all lift values to -1 (no ancestor)
        for (int[] row : lift)
            Arrays.fill(row, -1);
    }

    // Depth-First Search to preprocess the 'lift' and 'depth' arrays
    public void dfs(int root, int parent) {
        // Base case: 2^0-th ancestor of 'root' is its immediate parent
        this.lift[0][root] = parent;
        // Fill in the 2^k-th ancestors using dynamic programming
        for (int k = 1; k <= this.LOG; k++) {
            if (this.lift[k - 1][root] != -1) {
                // up[k][node] = up[k-1][up[k-1][node]]
                // Meaning: 2^k-th ancestor is obtained by jumping two 2^(k-1) steps
                this.lift[k][root] = this.lift[k - 1][this.lift[k - 1][root]];
            } else {
                this.lift[k][root] = -1; // No such ancestor exists
            }
        }

        // Recur for all children that are not the parent (to avoid going back up)
        for (int child : tree.get(root)) {
            if (child != parent) {
                // Update child depth as parent depth + 1
                this.depth[child] = this.depth[root] + 1;
                // Recursive DFS on the subtree
                dfs(child, root);
            }
        }
    }

    // Lifts a node 'node' by 'k' levels up using binary lifting
    public int liftNode(int node, int k) {
        int ancestor = node;
        for (int i = 0; i <= this.LOG; i++) {
            // Check if the i-th bit is set in k
            if (((k >> i) & 1) == 1) {
                // Jump 2^i steps up
                ancestor = this.lift[i][ancestor];
                if (ancestor == -1) break; // Early termination if ancestor doesn't exist
            }
        }
        return ancestor;
    }

    // Computes the Lowest Common Ancestor (LCA) of node1 and node2
    public int lca(int node1, int node2) {
        // Step 1: Equalize the depths
        if (this.depth[node1] < this.depth[node2]) {
            node2 = liftNode(node2, this.depth[node2] - this.depth[node1]);
        } else if (this.depth[node1] > this.depth[node2]) {
            node1 = liftNode(node1, this.depth[node1] - this.depth[node2]);
        }
        // Step 2: If one is ancestor of the other
        if (node1 == node2) return node1;
        // Step 3: Lift both nodes together until their ancestors diverge
        for (int k = this.LOG; k >= 0; k--) {
            // Until ancestor is possible, and until they do not get a common ancestor
            if (this.lift[k][node1] != -1 && this.lift[k][node1] != this.lift[k][node2]) {
                node1 = this.lift[k][node1];
                node2 = this.lift[k][node2];
            }
        }
        // Step 4: Return the parent of the diverging point which is the LCA
        return this.lift[0][node1];     // return immediate parent hence 2^0 of any of the node
    }
}

}
