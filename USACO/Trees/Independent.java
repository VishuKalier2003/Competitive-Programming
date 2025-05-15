import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Independent {
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

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        tree.add(new ArrayList<>());
        for(int i = 1; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        solve(n);
    }

    public static List<List<Integer>> tree;
    public static long dp[][];
    public static final int MOD = 1_000_000_007;

    public static void solve(final int n) {
        dp = new long[n+1][2];
        dfs(1, -1);
        final StringBuilder output = new StringBuilder();
        output.append((dp[1][0] + dp[1][1]) % MOD);
        System.out.print(output);
    }

    // Imp- implies painting white, 1 implies painting black
    public static void dfs(int root, int parent) {
        // Imp- Since multiplication is being done we need to initialize all nodes (both leaf and non-leaf)
        dp[root][0] = 1L;        // painted white
        dp[root][1] = 1L;        // painted black
        for(int child : tree.get(root)) {
            if(child != parent) {
                dfs(child, root);
                // Post orderly if we paint root white, then number of ways are the ways to paint each child u either black or white % MOD
                dp[root][0] = (dp[root][0] * (dp[child][0] + dp[child][1])) % MOD;
            }
        }
        for(int child : tree.get(root)) {
            if(child != parent)
                // Post orderly if we paint root black, then number of ways are the ways to paint each child white % MOD
                dp[root][1] = (dp[root][1] * dp[child][0]) % MOD;
        }
    }
}
