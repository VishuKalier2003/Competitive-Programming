// Todo: CSES Problem - Path Queries I (https://cses.fi/problemset/task/1138)

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeavyLightDecompose {
    // Hack: Fastest Input Output reading in Java
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;        // Reading data between 0 to 255 characters
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
                x = x * 10 + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static List<List<Integer>> tree;     // Tree list
    public static List<int[]> queries;          // Queries list

    public static void main(String args[]) {
        Thread overflowThread = new Thread(null, // Thread without thread-manager
        () -> {
            try {
                callMain(args);     // Note: Calling the main function inside thread
            } catch(IOException e) {
                e.getLocalizedMessage(); 
            }
        },
        "HLD-Threading",        // random thread name
        1 << 26);           // Give 1 << 26 (64 MB)
        overflowThread.start();         // Info: start the thread
        try {
            overflowThread.join();      // Hack: Ensures JVM waits for your overflowThread and does not ends immediately
        } catch(InterruptedException iE) {
            Thread.currentThread().interrupt();     // Error handling
        }
    }

    // Info: Input reading
    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        tree = new ArrayList<>();
        queries = new ArrayList<>();
        final int n = fast.nextInt(), q = fast.nextInt();
        long initial[] = new long[n+1];
        for(int i = 1; i <= n; i++)
            initial[i] = fast.nextInt();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        for(int i = 0; i < q; i++) {
            int query = fast.nextInt();
            if(query == 1)
                queries.add(new int[]{query, fast.nextInt(), fast.nextInt()});
            else
                queries.add(new int[]{query, fast.nextInt()});
        }
        solve(n, initial);
    }

    public static void solve(final int n, final long initial[]) {
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        HighLevelDecomposition hldTechnique = new HighLevelDecomposition(n, initial);
        System.out.println("HLD base : "+Arrays.toString(hldTechnique.baseArray));
        System.out.println("HLD Pos array : "+Arrays.toString(hldTechnique.pos));
        for(int query[] : queries) {
            if(query[0] == 1)
                hldTechnique.updateDecomposition(query[1], query[2]);
            else
                output.append(hldTechnique.sumDecomposition(1, query[1])).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    // Hack: This High Level Decomposition is the technique where we need to perform queries on two nodes where one node may not always be root, it is a more complex format of euler tour (euler tour flattens for a subtree), HLD flattens for path queries
    public static final class HighLevelDecomposition {
        private final int n;
        private final int parent[],         // Storing the parent of each node
                        subtree[],          // Counting subtree size for each node inclusive
                        heavyChild[],       // Storing the heavy chain child for each node (Used during decomposition)
                        depth[],            // Stores the depth of each node from root
                        pos[],              // Stores the position of each node in flattened array (fenwick tree is accessed via this array)
                        chainHead[];        // Storing the head (top) node of the chain in which the current node is placed for each node 
        private final long baseArray[];        // Hack: We always access base array from pos array and not directly, since it is flattened tree
        private int currentPos;
        private final Fenwick fenwick;         // Hack: All fenwick operations also will be applied by accessing the pos index

        public HighLevelDecomposition(final int n, final long initial[]) {
            this.n = n;
            this.parent = new int[n+1];
            this.subtree = new int[n+1];
            this.heavyChild = new int[n+1];
            this.depth = new int[n+1];
            this.pos = new int[n+1];
            this.chainHead = new int[n+1];
            this.baseArray = new long[n+1];
            Arrays.fill(this.heavyChild, -1); 
            dfs(1, 0);
            // Note: 1 based indexing change reflected in currentPos and decompose
            currentPos = 1;
            decompose(1, 1);        // Info: Start the decomposition from the root
            // dfsIterative(1);
            // decomposeIterative();
            for(int i = 1; i <= n; i++)     // Fill the base-array with initial values
                this.baseArray[this.pos[i]] = initial[i];
            // Hack: Create a fenwick tree on base arrays
            fenwick = new Fenwick(this.baseArray);
        }

        // Hack: In dfs we fill arrays of parent, depth, subtree and heavyChild
        public int dfs(int root, int parent) {
            // Fill the parent array and assign the subtree values
            this.parent[root] = parent;
            this.subtree[root] = 1;
            int maxSize = 0;
            for(int child : tree.get(root)) {
                if(child != parent) {
                    this.depth[child] = this.depth[root] + 1;       // update depth
                    int childSize = dfs(child, root);
                    this.subtree[root] += childSize;        // Info: Post orderly update the subtree
                    if(childSize > maxSize) {
                        this.heavyChild[root] = child;      // Info: Update the heavyChild with the node value having maximum subtree size
                        maxSize = childSize;
                    }
                }
            }
            return this.subtree[root];      // return the subtree of root value
        }

        public void dfsIterative(int root) {
            ArrayDeque<int[]> stack = new ArrayDeque<>();       // Hack: Use ArrayDeque for faster asynchronous operations, better than Stack
            stack.push(new int[]{root, 0, 0});
            while(!stack.isEmpty()) {
                int current[] = stack.pop();
                // Note: The phase represents the logic before and after the recursive call
                int node = current[0], parentNode = current[1], phase = current[2]; // Extract data of node, parent and phase
                // When doing before recursive call
                if(phase == 0) {
                    this.parent[node] = parentNode;
                    this.subtree[node] = 1;
                    stack.push(new int[]{node, parentNode, 1});     // Note: Now we will place our parameters as after the recursion (hence phase is 1)
                    for(int child : tree.get(node)) {
                        if(child == parentNode)
                            continue;
                        this.depth[child] = this.depth[node] + 1;       // update depth
                        // Since we are going post order, we need to add parent before child, so that child is popped first
                        stack.push(new int[]{child, node, 0});      // We will place the child nodes in phase 0 as they need to perform the earlier computations
                    }
                } else {        // If the phase is 1
                    int maxSize = 0;
                    for(int child : tree.get(node)) {
                        if(child == parentNode)
                            continue;
                        this.subtree[node] += this.subtree[child];          // Info: We update the subtree in post order fashion
                        if(this.subtree[child] > maxSize) {         // Mark the heavy nodes as well
                            this.heavyChild[node] = child;
                            maxSize = this.subtree[child];
                        }
                    }
                }
            }
        }

        // Hack: This function is used to create the heavy-light chains
        public void decompose(int root, int head) {
            this.chainHead[root] = head;
            this.pos[root] = this.currentPos++;
            if(this.heavyChild[root] != -1)
                decompose(this.heavyChild[root], head);
            for(int child : tree.get(root))
                if(child != this.parent[root] && child != this.heavyChild[root])
                    decompose(child, child);    // We start decompose with head value as child because the chains are disjoint
        }

        public void decomposeIterative() {
            this.currentPos = 1;        // Note: Start with the currentPos as 1
            for(int node = 1; node <= this.n; node++) {
                // The nodes that have a parent (not a root) and not the heavy child (since it is already filled)
                if(this.parent[node] == 0 || this.heavyChild[this.parent[node]] != node) {
                    // Note: The rest of the unfilled values along the chain will have the same value as that of the head of the chain
                    for(int child = node; child != -1; child = this.heavyChild[child]) {
                        this.chainHead[child] = node;
                        this.pos[child] = this.currentPos++;        // update the currentPos iteratively
                    }
                }
            }
        }

        public void updateDecomposition(int index, long newValue) {
            long x = newValue - this.baseArray[this.pos[index]];
            this.fenwick.pointUpdate(this.pos[index], x);       // Note: Add the updated value (delta)
            // Info: Update the value by assigning newValue in the base array as well
            this.baseArray[this.pos[index]] = newValue;
        }

        // Hack: Evaluates the sum along the path leveraging Heavy Light Decomposition (HLD) Technique
        public long sumDecomposition(int u, int v) {
            long sum = 0l;      // store the sum here
            // Info: while the pointers of both the nodes are not same (they haven't reached LCA yet)
            while(this.chainHead[u] != this.chainHead[v]) {
                if(this.depth[this.chainHead[u]] > this.depth[this.chainHead[v]]) {     // We always make sure that node v is deeper
                    int temp = u;       // If node v is not deeper then we make it by swapping
                    u = v;
                    v = temp;
                }
                int headOfChain = this.chainHead[v];        // Info: Determine the head of current chain using the chainHead array
                // Hack: We perform the range Sum from the top of the chain to the current node v that we are standing at, since the top of the chain can be found in O(1) per query and the sum of the range can be evaluated in O(log n) via fenwick tree, the complexity is O(log n here)
                sum += this.fenwick.rangeSumQuery(this.pos[headOfChain], this.pos[v]);
                // Hack: We are shifting chains now, and there can be O(log n) chain shifts at worst
                v = this.parent[headOfChain];       // Updae the current node v as the parent of the head of the current chain
            }
            // If the nodes are in same chains but not at same depth
            // Hack: We need to confirm that node v should be lower than node u everytime because in fenwick tree we have [left, right) query in which left <= right
            if(depth[u] > depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            sum += this.fenwick.rangeSumQuery(this.pos[u], this.pos[v]);        // Perform the range sum again to accumulate the sum
            return sum;
        }
    }

    // Hack: This is the Binary Indexed Tree (BIT) that is used for point updates and range queries in O(log n) time
    public static final class Fenwick {
        public long tree[];
        public int size;

        public Fenwick(long nums[]) {
            // Info: Since the array passed here is already 1-based indexing, we reduce the size by 1, to remove off by one error
            this.size = nums.length-1;
            this.tree = new long[this.size+1];
            for(int i = 1; i <= this.size; i++)
                pointUpdate(i, nums[i]);        // Fill the tree by point updates
        }

        public void pointUpdate(int index, long delta) {
            while(index <= this.size) {
                this.tree[index] += delta;
                index += index & -index;        // Info: Extracts the lowest set bit and add to the index
            }
        }

        public long sumQuery(int index) {
            long sum = 0l;
            while(index > 0) {
                sum += this.tree[index];
                index -= index & -index;        // Info: Extracts the lowest set bit and remove from the index
            }
            return sum;
        }

        public long rangeSumQuery(int left, int right) {
            // Note: Perform the range Sum Queries on the range [l, r) so we use the formula of query(r) - query(l-1);
            return sumQuery(right) - sumQuery(left - 1);
        }
    }
}
