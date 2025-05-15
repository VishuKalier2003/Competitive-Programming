/*
 * Ques - Badge - https://codeforces.com/problemset/problem/1020/B
 * Tags - Graph, DFS, Set
 * Rating - 1000
 */

import java.util.*;

public class Badge {
    public static void main(String[] args) {
        int n;
        Map<Integer, List<Integer>> graph = new HashMap<>();    //! Graph defined...
        input: {
            Scanner sc = new Scanner(System.in); // Input class...
            n = sc.nextInt(); // Number of nodes...
            sc.nextLine();
            for (int i = 1; i <= n; i++)
                graph.put(i, new ArrayList<>());    // Creating or Initialising a graph...
            // Filling the edges...
            String str[] = sc.nextLine().split(" ");    //! Taking String input...
            for (int i = 1; i <= n; i++)
                graph.get(i).add(Integer.parseInt(str[i-1]));       // p of i...
            sc.close();
            break input;
        }
        output: {
            findCulprit(graph, n);
            break output;
        }
    }

    public static void findCulprit(Map<Integer, List<Integer>> graph, int n) {
        for(int student = 1; student <= n; student++)     // Running the recursive call for the ith student...
            simulate(graph, student, new boolean[n+1]);
        return;
    }

    public static void simulate(Map<Integer, List<Integer>> graph, int node, boolean visited[]) {
        // Base case initialized...
        if(visited[node]) {
            // When reaching a node that is visited, that means that student will get the second hole...
            System.out.print(node+" ");
            return;
        }
        visited[node] = true;   // Marking the student visited...
        for(int neighbor : graph.get(node))
            simulate(graph, neighbor, visited);     // Recursive call with neighbor nodes...
        return;
    }
}

/*

 */
