import java.io.*;
import java.util.*;

public class P6RoundTrip {
    static int n, m;
    static List<Integer>[] graph;
    static boolean[] visited;
    static int[] parent;
    static int cycleStart = -1, cycleEnd = -1;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        // Fast input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        // Initialize graph
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();

        // Read edges
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph[a].add(b);
            graph[b].add(a);
        }

        visited = new boolean[n + 1];
        parent = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            if (!visited[i] && dfs(i, -1)) break;
        }

        if (cycleStart == -1) {
            System.out.println("IMPOSSIBLE");
        } else {
            List<Integer> cycle = new ArrayList<>();
            cycle.add(cycleStart);
            for (int v = cycleEnd; v != cycleStart; v = parent[v]) {
                cycle.add(v);
            }
            cycle.add(cycleStart); // To complete the round trip
            Collections.reverse(cycle);
            System.out.println(cycle.size());
            for (int city : cycle) {
                System.out.print(city + " ");
            }
            System.out.println();
        }
    }

    static boolean dfs(int node, int par) {
        visited[node] = true;
        parent[node] = par;

        for (int neighbor : graph[node]) {
            if (neighbor == par) continue;
            if (visited[neighbor]) {
                cycleStart = neighbor;
                cycleEnd = node;
                return true;
            } else {
                if (dfs(neighbor, node)) return true;
            }
        }
        return false;
    }
}
