/*
 * Ques - Students and Shoelaces - https://codeforces.com/problemset/problem/129/B
 * Tags - Graph, Indegree
 * Rating - 1200
 */

import java.util.*;

public class Shoelaces {
    public static class TestCase {  // Graph Test case...
        // Variables...
        int students, shoelaces;
        Map<Integer, TreeSet<Integer>> adjList = new HashMap<>();   // Adjacency List...
        int degree[];       // In degree array for each node (student)...

        public TestCase(String str) {       // Parametrized Constructor...
            String s[] = str.split(" ");
            this.students = Integer.parseInt(s[0]);
            this.shoelaces = Integer.parseInt(s[1]);
            this.degree = new int[this.students];
        }

        public void createEdge(String str) {    // Updating a given edge into the adjacency list...
            int s1 = Integer.parseInt(str.split(" ")[0])-1, s2 = Integer.parseInt(str.split(" ")[1])-1;
            // Since undirected, need to be added for two nodes (both A and B)...
            if(!adjList.containsKey(s1))
                adjList.put(s1, new TreeSet<Integer>());
            if(!adjList.containsKey(s2))
                adjList.put(s2, new TreeSet<Integer>());
            adjList.get(s1).add(s2);
            adjList.get(s2).add(s1);
            degree[s1]++; degree[s2]++;     // Update the in degree as well simultaneously...
        }
    }
    public static void main(String[] args) {
        TestCase test;      // Test case object defined...
        input: {    // Input block...
            Scanner sc = new Scanner(System.in);
            test = new TestCase(sc.nextLine());
            for(int i = 0; i < test.shoelaces; i++)
                test.createEdge(sc.nextLine());
            sc.close();
            break input;
        }
        System.out.println(groupsRemoved(test.adjList, test.degree));   // Function call...
    }

    public static int groupsRemoved(Map<Integer, TreeSet<Integer>> graph, int degree[]) {
        int groups = 0;
        List<Integer> valency = valenceStudents(degree);    // Finding the pendent (valence) nodes...
        graphLogic: {       // Problem logic...
            while(!valency.isEmpty()) {
                groups++;
                for(int student : valency) {
                    int otherStudent = graph.get(student).isEmpty() ? -1 : graph.get(student).first();
                    if(otherStudent == -1)  continue;   // If the edge is already removed...
                    // Since undirected, remove from two nodes, both A and B respectively...
                    graph.get(student).remove(otherStudent);
                    graph.get(otherStudent).remove(student);
                    degree[student]--; degree[otherStudent]--;      // Update the in degree simultaneously...
                }
                valency = valenceStudents(degree);      // Again evaluate the pendent (valence) nodes...
            }
            break graphLogic;
        }
        return groups;
    }

    public static List<Integer> valenceStudents(int degree[]) {
        List<Integer> valency = new ArrayList<Integer>();
        for(int i = 0; i < degree.length; i++)
            if(degree[i] == 1)      // The nodes having degree of 1...
                valency.add(i);     // Added into the list of valence nodes...
        return valency;
    }
}
