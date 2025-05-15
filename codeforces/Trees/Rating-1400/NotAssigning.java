// https://codeforces.com/problemset/problem/1627/C

import java.io.*;
import java.util.*;

public class NotAssigning {
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

    // A simple pair to hold (neighbor, edgeIndex)
    static class Edge {
        int to, idx;
        Edge(int to, int idx) {
            this.to = to;
            this.idx = idx;
        }
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        StringBuilder output = new StringBuilder();

        int t = in.nextInt();
        while (t-- > 0) {
            int n = in.nextInt();
            // 1-based vertices, edges 0…n-2
            List<List<Edge>> adj = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());

            int[] uArr = new int[n - 1], vArr = new int[n - 1];
            for (int i = 0; i < n - 1; i++) {
                int u = in.nextInt();
                int v = in.nextInt();
                uArr[i] = u; vArr[i] = v;
                adj.get(u).add(new Edge(v, i));
                adj.get(v).add(new Edge(u, i));
            }

            // 1. Check max degree ≤ 2
            boolean valid = true;
            int start = 1;
            for (int i = 1; i <= n; i++) {
                if (adj.get(i).size() > 2) {
                    valid = false;
                    break;
                }
                // pick a leaf as starting point
                if (adj.get(i).size() == 1) {
                    start = i;
                }
            }
            if (!valid) {
                output.append("-1\n");
                continue;
            }

            // 2. Walk the path from one leaf to the other
            int[] answer = new int[n - 1];
            boolean[] visited = new boolean[n + 1];
            int curr = start;
            int prev = 0;
            int weightToggle = 0; // 0→assign 2, 1→assign 3

            // traverse exactly n-1 edges
            for (int step = 0; step < n - 1; step++) {
                visited[curr] = true;
                // find the one neighbour not equal to prev
                for (Edge e : adj.get(curr)) {
                    if (!visited[e.to]) {
                        // assign prime weight
                        answer[e.idx] = (weightToggle == 0 ? 2 : 3);
                        weightToggle ^= 1;
                        prev = curr;
                        curr = e.to;
                        break;
                    }
                }
            }

            // 3. Emit in input order
            for (int w : answer) {
                output.append(w).append(' ');
            }
            output.append('\n');
        }

        System.out.print(output);
    }
}
