import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RangeUpdates {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt();
        long nums[] = new long[n];      // Array initially 0 indexed
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextLong();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < k; i++) {
            int query = fast.nextInt();
            if(query == 1)
                queries.add(new int[]{1, fast.nextInt(), fast.nextInt(), fast.nextInt()});
            else
                queries.add(new int[]{2, fast.nextInt()});
        }
        solve(n, nums, queries);
    }

    public static void solve(final int n, final long nums[], final List<int[]> queries) throws IOException {
        SegmentTree segmentTree = new SegmentTree(n, nums);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));            // Fastest Writer
        for(int query[] : queries) {
            if(query[0] == 1)
                segmentTree.update(1, 1, n, query[1], query[2], query[3]);      // 1based indexing
            else
                out.write(segmentTree.pointQuery(1, 1, n, query[1])+"\n");     // 1based indexing
        }
        out.flush();
    }

    public static class SegmentTree {
        public long tree[];
        public long lazy[];
        public int size;

        public SegmentTree(int n, long nums[]) {
            this.size = n;
            this.tree = new long[4*n+50];
            this.lazy = new long[4*n+50];
            build(1, 1, n, nums);       // Imp- Segment tree is always built by 1based indexing
        }

        public void build(int index, int left, int right, long nums[]) {
            if(left == right) {
                // Since array is 0 indexed and tree is 1 indexed, we subtract 1 from left to make it 0 indexed
                this.tree[index] = nums[left-1];        // Imp- the leaf nodes are marked by left-1 (0 based indexing), or left (1 based indexing)
                return;
            }
            int mid = left + ((right - left) >> 1);
            // Imp- Building tree in bitwise fashion (2^i)
            build(2*index, left, mid, nums);
            build(2*index+1, mid+1, right, nums);
        }

        public void update(int index, int rangeLeft, int rangeRight, int queryLeft, int queryRight, long value) {
            lazyPropagate(index, rangeLeft, rangeRight);        // Lazy propagation before updation
            if(queryLeft > rangeRight || queryRight < rangeLeft)        // No overlap
                return;
            if(queryLeft <= rangeLeft && queryRight >= rangeRight) {        // Complete Overlap
                this.tree[index] += value;
                if(rangeLeft != rangeRight) {   // Imp- update the lazy values of the children if current node is not leaf
                    this.lazy[2*index] += value;
                    this.lazy[2*index+1] += value;
                }
                // Since we are backing now, so we store the current update as lazy into the nodes of children
                return;
            }
            // Partial overlap
            int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
            update(2*index, rangeLeft, mid, queryLeft, queryRight, value);
            update(2*index+1, mid+1, rangeRight, queryLeft, queryRight, value);
        }

        public long pointQuery(int index, int rangeLeft, int rangeRight, int query) {
            lazyPropagate(index, rangeLeft, rangeRight);        // Lazy propagation before querying
            if(rangeLeft == rangeRight)     // Leaf node
                return this.tree[index];
            int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
            if(query <= mid)        // Imp- If we need to recurse left
                return pointQuery(2*index, rangeLeft, mid, query);
            else                // Imp- If we need to recurse right
                return pointQuery(2*index+1, mid+1, rangeRight, query);
        }

        public void lazyPropagate(int index, int rangeLeft, int rangeRight) {
            if(lazy[index] != 0) {      // Imp- If there is a lazy value present, we need to pass down to children nodes
                this.tree[index] += this.lazy[index];       // First update the current with lazy
                // If current node is not leaf
                if(rangeLeft != rangeRight) {
                    // Pass lazy value down to the children lazy attribute
                    this.lazy[2*index] += this.lazy[index];
                    this.lazy[2*index+1] += this.lazy[index];
                }
                this.lazy[index] = 0;       // Update current lazy after passing down
            }
        }
    }
}
