/*
 * Ques - Lunar New Year and Wander - https://codeforces.com/problemset/problem/1106/D
 * Tags - Graph, Dijkstra, heap, greedy
 * Rating - 1500
 */

import java.util.*;

public class LunarYear {
    public static void main(String[] args) {
        int n, m;
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        input: {    // Input block...
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
            m = sc.nextInt();
            sc.nextLine();
            for(int i = 1; i <= n; i++)
                adjList.put(i, new ArrayList<Integer>());
            for(int i = 0; i < m; i++) {
                String s[] = sc.nextLine().split(" ");
                int node1 = Integer.parseInt(s[0]), node2 = Integer.parseInt(s[1]);
                // Adding bi-directional edges...
                adjList.get(node1).add(node2);
                adjList.get(node2).add(node1);
            }
            sc.close();
            break input;
        }
        dijkstra(adjList, n);   // Calling the dijkstra algorithm...
    }

    public static void dijkstra(Map<Integer, List<Integer>> adjList, int n) {
        // Minheap defined for the dijkstra to store the greedy smallest nodes among current nodes...
        PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>();
        StringBuilder sb = new StringBuilder();
        boolean visited[] = new boolean[n+1];   // Visited array to compute nodes once...
        minHeap.add(1);     // Start node added...
        while(!minHeap.isEmpty()) {
            int node = minHeap.poll();  // Extracting the smallest node...
            sb.append(node+" ");
            visited[node] = true;       // Marking node as visited...
            for(int neighbor : adjList.get(node))
                if(!visited[neighbor]) {
                    visited[neighbor] = true;
                    minHeap.add(neighbor);
                }
        }
        System.out.println(sb.toString().trim());   // Printing the lexicographically smallest traversal...
        return;
    }
}
