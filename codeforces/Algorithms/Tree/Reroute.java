import java.io.*;
import java.util.*;

public class Reroute {
    static List<List<Integer>> tree;
    static int[] subtree, dpSum, result;
    static int n;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        n = fast.nextInt();

        // 1‑based, undirected adjacency
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++) tree.add(new ArrayList<>());
        for (int i = 1; i < n; i++) {
            int a = fast.nextInt(), b = fast.nextInt();
            tree.get(a).add(b);
            tree.get(b).add(a);
        }

        subtree = new int[n+1];
        dpSum   = new int[n+1];
        result  = new int[n+1];

        // Phase 1: post‑order DP from root=1
        dfs1(1, 0);

        // Phase 2: rerooting (pre‑order)
        result[1] = dpSum[1];
        dfs2(1, 0);

        // output
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) sb.append(result[i]).append(' ');
        w.write(sb.toString());
        w.flush();
    }

    // compute subtree sizes and sum‑of‑distances within each subtree
    static void dfs1(int u, int parent) {
        subtree[u] = 1;
        dpSum[u]   = 0;
        for (int v : tree.get(u)) {
            if (v == parent) continue;
            dfs1(v, u);
            subtree[u] += subtree[v];
            dpSum[u]   += dpSum[v] + subtree[v];
        }
    }

    // reroot: propagate the full‑tree sum from u → v
    static void dfs2(int u, int parent) {
        for (int v : tree.get(u)) {
            if (v == parent) continue;
            // Hack: nodes in v‑subtree get 1 closer, all others get 1 farther
            result[v] = result[u] - subtree[v] + (n - subtree[v]);
            dfs2(v, u);
        }
    }

    // fast input helper (unchanged)
    static class FastReader {
        BufferedReader buffer;
        StringTokenizer tok;
        FastReader() { buffer = new BufferedReader(new InputStreamReader(System.in)); }
        @SuppressWarnings("CallToPrintStackTrace")
        String next() {
            while (tok == null || !tok.hasMoreTokens()) {
                try { tok = new StringTokenizer(buffer.readLine()); }
                catch (IOException e) { e.printStackTrace(); }
            }
            return tok.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
    }
}