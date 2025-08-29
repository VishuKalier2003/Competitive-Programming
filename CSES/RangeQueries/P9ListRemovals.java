import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P9ListRemovals {
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
        }, "https://cses.fi/problemset/task/1749", 1 << 26);
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
        int[] nums = new int[n], indices = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        for(int j = 0; j < n; j++)
            indices[j] = fr.nextInt();
        fw.attachOutput(solve(n, nums, indices));
        fw.printOutput();
    }

    private static StringBuilder solve(final int n, final int nums[], final int indices[]) {
        SegmentTree segTree = new SegmentTree(n);
        final StringBuilder output = new StringBuilder();
        for(int i = 0; i < n; i++) {
            int k = indices[i];
            // Fetches the index of leaf node, here index of the point to be removed in O(log n)
            int idx = segTree.findKthElement(1, 0, n, k);       // Info: Passing entire interval for search space
            output.append(nums[idx]).append(" ");
            // Removes the element by updating the value of the point to 0 and updating their prefix sums (interval) in O(log n)
            segTree.updatePoint(1, 0, n, idx);      // Info: passing entire interval for update space
        }
        return output;
    }

    private static class SegmentTree {
        private final int tree[];
        private final int size;

        public SegmentTree(int n) {
            this.size = n;
            this.tree = new int[this.size << 2];
            // Note: leaf nodes are present at the end, occupying range from [n, 2n] and root node is situated at index 1
            build(1, 0, n);
        }

        // Info: treeIndex stores the index of node of segment tree compressed in array which is why we apply bit technique to jump to the specific node
        private void build(int treeIndex, int l, int r) {
            if(r-l == 1) {
                tree[treeIndex] = 1;        // Each leaf node is passed value 1
                return;
            }
            int mid = (l+r) >>> 1;
            build(treeIndex << 1, l, mid);
            build(treeIndex << 1 | 1, mid, r);
            tree[treeIndex] = tree[treeIndex << 1]      // left child
            + tree[treeIndex << 1 | 1];     // right child
        }

        // This updates the index element, leaf node updated
        public void updatePoint(int treeIndex, int l, int r, int index) {
            if(r-l == 1) {
                tree[treeIndex] = 0;        // updating the leaf node
                return;
            }
            int mid = (l+r) >>> 1;
            if(index < mid)     // If the index to be searched is lower than mid, go left
                updatePoint(treeIndex << 1, l, mid, index);
            else        // If the index to be searched is higher or greater than mid, g right
                updatePoint(treeIndex << 1 | 1, mid, r, index);
            // Info: this ensures that tree gets updated post orderly (the range query gets updated in logarithmic time)
            tree[treeIndex] = tree[treeIndex << 1] + tree[treeIndex << 1 | 1];
        }

        // This gives the index (l) range of [0, n) which is the element to be removed next
        public int findKthElement(int treeIndex, int l, int r, int k) {
            if(r-l == 1)        // If leaf node found
                return l;       // return the index
            int mid = (l+r) >>> 1;
            if(tree[treeIndex << 1] >= k)       // If left child range sum is greater than k, then k is present in left
                return findKthElement(treeIndex << 1, l, mid, k);
            else        // Else in right, and we remove the sum of left child from right child each time
                return findKthElement(treeIndex << 1 | 1, mid, r, k - tree[treeIndex << 1]);
        }
    }
}
