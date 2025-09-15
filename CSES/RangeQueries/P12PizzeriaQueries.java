import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class P12PizzeriaQueries {
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
        }, "Pizzeria-Queries-(https://cses.fi/problemset/task/2206)", 1 << 26);
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
        long nums[] = new long[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            int type = fr.nextInt();
            if (type == 1)
                queries.add(new int[] { type, fr.nextInt(), fr.nextInt() });
            else
                queries.add(new int[] { type, fr.nextInt() });
        }
        fw.attachOutput(solve(n, q, nums, queries));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int q, final long nums[], final List<int[]> queries) {
        long[] pA = new long[n], pB = new long[n];
        // Note: Linearization trick to break modulus
        for (int i = 0; i < n; i++) {
            pA[i] = nums[i] - i;
            pB[i] = nums[i] + i;
        }
        // Creating two segment trees one for p[i] - i, and other for p[i] + i
        SegmentTree segTreeI = new SegmentTree(pA), segTreeII = new SegmentTree(pB);
        final StringBuilder output = new StringBuilder();
        for (int[] query : queries) {
            if (query[0] == 1) {
                int k = query[1], x = query[2];
                k--; // Our segment tree is 0 based so reduce k by 1
                // Info: Analogous updates (value +- index) technique
                segTreeI.pointUpdate(1, 0, n, k, x - k + 0l);
                segTreeII.pointUpdate(1, 0, n, k, x + k + 0l);
            } else {
                int x = query[1];
                x--;
                // Range queries and we add and subtract the b value (here x)
                long left = segTreeI.rangeQuery(1, 0, n, 0, x + 1) + x;
                long right = segTreeII.rangeQuery(1, 0, n, x, n) - x;
                long min = Math.min(left, right);
                output.append(min).append("\n");
            }
        }
        return output;
    }

    // Note: The segment tree is hybrid (iterative + recursive) build, open interval
    // and 1-based indexing
    public static class SegmentTree {
        private final long[] tree;
        private final int size;

        public SegmentTree(long nums[]) {
            this.size = nums.length;
            this.tree = new long[this.size << 2]; // size 4 x n
            build(1, 0, size, nums); // recursive building
        }

        public final void build(int node, int l, int r, long nums[]) {
            if (r - l == 1) {
                tree[node] = nums[l]; // fill the lead nodes
                return;
            }
            int mid = (l + r) >>> 1;
            build(node << 1, l, mid, nums);
            build(node << 1 | 1, mid, r, nums);
            // post orderly generate the min tree
            tree[node] = Math.min(tree[node << 1], tree[node << 1 | 1]);
        }

        public void pointUpdate(int node, int l, int r, int qIndex, long value) {
            if (r - l == 1) { // When leaf node reached, update the value
                tree[node] = value;
                return;
            }
            int mid = (l + r) >>> 1;
            if (qIndex < mid) // If query is smaller than mid, move left, else move right
                pointUpdate(node << 1, l, mid, qIndex, value);
            else
                pointUpdate(node << 1 | 1, mid, r, qIndex, value);
            // When going back, update the minimums
            tree[node] = Math.min(tree[node << 1], tree[node << 1 | 1]);
        }

        public long rangeQuery(int node, int l, int r, int ql, int qr) {
            if (l >= qr || r <= ql) // no overlap
                return Long.MAX_VALUE;
            if (ql <= l && r <= qr) // complete overlap
                return tree[node];
            int mid = (l + r) >>> 1;
            // partial overlap
            return Math.min(rangeQuery(node << 1, l, mid, ql, qr), rangeQuery(node << 1 | 1, mid, r, ql, qr));
        }
    }
}
