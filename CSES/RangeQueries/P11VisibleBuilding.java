import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P11VisibleBuilding {
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
        }, "https://cses.fi/problemset/task/3304", 1 << 26);
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
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        int queries[][] = new int[q][2];
        for (int j = 0; j < q; j++) {
            queries[j][0] = fr.nextInt();
            queries[j][1] = fr.nextInt();
        }
        fw.attachOutput(solve(n, q, nums, queries));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int q, final int nums[], final int queries[][]) {
        SegmentTree segTree = new SegmentTree(nums);
        final StringBuilder output = new StringBuilder();
        for (int query[] : queries) {
            final int l = query[0]-1, r = query[1]-1;
            int count = 0, currMax = Integer.MIN_VALUE;
            int pos = l;
            while(pos <= r) {
                int idx = segTree.nextGreater(1, 0, n, pos, r+1, currMax);
                if(idx == -1)
                    break;
                count++;
                currMax = nums[idx];
                pos = idx+1;
            }
            output.append(count).append("\n");
        }
        return output;
    }

    public static class SegmentTree {
        private final int tree[];
        private final int size;

        public SegmentTree(int nums[]) {
            this.size = nums.length;
            this.tree = new int[this.size << 2];
            build(1, 0, size, nums);
        }

        public final void build(int node, int l, int r, int nums[]) {
            if (r - l == 1) {
                tree[node] = nums[l];
                return;
            }
            int mid = (l + r) >>> 1;
            build(node << 1, l, mid, nums);
            build(node << 1 | 1, mid, r, nums);
            tree[node] = Math.max(tree[node << 1], tree[node << 1 | 1]);
        }

        public int rangeQuery(int node, int l, int r, int ql, int qr) {
            if (ql >= r || qr <= l)
                return Integer.MIN_VALUE;
            if (ql <= l && qr >= r)
                return tree[node];
            int mid = (l + r) >>> 1;
            return Math.max(rangeQuery(node << 1, l, mid, ql, qr), rangeQuery(node << 1 | 1, mid, r, ql, qr));
        }

        // Info: neat technique to return the index of next greater element
        public int nextGreater(int node, int l, int r, int ql, int qr, int value) {
            if(ql >= r || qr <= l)
                return -1;
            if(ql <= l && qr >= r) {
                if(tree[node] <= value)
                    return -1;
                if(r-l == 1)
                    return l;
            }
            int mid = (l+r) >>> 1;
            int left = nextGreater(node << 1, l, mid, ql, qr, value);
            if(left != -1)
                return left;
            return nextGreater(node << 1 | 1, mid, r, ql, qr, value);
        }
    }
}
