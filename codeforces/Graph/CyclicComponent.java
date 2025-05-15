/*
 * Ques - Cyclic Components - https://codeforces.com/problemset/problem/977/E
 * Tags - Graph, Map, DSU
 * Rating - 1400
 */

import java.util.*;

public class CyclicComponent {
    public static void main(String[] args) {
        int n, m;
        CyclicComponent cyclicComponent = new CyclicComponent();
        Map<Integer, Set<Integer>> adjList = new HashMap<>();
        input: {
            Scanner sc = new Scanner(System.in);
            String s[] = sc.nextLine().split(" ");
            n = Integer.parseInt(s[0]);
            m = Integer.parseInt(s[1]);
            for(int i = 1; i <= n; i++)
                adjList.put(i, new HashSet<Integer>());
            for(int j = 0; j < m; j++) {
                String edge[] = sc.nextLine().split(" ");
                int node1 = Integer.parseInt(edge[0]), node2 = Integer.parseInt(edge[1]);
                adjList.get(node1).add(node2);
                adjList.get(node2).add(node1);
            }
            sc.close();
            break input;
        }
        output: {
            System.out.println(cyclicComponent.countCyclicComponents(adjList, n, cyclicComponent));
            break output;
        }
    }

    public int countCyclicComponents(Map<Integer, Set<Integer>> adjList, int n, CyclicComponent cyclicComponent) {
        DSU dsu = cyclicComponent.new DSU(n);
        for(int i = 1; i <= n; i++)
            for(int neighbor : adjList.get(i))
                dsu.union(i, neighbor);
        Set<Integer> searched = new HashSet<Integer>();
        int count = 0;
        for(int i = 1; i <= n; i++) {
            int root = dsu.find(i);
            if(searched.add(root) && cyclicComponent.findCycles(root, adjList, dsu))
                count++;
        }
        return count;
    }

    public boolean findCycles(int root, Map<Integer, Set<Integer>> adjList, DSU dsu) {
        for(int node : adjList.keySet())
            if(dsu.find(node) == root && adjList.get(node).size() != 2)
                return false;
        return true;
    }

    public class DSU {
        int parent[], rank[];

        public DSU(int n) {
            this.parent = new int[n+1];
            this.rank = new int[n+1];
            for(int i = 0; i <= n; i++)
                parent[i] = i;
        }

        public int find(int x) {
            if(parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if(rootX != rootY) {
                if(rank[rootX] < rank[rootY])
                    parent[rootX] = rootY;
                else if(rank[rootY] < rank[rootX])
                    parent[rootY] = rootX;
                else {
                    parent[rootX] = rootY;
                    rank[rootY]++;
                }
            }
        }
    }
}
