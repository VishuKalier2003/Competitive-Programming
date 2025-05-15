/*
 * Ques - Chultu - https://codeforces.com/problemset/problem/103/B
 * Tags - Graph, Cycle Detection, DSU
 * Rating - 1500
 */

import java.util.*;

public class Chultu {
    public static void main(String[] args) {
        int n, m;
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        input: {
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
            m = sc.nextInt();
            sc.nextLine();
            for(int i = 1; i <= n; i++)
                adjList.put(i, new ArrayList<Integer>());
            for(int i = 0; i < m; i++) {
                String str[] = sc.nextLine().split(" ");
                int node1 = Integer.parseInt(str[0]), node2 = Integer.parseInt(str[1]);
                adjList.get(node1).add(node2);
                adjList.get(node2).add(node1);
            }
            sc.close();
            break input;
        }
        System.out.println(findChultu(m, n, adjList));
    }

    public static String findChultu(int m, int n, Map<Integer, List<Integer>> adjList) {
        if(n != m)
            return "NO";
        DSU dsu = new DSU(n);
        int countCycle = 0;
        boolean visited[] = new boolean[n+1];
        for(Map.Entry<Integer, List<Integer>> entry : adjList.entrySet()) {
            int node = entry.getKey();
            for(int neighbor : adjList.get(node))
                dsu.union(node, neighbor);
            if(!visited[node]) {
                if(detectCycle(node, -1, adjList, visited)) {
                    countCycle++;
                    System.out.println("node cycle starts : "+node);
                }
            }
        }
        System.out.println(countCycle);
        System.out.println(Arrays.toString(dsu.parent));
        int parent = dsu.parent[1];
        for(int i = 1; i < dsu.parent.length; i++)
            if(parent != dsu.parent[i])
                return "NO";
        return countCycle == 1 ? "FHTAGN!" : "NO";
    }

    public static boolean detectCycle(int node, int parent, Map<Integer, List<Integer>> adjList, boolean[] visited) {
        visited[node] = true; // Mark the current node as visited
        for (int neighbor : adjList.get(node)) {
            if (!visited[neighbor]) {
                // Recur for the unvisited neighbor
                if (detectCycle(neighbor, parent, adjList, visited)) {
                    return true; // Cycle detected in recursion
                }
            } else if (neighbor == parent) {
                // If visited and not the parent, a cycle is detected
                return true;
            }
        }
        return false; // No cycle found in this DFS path
    }



    public static class DSU {
        int parent[], rank[];
        public DSU(int size) {
            this.parent = new int[size+1];
            this.rank = new int[size+1];
            for(int i = 1; i <= size; i++)
                this.parent[i] = i;
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
                else if(rank[rootX] > rank[rootY])
                    parent[rootY] = rootX;
                else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
}
