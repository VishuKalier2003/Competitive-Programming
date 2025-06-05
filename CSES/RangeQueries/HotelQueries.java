import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class HotelQueries {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("PrintStackTrace")
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

    public static class FastWriter {        // Writer class
        BufferedWriter writer;

        public FastWriter() {this.writer = new BufferedWriter(new OutputStreamWriter(System.out));}

        public void print(String s) throws IOException {
            this.writer.write(s);
        }

        public void close() throws IOException {this.writer.close();}
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        int groups[] = new int[q];
        for(int i = 0; i < q; i++)
            groups[i] = fast.nextInt();
        solve(n, q, nums, groups);
    }

    public static void solve(final int n, final int q, final int nums[], final int queries[]) throws IOException {
        SegmentTree segmentTree = new SegmentTree(nums);
        FastWriter writer = new FastWriter();
        for(int query : queries) {
            // Imp- The node at 1 is the root node (here stores the highest value)
            if(segmentTree.tree[1] < query)
                writer.print("0 ");
            else {
                int hotel = segmentTree.pointQuery(1, 1, n, query);     // Find the hotel (index)
                writer.print(hotel+" ");
                segmentTree.pointUpdate(hotel, -query);     // Decrease the hotel capacity by -query (group)
            }
        }
        writer.close();
    }

    public static class SegmentTree {
        public int tree[], lazy[];
        public int size;

        public SegmentTree(int nums[]) {
            int n = nums.length;
            this.size = n;
            this.tree = new int[4*n+50];
            buildTree(1, 1, n, nums);       // Build from 1 based indexing
        }

        public int midIndex(int left, int right) {
            return left + ((right - left) >> 1);
        }

        public void buildTree(int index, int left, int right, int nums[]) {
            // Building tree from an array
            if(left == right) {
                this.tree[index] = nums[left-1];        // Imp- Update index (node) with left-1 (1 based indexing)
                return;
            }
            int mid = midIndex(left, right);
            buildTree(2*index, left, mid, nums);
            buildTree(2*index+1, mid+1, right, nums);
            this.tree[index] = Math.max(this.tree[2*index], this.tree[2*index+1]);      // Post order operation
            return;
        }

        public void pointUpdate(int query, int delta) {
            // Imp- Use fenwick tree point update logic, updating a leaf node by delta, evaluating the function and transferring value till the root
            update(1, 1, this.size, query, delta);
        }

        public void update(int index, int left, int right, int query, int delta) {
            if(left == right) {     // When leaf node reached
                this.tree[index] += delta;      // Add delta
                return;
            }
            int mid = midIndex(left, right);
            if(query <= mid)
                update(2*index, left, mid, query, delta);       // Left call
            else
                update(2*index+1, mid+1, right, query, delta);      // Right call
            this.tree[index] = Math.max(this.tree[2*index], this.tree[2*index+1]);      // Evaluate in post order fashion
            return;
        }

        public int pointQuery(int index, int left, int right, int query) {
            if(left == right)
                return left;        // Here we are passing hotel index (left or right), not node (index)
            int mid = midIndex(left, right);
            // Imp- When performing binary traversal on Segment tree always use index (node) and the check factor
            if(query <= this.tree[2*index])
                return pointQuery(2*index, left, mid, query);
            else
                return pointQuery(2*index+1, mid+1, right, query);
        }
    }
}
