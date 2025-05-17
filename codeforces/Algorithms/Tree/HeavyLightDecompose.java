
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String args[]) {
        Thread overflowThread = new Thread(null, () -> {
            try {
                callMain(args);
            } catch(IOException e) {
                e.getLocalizedMessage(); 
            }
        },
        "HLD-Threading",
        1 << 26);
        overflowThread.start();
        try {
            overflowThread.join();
        } catch(InterruptedException iE) {
            Thread.currentThread().interrupt();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        tree = new ArrayList<>();
        queries = new ArrayList<>();
        final int n = fast.nextInt(), q = fast.nextInt();
        long initial[] = new long[n+1];
        for(int i = 1; i <= n; i++)
            initial[i] = fast.nextInt();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        for(int i = 0; i < q; i++) {
            int query = fast.nextInt();
            if(query == 1)
                queries.add(new int[]{query, fast.nextInt(), fast.nextInt()});
            else
                queries.add(new int[]{query, fast.nextInt()});
        }
        solve(n, initial);
    }

    public static void solve(final int n, final long initial[]) {
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        HighLevelDecomposition hldTechnique = new HighLevelDecomposition(n, initial);
        for(int query[] : queries) {
            if(query[0] == 1)
                hldTechnique.updateDecomposition(query[1], query[2]);
            else
                output.append(hldTechnique.sumDecomposition(1, query[1])).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static final class HighLevelDecomposition {
        public int parent[], subtree[], heavyChild[], depth[], pos[], chainHead[];
        public long baseArray[];        // Hack: We always access base array from pos array and not directly, since it is flattened tree
        public int currentPos;
        public Fenwick fenwick;         // Hack: All fenwick operations also will be applied by accessing the pos index

        public HighLevelDecomposition(final int n, final long initial[]) {
            this.parent = new int[n+1];
            this.subtree = new int[n+1];
            this.heavyChild = new int[n+1];
            this.depth = new int[n+1];
            this.pos = new int[n+1];
            this.chainHead = new int[n+1];
            this.baseArray = new long[n+1];
            Arrays.fill(this.heavyChild, -1);
            dfs(1, 0);
            // Note: 1 based indexing change reflected in currentPos and decompose
            currentPos = 1;
            decompose(1, 1);
            for(int i = 1; i <= n; i++)
                this.baseArray[this.pos[i]] = initial[i];
            fenwick = new Fenwick(this.baseArray);
        }

        public int dfs(int root, int parent) {
            this.parent[root] = parent;
            this.subtree[root] = 1;
            int maxSize = 0;
            for(int child : tree.get(root)) {
                if(child != parent) {
                    this.depth[child] = this.depth[root] + 1;
                    int childSize = dfs(child, root);
                    this.subtree[root] += childSize;
                    if(childSize > maxSize) {
                        this.heavyChild[root] = child;
                        maxSize = childSize;
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

        public void updateDecomposition(int index, long newValue) {
            long x = newValue - this.baseArray[this.pos[index]];
            this.fenwick.pointUpdate(this.pos[index], x);       // Note: Add the updated value (delta)
            // Info: Update the value in the base array as well
            this.baseArray[this.pos[index]] = newValue;
        }

        public long sumDecomposition(int u, int v) {
            long sum = 0l;
            while(this.chainHead[u] != this.chainHead[v]) {
                if(this.depth[this.chainHead[u]] > this.depth[this.chainHead[v]]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }
                int headOfChain = this.chainHead[v];
                sum += this.fenwick.rangeSumQuery(this.pos[headOfChain], this.pos[v]);
                v = this.parent[headOfChain];
            }
            if(depth[u] > depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            sum += this.fenwick.rangeSumQuery(this.pos[u], this.pos[v]);
            return sum;
        }
    }

    public static final class Fenwick {
        public long tree[];
        public int size;

        public Fenwick(long nums[]) {
            this.size = nums.length-1;
            this.tree = new long[this.size+1];
            for(int i = 1; i <= this.size; i++)
                pointUpdate(i, nums[i]);
        }

        public void pointUpdate(int index, long delta) {
            while(index <= this.size) {
                this.tree[index] += delta;
                index += index & -index;
            }
        }

        public long sumQuery(int index) {
            long sum = 0l;
            while(index > 0) {
                sum += this.tree[index];
                index -= index & -index;
            }
            return sum;
        }

        public long rangeSumQuery(int left, int right) {
            return sumQuery(right) - sumQuery(left - 1);
        }
    }
}
