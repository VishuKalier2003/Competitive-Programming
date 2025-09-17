import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SegmentTreeLevel3 {
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
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
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
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
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
        }, "Segment-Tree-(Persistent-Segment-Tree)", 1 << 26);
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
        final int n = fr.nextInt(), q = fr.nextInt();
        // keep array 1 based indexing and have nums size as n+1
        int nums[] = new int[n + 1];
        // for 1 based indexing keeping the array as 1 based
        for (int i = 1; i <= n; i++)
            nums[i] = fr.nextInt();
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; i++)
            queries.add(new int[] { fr.nextInt(), fr.nextInt(), fr.nextInt() });
        solve(n, nums, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();
    private static final int INF = Integer.MAX_VALUE;

    public static void solve(final int n, final int nums[], final List<int[]> queries) {
        SegmentTree sgTree = new SegmentTree(nums);
        for(int q[] : queries) {
            int type = q[0];
            switch (type) {
            case 1 -> {
                sgTree.pointUpdate(INF, q[1], q[2]);
            }
            case 2 -> {
                output.append(sgTree.rangeQuery(sgTree.countVersions()-1, q[1], q[2]+1)).append("\n");
            }
            default -> {
                output.append(sgTree.latestRangeQuery(q[1], q[2]+1)).append("\n");
            }
            }
        }
    }

    // The Segment tree in which we can travel back to history and the changes or updates are stored
    public static class SegmentTree {
        // Each update is stored as a version and these versions are stored as lists
        private final List<Node> versions;
        private final int n;

        public SegmentTree(int nums[]) {
            // for 1 based array the value of n is always length-1
            this.n = nums.length - 1;
            this.versions = new ArrayList<>();
            // Build the very first version
            final Node root = build(nums, 1, n+1);
            // add the version to the root
            versions.add(root);
        }

        /**
         * <p><b> Time Complexity </b> O(n log n)</p>
         * @param nums input array (1 based indexing)
         * @param l left boundary
         * @param r right boundary
         * <p>builds the persistent segment tree, the first version of build is exactly same as a skeleton segment tree, where the leaf nodes are the base cases, and the computation is done in post order from leaf to root</p>
         * @return no return 
         */
        private Node build(int nums[], int l, int r) {
            if (r - l == 1)     // base case leaf node
                return new Node(nums[l], null, null);
            int mid = (l + r) >>> 1;
            // left and right children evaluated
            Node left = build(nums, l, mid), right = build(nums, mid, r);
            // the node returned stores the additive sum and left and right markers for the children
            return new Node(left.sum + right.sum, left, right);
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param version the version of root (history timestamp) of segment tree
         * @param idx the index in the array that needs to be modified
         * @param val the new value
         * <p>The helper function that is updates the segment tree by creating new nodes, and appending them to their respective necessary children</p>
         * @return the version (timestamp) for the current update
         */
        public int pointUpdate(int version, int idx, int val) {
            // for each new update, create a new version (root) and append the updated nodes
            Node newRoot = queryPointUpdate(versions.get(versions.size()-1), 1, n+1, idx, val);
            versions.add(newRoot);      // new version added
            // the version number of the update is returned
            return versions.size()-1;
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param root the recursive pointer of current node
         * @param l left boundary
         * @param r right boundary
         * @param qIdx query Index of the array
         * @param value the value to be update
         * <p>The function that updates the segment tree by creating new nodes, and reaching the latest depth of the tree by creating nodes and assigning pointers, the tree behaves more like a graph, where the entry points are different versions of root, and the new left and right pointers of new nodes are attached to the previous segment tree versions to the values that are not updated, the unchanged subtree is attached to the new node pointers as it is</p>
         * @return the root of the new version of segment tree
         */
        private Node queryPointUpdate(Node root, int l, int r, int qIdx, int value) {
            if(r-l == 1)        // base case leaf node
                return new Node(value, null, null);
            int mid = (l+r) >>> 1;
            // left and right children of current node
            Node left = root.left, right = root.right;
            // If towards left subtree
            if(qIdx < mid) {
                // recursive left subtree call
                Node newLeftNode = queryPointUpdate(left, l, mid, qIdx, value);
                // the new node stores the attribute sum of new left node and previous right, and updated left node pointer, the unchanged subtree is attached as it is
                return new Node(newLeftNode.sum + right.sum, newLeftNode, right);
            } else {
                // recursive right subtree call
                Node newRightNode = queryPointUpdate(right, mid, r, qIdx, value);
                // the new node stores the attribute sum of new right node and previous left, and updated right node pointer, the unchanged subtree is attached as it is
                return new Node(left.sum + newRightNode.sum, left, newRightNode);
            }
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param l left boundary
         * @param r right boundary
         * <p>The helper function to get the sum of range of open interval of [l, r) for the latest version of segment tree</p>
         * @return the sum of the range [l, r) for the latest version of segment tree
         */
        public long latestRangeQuery(int l, int r) {
            // querying in the last version Id
            return queryRangeSum(versions.get(countVersions()-1), 1, n+1, l, r);
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param versionId version of the root that needs to be traversed
         * @param l left boundary
         * @param r right boundary
         * <p>The helper function to get the sum of range of open interval of [l, r) for the given version of segment tree</p>
         * @return the sum of the range [l, r) for the given version of segment tree
         */
        public long rangeQuery(int versionId, int l, int r) {
            // querying in the required version Id
            return queryRangeSum(versions.get(versionId), 1, n+1, l, r);
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p?
         * @param root the recursive pointer for current node
         * @param l left boundary
         * @param r right boundary 
         * @param ql query left boundary
         * @param qr query right boundary
         * <p>returns the query range sum of open interval of [l, r) for the given version of segment tree whose version root is passed during query call
         * @return the sum for the range [l, r)
         */
        private static long queryRangeSum(Node root, int l, int r, int ql, int qr) {
            if(qr <= l || ql >= r)      // no overlap
                return 0l;
            if(ql <= l && qr >= r)      // total overlap
                return root.sum;
            int mid = (l+r) >>> 1;
            // computation done in post order, since the computation is passed from leaves to root
            return queryRangeSum(root.left, l, mid, ql, qr) + queryRangeSum(root.right, mid, r, ql, qr);
        }

        /**
         * <p><b> Time Complexity </b> O(1) </p>
         * <p> return the dynamic version count of persistent segment tree hence the number of updates done </p>
         * @return the number of version roots, or updates till present call
         */
        public int countVersions() {return versions.size();}
    }

    // Node class to store states of each segment tree node
    private static class Node {
        private long sum;
        public final Node left, right;

        public Node(long value, Node n1, Node n2) {
            this.sum = value;
            this.left = n1;
            this.right = n2;
        }
    }
}
