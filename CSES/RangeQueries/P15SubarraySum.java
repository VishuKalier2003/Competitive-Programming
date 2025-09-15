import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P15SubarraySum {
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
        }, "Subarray-Sum-Queries-(https://cses.fi/problemset/task/1190)", 1 << 26);
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
        final int n = fr.nextInt(), m = fr.nextInt();
        long nums[] = new long[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextLong();
        int queries[][] = new int[m][2];
        for (int i = 0; i < m; i++) {
            queries[i][0] = fr.nextInt();
            queries[i][1] = fr.nextInt();
        }
        solve(n, nums, m, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int n, final long nums[], final int q, final int queries[][]) {
        SegmentTree sgTree = new SegmentTree(nums); // building segment tree
        for (int qry[] : queries) {
            sgTree.updateQuery(qry[0], qry[1]);
            // querying the entire tree to get the maxSubarray from entire tree, ranging [1,n+1)
            output.append(sgTree.maxSubarrayQuery(1, n + 1)).append("\n");
        }
    }

    // Note: Enriched Segment Tree (passing not values but states during query evaluation)
    public static class SegmentTree {
        private final long prefix[], suffix[], sum[], maxSum[];
        private final int n;

        public SegmentTree(long nums[]) {
            this.n = nums.length;
            this.prefix = new long[n << 2];
            this.suffix = new long[n << 2];
            this.sum = new long[n << 2];
            this.maxSum = new long[n << 2];
            build(1, 1, n + 1, nums); // build with [l,r) range as [1,n+1) with root node at 1
        }

        private void build(int node, int l, int r, long nums[]) {
            if (r - l == 1) {
                sum[node] = nums[l - 1];
                prefix[node] = suffix[node] = maxSum[node] = Math.max(nums[l - 1], 0L);
                return;
            }
            int mid = (l + r) >>> 1;
            build(node << 1, l, mid, nums);
            build(node << 1 | 1, mid, r, nums);
            // Note: The main part of Enriched Segment tree is the computation of states
            sum[node] = sum[node << 1] + sum[node << 1 | 1];
            // The max prefix sum can be either completely in L or a sum of L segment and max prefix of R
            prefix[node] = Math.max(prefix[node << 1], sum[node << 1] + prefix[node << 1 | 1]);
            // The max suffix sum can be either completely in R or a sum of R segment and max suffix of L
            suffix[node] = Math.max(suffix[node << 1 | 1], sum[node << 1 | 1] + suffix[node << 1]);
            // The max subarray can be max of left or right, or a new combined sum of max suffix of L and max prefix of R
            maxSum[node] = Math.max(Math.max(maxSum[node << 1], maxSum[node << 1 | 1]), suffix[node << 1] + prefix[node << 1 | 1]);
        }

        public void updateQuery(int index, long value) {
            update(1, 1, n + 1, index, value); // 1 based indexing the l starts with 1 and r ends at n+1 and root as 1
        }

        public void update(int node, int l, int r, int qIdx, long val) {
            if (r - l == 1) { // leaf cases
                sum[node] = val;
                prefix[node] = suffix[node] = maxSum[node] = Math.max(val, 0L); // non-empty can be an answer
                return;
            }
            int mid = (l + r) >>> 1;
            if (qIdx < mid)
                update(node << 1, l, mid, qIdx, val);
            else
                update(node << 1 | 1, mid, r, qIdx, val);
            // Info: The precomputation is done again, in the same way while building the
            // tree
            sum[node] = sum[node << 1] + sum[node << 1 | 1];
            prefix[node] = Math.max(prefix[node << 1], sum[node << 1] + prefix[node << 1 | 1]);
            suffix[node] = Math.max(sum[node << 1 | 1] + suffix[node << 1], suffix[node << 1 | 1]);
            maxSum[node] = Math.max(Math.max(maxSum[node << 1], maxSum[node << 1 | 1]), suffix[node << 1] + prefix[node << 1 | 1]);
        }

        public long maxSubarrayQuery(int ql, int qr) {
            return maxSubarray(1, 1, n + 1, ql, qr).maxSum; // extracting the maxSum property from the output node
        }

        // Note: Each time the parent node requires the values of sum, prefix, suffix and maxSum from the children, so we need to pass all of them recursively
        public Node maxSubarray(int node, int l, int r, int ql, int qr) {
            if (ql >= r || qr <= l) // Note: No overlap, pass default values in the node, this is actually never hit, we never hit neutral identity here
                return new Node();
            // Total overlap, pass the stored values, after encapsulating them in a node
            if (ql <= l && qr >= r)
                return new Node(sum[node], prefix[node], suffix[node], maxSum[node]);
            int mid = (l + r) >>> 1;
            // Left and right results stored as node class
            Node left = maxSubarray(node << 1, l, mid, ql, qr), right = maxSubarray(node << 1 | 1, mid, r, ql, qr);
            // Info: The same precomputation, as while building the tree
            long s = left.sum + right.sum;
            long pre = Math.max(left.prefix, left.sum + right.prefix);
            long suf = Math.max(right.suffix, right.sum + left.suffix);
            long ms = Math.max(Math.max(left.maxSum, right.maxSum), left.suffix + right.prefix);
            return new Node(s, pre, suf, ms); // pass the values encapsulated as node
        }
    }

    public static class Node { // Node class to store the state, that will be passed along the segment tree nodes
        public final long sum, prefix, suffix, maxSum;

        public Node(long s, long pre, long suf, long ms) {
            this.sum = s;
            this.prefix = pre;
            this.suffix = suf;
            this.maxSum = ms;
        }

        public Node() { // base constructor with base values
            this.sum = 0L;
            this.prefix = this.suffix = this.maxSum = 0L;
        }
    }
}
