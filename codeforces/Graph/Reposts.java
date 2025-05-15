/*
 * Ques - Reposts - https://codeforces.com/problemset/problem/522/A
 * Tags - Graph, BFS
 * Rating - 1100
 */

import java.util.*;

public class Reposts {
    private static class Graph {    // Class to store the adjacency list...
        Map<String, List<String>> graph = new HashMap<>();

        public void addValue(String s) {    // Creating adjacency list from String...
            String str[] = s.split(" ");
            if(!graph.containsKey(str[2].toLowerCase()))
                graph.put(str[2].toLowerCase(), new ArrayList<String>());
            if(!graph.containsKey(str[0].toLowerCase()))
                graph.put(str[0].toLowerCase(), new ArrayList<String>());
            graph.get(str[2].toLowerCase()).add(str[0].toLowerCase());
        }
    }
    public static void main(String[] args) {
        inputOutput: {  // Input and Output code block...
            Scanner sc = new Scanner(System.in);
            int t = sc.nextInt();
            sc.nextLine();
            Graph graph = new Graph();
            for(int i = 0; i < t; i++)
                graph.addValue(sc.nextLine());
            sc.close();
            System.out.println(maxDepth(graph.graph));
            break inputOutput;
        }
    }

    public static int maxDepth(Map<String, List<String>> graph) {
        // Applying a BFS will give the max depth...
        Queue<String> queue = new LinkedList<String>();
        queue.add("polycarp");      // Start node...
        int depth = 0;
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                String node = queue.poll();
                for(String sent : graph.get(node))
                    queue.add(sent);
            }
            depth++;    // Increment depth level by level...
        }
        return depth;
    }
}
