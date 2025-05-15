/*
 * Ques - Mr. Kitayuta's Colorful Graph - https://codeforces.com/problemset/problem/505/B
 * Tags - Graph, DSU
 * Rating - 1400
 */

import java.util.*;

public class ColorfulGraph {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); // Number of vertices...
        int m = sc.nextInt(); // Number of edges...
        // Map to store DSU (disjoint set union) for each color...
        Map<Integer, DSU> colorGroups = new HashMap<>();
        // Read edges and populate DSUs for each color...
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int color = sc.nextInt();
            // Initialize DSU for this color if not already present...
            colorGroups.putIfAbsent(color, new DSU(n));
            colorGroups.get(color).union(u, v);     //Connect the two nodes having same color...
        }
        // Process queries...
        int q = sc.nextInt();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < q; i++) {
            int u = sc.nextInt();   // Node 1...
            int v = sc.nextInt();   // Node 2...
            int count = 0;
            // Check each color to see if `u` and `v` are connected...
            for (int color : colorGroups.keySet()) {
                // Check if the two nodes belong to the same color group...
                if (colorGroups.get(color).find(u) == colorGroups.get(color).find(v)) {
                    count++;
                }
            }
            result.append(count).append("\n");
        }
        System.out.print(result);   // Return the string...
        sc.close();
    }
}

// Disjoint Set Union (Union-Find) class...
class DSU {
    int[] parent, rank;

    public DSU(int size) {
        // Extra size as +1 for 1-based indexing...
        parent = new int[size + 1];
        rank = new int[size + 1];
        for (int i = 1; i <= size; i++) {
            parent[i] = i;
        }
    }

    // Find the root of the set containing x...
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression...
        }
        return parent[x];
    }

    // Union two sets
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            // Merge smaller tree with larger tree as per rank...
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}
