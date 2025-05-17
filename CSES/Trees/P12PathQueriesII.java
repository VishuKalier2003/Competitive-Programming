import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class P12PathQueriesII {
    // Hack: Fastest Input Output reading in Java
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
            return buffer[ptr++] & 0xff;        // Reading data between 0 to 255 characters
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
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String[] args) {
        Thread extendThread = new Thread(null, 
        ()-> {
            try {
                callMain(args);
            } catch(Exception e) {
                e.getLocalizedMessage();
            }
        },
        "path-queries-ii",
        1 << 26);
        extendThread.start();
        try {
            extendThread.join();
        } catch(InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) {}

    public static final class HeavyLightDecompose {
        private final int n;
        private final int parent[], depth[], subtree[], heavyChild[], pos[], chainHead[];
        private int currentPos;
        private final int baseArray[];
        private final SegmentTree segmentTree;

        public HeavyLightDecompose(final int n, final int initial[]) {
            this.n = n;
            this.parent = new int[n+1];
            this.depth = new int[n+1];
            this.heavyChild = new int[n+1];
            this.chainHead = new int[n+1];
            this.subtree = new int[n+1];
            this.pos = new int[n+1];
            this.baseArray = new int[n+1];
            Arrays.fill(this.heavyChild, -1);
            dfs(1, 0);
            decompose(1, 1);
            for(int i = 1; i <= n; i++)
                this.baseArray[this.pos[i]] = initial[i];
            segmentTree = new SegmentTree(this.baseArray);
        }

        public int dfs(int root, int parentNode) {
            this.parent[root] = parentNode;
            this.subtree[root] = 1;
            int maxSize = 0;
            for(int child : tree.get(root))
                if(child != parentNode) {
                    this.depth[child] = this.depth[root] + 1;
                    int childSize = dfs(child, root);
                    this.subtree[root] += childSize;
                    if(childSize > maxSize) {
                        this.heavyChild[root] = child;
                        maxSize = childSize;
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
                    decompose(child, child);        // Want it to be disjoint
        }

        public void update(int index, int value) {
            this.baseArray[this.pos[index]] = value;
            segmentTree.pointUpdateCall(this.pos[index], value);
        }

        public int maxDecomposition(int u, int v) {
            int max = Integer.MIN_VALUE;
            while(this.chainHead[u] != this.chainHead[v]) {
                if(this.depth[u] > this.depth[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }
                int headOfChain = this.chainHead[v];
                max = Math.max(max, segmentTree.maxRangeCall(this.pos[headOfChain], this.pos[v]));
                v = this.parent[headOfChain];
            }
            if(this.depth[u] > this.depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            max = Math.max(max, segmentTree.maxRangeCall(this.pos[u], this.pos[v]));
            return max;
        }
    }

    public static final class SegmentTree {
        private final int tree[], lazy[];
        private int n;

        public SegmentTree(final int nums[]) {
            this.n = 1;
            int size = nums.length;
            while(this.n <= size)
                n <<= 1;
            this.tree = new int[this.n << 1]; 
            this.lazy = new int[this.n << 1];
        }

        public void buildTree(final int nums[]) {
            System.arraycopy(nums, 0, this.tree, this.n, nums.length);
            for(int i = this.n-1; i >= 1; i--)
                this.tree[i] = Math.max(this.tree[i << 1], this.tree[i << 1 | 1]);
        }

        private void push(int index, int left, int right) {
            if(this.lazy[index] != 0) {
                this.tree[index] += (right - left + 1) * this.lazy[index];
                if(left != right) {
                    this.lazy[index << 1] += this.lazy[index];
                    this.lazy[index << 1 | 1] += this.lazy[index];
                    this.lazy[index] = 0;
                }
            }
        }

        public void pointUpdateCall(int point, int value) {
            pointUpdate(1, 0, this.n-1, point, value);
        }

        public void pointUpdate(int index, int left, int right, int point, int value) {
            push(index, left, right);
            if(left == right) {
                this.tree[index] = value;
                return;
            }
            int mid = (left + right) >>> 1;
            if(point <= mid)
                pointUpdate(index << 1, left, mid, point, value);
            else
                pointUpdate(index << 1 | 1, mid + 1, right, point, value);
            this.tree[index] = Math.max(this.tree[index << 1], this.tree[index << 1 | 1]);
        }

        public int maxRangeCall(int ql, int qr) {
            return maxRangeQuery(1, 0, this.n-1, ql, qr);
        }

        private int maxRangeQuery(int index, int left, int right, int ql, int qr) {
            push(index, left, right);
            if(ql > right || qr < left)
                return Integer.MAX_VALUE;
            if(ql <= left && qr >= right)
                return this.tree[index];
            int mid = (left + right) >>> 1;
            return Math.max(maxRangeQuery(index << 1, left, mid, ql, qr), maxRangeQuery(index << 1 | 1, mid + 1, right, ql, qr));
        }
    }
}
