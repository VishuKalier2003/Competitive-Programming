import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BookingConcert {
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
        }, "https://leetcode.com/problems/queries-on-a-permutation-with-key/", 1 << 26);
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
        final int n = fr.nextInt(), m = fr.nextInt(), q = fr.nextInt();
        List<int[]> queries = new ArrayList<>();
        for (int j = 0; j < q; j++)
            queries.add(new int[] { fr.nextInt(), fr.nextInt(), fr.nextInt() });
        fw.attachOutput(solve(n, m, q, queries));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int m, final int q, List<int[]> queries) {
        SegmentTree sTree = new SegmentTree(n, m);
        for(int query[] : queries) {
            if(query[0] == 1) {
                gather(sTree, query[1], query[2], n);
            } else {
                scatter(sTree, query[1], query[2], n);
            }
        }
        return new StringBuilder();
    }

    public static int[] gather(SegmentTree sTree, final int maxRow, final int groupSize, final int n) {
        int idx = sTree.findSmallestMax(1, 0, n, 0, maxRow+1, groupSize);
        if(idx == -1 || idx > maxRow)
            return new int[]{};
        int seatStart = sTree.m - sTree.getRemainingSeats(idx);
        sTree.pointUpdate(1, 0, n, idx, groupSize);
        return new int[]{idx, seatStart};
    }

    public static boolean scatter(SegmentTree sTree, final int maxRow, int groupSize, final int n) {
        long available = sTree.querySum(1, 0, n, 0, maxRow+1);
        if(available < groupSize)
            return false;
        for(int row = 0; row <= maxRow && groupSize > 0; row++) {
            int free = sTree.getRemainingSeats(row);
            if(free == 0)
                continue;
            int take = Math.min(free, groupSize);
            sTree.pointUpdate(1, 0, n, row, take);
            groupSize -= take;
        }
        return true;
    }

    public static class SegmentTree {
        private final long sumTree[], maxTree[];
        private final int rowSeat[];
        private final int n, m;

        public SegmentTree(int rows, int cols) {
            this.n = rows;
            this.m = cols;
            this.sumTree = new long[n << 2];
            this.maxTree = new long[n << 2];
            this.rowSeat = new int[n];
            build(1, 0, n);
        }

        public final void build(int treeIdx, int l, int r) {
            if(r-l == 1) {
                sumTree[treeIdx] = m;
                maxTree[treeIdx] = m;
                rowSeat[l] = m;
                return;
            }
            int mid = (l+r) >>> 1;
            build(treeIdx << 1, l, mid);
            build(treeIdx << 1 | 1, mid, r);
            sumTree[treeIdx] = sumTree[treeIdx << 1] + sumTree[treeIdx << 1 | 1];
            maxTree[treeIdx] = Math.max(maxTree[treeIdx << 1], maxTree[treeIdx << 1 | 1]);
        }

        public void pointUpdate(int treeIdx, int l, int r, int qIdx, int value) {
            if(r-l == 1) {
                sumTree[treeIdx] -= value;
                maxTree[treeIdx] -= value;
                rowSeat[qIdx] -= value;
                return;
            }
            int mid = (l+r) >>> 1;
            if(qIdx < mid)
                pointUpdate(treeIdx << 1, l, mid, qIdx, value);
            else
                pointUpdate(treeIdx << 1 | 1, mid, r, qIdx, value);
            sumTree[treeIdx] = sumTree[treeIdx << 1] + sumTree[treeIdx << 1 | 1];
            maxTree[treeIdx] = Math.max(maxTree[treeIdx << 1], maxTree[treeIdx << 1 | 1]);
        }

        public int findSmallestMax(int treeIdx, int l, int r, int ql, int qr, int value) {
            if(ql >= r || qr <= l)
                return -1;
            if(ql <= l && qr >= r) {
                if(maxTree[treeIdx] < value)
                    return -1;
                if(r-l == 1)
                    return l;
            }
            int mid = (l+r) >>> 1;
            int left = findSmallestMax(treeIdx << 1, l, mid, ql, qr, value);
            if(left != -1)
                return left;
            return findSmallestMax(treeIdx << 1 | 1, mid, r, ql, qr, value);
        }

        public long querySum(int treeIdx, int l, int r, int ql, int qr) {
            if(ql >= r || qr <= l)
                return 0L;
            if(ql <= l && qr >= r)
                return sumTree[treeIdx];
            int mid = (l+r) >>> 1;
            return querySum(treeIdx << 1, l, mid, ql, qr) + querySum(treeIdx << 1 | 1, mid, r, ql, qr);
        }

        public int getRemainingSeats(int row) {
            return rowSeat[row];
        }
    }
}
