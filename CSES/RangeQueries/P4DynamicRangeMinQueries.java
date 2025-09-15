import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P4DynamicRangeMinQueries {
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
        }, "Dynamic-Range-Minimum-Queries-(https://cses.fi/problemset/task/1649)", 1 << 26);
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
        int nums[] = new int[n + 1];
        // segment tree so we keep array as 1 based indexing
        for (int i = 1; i <= n; i++)
            nums[i] = fr.nextInt();
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

    public static void solve(final int n, final int nums[], final int q, final int queries[][]) {
        SegmentTree sgTree = new SegmentTree(nums);
        for(int qry[] : queries) {
            if(qry[0] == 1)
                sgTree.update(qry[1], qry[2]);
            else
                output.append(sgTree.findMin(qry[1], qry[2]+1)).append("\n");
        }
    }

    public static class SegmentTree {
        private final int min[];
        private final int n;

        public SegmentTree(int nums[]) {
            this.n = nums.length-1;
            this.min = new int[n << 2];
            build(1, 1, n+1, nums);
        }

        private void build(int root, int l, int r, int nums[]) {
            if(r-l == 1) {
                min[root] = nums[l];
                return;
            }
            int mid = (l+r) >>> 1;
            build(root << 1, l, mid, nums);
            build(root << 1 | 1, mid, r, nums);
            min[root] = Math.min(min[root << 1], min[root << 1 | 1]);
        }

        public void update(int l, int value) {
            queryUpdate(1, 1, n+1, l, value);
        }

        public void queryUpdate(int root, int l, int r, int qIdx, int value) {
            if(r-l == 1) {
                min[root] = value;
                return;
            }
            int mid = (l+r) >>> 1;
            if(qIdx < mid)
                queryUpdate(root << 1, l, mid, qIdx, value);
            else
                queryUpdate(root << 1 | 1, mid, r, qIdx, value);
            min[root] = Math.min(min[root << 1], min[root << 1 | 1]);
        }

        public int findMin(int l, int r) {
            return queryMin(1, 1, n+1, l, r);
        }

        private int queryMin(int root, int l, int r, int ql, int qr) {
            if(ql >= r || qr <= l)
                return Integer.MAX_VALUE;
            if(ql <= l && qr >= r)
                return min[root];
            int mid = (l+r) >>> 1;
            return Math.min(queryMin(root << 1, l, mid, ql, qr), queryMin(root << 1 | 1, mid, r, ql, qr));
        }
    }
}