import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SegmentTreeLevel5 {
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
        }, "Segment-Tree-(Segment-Tree-Beats)", 1 << 26);
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
        long nums[] = new long[n + 1];
        // for 1 based indexing keeping the array as 1 based
        for (int i = 1; i <= n; i++)
            nums[i] = fr.nextLong();
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            int type = fr.nextInt();
            if(type == 1)
                queries.add(new int[]{type, fr.nextInt(), fr.nextInt(), fr.nextInt()});
            else
                queries.add(new int[]{type, fr.nextInt(), fr.nextInt()});
        }
        solve(n, nums, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int n, final long nums[], final List<int[]> queries) {
        SegmentTree sgTree = new SegmentTree(nums);
        for(int q[] : queries) {
            if(q[0] == 1)
                sgTree.update(q[0], q[1], q[2]);        // update
            else        // query needs to be made open interval [l, r] to be made into [l, r+1)
                output.append(sgTree.query(q[1], q[2]+1)).append("\n");
        }
    }

    // Segment Tree beats makes no overlap more lineant and full overlap more stricter than skeleton segment tree to handle powerful queries
    public static class SegmentTree {
        // properties represented as arrays (1 based indexing)
        private final long[] sum, max, maxSecond;
        private final int[] maxCount;
        private final int n;

        public SegmentTree(long nums[]) {
            this.n = nums.length-1;     // 1 based hence 1 subtracted from length
            this.sum = new long[n << 2];
            this.max = new long[n << 2];
            this.maxSecond = new long[n << 2];
            this.maxCount = new int[n << 2];
            // root call for building the segment tree
            build(1, 1, n+1, nums);
        }

        /**
         * <p><b> Time Complexity </b> O(n log n) </p>
         * @param root current node pointer
         * @param l left boundary
         * @param r right boundary
         * @param nums the array from which the segment tree is to be built
         */
        private void build(int root, int l, int r, long nums[]) {
            if(r-l == 1) {      // base case condition
                sum[root] = max[root] = maxSecond[root] = nums[l];
                maxSecond[root] = Long.MIN_VALUE;
                maxCount[root] = 1;
                return;
            }
            int mid = (l+r) >>> 1;
            // left subtree recurse
            build(root << 1, l, mid, nums);
            // right subtree recurse
            build(root << 1 | 1, mid, r, nums);
            merge(root);        // propagate updates from root to children
        }

        /**
         * <p><b> Time Complexity </b> O(1) </p>
         * @param root the current node
         * <p>merges the properties of child nodes into the parent node (here root)</p>
         */
        private void merge(int root) {
            sum[root] = sum[root << 1] + sum[root << 1 | 1];
            // If left child has more max
            if(max[root << 1] > max[root << 1 | 1]) {
                // update root with left
                max[root] = max[root << 1];
                maxCount[root] = maxCount[root << 1];
                // update root from the max of second count 
                maxSecond[root] = Math.max(maxSecond[root << 1], max[root << 1 | 1]);
            // If right child has more max
            } else if(max[root << 1] < max[root << 1 | 1]) {
                // update root with right
                max[root] = max[root << 1 | 1];
                maxCount[root] = maxCount[root << 1 | 1];
                // update root from the max of second count
                maxSecond[root] = Math.max(maxSecond[root << 1 | 1], max[root << 1]);
            } else {    // otherwise
                max[root] = max[root << 1];
                maxCount[root] = maxCount[root << 1] + maxCount[root << 1 | 1];
                maxSecond[root] = Math.max(maxSecond[root << 1], maxSecond[root << 1 | 1]);
            }
        }

        /**
         * <p><b> Time Complexity </b> depends upon query but always lesser than O(n) </b>
         * @param l left boundary
         * @param r right boundary
         * @param x the value (x) passed in the query
         * <p>helper function called to update the range of [l,r) with a[i] = Math.min(a[i], x) since not all elements will be updated</p>
         */
        public void update(int l, int r, long x) {
            queryRangeMinUpdate(1, 1, n+1, l, r, x);
        }

        /**
         * <p><b> Time Complexity </b> depends upon query but always lesser than O(n) </b>
         * @param root current node which is used as a recursive pointer
         * @param l left boundary of root
         * @param r right boundary of root
         * @param ql left boundary of query
         * @param qr right boundary of query
         * @param x the parameter with which the min attribute will be compared
         * <p>function to update the values for the range [l, r) using a[i] = Math.min(a[i], x), where it might happen that each children is not updated, so there is partial updation chances in the range, hence we update the break (no overlap) and tag (full overlap) conditions more lineant and stricter to lower the porpagation, hence faster updates (not in linear time comparison)</p>
         */
        private void queryRangeMinUpdate(int root, int l, int r, int ql, int qr, long x) {
            // Segment Tree Beats break condition (more lineant condition to skip dead nodes, that need not updated) leading to smaller propagation
            if(ql >= r || qr <= l || max[root] <= x)
                return;
            // Segment Tree Beats tag condition (more strict condition to propogate), leading to smaller propagation
            if (ql <= l && r <= qr && maxSecond[root] < x && max[root] > x) {
                // Only elements equal to max[root] will be reduced to x
                sum[root] -= (max[root] - x) * maxCount[root]; // update sum efficiently
                max[root] = x;                                  // update max
                // secondMax[root] stays the same (all max values reduced, secondMax unchanged)
            return;
            }
            if(r-l == 1) {      // leaf node base case
                sum[root] = max[root] = x;
                return;
            }
            int mid = (l+r) >>> 1;
            queryRangeMinUpdate(root << 1, l, mid, ql, qr, x);      // left subtree recursion
            queryRangeMinUpdate(root << 1 | 1, mid, r, ql, qr, x);  // right subtree recursion
            merge(root);        // merge
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param l left boundary
         * @param r right boundary
         * <p>helper function when called evaluates the sum of range [l, r)</p>
         * @return the sum of range [l,r)
         */
        public long query(int l, int r) {
            return queryRangeSum(1, 1, n+1, l, r);
        }

        /**
         * <p><b> Time Complexity </b> O(log n) </p>
         * @param root current node used as recursive pointer
         * @param l left boundary of root
         * @param r right boundary of root
         * @param ql left boundary of query
         * @param qr right boundary of query
         * <p>function to return the sum of open interval [l, r) via recursively partitioning partial overlaps into full and no overlaps and performing the computaition, here sum post orderly</p>
         * @return the sum of range [l, r)
         */
        public long queryRangeSum(int root, int l, int r, int ql, int qr) {
            if(ql >= r || qr <= l)
                return 0L;
            if(ql <= l && qr >= r)
                return sum[root];
            int mid = (l+r) >>> 1;
            return queryRangeSum(root << 1, l, mid, ql, qr) + queryRangeSum(root << 1 | 1, mid, r, ql, qr);
        }
    }
}
