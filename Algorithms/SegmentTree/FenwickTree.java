package SegmentTree;

import java.util.Arrays;

// Fenwick Tree (Binary Indexed Tree) implementation in Java
public class FenwickTree {
    private final int[] tree;
    private final int size;

    // Constructor to initialize the tree with a given size
    public FenwickTree(int n) {
        this.size = n;
        this.tree = new int[n + 1]; // 1-based indexing
    }

    // Update the tree at position 'index' by adding 'value'
    public void update(int index, int value) {
        while (index <= size) {
            tree[index] += value;
            index += index & -index; // Move to parent node
        }
    }

    // Compute prefix sum from index 1 to 'index'
    public int query(int index) {
        int result = 0;
        while (index > 0) {
            result += tree[index];
            index -= index & -index; // Move to previous node
        }
        return result;
    }

    // Compute the sum in the range [left, right]
    public int rangeQuery(int left, int right) {
        return query(right) - query(left - 1);
    }

    public static void main(String[] args) {
        int n = 8; // Size of the array
        FenwickTree ft = new FenwickTree(n);
        for(int i = 1; i <= n; i++)
            ft.update(i, i);
        System.out.println(Arrays.toString(ft.tree));
    }
}


