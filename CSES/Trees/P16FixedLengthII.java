import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P16FixedLengthII {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static List<List<Integer>> tree;

    public static void main(String[] args) {
        @SuppressWarnings("CallToPrintStackTrace")
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },
                "fixed-length-paths-II",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k1 = fast.nextInt(), k2 = fast.nextInt();
        tree = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int u = fast.nextInt(), v = fast.nextInt();
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        solve(n, k1, k2);
    }

    public static void solve(final int n, final int k1, final int k2) {
        CentroidDecomposition cd = new CentroidDecomposition(n, k1, k2);
        cd.decompose(1);
        final StringBuilder output = new StringBuilder();
        output.append(cd.getAns());
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static final class CentroidDecomposition {
        private final int n, k1, k2;
        private final boolean dead[];
        private final int subtree[];
        private final Fenwick fenwick;
        private long ans = 0;

        // track which Fenwick indices were touched at the current centroid
        private final List<Integer> usedDepths = new ArrayList<>();

        public CentroidDecomposition(final int n, final int k1, final int k2) {
            this.n = n;
            this.k1 = k1;
            this.k2 = k2;
            this.dead = new boolean[this.n + 1];
            this.subtree = new int[this.n + 1];
            this.fenwick = new Fenwick(this.k2 + 1);
        }

        // 1) compute subtree sizes iteratively, ignoring dead nodes
        private void computeSizes(int root) {
            Deque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{root, -1, 0});
            while (!stack.isEmpty()) {
                int[] cur = stack.pop();
                int u = cur[0], p = cur[1], phase = cur[2];
                if (phase == 0) {
                    subtree[u] = 1;
                    stack.push(new int[]{u, p, 1});
                    for (int v : tree.get(u)) {
                        if (v != p && !dead[v]) {
                            stack.push(new int[]{v, u, 0});
                        }
                    }
                } else {
                    if (p != -1) {
                        subtree[p] += subtree[u];
                    }
                }
            }
        }

        // 2) find the centroid of the component containing 'root'
        private int findCentroid(int root, int compSize) {
            int u = root, p = -1;
            boolean moved;
            do {
                moved = false;
                for (int v : tree.get(u)) {
                    if (v != p && !dead[v] && subtree[v] > compSize / 2) {
                        p = u;
                        u = v;
                        moved = true;
                        break;
                    }
                }
            } while (moved);
            return u;
        }

        // 3) collect distances from 'start' up to depth k2, ignoring dead
        private void storeDistances(int start, int parent, List<Integer> dist) {
            Deque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{start, parent, 1});
            while (!stack.isEmpty()) {
                int[] cur = stack.pop();
                int u = cur[0], p = cur[1], d = cur[2];
                if (d > k2) continue;
                dist.add(d);
                for (int v : tree.get(u)) {
                    if (v != p && !dead[v]) {
                        stack.push(new int[]{v, u, d + 1});
                    }
                }
            }
        }

        // 4) count all paths of length in [k1,k2] passing through centroid 'c'
        private void countPaths(int c) {
            // seed distance = 0 at the centroid itself
            fenwick.update(1, +1);       // depth 0 ⟶ Fenwick index 1
            usedDepths.add(1);

            for (int v : tree.get(c)) {
                if (dead[v]) continue;

                // collect depths from c→v
                List<Integer> dist = new ArrayList<>();
                storeDistances(v, c, dist);

                // match: for each depth d, count all existing x with k1<=d+x<=k2
                for (int d : dist) {
                    // Fenwick indices run 1..k2+1 corresponding to depths 0..k2
                    // we want x in [k1-d, k2-d]
                    int lo = Math.max(0, k1 - d);
                    int hi = k2 - d;
                    if (lo <= hi) {
                        long sumHi = fenwick.sum(hi + 1);
                        long sumLo = fenwick.sum(lo);
                        ans += (sumHi - sumLo);
                    }
                }

                // merge: add these depths so future subtrees can match with them
                for (int d : dist) {
                    if (d <= k2) {
                        int idx = d + 1;
                        // if first time touching this depth, record for later reset
                        int before = fenwick.sum(idx) - fenwick.sum(idx - 1);
                        if (before == 0) usedDepths.add(idx);
                        fenwick.update(idx, +1);
                    }
                }
            }

            // cleanup: reset Fenwick entries we touched
            for (int idx : usedDepths) {
                int cnt = fenwick.sum(idx) - fenwick.sum(idx - 1);
                if (cnt != 0) fenwick.update(idx, -cnt);
            }
            usedDepths.clear();
        }

        // 5) recursive decomposition (depth O(log n))
        public void decompose(int root) {
            computeSizes(root);
            int compSize = subtree[root];
            int c = findCentroid(root, compSize);
            countPaths(c);
            dead[c] = true;
            for (int v : tree.get(c)) {
                if (!dead[v]) {
                    decompose(v);
                }
            }
        }

        public long getAns() {
            return ans;
        }
    }

    public static final class Fenwick {
        private final int tree[];
        private final int n;

        public Fenwick(int n) {
            this.n = n;
            this.tree = new int[this.n + 1];
        }

        // point update at 1-based index
        public void update(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += index & -index;
            }
        }

        // prefix sum [1..index]
        public int sum(int index) {
            int s = 0;
            while (index > 0) {
                s += tree[index];
                index -= index & -index;
            }
            return s;
        }
    }
}
