import java.io.*;
import java.util.*;

public class maxflow {
    static final int MAXN = 50000;
    static final int LOGN = 16;

    @SuppressWarnings("unchecked")
    static List<Integer>[] adj = new ArrayList[MAXN + 1];
    static int[][] parent = new int[MAXN + 1][LOGN + 1];
    static int[] depth = new int[MAXN + 1];
    static long[] diff = new long[MAXN + 1];
    static long maxFlow = 0;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("maxflow.in")); PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("maxflow.out")))) {
            
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int K = Integer.parseInt(st.nextToken());
            
            for (int i = 1; i <= N; i++) {
                adj[i] = new ArrayList<>();
            }
            
            // Read tree edges
            for (int i = 0; i < N - 1; i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                adj[x].add(y);
                adj[y].add(x);
            }
            
            dfsBuild(1, 0);
            
            for (int k = 1; k <= LOGN; k++) {
                for (int v = 1; v <= N; v++) {
                    int mid = parent[v][k - 1];
                    parent[v][k] = (mid == 0 ? 0 : parent[mid][k - 1]);
                }
            }
            
            // Process pumping paths
            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());
                int s = Integer.parseInt(st.nextToken());
                int t = Integer.parseInt(st.nextToken());
                int l = lca(s, t);
                
                diff[s]++;
                diff[t]++;
                diff[l]--;
                if (parent[l][0] != 0) {
                    diff[parent[l][0]]--;
                }
            }
            
            dfsAccumulate(1, 0);
            
            out.println(maxFlow);
        }
    }

    static void dfsBuild(int u, int p) {
        parent[u][0] = p;
        depth[u] = depth[p] + 1;
        for (int v : adj[u]) {
            if (v != p) {
                dfsBuild(v, u);
            }
        }
    }

    static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
        }

        int diffD = depth[u] - depth[v];
        for (int k = 0; k <= LOGN; k++) {
            if (((diffD >> k) & 1) == 1) {
                u = parent[u][k];
            }
        }

        if (u == v) return u;

        for (int k = LOGN; k >= 0; k--) {
            if (parent[u][k] != parent[v][k]) {
                u = parent[u][k];
                v = parent[v][k];
            }
        }

        return parent[u][0];
    }

    static void dfsAccumulate(int u, int p) {
        for (int v : adj[u]) {
            if (v != p) {
                dfsAccumulate(v, u);
                diff[u] += diff[v];
            }
        }
        maxFlow = Math.max(maxFlow, diff[u]);
    }
}
