import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class ProblemH {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single
        // System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
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

        public int readInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long readLong() throws IOException {
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
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

        public String readLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "problem-H",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        while (t-- > 0) {
            final int n = fr.readInt();
            int nums1[] = new int[n], nums2[] = new int[n];
            for (int i = 0; i < n; i++) {
                nums1[i] = fr.readInt();
                nums2[i] = fr.readInt();
            }
            output.append(solve(n, nums1, nums2)).append("\n");
        }
        PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(final int n, final int nums1[], final int nums2[]) {
        // 1) Coordinate‐compress
        List<Integer> coords = new ArrayList<>(2 * n);
        for (int i = 0; i < n; i++) {
            coords.add(nums1[i]);
            coords.add(nums2[i]);
        }
        Collections.sort(coords);
        coords = new ArrayList<>(new LinkedHashSet<>(coords));
        int M = coords.size();
        int[] L = new int[n], R = new int[n];
        for (int i = 0; i < n; i++) {
            L[i] = Collections.binarySearch(coords, nums1[i]);
            R[i] = Collections.binarySearch(coords, nums2[i]);
        }
        SegTree st = new SegTree(M);
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < n; k++) {
            int l = L[k], r = R[k];
            int best = st.queryMax(0, r + 1);
            st.rangeChmax(l, r + 1, best + 1);
            sb.append(st.queryMax(0, M)).append(' ');
        }
        return sb;
    }

    static class SegTree {
        int n;
        int[] max, smax, maxCount, lazy;

        public SegTree(int size) {
            n = 1;
            while (n < size)
                n <<= 1;
            int N = 2 * n;
            max = new int[N];
            smax = new int[N];
            maxCount = new int[N];
            lazy = new int[N];
            build(1, 0, n);
        }

        private void build(int idx, int l, int r) {
            if (r - l == 1) {
                max[idx] = 0;
                smax[idx] = Integer.MIN_VALUE;
                maxCount[idx] = 1;
                lazy[idx] = 0;
            } else {
                int mid = (l + r) >>> 1;
                build(idx << 1, l, mid);
                build(idx << 1 | 1, mid, r);
                pull(idx);
                lazy[idx] = 0;
            }
        }

        private void pull(int idx) {
            int L = idx << 1, R = idx << 1 | 1;
            if (max[L] == max[R]) {
                max[idx] = max[L];
                maxCount[idx] = maxCount[L] + maxCount[R];
                smax[idx] = Math.max(smax[L], smax[R]);
            } else if (max[L] > max[R]) {
                max[idx] = max[L];
                maxCount[idx] = maxCount[L];
                smax[idx] = Math.max(smax[L], max[R]);
            } else {
                max[idx] = max[R];
                maxCount[idx] = maxCount[R];
                smax[idx] = Math.max(max[L], smax[R]);
            }
        }

        private void applyChmax(int idx, int x) {
            if (max[idx] >= x)
                return;
            max[idx] = x;
            lazy[idx] = Math.max(lazy[idx], x);
        }

        private void push(int idx) {
            if (lazy[idx] != 0) {
                applyChmax(idx << 1, lazy[idx]);
                applyChmax(idx << 1 | 1, lazy[idx]);
                lazy[idx] = 0;
            }
        }

        public void rangeChmax(int L, int R, int x) {
            rangeChmax(L, R, x, 1, 0, n);
        }

        private void rangeChmax(int L, int R, int x, int idx, int l, int r) {
            if (r <= L || R <= l || max[idx] >= x)
                return;
            if (L <= l && r <= R && smax[idx] < x) {
                applyChmax(idx, x);
                return;
            }
            push(idx);
            int mid = (l + r) >>> 1;
            rangeChmax(L, R, x, idx << 1, l, mid);
            rangeChmax(L, R, x, idx << 1 | 1, mid, r);
            pull(idx);
        }

        public int queryMax(int L, int R) {
            return queryMax(L, R, 1, 0, n);
        }

        private int queryMax(int L, int R, int idx, int l, int r) {
            if (r <= L || R <= l)
                return Integer.MIN_VALUE;
            if (L <= l && r <= R)
                return max[idx];
            push(idx);
            int mid = (l + r) >>> 1;
            return Math.max(
                    queryMax(L, R, idx << 1, l, mid),
                    queryMax(L, R, idx << 1 | 1, mid, r));
        }
    }

}
