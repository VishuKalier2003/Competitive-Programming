import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class BarnPaint {
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

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt();
        tree = new ArrayList<>();
        tree.add(new ArrayList<>());
        for(int i = 1; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        painted = new HashMap<>();
        painted.put(1, new HashSet<>());
        painted.put(2, new HashSet<>());
        painted.put(3, new HashSet<>());
        for(int i = 0; i < k; i++) {
            int node = fast.nextInt(), color = fast.nextInt();
            painted.get(color).add(node);
        }
        solve(n);
    }

    public static List<List<Integer>> tree;
    public static long dp[][];
    public static final int MOD = 1_000_000_007;
    public static Map<Integer, Set<Integer>> painted;

    public static void solve(final int n) {
        dp = new long[n+1][3];
        dfs(1, -1);
        final StringBuilder output = new StringBuilder();
        output.append((dp[1][0] + dp[1][1] + dp[1][2]) % MOD);
        System.out.print(output);
    }

    public static void dfs(int root, int parent) {
    // Determine which colors are allowed for this node
    boolean[] allowed = new boolean[3]; // color 0,1,2 → 1,2,3
    Arrays.fill(allowed, true);

    for (int color = 0; color < 3; color++) {
        if (painted.get(color + 1).contains(root)) {
            Arrays.fill(allowed, false);
            allowed[color] = true;
            break;
        }
    }

    for (int color = 0; color < 3; color++) {
        if (!allowed[color]) {
            dp[root][color] = 0;
            continue;
        }

        long ways = 1;
        for (int child : tree.get(root)) {
            if (child == parent) continue;
            dfs(child, root);
            long childSum = 0;

            for (int c = 0; c < 3; c++) {
                if (c != color) {
                    childSum = (childSum + dp[child][c]) % MOD;
                }
            }

            ways = (ways * childSum) % MOD;
        }
        dp[root][color] = ways;
    }
}

}
