import java.io.*;
import java.util.*;

public class P18DistinctValueQueriesII {
    static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do x = x * 10 + (c - '0');
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    static class SegmentTree {
        public int n;
        int[] tree;

        SegmentTree(int n) {
            this.n = n;
            tree = new int[4 * n];
        }

        void build(int idx, int l, int r, int[] arr) {
            if (l == r) {
                tree[idx] = arr[l];
                return;
            }
            int mid = (l + r) / 2;
            build(idx * 2, l, mid, arr);
            build(idx * 2 + 1, mid + 1, r, arr);
            tree[idx] = Math.max(tree[idx * 2], tree[idx * 2 + 1]);
        }

        void update(int idx, int l, int r, int pos, int val) {
            if (l == r) {
                tree[idx] = val;
                return;
            }
            int mid = (l + r) / 2;
            if (pos <= mid) update(idx * 2, l, mid, pos, val);
            else update(idx * 2 + 1, mid + 1, r, pos, val);
            tree[idx] = Math.max(tree[idx * 2], tree[idx * 2 + 1]);
        }

        int query(int idx, int l, int r, int ql, int qr) {
            if (qr < l || ql > r) return 0; // neutral (since prev[] ≥ 0)
            if (ql <= l && r <= qr) return tree[idx];
            int mid = (l + r) / 2;
            return Math.max(
                query(idx * 2, l, mid, ql, qr),
                query(idx * 2 + 1, mid + 1, r, ql, qr)
            );
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int n = fr.nextInt(), q = fr.nextInt();
        int[] nums = new int[n + 1];
        for (int i = 1; i <= n; i++) nums[i] = fr.nextInt();

        // Track occurrences per value
        Map<Integer, TreeSet<Integer>> occ = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            occ.computeIfAbsent(nums[i], _ -> new TreeSet<>()).add(i);
        }

        // Build prev array
        int[] prev = new int[n + 1];
        for (TreeSet<Integer> set : occ.values()) {
            int last = 0;
            for (int pos : set) {
                prev[pos] = last;
                last = pos;
            }
        }

        SegmentTree seg = new SegmentTree(n);
        seg.build(1, 1, n, prev);

        // Process queries
        for (int i = 0; i < q; i++) {
            int type = fr.nextInt();
            if (type == 1) {
                int k = fr.nextInt(), u = fr.nextInt();
                if (nums[k] == u) continue; // no change

                // Remove old value link
                TreeSet<Integer> oldSet = occ.get(nums[k]);
                Integer next = oldSet.higher(k);
                Integer prevPos = oldSet.lower(k);
                oldSet.remove(k);
                if (oldSet.isEmpty()) occ.remove(nums[k]);

                if (next != null) {
                    // fix prev[next]
                    prev[next] = (prevPos == null ? 0 : prevPos);
                    seg.update(1, 1, n, next, prev[next]);
                }

                // Insert new value
                TreeSet<Integer> newSet = occ.computeIfAbsent(u, _ -> new TreeSet<>());
                Integer newPrev = newSet.lower(k);
                newSet.add(k);
                prev[k] = (newPrev == null ? 0 : newPrev);
                seg.update(1, 1, n, k, prev[k]);

                Integer newNext = newSet.higher(k);
                if (newNext != null) {
                    prev[newNext] = k;
                    seg.update(1, 1, n, newNext, k);
                }

                nums[k] = u;

            } else {
                int a = fr.nextInt(), b = fr.nextInt();
                int maxPrev = seg.query(1, 1, n, a, b);
                sb.append(maxPrev < a ? "YES\n" : "NO\n");
            }
        }

        System.out.print(sb);
    }
}
