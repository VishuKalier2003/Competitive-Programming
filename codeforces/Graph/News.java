/*
 * QUes - News Distribution - https://codeforces.com/problemset/problem/1167/C
 */

import java.util.*;

public class News {
    public static void main(String[] args) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int n, m;
        DSU dsu;
        input: {
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            n = Integer.parseInt(str.split(" ")[0]);
            m = Integer.parseInt(str.split(" ")[1]);
            dsu = new DSU(n);
            for(int i = 1; i <= n; i++)
                graph.put(i, new ArrayList<Integer>());
            for(int i = 0; i < m; i++) {
                updateComponents(dsu, sc.nextLine());
            }
            sc.close();
            break input;
        }
        output: {
            System.out.println(evaluateResult(dsu));
            break output;
        }
    }

    public static void updateComponents(DSU dsu, String s) {
        int foundParent = 0;
        String str[] = s.split(" ");
        if(str.length == 1)
            return;
        for(int i = 1; i < str.length; i++) {
            int node = Integer.parseInt(str[i]);
            if(dsu.parent[node] != node)
                foundParent = i;
        }
        int node1 = foundParent != 0 ? foundParent : Integer.parseInt(str[1]);
        for(int i = 1; i < str.length; i++)
            dsu.union(node1, Integer.parseInt(str[i]));
        return;
    }

    public static String evaluateResult(DSU dsu) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for(int i = 1; i < dsu.parent.length; i++)
            freqMap.put(dsu.parent[i], freqMap.getOrDefault(dsu.parent[i], 0)+1);
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < dsu.parent.length; i++)
            sb.append(freqMap.get(dsu.parent[i])+" ");
        return sb.toString().trim();
    }

    public static class DSU {
        int rank[], parent[];

        public DSU(int n) {
            this.rank = new int[n+1];
            this.parent = new int[n+1];
            for(int i = 0; i < n+1; i++)
                parent[i] = i;
        }

        public int find(int x) {
            if(x != parent[x])
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if(rootX != rootY) {
                // Smaller tree merged with larger tree...
                if(rank[rootX] < rank[rootY])
                    parent[rootX] = rootY;
                else if(rank[rootY] < rank[rootX])
                    parent[rootY] = rootX;
                else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
            return;
        }
    }
}
