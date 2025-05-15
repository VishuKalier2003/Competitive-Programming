package SegmentTree;

import java.io.*;
import java.util.*;

public class IterativeSegmnetTree {
    public static class FastReader {
        BufferedReader buffer;
        StringTokenizer tokenizer;

        // FastReader for efficient input reading
        public FastReader() {
            buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        // Read the next token (String) from the input
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        // Read the next integer from the input
        public int nextInt() { return Integer.parseInt(next()); }

        // Read the next long integer from the input
        public long nextLong() { return Long.parseLong(next()); }
    }

    public static class FastWriter {
        BufferedWriter writer;

        // FastWriter for efficient output writing
        public FastWriter() {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        }

        // Write a string to output
        public void write(String s) throws IOException {
            writer.write(s);
        }

        // Close the writer (flushes the content)
        public void close() throws IOException {
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        int n = fast.nextInt(); // Read the size of the array
        int q = fast.nextInt(); // Read the number of queries

        long[] nums = new long[n];
        for (int i = 0; i < n; i++) nums[i] = fast.nextLong(); // Read the initial array elements

        // Store all queries
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt(), fast.nextInt()});

        solve(n, q, nums, queries);
    }

    // Function to solve the queries (range max update & range max query)
    public static void solve(int n, int q, long[] nums, List<int[]> queries) throws IOException {
        FastWriter writer = new FastWriter();
        SegmentTree tree = new SegmentTree(nums); // Initialize the segment tree

        // Process each query
        for (int[] query : queries) {
            if (query[0] == 1)
                tree.rangeUpdate(query[1], query[2], query[2]); // Range update: set value in range [l, r]
            else
                writer.write(tree.rangeQuery(query[1], query[2]) + "\n"); // Range max query: find max in range [l, r]
        }

        writer.close(); // Close the output writer
    }

    // Segment Tree class for range maximum queries and updates
    public static class SegmentTree {
        int size;            // Size of the tree (next power of 2 >= n)
        long[] tree;         // Segment tree for storing range max values
        long[] lazy;         // Lazy array for pending range updates

        // Constructor to initialize the segment tree with input array
        public SegmentTree(long[] arr) {
            int n = arr.length;
            size = 1;
            // Compute the next power of two greater than or equal to n
            while (size < n) size <<= 1;

            tree = new long[2 * size];  // Tree array size (2 * size of complete binary tree)
            lazy = new long[2 * size];  // Lazy array for deferred updates

            // Initialize the leaves of the segment tree with input values
            for (int i = 0; i < n; i++)
                tree[size + i] = arr[i];

            // Build the tree from the leaves by calculating maximum values bottom-up
            for (int i = size - 1; i > 0; i--)
                tree[i] = Math.max(tree[2 * i], tree[2 * i + 1]);
        }

        // Function to propagate lazy updates to the children
        private void push(int index, int l, int r) {
            if (lazy[index] != 0) {
                // Apply the lazy update to the current node (max value)
                tree[index] = Math.max(tree[index], lazy[index]);

                if (l != r) {
                    // Propagate the lazy value down to the children if not a leaf node
                    lazy[2 * index] = Math.max(lazy[2 * index], lazy[index]);
                    lazy[2 * index + 1] = Math.max(lazy[2 * index + 1], lazy[index]);
                }
                lazy[index] = 0; // Reset lazy value after propagating
            }
        }

        // Iterative range update (set the value to `val` in range [l, r])
        public void rangeUpdate(int l, int r, long val) {
            int L = l + size - 1;
            int R = r + size - 1;
            // Start the update process from the leaf nodes
            while (L <= R) {
                if ((L & 1) == 1) { // If L is a right child
                    tree[L] = Math.max(tree[L], val); // Apply update to the node
                    if (L < size) lazy[L] = Math.max(lazy[L], val); // Propagate lazily to parent
                    L++;
                }
                if ((R & 1) == 0) { // If R is a left child
                    tree[R] = Math.max(tree[R], val); // Apply update to the node
                    if (R < size) lazy[R] = Math.max(lazy[R], val); // Propagate lazily to parent
                    R--;
                }
                L >>= 1; // Move up to the parent node
                R >>= 1; // Move up to the parent node
            }
            // Rebuild the tree for the affected range after the update
            rebuild(L);
            rebuild(R);
        }

        // Rebuild the nodes after an update
        private void rebuild(int index) {
            while (index > 1) {
                index >>= 1;
                // Recalculate the max value for the current node based on its children
                tree[index] = Math.max(tree[2 * index], tree[2 * index + 1]);
            }
        }

        // Iterative range query (find the max value in the range [l, r])
        public long rangeQuery(int l, int r) {
            l += size - 1;
            r += size - 1;
            long maxVal = Long.MIN_VALUE; // Initialize the max value
            // Traverse the tree from the leaves upwards
            while (l <= r) {
                // Push down any pending updates
                push(l, 0, 0);
                push(r, 0, 0);
                if ((l & 1) == 1) { // If L is a right child
                    maxVal = Math.max(maxVal, tree[l]); // Update maxVal
                    l++; // Move to the next node
                }
                if ((r & 1) == 0) { // If R is a left child
                    maxVal = Math.max(maxVal, tree[r]); // Update maxVal
                    r--; // Move to the next node
                }
                l >>= 1; // Move up to the parent node
                r >>= 1; // Move up to the parent node
            }
            return maxVal;
        }
    }
}
