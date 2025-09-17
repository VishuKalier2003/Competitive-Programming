import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Note: Merge-sort Segment Tree
public class SegmentTreeLevel2 {
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
        }, "Segment-Tree-(Merge-sort)", 1 << 26);
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
        for (int i = 0; i < q; i++) {
            // All indexing are 1 based
            queries.add(new int[] { fr.nextInt(), fr.nextInt(), fr.nextInt(), fr.nextInt() });
        }
        solve(n, nums, q, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int n, final int nums[], final int q, final List<int[]> queries) {
        SegmentTree sgTree = new SegmentTree(nums);
        // open interval segment tree, hence the right boundaries are exclusive, instead of [l,r] we pass [l,r+1)
        for (int qry[] : queries) {
            if (qry[0] == 1)       
                output.append(sgTree.rangeLowerBound(qry[1], qry[2] + 1, qry[3])).append("\n");
            else
                output.append(sgTree.rangeUpperBound(qry[1], qry[2] + 1, qry[3])).append("\n");
        }
    }

    // Segment Tree data structure of 1 based indexing and open-interval [l, r)
    public static class SegmentTree {
        // 2d tree, where each node stores the sorted list of values
        private final int[][] tree;
        private final int n;

        public SegmentTree(int nums[]) {
            this.n = nums.length - 1;
            // the tree is initialzed with size 4*n, but the inner arrays are kept deferenced
            tree = new int[n << 2][];
            build(1, 1, n + 1, nums);
        }

        /**
         * <p><b>Time Complexity</b> - O((n+m) log (n+m))</p>
         * @param root the node indexed 1 as the root node, marking the entire array as the range (current node)
         * @param l the left end of the range
         * @param r the right end of the range
         * @param nums the array storing the values to be passed down to the leaf
         * <p>the build function builds the segment tree by breaking the range [l, r) into [l, mid) as left range and [mid, r) as right range and then performing the computation in post order fashion since the computation needs to be done from leaf to root, smaller lists are merged together to make the larger lists</p>
         * @return no return type
         */
        private void build(int root, int l, int r, int nums[]) {
            if (r - l == 1) {       // base case leaf node
                tree[root] = new int[] { nums[l] };     // the inner array of leaf is initialised and filled
                return;
            }
            int mid = (l + r) >>> 1;
            // left subtree recursion
            build(root << 1, l, mid, nums);
            // right subtree recursion
            build(root << 1 | 1, mid, r, nums);
            // postorder merging of left and right child sorted arrays into parent array
            mergeLists(root, root << 1, root << 1 | 1);
        }

        /**
         * <p><b> Time Complexity - </b> O(n+m) </p>
         * @param root the current node (here parent)
         * @param left  left node
         * @param right right node
         * @return no value (just sorts the two list intersectingly and updates the parent list)
         */
        private void mergeLists(int root, int left, int right) {
            int[] l = tree[left], r = tree[right];
            int[] p = new int[l.length + r.length];     // Allocate array here
            int i = 0, j = 0, k = 0;
            // simple merging of two sorted arrays into one, using two pointers
            while (i < l.length && j < r.length)
                p[k++] = l[i] <= r[j] ? l[i++] : r[j++];
            while (i < l.length)
                p[k++] = l[i++];
            while (j < r.length)
                p[k++] = r[j++];
             // assign after merge
            tree[root] = p;
        }

        /**
         * <p><b> Time Complexity </b> O(log^2 n) </b>
         * @param ql left boundary of query range
         * @param qr right boundary of query range
         * @param x the bottleneck value of upper bound
         * <p>helper function used in solve() to compute the count of values <= x (the upper bound) using binary search </p>
         * @return the count of numbers <= x in range [ql, qr)
         */
        public int rangeUpperBound(int ql, int qr, int x) {
            // left inclusive and right exclusive root hence [1, n+1)
            return queryUpper(1, 1, n + 1, ql, qr, x);
        }

        /**
         * <p><b> Time Complexity </b> O(log^2 n) </b>
         * @param root  current root
         * @param l node left boundary
         * @param r node right boundary
         * @param ql query left boundary
         * @param qr query right boundary
         * @param x the value for the upper bound
         * <p>provides count of values <= x in range [ql, qr) using binary search on every total overlap segment and if partial overlap is found, recursively going deeper until the base case of no-overlap or total overlap is reached</p>
         * @return count of numbers <= x in range [ql, qr)
         */
        private int queryUpper(int root, int l, int r, int ql, int qr, int x) {
            if (ql >= r || qr <= l)     // no overlap
                return 0;
            if (ql <= l && qr >= r)     // complete overlap, perform binary search of upper bound
                return binarySearchUpper(tree[root], x);
            int mid = (l + r) >>> 1;
            return queryUpper(root << 1, l, mid, ql, qr, x) + queryUpper(root << 1 | 1, mid, r, ql, qr, x);
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param nums  the sorted array of the passed segment tree node
         * @param value the value to be searched for upper bound
         * <p>performs binary search on sorted array to get the upper bound of the array for the given parameter value</p>
         * @return the rightmost index till elements are <= x
         */
        private int binarySearchUpper(int[] nums, int value) {
            int l = 0, r = nums.length;
            // binary search
            while (l < r) {
                int mid = (l + r) >>> 1;
                // upper bound logic
                if (nums[mid] <= value)
                    l = mid + 1;
                else
                    r = mid;
            }
            return l;
        }

        /**
         * <p><b> Time Complexity </b> O(log^2 n) </b>
         * @param ql left boundary of query range
         * @param qr right boundary of query range
         * @param x the bottleneck value of lower bound
         * <p>helper function used in solve() to compute the count of values < x (the lower bound) using binary search </p>
         * @return the count of numbers <= x in range [ql, qr)
         */
        public int rangeLowerBound(int ql, int qr, int x) {
            return queryLower(1, 1, n + 1, ql, qr, x);
        }

        /**
         * <p><b> Time Complexity </b> O(log^2 n) </b>
         * @param root  current root
         * @param l node left boundary
         * @param r node right boundary
         * @param ql query left boundary
         * @param qr query right boundary
         * @param x the value for the lower bound
         * <p>provides count of values < x in range [ql, qr) using binary search on every total overlap segment and if partial overlap is found, recursively going deeper until the base case of no-overlap or total overlap is reached</p>
         * @return count of numbers < x in range [ql, qr)
         */
        private int queryLower(int root, int l, int r, int ql, int qr, int x) {
            if (ql >= r || qr <= l)     // no overlap
                return 0;
            if (ql <= l && qr >= r)     // total overlap, binary search for lower bound
                return binarySearchLower(tree[root], x);
            int mid = (l + r) >>> 1;
            return queryLower(root << 1, l, mid, ql, qr, x) + queryLower(root << 1 | 1, mid, r, ql, qr, x);
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param nums  the sorted array of the passed segment tree node
         * @param value the value to be searched for lower bound
         * <p>performs binary search on sorted array to get the lower bound of the array for the given parameter value</p>
         * @return the rightmost index till elements are < x
         */
        private int binarySearchLower(int[] nums, int value) {
            int l = 0, r = nums.length;
            // binary search
            while (l < r) {
                int mid = (l + r) >>> 1;
                // lower bound logic
                if (nums[mid] < value)
                    l = mid + 1;
                else
                    r = mid;
            }
            return l;
        }
    }
}