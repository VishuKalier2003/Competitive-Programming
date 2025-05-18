import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P12PathQueriesII {
    // Hack: Fastest Input Output reading in Java
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;
        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }
        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String[] args) {
        @SuppressWarnings("CallToPrintStackTrace")
        Thread t = new Thread(null, () -> {
            try { callMain(args); }
            catch (IOException e) { e.printStackTrace(); }
        }, "path-queries-ii", 1<<26);
        t.start();
        try { t.join(); }
        catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        int n = fast.nextInt(), q = fast.nextInt();
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) vals[i] = fast.nextInt();
        tree = new ArrayList<>(n+1);
        for (int i = 0; i <= n; i++) tree.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int u = fast.nextInt(), v = fast.nextInt();
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        queries = new ArrayList<>(q);
        for (int i = 0; i < q; i++) {
            queries.add(new int[]{fast.nextInt(), fast.nextInt(), fast.nextInt()});
        }
        solve(n, vals);
    }

    public static void solve(int n, int[] vals) {
        HeavyLightDecompose hld = new HeavyLightDecompose(n, vals);
        StringBuilder out = new StringBuilder();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for (int[] qu : queries) {
            if (qu[0] == 1) {
                hld.updateDecomposition(qu[1], qu[2]);
            } else {
                out.append(hld.maxDecomposition(qu[1], qu[2])).append(' ');
            }
        }
        writer.print(out.append('\n').toString());
        writer.flush();
    }

    public static final class HeavyLightDecompose {
        private final int LOG = 18;
        private final int[] parent, depth, subtree, heavyChild, pos, chainHead;
        private final int[][] up;
        private int currentPos;
        private final SegmentTree segmentTree;

        public HeavyLightDecompose(int n, int[] init) {
            parent     = new int[n+1];
            depth      = new int[n+1];
            subtree    = new int[n+1];
            heavyChild = new int[n+1];
            chainHead  = new int[n+1];
            pos        = new int[n+1];
            up         = new int[n+1][LOG];
            Arrays.fill(heavyChild, -1);

            // 1) DFS to compute parent[], depth[], subtree[] and heavyChild[]
            dfs(1, 0);

            // 2) Build binary-lifting table up[v][j]
            for (int v = 1; v <= n; v++) {
                up[v][0] = parent[v];
            }
            for (int j = 1; j < LOG; j++) {
                for (int v = 1; v <= n; v++) {
                    up[v][j] = up[ up[v][j-1] ][j-1];
                }
            }

            // 3) Decompose into heavy paths
            currentPos = 1;
            decompose(1, 1);

            // 4) Build array in HLD order and build segment tree
            int[] arr = new int[n];
            for (int v = 1; v <= n; v++) {
                arr[pos[v]-1] = init[v-1];
            }
            segmentTree = new SegmentTree(arr);
        }

        // recursive DFS to compute sizes and heavy child
        private int dfs(int u, int p) {
            parent[u] = p;
            subtree[u] = 1;
            int maxSz = 0;
            for (int w : tree.get(u)) {
                if (w == p) continue;
                depth[w] = depth[u] + 1;
                int sub = dfs(w, u);
                if (sub > maxSz) {
                    maxSz = sub;
                    heavyChild[u] = w;
                }
                subtree[u] += sub;
            }
            return subtree[u];
        }

        // recursive decompose
        private void decompose(int u, int head) {
            chainHead[u] = head;
            pos[u] = currentPos++;
            if (heavyChild[u] != -1) {
                decompose(heavyChild[u], head);
            }
            for (int w : tree.get(u)) {
                if (w != parent[u] && w != heavyChild[u]) {
                    decompose(w, w);
                }
            }
        }

        public void updateDecomposition(int u, int val) {
            // 0-based index for segment tree
            segmentTree.update(pos[u] - 1, val);
        }

        public int maxDecomposition(int u, int v) {
            int c = lca(u, v);
            int res = Math.max(maxUp(u, c), maxUp(v, c));
            // include the LCA itself
            res = Math.max(res, segmentTree.query(pos[c] - 1, pos[c] - 1));
            return res;
        }

        // iterative binary-lifting LCA
        private int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                int t = u; u = v; v = t;
            }
            int diff = depth[u] - depth[v];
            for (int j = 0; j < LOG; j++) {
                if ((diff & (1 << j)) != 0) {
                    u = up[u][j];
                }
            }
            if (u == v) return u;
            for (int j = LOG - 1; j >= 0; j--) {
                if (up[u][j] != up[v][j]) {
                    u = up[u][j];
                    v = up[v][j];
                }
            }
            return parent[u];
        }

        // climb from x up to anc (exclusive), taking max over segments
        private int maxUp(int x, int anc) {
            int res = Integer.MIN_VALUE;
            while (chainHead[x] != chainHead[anc]) {
                res = Math.max(res,
                    segmentTree.query(pos[ chainHead[x] ] - 1, pos[x] - 1));
                x = parent[ chainHead[x] ];
            }
            if (x != anc) {
                // same chain, skip anc itself
                res = Math.max(res,
                    segmentTree.query(pos[anc], pos[x] - 1));
            }
            return res;
        }
    }

    // iterative bottom-up segment tree (range-max, point-update)
    public static final class SegmentTree {
        private final int N;
        private final int[] t;

        public SegmentTree(int[] a) {
            int n = a.length, p = 1;
            while (p < n) p <<= 1;
            N = p;
            t = new int[2 * N];
            System.arraycopy(a, 0, t, N, n);
            for (int i = N - 1; i > 0; i--) {
                t[i] = Math.max(t[i << 1], t[i << 1 | 1]);
            }
        }

        public void update(int i, int v) {
            i += N;
            t[i] = v;
            for (i >>= 1; i > 0; i >>= 1) {
                t[i] = Math.max(t[i << 1], t[i << 1 | 1]);
            }
        }

        public int query(int l, int r) {
            int res = Integer.MIN_VALUE;
            for (l += N, r += N; l <= r; l >>= 1, r >>= 1) {
                if ((l & 1) == 1) res = Math.max(res, t[l++]);
                if ((r & 1) == 0) res = Math.max(res, t[r--]);
            }
            return res;
        }
    }
}
