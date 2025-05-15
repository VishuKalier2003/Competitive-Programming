import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class P2TreeMatching {
    public static class FastReader {
        BufferedReader buffer;
        StringTokenizer tokenizer;

        public FastReader() {
            buffer = new BufferedReader(new InputStreamReader(System.in));
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
    }

    static List<List<Integer>> tree;
    static int[][] dp;

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int n = fast.nextInt();
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int a = fast.nextInt();
            int b = fast.nextInt();
            tree.get(a).add(b);
            tree.get(b).add(a);
        }
        dp = new int[n + 1][2];
        iterativeDFS(n);
        System.out.println(Math.max(dp[1][0], dp[1][1]));
    }

    static void iterativeDFS(int n) {
        boolean[] visited = new boolean[n + 1];
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> postOrder = new Stack<>();
        stack.push(1);
        visited[1] = true;
        // First DFS to collect postorder
        while (!stack.isEmpty()) {
            int node = stack.pop();
            postOrder.push(node);
            for (int neighbor : tree.get(node)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                    visited[neighbor] = true;
                }
            }
        }
        // Postorder processing to fill dp values
        while (!postOrder.isEmpty()) {
            int node = postOrder.pop();
            int sum = 0;
            for (int child : tree.get(node)) {
                if (dp[child][0] == 0 && dp[child][1] == 0) continue;
                sum += Math.max(dp[child][0], dp[child][1]);
            }
            dp[node][0] = sum;
            for (int child : tree.get(node)) {
                int match = 1 + dp[child][0] + (sum - Math.max(dp[child][0], dp[child][1]));
                dp[node][1] = Math.max(dp[node][1], match);
            }
        }
    }
}
