/*
 * Ques - Hungcow Builds a Nation - https://codeforces.com/problemset/problem/744/A
 * Tags - Graph, Math, Components
 * Ratings - 1600
 */

import java.util.*;

public class Nation {
    class Country {     //Country class to store details of each group individually...
        int edges;      // Storing the number of edges already present...
        int nodes;      // Storing the number of nodes already present...
        //Functions to add more edges and nodes...
        void addOneEdge() {this.edges++;}
        void addOneNode() {this.nodes++;}
    }
    public static void main(String[] args) {
        int n, m, k;
        int parliaments[];      // The nodes which are parliaments (a country's house)...
        Nation nation = new Nation();
        // This map will be used for bfs to start each bfs from the parliament node...
        Map<Integer, Country> countryMap = new HashMap<>();
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        input:{     // Input block...
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
            m = sc.nextInt();
            k = sc.nextInt();
            sc.nextLine();
            parliaments = new int[k];
            String s[] = sc.nextLine().split(" ");
            for(int i = 0; i < parliaments.length; i++) {
                parliaments[i] = Integer.parseInt(s[i]);
                if(!countryMap.containsKey(parliaments[i]))     // Create a country...
                    countryMap.put(parliaments[i], nation.new Country());
            }
            for(int i = 1; i <= n; i++)
                adjList.put(i, new ArrayList<Integer>());
            for(int i = 0; i < m; i++) {    // Update adjacency list...
                String str[] = sc.nextLine().split(" ");
                int node1 = Integer.parseInt(str[0]), node2 = Integer.parseInt(str[1]);
                adjList.get(node1).add(node2);
                adjList.get(node2).add(node1);
            }
            sc.close();
            break input;
        }
        System.out.println(maxStableEdges(adjList, countryMap, parliaments, n));
    }

    public static int governance[];     // The array stores the country to which a node belongs to...
    public static boolean freeNations[];    // This marks the nodes which do not belong to a government...
    public static int freeEdges = 0;    // The edges contained in such free nodes...
    public static long maxStableEdges(Map<Integer, List<Integer>> adjList, Map<Integer, Country> countries, int parliaments[], int n) {
        governance = new int[n+1];
        freeNations = new boolean[n+1];
        Arrays.fill(governance, -1);
        for(int parliament : parliaments)       // Used to find the edges of nodes which are part of a country...
            findRoads(adjList, countries, parliament, n);
        findFreeNationRoads(adjList);       // Used to find edges of nodes which are free (not part of any country)...
        int count = 0;
        for(int i = 1; i <= n; i++)
            count += governance[i] == -1 ? 1 : 0;
        return mergeAndMakeStable(countries, count);    // Merge the free nodes...
    }

    public static void findFreeNationRoads(Map<Integer, List<Integer>> adjList) {
        for(int i = 1; i < governance.length; i++)
            if(governance[i] == -1)
                bfs(i, adjList);        // Perform bfs...
    }

    public static void bfs(int node, Map<Integer, List<Integer>> adjList) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(node);
        freeNations[node] = true;
        // Here we traverse a node only once, so we add the edge also only once...
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                int free = queue.poll();
                freeNations[free] = true;
                for(int freeNation : adjList.get(free))
                    if(!freeNations[freeNation]) {
                        freeNations[freeNation] = true;
                        freeEdges++;        // Update the edges...
                        queue.add(freeNation);
                    }
            }
        }
    }

    public static long mergeAndMakeStable(Map<Integer, Country> countries, int merge) {
        int max = 0, edge = 0, government = 0;
        // The free nodes will be merged to the country with largest nodes count...
        for(int key : countries.keySet()) {
            if(countries.get(key).nodes > max) {
                max = countries.get(key).nodes;
                edge = countries.get(key).edges;
                government = key;
            }   // If the two countries have same nodes, then we check the edges...
            else if(countries.get(key).nodes == max && countries.get(key).edges == edge) {
                max = countries.get(key).nodes;
                edge = countries.get(key).edges;
                government = key;
            }
        }
        countries.get(government).nodes += merge;       // Update the nodes...
        countries.get(government).edges += freeEdges;   // Update the free edges...
        long sum = 0;
        for(Country country : countries.values())
            // Use mathematical method to count the number of extra edges possible...
            sum += (((country.nodes) * (country.nodes-1))/2 - country.edges);
        return sum;
    }

    public static void findRoads(Map<Integer, List<Integer>> adjList, Map<Integer, Country> countries, int parliament, int n) {
        Queue<Integer> queue = new LinkedList<>();      // BFS algorithm...
        boolean visited[] = new boolean[n+1];
        queue.add(parliament);
        governance[parliament] = parliament;    // Mark the country to which the node belongs to...
        countries.get(parliament).addOneNode();
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                int node = queue.poll();
                visited[node] = true;
                for (int neighbor : adjList.get(node)) {
                    if (!visited[neighbor]) {
                        countries.get(parliament).addOneEdge(); // Count this edge...
                        countries.get(parliament).addOneNode(); // Update node...
                        governance[neighbor] = parliament;
                        queue.add(neighbor);
                        visited[neighbor] = true;
                    } else if (governance[neighbor] == parliament) {
                        // Count edge if it's already part of the same country...
                        countries.get(parliament).addOneEdge();
                    }
                }
            }
        }
        // Divide edges by 2, since each edge is counted twice...
        countries.get(parliament).edges = countries.get(parliament).edges/2;
        return;
    }
}
