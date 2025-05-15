/*
* Ques - Dorm Water Supply - https://codeforces.com/problemset/problem/107/A
* Tags - Graph, MST
* Rating - 1400
 */

import java.util.*;

public class WaterSupply {
    public static void main(String[] args) {
        int n, p;   // Number of houses and pipes...
        Map<Integer, int[]> map = new HashMap<>();  // Adjacency list...
        boolean tanks[];    // Used later to check the houses with tanks...
        input: {
            Scanner scanner = new Scanner(System.in);
            String str[] = scanner.nextLine().split(" ");
            n = Integer.parseInt(str[0]); p = Integer.parseInt(str[1]);
            for(int i = 1; i <= n; i++)     // Initializing the map...
                map.put(i, new int[]{-1, -1});
            tanks = new boolean[n+1];
            for(int i = 0; i < p; i++) {
                String s[] = scanner.nextLine().split(" ");
                int node1 = Integer.parseInt(s[0]), node2 = Integer.parseInt(s[1]);
                int pipe = Integer.parseInt(s[2]);
                map.get(node1)[0] = node2;      // Directed graph...
                map.get(node1)[1] = pipe;   // Update the diameter into the adjacency list...
                tanks[node2] = true;        // Set next node as tap...
            }
            scanner.close();
            break input;
        }
        output: {
            if(p != 0)  // If there are pipes provided...
                findOptimalWaterFlow(map, tanks, n);
            else    System.out.println(0);
            break output;
        }
    }

    public static int pipe;
    public static void findOptimalWaterFlow(Map<Integer, int[]> graph, boolean tanks[], int n) {
        Queue<Integer> queue = new LinkedList<Integer>();
        for(int i = 1; i <= n; i++)
            // Finding tanks which connect to at least one house...
            if(!tanks[i] && graph.get(i)[0] != -1)
                queue.add(i);
        System.out.println(queue.size());
        while(!queue.isEmpty()) {   // Processing each tank branch...
            pipe = 0;   // Static variable re-initialized every time...
            // Extracting the maximum diameter possible...
            int diameter = maxWaterFlow(graph, queue.peek(), Integer.MAX_VALUE);
            if(pipe != queue.peek())    // If the tap and tank are not same...
                System.out.println(queue.peek()+" "+pipe+" "+diameter);
            queue.remove();
        }
        return;
    }

    public static int maxWaterFlow(Map<Integer, int[]> graph, int node, int flow) {
        while(graph.get(node)[0] != -1) {
            // Looping till reaching the sink...
            flow = Math.min(flow, graph.get(node)[1]);
            node = graph.get(node)[0];
        }
        pipe = node;    // Update last house as the tap...
        return flow;    // Return the max diameter possible...
    }
}
