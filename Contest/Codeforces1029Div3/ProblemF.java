import java.io.*;
import java.util.*;

public class ProblemF {
    static final int MOD = 1_000_000_007;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        int t = Integer.parseInt(in.readLine().trim());
        while (t-- > 0) {
            int n = Integer.parseInt(in.readLine().trim());
            List<Integer>[] adj = new List[n+1];
            for (int i = 1; i <= n; i++) adj[i] = new ArrayList<>();
            for (int i = 0; i < n-1; i++) {
                StringTokenizer st = new StringTokenizer(in.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                adj[u].add(v);
                adj[v].add(u);
            }

            // 1) Compute subtree sizes sz[u] by DFS from root=1
            int[] sz = new int[n+1];
            dfsSize(1, 0, adj, sz);

            // 2) Build intervals [sz[u], 2*sz[u]] for each node u
            Interval[] intervals = new Interval[n];
            for (int u = 1; u <= n; u++) {
                intervals[u-1] = new Interval(sz[u], 2 * sz[u]);
            }

            // 3) Sort by right endpoint ascending
            Arrays.sort(intervals, Comparator.comparingInt(iv -> iv.r));

            // 4) Greedy count
            long ways = 1;
            // 'used' = how many integers we've already chosen
            int used = 0;
            for (Interval iv : intervals) {
                int length = iv.r - iv.l + 1;
                int choices = length - used;
                if (choices <= 0) {
                    ways = 0;
                    break;
                }
                ways = (ways * choices) % MOD;
                used++;
            }

            out.append(ways).append('\n');
        }

        System.out.print(out);
    }

    // Standard DFS to compute subtree sizes
    static void dfsSize(int u, int p, List<Integer>[] adj, int[] sz) {
        sz[u] = 1;
        for (int v : adj[u]) {
            if (v == p) continue;
            dfsSize(v, u, adj, sz);
            sz[u] += sz[v];
        }
    }

    // Simple pair for an integer interval [l, r]
    static class Interval {
        int l, r;
        Interval(int l, int r) { this.l = l; this.r = r; }
    }
}
