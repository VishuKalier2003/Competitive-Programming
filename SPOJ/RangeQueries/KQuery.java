import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class KQuery {
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
        }, "K-Query-(https://www.spoj.com/problems/KQUERY/)", 1 << 26);
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
        final int n = fr.nextInt();
        long nums[] = new long[n + 1];
        // segment tree so we keep array as 1 based indexing
        for (int i = 1; i <= n; i++)
            nums[i] = fr.nextLong();
        final int q = fr.nextInt();
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
            int upperK = sgTree.helper(qry[0], qry[1]+1, qry[2]);
            output.append(qry[1]+1-qry[0]-upperK).append("\n");
        }
    }

    public static class SegmentTree {
        private final long tree[][];
        private final int n;

        public SegmentTree(long nums[]) {
            this.n = nums.length-1;
            this.tree = new long[n << 2][];
            build(1, 1, n+1, nums);
        }

        private void build(int root, int l, int r, final long nums[]) {
            if(r-l == 1) {
                tree[root] = new long[]{nums[l]};
                return;
            }
            int mid = (l+r) >>> 1;
            build(root << 1, l, mid, nums);
            build(root << 1 | 1, mid, r, nums);
            mergeSort(root, root << 1, root << 1 | 1);
        }

        private void mergeSort(int root, int left, int right) {
            long l[] = tree[left], r[] = tree[right];
            int ls = l.length, m = r.length;
            long p[] = new long[ls+m];
            int i = 0, j = 0, k = 0;
            while(i < ls && j < m) {
                if(l[i] < r[j])
                    p[k++] = l[i++];
                else
                    p[k++] = r[j++];
            }
            while(i < ls)
                p[k++] = l[i++];
            while(j < m)
                p[k++] = r[j++];
            tree[root] = p;
        }

        public int helper(int ql, int qr, int k) {
            return queryRange(1, 1, n+1, ql, qr, k);
        }

        public int queryRange(int root, int l, int r, int ql, int qr, int k) {
            if(ql >= r || qr <= l)
                return 0;
            if(ql <= l && qr >= r)
                return upperBound(tree[root], k);
            int mid = (l+r) >>> 1;
            return queryRange(root << 1, l, mid, ql, qr, k) + queryRange(root << 1 | 1, mid, r, ql, qr, k);
        }

        public int upperBound(long nums[], int k) {
            int l = 0, r = nums.length;
            while(l < r) {
                int mid = (l+r) >>> 1;
                if(nums[mid] <= k)
                    l = mid+1;
                else
                    r = mid;
            }
            return l;
        }
    }
}
