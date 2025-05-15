package SegmentTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SegmentTreePoint {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public static void main(String[] args) {
        FastReader fastReader = new FastReader();
        final int n = fastReader.nextInt(), k = fastReader.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fastReader.nextInt();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < k; i++)
            queries.add(new int[]{fastReader.nextInt(), fastReader.nextInt(), fastReader.nextInt()});
        System.out.print(solve(n, nums, queries));
    }

    public static StringBuilder solve(final int n, final int nums[], List<int[]> queries) {
        SegmentTree segmentTree = new SegmentTree(0, n);
        final StringBuilder result = new StringBuilder();
        segmentTree.root = segmentTree.buildTree(0, n-1, nums);
        for(int[] query : queries) {
            if(query[0] == 1)
                segmentTree.updateQuery(segmentTree.root, query[1]-1, query[2]);
            else
                result.append(segmentTree.rangeMinQuery(segmentTree.root, query[1]-1, query[2]-1)).append("\n");
        }
        return result;
    }

    public static class SegmentTree {

        public static class Node {
            public Node left, right;
            public int rangeLeft, rangeRight;
            public int min;

            public Node(int rangeLeft, int rangeRight) {
                this.rangeLeft = rangeLeft; this.rangeRight = rangeRight;
                this.min = Integer.MAX_VALUE;
            }

            public Node(int rangeLeft, int rangeRight, int value) {
                this.rangeLeft = rangeLeft; this.rangeRight = rangeRight;
                this.min = value;
            }
        }

        public Node root;
        public int start, end;

        public SegmentTree(int start, int end) {
            this.start = start; this.end = end;
            this.root = new Node(start, end);
        }

        public Node buildTree(int rangeLeft, int rangeRight, int nums[]) {  // Imp- build tree from array using only ranges
            if(rangeLeft == rangeRight)
                return new Node(rangeLeft, rangeRight, nums[rangeLeft]);
            int mid = rangeLeft + ((rangeRight - rangeLeft) >> 1);
            Node leftChild = buildTree(rangeLeft, mid, nums);
            Node rightChild = buildTree(mid+1, rangeRight, nums);
            Node node = new Node(rangeLeft, rangeRight);
            node.left = leftChild;
            node.right = rightChild;
            node.min = Math.min(leftChild.min, rightChild.min);
            return node;
        }

        public void updateQuery(Node node, int index, int value) {      // Only for point update
            if (node.rangeRight == node.rangeLeft) {
                node.min = value;
                return;
            }
            int mid = node.rangeLeft + ((node.rangeRight - node.rangeLeft) >> 1);
            if(index <= mid)
                updateQuery(node.left, index, value);
            else
                updateQuery(node.right, index, value);
            node.min = Math.min(getMin(node.left), getMin(node.right));
            return;
        }

        public int rangeMinQuery(Node node, int queryLeft, int queryRight) {
            if(node == null || queryLeft > node.rangeRight || queryRight < node.rangeLeft)
                return Integer.MAX_VALUE;
            if(queryLeft <= node.rangeLeft && queryRight >= node.rangeRight)
                return node.min;
            int leftMin = rangeMinQuery(node.left, queryLeft, queryRight);
            int rightMin = rangeMinQuery(node.right, queryLeft, queryRight);
            return Math.min(leftMin, rightMin);
        }

        public int getMin(Node node) {
            return node == null ? Integer.MAX_VALUE : node.min;
        }
    }
}
