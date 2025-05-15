import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class P1Subordinates {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
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

    public static void main(String args[]) throws IOException {
        FastReader fast = new FastReader();
        tree = new ArrayList<>();
        final int n = fast.nextInt();
        tree.add(new ArrayList<>());        // Add extra index for 1 based indexing
        for(int i = 1; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int j = 2; j <= n; j++)
            tree.get(fast.nextInt()).add(j);
        solve(n);
    }

    public static List<List<Integer>> tree;
    public static int ans[];

    public static void solve(final int n) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        ans = new int[n+1];
        countSubtree(1);
        final StringBuilder output = new StringBuilder();
        for(int i = 1; i <= n; i++)
            output.append(ans[i]).append(" ");
        writer.write(output.toString().trim());
        writer.flush();
    }

    // Imp- Recursive, can blow for skewed trees 2 x 10^5
    public static int subtree(int root) {
        if(tree.get(root).isEmpty())
            return 0;
        for(int child : tree.get(root))
            ans[root] += 1 + subtree(child);        // Add sum from all nodes not just the last level
        return ans[root];
    }

    public static void countSubtree(int root) {
        Stack<Integer> dfs = new Stack<>(), postOrder = new Stack<>();
        dfs.push(root);     // start the dfs
        while(!dfs.isEmpty()) {
            int node = dfs.pop();
            // Imp- Append in post order (stack), in order (queue)
            postOrder.push(node);
            for(int child : tree.get(node))
                dfs.push(child);
        }
        while(!postOrder.isEmpty()) {
            int node = postOrder.pop();
            // For each node check its children and perform the computation (add, count, etc.)
            for(int child : tree.get(node))
                ans[node] += 1 + ans[child];
        }
    }
}
