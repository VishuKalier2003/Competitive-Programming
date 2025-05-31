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
        SegmentTree segmentTree = new SegmentTree(nums);
        final StringBuilder result = new StringBuilder();
        for(int[] query : queries) {
            if(query[0] == 1)
                segmentTree.pointUpdateQuery(query[1]-1, query[2]);
            else
                result.append(segmentTree.rangeSumQuery(query[1]-1, query[2]-1)).append("\n");
        }
        return result;
    }

    public static final class SegmentTree {
        public int tree[], lazy[];
        public int N;

        public SegmentTree(int nums[]) {
            final int n = nums.length;
            this.N = 1;
            while(N < n)
                N <<= 1;
            this.tree = new int[2 * N];
            this.lazy = new int[2 * N];
            buildTree(nums);
        }

        public void buildTree(int nums[]) {
            System.arraycopy(nums, 0, this.tree, this.N, nums.length);      // Note: Start position of copying in tree is N
            for(int i = this.N - 1; i >= 1; i--)
                this.tree[i] = this.tree[i << 1]        // Info: Left child is always a power of 2
                + this.tree[i << 1 | 1];                // Info: Right child is always a power of 2 + 1 (added at 0 index)
        }

        private void push(int index, int left, int right) {
            if(this.lazy[index] != 0) {
                // We need to push the entire segment value to tree node, and since segment size is [l,r) and contains the same lazy values
                this.tree[index] += (right - left + 1) * this.lazy[index];
                if(left != right) {
                    this.lazy[index << 1] += this.lazy[index];
                    this.lazy[index << 1 | 1] += this.lazy[index];
                }
                this.lazy[index] = 0;
            }
        }

        public void pointUpdateQuery(int point, int value) {        // Note: Point updates in O(log n)
            pointUpdate(1, 0, this.N-1, point, value);
        }

        public void pointUpdate(int index, int left, int right, int point, int value) {
            push(index, left, right);       // Hack: Always lazy propagate first
            if(left == right) {
                this.tree[index] += value;
                return;
            }
            int mid = (left + right) >>> 1;
            if(point <= mid)
                pointUpdate(index << 1, left, mid, point, value);
            else
                pointUpdate(index << 1 | 1, mid+1, right, point, value);
            this.tree[index] = this.tree[index << 1] + this.tree[index << 1 | 1];
        }

        public void rangeUpdateQuery(int queryLeft, int queryRight, int value) {
            updateQuery(1, 0, this.N-1, queryLeft, queryRight, value);
        }

        public void updateQuery(int index, int left, int right, int ql, int qr, int value) {
            push(index, left, right);           // Hack: Always lazy propagate first
            if(ql > right || qr < left)
                return;
            if(ql <= left && qr >= right) {
                this.lazy[index] += value;      // Hack: Always push into lazy, when complete overlap found in range update query
                push(index, left, right);
                return;
            }
            int mid = (left + right) >>> 1;
            updateQuery(index << 1, left, mid, ql, qr, value);
            updateQuery(index << 1 | 1, mid+1, right, ql, qr, value);
            this.tree[index] = this.tree[index << 1] + this.tree[index << 1 | 1];
        }

        public int rangeSumQuery(int ql, int qr) {
            return sumQuery(1, 0, this.N-1, ql, qr);
        }

        public int sumQuery(int index, int left, int right, int ql, int qr) {
            push(index, left, right);       // Hack: Always lazy propagate first
            if(ql > right || qr < left)     // Info: No overlap
                return 0;
            if(ql <= left && qr >= right)   // Info: Complete Overlap
                return this.tree[index];
            int mid = (left + right) >>> 1;
            // Info: Partial Overlap
            int leftSum = sumQuery(index << 1, left, mid, ql, qr);
            int rightSum = sumQuery(index << 1 | 1, mid+1, right, ql, qr);
            return leftSum + rightSum;
        }
    }
}
