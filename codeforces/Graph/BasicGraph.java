import java.util.*;

public class BasicGraph {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);    // Input class defined...
        // Map created (key - int) and (value - list of neighboring nodes (int type))...
        Map<Integer, List<Integer>> graph = new HashMap<>();
        // Create Operation...
        create: {
            for(int i = 1; i <= 5; i++)
                graph.put(i, new ArrayList<Integer>()); // node -> empty arraylist (int)...
            break create;
        }
        // Add / Update Operation...
        add: {
            // Node 1...
            graph.get(1).add(2);    // Connect the neighbors...
            graph.get(1).add(3);
            // Node 2...
            graph.get(2).add(1);
            graph.get(2).add(4);
            graph.get(2).add(5);
            // Node 3...
            graph.get(3).add(1);
            graph.get(3).add(4);
            // Node 4...
            graph.get(4).add(2);
            graph.get(4).add(3);
            // Node 5...
            graph.get(5).add(2);
            System.out.println("Graph : "+graph);
            break add;
        }
        search: {
            System.out.println("Search result of Node 1 : "+graph.containsKey(1));
            System.out.println("Search result of Node 6 : "+graph.containsKey(6));
            break search;
        }
        delete: {
            graph.remove(5);
            System.out.println("Graph after removal of node 5 : "+graph);
            break delete;
        }
        sc.close();
    }
}

/*
 1 -- 2 -- 5
 |    |
 3 -- 4
 */
