// https://codeforces.com/problemset/problem/1830/A

import java.io.*;
import java.util.*;

public class CopilCopac {
    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try { st = new StringTokenizer(br.readLine()); }
                catch (IOException e) { e.printStackTrace(); }
            }
            return st.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
    }

    static List<List<Pair>> graph;
    static int[] dp, id;
    static int maxScan;

    static class Pair {
        int node, index;
        Pair(int node, int index) {
            this.node = node;
            this.index = index;
        }
    }

    public static void dfs(int u, int parent) {
        for (Pair neighbor : graph.get(u)) {
            int v = neighbor.node;
            int edgeIndex = neighbor.index;
            if (v == parent) continue;

            if (edgeIndex >= id[u]) {
                dp[v] = dp[u];
                id[v] = edgeIndex;
            } else {
                dp[v] = dp[u] + 1;
                id[v] = edgeIndex;
            }
            maxScan = Math.max(maxScan, dp[v]);
            dfs(v, u);
        }
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        int t = in.nextInt();
        StringBuilder out = new StringBuilder();

        while (t-- > 0) {
            int n = in.nextInt();
            graph = new ArrayList<>();
            for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());

            for (int i = 0; i < n - 1; i++) {
                int u = in.nextInt();
                int v = in.nextInt();
                graph.get(u).add(new Pair(v, i));
                graph.get(v).add(new Pair(u, i));
            }

            dp = new int[n + 1];
            id = new int[n + 1];
            Arrays.fill(dp, 0);
            Arrays.fill(id, Integer.MAX_VALUE);
            dp[1] = 1;
            id[1] = -1;
            maxScan = 1;

            dfs(1, 0);
            out.append(maxScan).append("\n");
        }

        System.out.print(out);
    }
}
