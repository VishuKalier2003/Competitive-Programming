import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P15FixedLenghtI {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
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
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },
                "fixed-length-paths-I",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt();
        tree = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int u = fast.nextInt(), v = fast.nextInt();
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        solve(n, k);
    }

    public static void solve(final int n, final int k) {
        CentroidDecomposition cd = new CentroidDecomposition(n, k);
        final StringBuilder output = new StringBuilder();
        output.append(cd.getAns());
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    // Hack: Use Centroid Decomposition to answer k distance queries efficiently by creating the centroid tree recursively
    public static final class CentroidDecomposition {
        private final boolean dead[];       // Pruning centroid nodes
        private final int subtree[];        // Subtree count
        private final int n, k;
        private long ans = 0; 

        public CentroidDecomposition(final int n, final int k) {
            this.n = n;
            this.k = k;
            this.dead = new boolean[this.n + 1];
            this.subtree = new int[this.n + 1];
            decompose(1);       // Info: Start recursive decompositions from node 1
        }

        // Hack: Reduces the problem recusively by half hence O(log n)
        public void decompose(int root) {
            dfsIterative(root, -1);         // Note: Perform subtree count on the given tree or subtree iteratively (preferred)
            int size = this.subtree[root];              // Size of the subtree rooted at root
            int centroid = centroid(root, -1, size);        // Info: From the given root, we will find the centroid of the subproblem (subtree)
            // Note: Count paths of size k arising from the centroid
            countPaths(centroid);
            this.dead[centroid] = true;         // Prune the centroid
            for (int child : tree.get(centroid))
                if (!this.dead[child])
                    decompose(child);           // Info: Recursive call on the available nodes
        }

        public void dfsIterative(int r, int p) {
            ArrayDeque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[] { r, p, 0 });      // Info: storing root, parent and phase
            while (!stack.isEmpty()) {
                int data[] = stack.pop();
                int root = data[0], parent = data[1], phase = data[2];
                if (phase == 0) {           // Pre order phase
                    this.subtree[root] = 1;
                    stack.push(new int[] { root, parent, 1 });
                    for (int child : tree.get(root))
                        if (child != parent)
                            stack.push(new int[] { child, root, 0 });
                } else {        // Post order phase
                    if (parent > 0)
                        this.subtree[parent] += this.subtree[root];
                }
            }
        }

        // Hack: Recursively find the centroid in O(log n)
        public int centroid(int root, int parent, int componentSize) {
            for (int child : tree.get(root))
                if (child != parent && this.subtree[child] > componentSize / 2)
                    return centroid(child, root, componentSize);
            return root;
        }

        // Hack: Collecting depths recursively by going till the depth defined as per the constraints and pruning after that
        public void collectDepths(int root, int parent, int depth, List<Integer> dist) {
            if (depth > this.k)     // Pruning criteria
                return;
            // Info: These distances added will be then collected by the map for checking the valid paths
            dist.add(depth);        // Add the distance into the list
            for (int child : tree.get(root))
                if (child != parent && !this.dead[child])
                    collectDepths(child, root, depth + 1, dist);
        }

        public void collectDepthsIterative(int start, int parent, int maxDepth, List<Integer> dist) {
            ArrayDeque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[] { start, parent, 1 });     // Info: recursion stores root, parent and depth
            while (!stack.isEmpty()) {
                int[] e = stack.pop();
                int u = e[0];
                int p = e[1];
                int depth = e[2];
                if (depth > maxDepth)
                    continue; // prune anything deeper than k
                dist.add(depth);
                for (int v : tree.get(u))
                    if (v != p && !dead[v])
                        stack.push(new int[] { v, u, depth + 1 });
            }
        }

        // Note: The main engine of the Centroid Decomposition
        public void countPaths(int centroid) {
            Map<Integer, Integer> depthCount = new HashMap<>();
            depthCount.put(0, 1); // Info: Distance 0 at centroid itself
            for (int child : tree.get(centroid)) {
                if (!this.dead[child]) {        // For each child (subtree) that is not dead
                    List<Integer> dist = new ArrayList<>();
                    // Info: Store all the distances that are feasible to reach from the child node till distance k and store it in dist
                    collectDepthsIterative(child, centroid, this.k, dist);
                    for (int d : dist)
                        // Info: for each d, find the number of distances k-d, and add them to the answer
                        this.ans += depthCount.getOrDefault(this.k - d, 0);
                    // Note: We add the subtree into consideratio  hence do +1 as the number of ways for each distance
                    for (int d : dist)
                        depthCount.put(d, depthCount.getOrDefault(d, 0) + 1);   // Note: for aggregation
                }
            }
        }

        public long getAns() {
            return this.ans;
        }
    }
}
