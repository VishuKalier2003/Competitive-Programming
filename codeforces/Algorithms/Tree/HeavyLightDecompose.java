
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeavyLightDecompose {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) <= '9' && c <= '0');
            return x;
        }
    }

    public static List<List<Integer>> tree;

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        tree = new ArrayList<>();
    }

    public static final class HighLevelDecomposition {
        public int parent[], subtree[], heavyChild[], depth[], pos[], chainHead[];
        public long baseArray[];
        public int currentPos;

        public HighLevelDecomposition(final int n) {
            this.parent = new int[n+1];
            this.subtree = new int[n+1];
            this.heavyChild = new int[n+1];
            this.depth = new int[n+1];
            this.pos = new int[n+1];
            this.chainHead = new int[n+1];
            this.baseArray = new long[n+1];
            Arrays.fill(this.heavyChild, -1);
            currentPos = 0;
            dfs(1, 0);
        }

        public int dfs(int root, int parent) {
            this.parent[root] = parent;
            this.subtree[root] = 1;
            int maxSize = 0;
            for(int child : tree.get(root)) {
                if(child != parent) {
                    this.depth[child] = this.depth[root] + 1;
                    int cSize = dfs(child, root);
                    this.subtree[child] += cSize;
                    if(cSize > maxSize) {
                        this.heavyChild[root] = child;
                        maxSize = cSize;
                    }
                }
            }
            return this.subtree[root];
        }

        public void decompose(int root, int head) {
            this.chainHead[root] = head;
            this.pos[root] = this.currentPos++;
            if(this.heavyChild[root] != -1)
                decompose(this.heavyChild[root], head);
            for(int child : tree.get(root))
                if(child != this.parent[root] && child != this.heavyChild[root])
                    decompose(child, child);    // We start decompose with head value as child because the chains are disjoint
        }

        public void update(int index, int value) {}
    }

    public static final class SegmentTree {
        public int tree[], lazy[];
        public int N;

        public SegmentTree(int nums[]) {
            final int n = nums.length;
            this.N = 1;
            while(N < n)
                N <<= 1;
            this.tree = new int[2 * N];
            this.lazy = new int[2 * N];
            buildTree(nums);
        }

        public void buildTree(int nums[]) {
            System.arraycopy(nums, 0, this.tree, this.N, nums.length);      // Note: Start position of copying in tree is N
            for(int i = this.N - 1; i >= 1; i--)
                this.tree[i] = this.tree[i << 1]        // Info: Left child is always a power of 2
                + this.tree[i << 1 | 1];                // Info: Right child is always a power of 2 + 1 (added at 0 index)
        }

        private void push(int index, int left, int right) {
            if(this.lazy[index] != 0) {
                // We need to push the entire segment value to tree node, and since segment size is [l,r) and contains the same lazy values
                this.tree[index] += (right - left + 1) * this.lazy[index];
                if(left != right) {
                    this.lazy[index << 1] += this.lazy[index];
                    this.lazy[index << 1 | 1] += this.lazy[index];
                }
                this.lazy[index] = 0;
            }
        }

        public void pointUpdateQuery(int point, int value) {        // Note: Point updates in O(log n)
            pointUpdate(1, 0, this.N-1, point, value);
        }

        public void pointUpdate(int index, int left, int right, int point, int value) {
            push(index, left, right);       // Hack: Always lazy propagate first
            if(left == right) {
                this.tree[index] += value;
                return;
            }
            int mid = (left + right) >>> 1;
            if(point <= mid)
                pointUpdate(index << 1, left, mid, point, value);
            else
                pointUpdate(index << 1 | 1, mid+1, right, point, value);
            this.tree[index] = this.tree[index << 1] + this.tree[index << 1 | 1];
        }

        public void rangeUpdateQuery(int queryLeft, int queryRight, int value) {
            updateQuery(1, 0, this.N-1, queryLeft, queryRight, value);
        }

        public void updateQuery(int index, int left, int right, int ql, int qr, int value) {
            push(index, left, right);           // Hack: Always lazy propagate first
            if(ql > right || qr < left)
                return;
            if(ql <= left && qr >= right) {
                this.lazy[index] += value;      // Hack: Always push into lazy, when complete overlap found in range update query
                push(index, left, right);
                return;
            }
            int mid = (left + right) >>> 1;
            updateQuery(index << 1, left, mid, ql, qr, value);
            updateQuery(index << 1 | 1, mid+1, right, ql, qr, value);
            this.tree[index] = this.tree[index << 1] + this.tree[index << 1 | 1];
        }

        public int rangeSumQuery(int ql, int qr) {
            return sumQuery(1, 0, this.N-1, ql, qr);
        }

        public int sumQuery(int index, int left, int right, int ql, int qr) {
            push(index, left, right);       // Hack: Always lazy propagate first
            if(ql > right || qr < left)     // Info: No overlap
                return 0;
            if(ql <= left && qr >= right)   // Info: Complete Overlap
                return this.tree[index];
            int mid = (left + right) >>> 1;
            // Info: Partial Overlap
            int leftSum = sumQuery(index << 1, left, mid, ql, qr);
            int rightSum = sumQuery(index << 1 | 1, mid+1, right, ql, qr);
            return leftSum + rightSum;
        }
    }
}
