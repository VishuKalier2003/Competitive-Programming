import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P16SubarraySumII {
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

    public static void solve(final int n, final long nums[], final int q, final int[][] queries) {
        SegmentTree sgTree = new SegmentTree(nums);
        for(int qry[] : queries)
            // Info: Since segment tree is open-interval and hence right range is exclusive (+1 to range)
            output.append(sgTree.maxSubarrayQuery(qry[0], qry[1]+1)).append("\n");
    }

    public static class SegmentTree {
        private final long[] sum, prefix, suffix, maxSum;
        private final int n;

        public SegmentTree(long nums[]) {
            this.n = nums.length;
            this.sum = new long[n << 2];
            this.prefix = new long[n << 2];
            this.suffix = new long[n << 2];
            this.maxSum = new long[n << 2];
            build(1, 1, n+1, nums);     // build call
        }

        private void build(int root, int l, int r, final long nums[]) {
            if(r-l == 1) {      // leaf node case
                sum[root] = nums[l-1];
                prefix[root] = suffix[root] = maxSum[root] = Math.max(0l, nums[l-1]);
                return;
            }
            int mid = (l+r) >>> 1;
            build(root << 1, l, mid, nums);
            build(root << 1 | 1, mid, r, nums);
            // Post order building
            sum[root] = sum[root << 1] + sum[root << 1 | 1];
            prefix[root] = Math.max(prefix[root << 1], prefix[root << 1 | 1] + sum[root << 1]);
            suffix[root] = Math.max(suffix[root << 1 | 1], sum[root << 1 | 1] + suffix[root << 1]);
            maxSum[root] = Math.max(Math.max(maxSum[root << 1], maxSum[root << 1 | 1]), suffix[root << 1] + prefix[root << 1 | 1]);
        }

        public long maxSubarrayQuery(int ql, int qr) {
            return maxSubarray(1, 1, n+1, ql, qr).maxSum;
        }

        // MaxSubarray logic, the result is passed as Node state
        public Node maxSubarray(int root, int l, int r, int ql, int qr) {
            if(ql >= r || qr <= l)
                return new Node();
            if(ql <= l && qr >= r)
                return new Node(sum[root], prefix[root], suffix[root], maxSum[root]);
            int mid = (l+r) >>> 1;
            Node left = maxSubarray(root << 1, l, mid, ql, qr), right = maxSubarray(root << 1 | 1, mid, r, ql, qr);
            long s = left.sum + right.sum;
            long pre = Math.max(left.prefix, left.sum + right.prefix);
            long suf = Math.max(right.suffix, right.sum + left.suffix);
            long ms = Math.max(Math.max(left.maxSum, right.maxSum), left.suffix + right.prefix);
            return new Node(s, pre, suf, ms);
        }
    }

    public static class Node {
        private final long sum, prefix, suffix, maxSum;

        public Node() {     // Note: base case matters, keep it thorough
            this.sum = 0L;
            this.prefix = this.suffix = this.maxSum = 0L;
        }

        public Node(long s, long pre, long suf, long ms) {
            this.sum = s;
            this.prefix = pre;
            this.suffix = suf;
            this.maxSum = ms;
        }
    }
}
