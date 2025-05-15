/*
 * Ques - White Black Balanced Subtrees - https://codeforces.com/problemset/problem/1676/G
 * Tags - Graph, Map, Tree, DP, Math
 * Rating - 1300
 */

import java.util.*;

public class WhiteBlack {
    static int equalNodes;  // Variable to count the equal subtree nodes...
    public record WhiteBlackTree(Map<Integer, int[]> tree) {}   // Class created with final variables...
    public static void main(String[] args) {
        int t;
        WhiteBlackTree whiteBlackTrees[];
        input: {
            Scanner sc = new Scanner(System.in);
            t = sc.nextInt();
            sc.nextLine();
            whiteBlackTrees = new WhiteBlackTree[t];    // Test case array...
            for(int i = 0; i < t; i++) {
                Map<Integer, int[]> adjList = initializeTree(sc.nextInt()); // Creating the adjacency list...
                sc.nextLine();
                adjList = fillTreeNodes(adjList, sc.nextLine());    // Filing the nodes in the tree...
                whiteBlackTrees[i] = new WhiteBlackTree(fillNodeColors(adjList, sc.next()));    // Filling node colors...
            }
            sc.close();
            break input;
        }
        output: {
            for(WhiteBlackTree whiteBlackTree : whiteBlackTrees) {
                equalNodes = 0; // Resetting node count to zero (since static variable)...
                // Function call to update the equalNodes variable...
                findColoredSubtrees(whiteBlackTree.tree, 1, new int[whiteBlackTree.tree.size()+1]);
                System.out.println(equalNodes);     // Printing the result...
            }
            break output;
        }
    }

    public static Map<Integer, int[]> initializeTree(int n) {
        Map<Integer, int[]> map = new HashMap<>();
        for(int i = 1; i <= n; i++)
            map.put(i, new int[3]);     // Array of three values - left child, right child, color...
        return map;
    }

    public static Map<Integer, int[]> fillTreeNodes(Map<Integer, int[]> map, String s) {
        String str[] = s.split(" ");
        int n = str.length, node = 2;   // Start index of node...
        for(int i = 0; i < n; i++, node++) {
            int Parentnode = Integer.parseInt(str[i]);  // Extracting the parent node...
            if(map.get(Parentnode)[0] != 0)     // If left child already filled...
                map.get(Parentnode)[1] = node;      // Fill right child...
            else    map.get(Parentnode)[0] = node;  // Otherwise fill left child...
        }
        return map;
    }

    public static Map<Integer, int[]> fillNodeColors(Map<Integer, int[]> map, String colors) {
        int n = colors.length();
        for(int i = 1; i <= n; i++)     // Node color 1 for Black and -1 for White...
            map.get(i)[2] = colors.charAt(i-1) == 'B' ? 1 : -1;
        return map;
    }

    // Dp[i] stores the sum of white and black nodes till the current node, if equal the sum will be zero...
    public static int findColoredSubtrees(Map<Integer, int[]> tree, int parent, int dp[]) {
        int parentColor = tree.get(parent)[2];      // Extracting the current node color...
        if(tree.get(parent)[0] == 0 && tree.get(parent)[1] == 0) {
            dp[parent] = parentColor;       // When the leaf node is found...
            return dp[parent];  // Update the dp and backtrack...
        }
        int leftChild = 0, rightChild = 0;
        if(tree.get(parent)[0] != 0)        // If left child exists...
            leftChild = findColoredSubtrees(tree, tree.get(parent)[0], dp);
        if(tree.get(parent)[1] != 0)        // If right child exists...
            rightChild = findColoredSubtrees(tree, tree.get(parent)[1], dp);
        // Update the parent dp as the sum of dp of left and right child with the parent...
        dp[parent] = parentColor + leftChild + rightChild;
        // When entire traversal is full completed...
        if(parent == 1) {
            for(int i = 1; i < dp.length; i++)
                if(dp[i] == 0)      // Count the nodes with the sum of black and white nodes as zero...
                    equalNodes++;
        }
        return dp[parent];      // Return the sum of nodes color of parent...
    }

}
