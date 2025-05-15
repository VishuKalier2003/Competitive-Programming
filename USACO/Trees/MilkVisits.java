import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class MilkVisits {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
            this.buffer = new BufferedReader(new FileReader("milkvisits.in"));
        }

        @SuppressWarnings("CallToPrintStackTrace")
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

    static List<Node> tree;
    static List<int[]> queries;

    public static class Node {
        public int gersey, holstein;
        public List<Integer> children;

        public Node() {
            this.gersey = 0; this.holstein = 0; this.children = new ArrayList<>();
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt();
        final String cows = "A"+fast.next();
        tree = new ArrayList<>();
        queries = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new Node());
        for(int i = 0; i < n-1; i++) {
            int node1 = fast.nextInt(), node2 = fast.nextInt();
            tree.get(node1).children.add(node2);
            tree.get(node2).children.add(node1);
        }
        for(int i = 0; i < k; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt(), fast.next().equals("G") ? 1 : -1});
        solve(n, k, cows);
    }

    public static void solve(final int n, final int k, final String cows) throws IOException {
        TreeTable treeTable = new TreeTable(n, cows);
        final StringBuilder output = new StringBuilder();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("milkvisits.out"))) {
            for(int query[] : queries) {
                int node1 = query[0], node2 = query[1], cow = query[2];
                int lca = treeTable.lca(node1, node2);
                int g1 = tree.get(node1).gersey;
                int g2 = tree.get(node2).gersey;
                int gL = tree.get(lca).gersey;
                int h1 = tree.get(node1).holstein;
                int h2 = tree.get(node2).holstein;
                int hL = tree.get(lca).holstein;
                int totalG = g1 + g2 - 2 * gL + (cows.charAt(lca) == 'G' ? 1 : 0), totalH = h1 + h2 - 2 * hL + (cows.charAt(lca) == 'H' ? 1 : 0);
                if(cow == 1 && totalG > 0)
                    output.append("1");
                else if(cow == -1 && totalH > 0)
                    output.append("1");
                else
                    output.append("0");
            }
            writer.write(output.toString());
            writer.flush();
        }
    }

    public static class TreeTable {     // Imp- Sparse Table with Binary Lifting
        public int LOG;
        public int lift[][];
        public int depth[];

        public TreeTable(final int n, final String cows) {
            this.LOG = 32 - Integer.numberOfLeadingZeros(n);
            this.lift = new int[LOG+1][n+1];
            this.depth = new int[n+1];
            for(int row[] : lift)
                Arrays.fill(row, -1);
            binaryLift(1, -1, cows.charAt(1) == 'G' ? 1 : 0, cows.charAt(1) == 'H' ? 1 : 0, cows);
        }

        public final void binaryLift(int root, int parent, int countG, int countH, final String cows) {
            this.lift[0][root] = parent;
            for(int k = 1; k <= LOG; k++) {
                // checking if k-1 power is built
                if(this.lift[k-1][root] != -1)      // update - building always from k-1 to k
                    this.lift[k][root] = this.lift[k-1][this.lift[k-1][root]];
                else
                    this.lift[k][root] = -1;
            }
            if(parent == -1)
                this.depth[root] = 0;
            else
                this.depth[root] = this.depth[parent] + 1;
            Node data = tree.get(root);
            // Preprocessing number of gersey and holstein cows from root to current node, pre orderly
            data.gersey = countG;
            data.holstein = countH;
            for(int child : data.children) {
                if(child != parent) {
                    if(cows.charAt(child) == 'G')       // If current cow is gersey, update gersey count
                        binaryLift(child, root, countG+1, countH, cows);
                    else        // If current cow is holstein, update holstein count
                        binaryLift(child, root, countG, countH+1, cows);
                }
            }
        }

        public int lifting(int root, int k) {
            int ancestor = root;
            for(int i = 0; i <= this.LOG; i++)
                if(((k >> i) & 1) != 0) {
                    ancestor = this.lift[i][ancestor];
                    if(ancestor == -1)
                        break;
                }
            return ancestor;
        }

        public int lca(int u, int v) {
            if(this.depth[v] > this.depth[u])
                v = lifting(v, this.depth[v] - this.depth[u]);
            else if(this.depth[u] > this.depth[v])
                u = lifting(u, this.depth[u] - this.depth[v]);
            if(u == v)
                return u;
            for(int i = this.LOG; i >= 0; i--)
                if(this.lift[i][u] != -1 && this.lift[i][u] != this.lift[i][v]) {
                    u = this.lift[i][u];
                    v = this.lift[i][v];
                }
            return this.lift[0][u];
        }
    }
}
