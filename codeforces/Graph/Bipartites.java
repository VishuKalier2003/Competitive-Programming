/*
 * Ques - Mahmud and Ehab and Bipartiteness - https://codeforces.com/problemset/problem/862/B
 * Tags - Graph, Bipartite, Math, BFS, Trees
 * Rating - 1200
 */

import java.util.*;

public class Bipartites {
    public static void main(String[] args) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int t, firstNode = 0;
        input: {    // Input block...
            Scanner sc = new Scanner(System.in);
            t = sc.nextInt();
            sc.nextLine();
            for(int i = 0; i < t-1; i++) {
                String s = sc.nextLine();
                int n1 = Integer.parseInt(s.split(" ")[0]), n2 = Integer.parseInt(s.split(" ")[1]);
                firstNode = firstNode == 0 ? n1 : firstNode;
                // Undirected graph, so every edge needs to be updated twice...
                if(!graph.containsKey(n1))
                    graph.put(n1, new ArrayList<Integer>());
                if(!graph.containsKey(n2))
                    graph.put(n2, new ArrayList<Integer>());
                graph.get(n1).add(n2);
                graph.get(n2).add(n1);
            }
            sc.close();
            break input;
        }
        output: {       // Output plock...
            System.out.println(maximumEdges(graph, t, firstNode));
            break output;
        }
    }

    public static long redNodes = 0;        // Number of red Nodes...
    public static long whiteNodes = 0;      // Number of white nodes...
    public static long maximumEdges(Map<Integer, List<Integer>> graph, int n, int start) {
        updateNodeColor(graph, n, start);       // Update Graph color...
        long newEdges = 0l;
        Queue<Integer> queue = new LinkedList<Integer>();       // Queue defined...
        queue.add(start);   // Start both bfs from same node, to maintain color atomicity...
        boolean red = true; boolean visited[] = new boolean[n];     // BFS elements...
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                int node = queue.poll();
                visited[node-1] = true;
                logic: {    // The logic for calculation of edges...
                    if(red)
                        newEdges += whiteNodes - graph.get(node).size();    // All nodes except neighbor...
                    else    newEdges += redNodes - graph.get(node).size();  // All nodes except neighbor...
                    break logic;
                }
                for(int neighbor : graph.get(node))
                    if(!visited[neighbor-1])
                        queue.add(neighbor);
            }
            red = !red;     // Alternating color at every depth...
        }
        return newEdges/2;
    }

    public static void updateNodeColor(Map<Integer, List<Integer>> graph, int n, int start) {
        bfs: {      // Breadth First Search...
            Queue<Integer> queue = new LinkedList<Integer>();
            queue.add(start);
            boolean red = true; boolean visited[] = new boolean[n];
            while(!queue.isEmpty()) {
                int size = queue.size();
                for(int i = 0; i < size; i++) {
                    int node = queue.poll();
                    visited[node-1] = true;
                    if(red)
                        redNodes++;
                    else    whiteNodes++;
                    for(int neighbor : graph.get(node))
                        if(!visited[neighbor-1])
                            queue.add(neighbor);
                }
                red = !red;     // Alternating color at every depth...
            }
            break bfs;
        }
        return;
    }
}
