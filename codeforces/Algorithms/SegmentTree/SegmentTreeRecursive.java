package SegmentTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SegmentTreeRecursive {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static class Node {
        Node left, right;
        int leftRange, rightRange;
        int count;

        public Node(int leftRange, int rightRange) {this.leftRange = leftRange; this.rightRange = rightRange; this.count = 0;}

        public void updateCount() {this.count++;}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int q = fast.nextInt();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(q, queries);
    }

    public static Node root;

    public static void solve(final int q, List<int[]> queries) {
        root = new Node(1, 8);
        for(int[] query : queries)
            update(root, 1, 8, query[0], query[1]);
    }

    public static Node update(Node root, int rangeLeft, int rangeRight, int queryLeft, int queryRight) {
        if(root == null)        // Creating a new node
            return new Node(rangeLeft, rangeRight);
        if(queryRight < rangeLeft || queryLeft > rangeRight)        // Non overlaps
            return root;
        if(queryLeft >= rangeLeft && queryRight <= rangeRight) {
            // Imp- Should be used when we want to perform lazy propagation (when complete overlap is found, we keep the update value stored, and only transfer it to subtrees when needed)
            root.updateCount();     // Update here and then update the lazy property as well
            return root;
        }
        int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
        root.left = update(root.left, rangeLeft, mid, queryLeft, queryRight);
        root.right = update(root.right, mid+1, rangeRight, queryLeft, queryRight);
        // Imp- when not using lazy propagation we need to update all data for all the nodes here in post order fashion
        return root;
    }

    public static int rangeMaxQuery(Node root, int rangeLeft, int rangeRight, int queryLeft, int queryRight) {
        if(root == null || queryLeft > rangeRight || queryRight < rangeLeft)        // Mark as non-overlaps
            return 0;
        if(queryLeft <= rangeRight && queryRight >= rangeLeft)      // When full overlap we just return the value of the current segment
            return root.count;
        int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
        return Math.max(rangeMaxQuery(root.left, rangeLeft, mid, queryLeft, queryRight), rangeMaxQuery(root.right, rangeLeft, mid+1, queryLeft, queryRight));
    }
}
