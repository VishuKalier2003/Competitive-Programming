import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

// Note: Skeleton Segment Tree
public class SegmentTreeLevel0 {
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
        }, "Skeleton-Segment-Tree", 1 << 26);
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
        long nums[] = new long[n + 1];
        // segment tree so we keep array as 1 based indexing
        for (int i = 1; i <= n; i++)
            nums[i] = fr.nextLong();
        int queries[][] = new int[q][3];
        // The queries are 1 based and inclusive [l, r]
        for (int i = 0; i < q; i++) {
            queries[i][0] = fr.nextInt();
            queries[i][1] = fr.nextInt();
            queries[i][2] = fr.nextInt();
        }
        solve(n, nums, q, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int n, final long nums[], final int q, final int queries[][]) {
        SegmentTree sgTree = new SegmentTree(nums);
        for(int qry[] : queries) {
            if(qry[0] == 1)
                sgTree.update(qry[1], qry[2]);
            else
                // The segment tree is open-interval [l,r) hence right range is increased by 1, since queries are inclusive [l,r]
                output.append(sgTree.rangeSum(qry[1], qry[2]+1)).append(" ");
        }
    }

    // Segment Tree data structure of 1 based indexing and open-interval [l, r)
    public static class SegmentTree {
        // The control always starts at root which is index 1
        private final long sum[];       // sum array for storing sum of respective segments
        private final int n;

        public SegmentTree(long nums[]) {
            // length of the n marked as length-1, since array is 1 indexed
            this.n = nums.length-1;
            this.sum = new long[n << 2];
            // build function called with entire range [1, n+1), the range of input array
            build(1, 1, n+1, nums);     // root starts at 1 and hence segment tree is also 1 based indexing
        }

        /**
         * <p><b>Time Complexity</b> - O(n log n)</p>
         * @param root the node indexed 1 as the root node, marking the entire array as the range
         * @param l the left end of the range
         * @param r the right end of the range
         * @param nums the array storing the values to be passed down to the leaf
         * <p>the build function builds the segment tree by breaking the range [l, r) into [l, mid) as left range and [mid, r) as right range and then performing the computation in post order fashion since the computation needs to be done from leaf to root</p>
         * @return no return type
         */
        private void build(int root, int l, int r, long nums[]) {
            if(r-l == 1) {      // leaf node base case
                sum[root] = nums[l];        // 1 based indexing
                return;
            }
            int mid = (l+r) >>> 1;
            // left segment recursion
            build(root << 1, l, mid, nums);
            // right segment recursion
            build(root << 1 | 1, mid, r, nums);
            // post order summation since the sum is passed from leaves to root
            sum[root] = sum[root << 1] + sum[root << 1 | 1];
        }

        /**
         * <p><b>Time Complexity </b> - O(log n)</p>
         * @param idx index where the value is to be updated
         * @param value the updated value
         * helper function for the solve() method, where it takes only the necessary input and all the required constant parameters are passed by itself to the actual update function of the segment tree
         * @return no return type
         */
        public void update(int idx, long value) {
            // passed with root, left range and exclusive right range
            queryUpdate(1, 1, n+1, idx, value);
        }

        /**
         * <p><b>Time Complexity</b> - O(log n)</p>
         * @param root the node indexed 1 as the root node, marking the entire array as the range
         * @param l the left end of the range
         * @param r the right end of the range
         * @param qIdx index where the value needs to be updated
         * @param value the updated value 
         * <p>finds the leaf node via qIdx recursively that needs to be updated, then adds the new value, and finally performs the computation via post order since the computation needs to be done from leaf to root</p>
         * @return no return type
         */
        public void queryUpdate(int root, int l, int r, int qIdx, long value) {
            if(r-l == 1) {      // leaf node base case
                sum[root] += value;
                return;
            }
            int mid = (l+r) >>> 1;
            // The qIdx if less than mid, lies in left range [l, mid)
            if(qIdx < mid)
                queryUpdate(root << 1, l, mid, qIdx, value);
            else        // Otherwise in right half [mid, r)
                queryUpdate(root << 1 | 1, mid, r, qIdx, value);
            // post order summation since the sum is passed from leaves to root
            sum[root] = sum[root << 1] + sum[root << 1 | 1];
        }

        /**
         * <p><b>Time Complexity </b> - O(log n)</p>
         * @param l the left boundary of the range
         * @param r the right boundary of the range
         * helper function for the solve() method, where it takes only the necessary input and all the required constant parameters are passed by itself to the actual sum function of the segment tree
         * @return no return type
         */
        public long rangeSum(int l, int r) {
            return queryRangeSum(1, 1, n+1, l, r);
        }

        /**
         * <p><b>Time Complexity</b> - O(log n)</p>
         * @param root the node indexed 1 as the root node, marking the entire array as the range
         * @param l the left end of the range
         * @param r the right end of the range
         * @param ql the constant left boundary of the query
         * @param qr the constant right boundary of the query 
         * <p>checks conditions of no overlap and full overlap as the base cases, then recursively reduces the range until the base case is reached, performing computation in post order from leaf to root</p>
         * @return the sum of range [l,r)
         */
        public long queryRangeSum(int root, int l, int r, int ql, int qr) {
            if(ql >= r || qr <= l)      // no overlap
                return 0L;
            if(ql <= l && qr >= r)      // partial overlap
                return sum[root];
            int mid = (l+r) >>> 1;
            long left = queryRangeSum(root << 1, l, mid, ql, qr);
            long right = queryRangeSum(root << 1 | 1, mid, r, ql, qr);
            return left + right;
        }
    }
}