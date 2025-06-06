
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P6CompanyQueriesI {
    public static class FastReader {
        private final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
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
                x = 10 * x + (c-'0');
            } while((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
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
            } while((c = read()) >= '0' && c <= '9');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        tree = new ArrayList<>();
        queries = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 2; i <= n; i++) {
            int boss = fast.nextInt();
            tree.get(boss).add(i);
            tree.get(i).add(boss);
        }
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n);
    }

    public static void solve(final int n) {
        SparseTable sparseTable = new SparseTable(n);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for(int query[] : queries)
            output.append(sparseTable.lifting(query[0], query[1])).append("\n");
        writer.write(output.toString());
        writer.flush();
    }

    public static final class SparseTable {
        public int lift[][];
        public int maxLog;

        public SparseTable(final int n) {
            this.maxLog = 32 - Integer.numberOfLeadingZeros(n);
            // Note: for binary lifting always use maxLog+1 and n+1 (root node is generally 1 based)
            this.lift = new int[maxLog+1][n+1];
            for(int row[] : lift)
                Arrays.fill(row, -1);
            binaryLiftingIterative();
        }

        public void binaryLifting(int root, int parent) {
            this.lift[0][root] = parent;        // Info: Fill the immediate parent
            for(int k = 1; k <= this.maxLog; k++) {
                // Note: When previous power of two is already computed (k-1 != -1), then we can jump to next power (k) by jumping (k-1) power twice
                if(this.lift[k-1][root] != -1)
                    this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];
                else
                    this.lift[k][root] = -1;
            }
            for(int child : tree.get(root))
                if(child != parent)
                    binaryLifting(child, root);
        }

        public void binaryLiftingIterative() {
            Deque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{1, -1});
            while(!stack.isEmpty()) {
                int data[] = stack.pop();
                int root = data[0], parent = data[1];
                this.lift[0][root] = parent;
                for(int k = 1; k <= this.maxLog; k++) {
                    if(this.lift[k-1][root] != -1)
                        this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];
                    else
                        this.lift[k][root] = -1;
                }
                for(int child : tree.get(root))
                    if(child != parent)
                        stack.push(new int[]{child, root});
            }
        }

        public int lifting(int root, int k) {
            int ancestor = root;
            for(int i = 0; i <= this.maxLog; i++) {
                if((k & (1 << i)) != 0) {       // Note: Checking if i-th bit is set
                    ancestor = this.lift[i][ancestor];
                    if(ancestor == -1)      // If next higher ancestor is not there
                        break;
                }
            }
            return ancestor;
        }
    }
}
