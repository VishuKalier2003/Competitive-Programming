// Ques - https://codeforces.com/problemset/problem/1559/D1

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class MochaDiana {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread th1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "room-allocation", 1 << 26);
        th1.start();
        try {
            th1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();     // Reading Input
        final int n = fast.nextInt(), m1 = fast.nextInt(), m2 = fast.nextInt();
        int nums1[][] = new int[m1][2], nums2[][] = new int[m2][2];
        for (int i = 0; i < m1; i++) {
            nums1[i][0] = fast.nextInt();
            nums1[i][1] = fast.nextInt();
        }
        for (int i = 0; i < m2; i++) {
            nums2[i][0] = fast.nextInt();
            nums2[i][1] = fast.nextInt();
        }
        solve(n, nums1, nums2);
    }

    private static Set<Integer> hashes;
    private static final int C1 = 1_000, C2 = 1;

    public static void solve(final int n, final int edges1[][], final int edges2[][]) {
        hashes = new HashSet<>();           // Creating hash set to store the edges
        DSU dsu1 = new DSU(n), dsu2 = new DSU(n);
        // Hack: We can create a unique hash for each node as range that is n, n*e1 + 1*e2 and vice versa, since the range will make sure that each edge hash value is different
        for (int e1[] : edges1) {
            hashes.add((C1 * e1[0]) + (C2 * e1[1]));
            hashes.add((C1 * e1[1]) + (C2 * e1[0]));
            dsu1.union(e1[0], e1[1]);       // Union them
        }
        for (int e2[] : edges2) {
            hashes.add((C1 * e2[0]) + (C2 * e2[1]));
            hashes.add((C1 * e2[1]) + (C2 * e2[0]));
            dsu2.union(e2[0], e2[1]);       // Union them
        }
        int count = 0;
        final StringBuilder output = new StringBuilder();
        for (int i = 1; i <= n; i++)
            for (int j = i + 1; j <= n; j++) {
                int hash1 = (C1 * i) + (C2 * j), hash2 = (C1 * j) + (C2 * i);
                // Info: Check if the edge does not exist in any of the two graphs and the connection is not of the vertices in the same component
                if (!hashes.contains(hash1) && !hashes.contains(hash2) && !dsu1.sameComponent(i, j) && !dsu2.sameComponent(i, j)) {
                    // Note: Connecting two nodes under the same component creates a cycle (hence will not remain a tree)
                    output.append(i).append(" ").append(j).append("\n");
                    dsu1.union(i, j); dsu2.union(i, j); count++;
                }
            }
        final StringBuilder result = new StringBuilder().append(count).append("\n").append(output);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(result.toString());
        writer.flush();
    }

    public static class DSU {       // Hack: Disjoint Set Union (DSU) of Krushkal's Algorithm
        private final int n;
        private final int[] parent, rank;

        public DSU(int size) {
            this.n = size + 1;
            this.parent = new int[n];
            this.rank = new int[n];
            for (int i = 0; i < n; i++)
                this.parent[i] = i;
        }

        public int find(int x) {        // Finding the root node of the component
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {       // Union the two vertices under same component
            int rX = find(x), rY = find(y);
            if (rX != rY) {     // Info: Path compression where the smaller depth component is merged into the larger depth component
                if (rank[rX] < rank[rY])
                    parent[rX] = parent[rY];
                else if (rank[rX] > rank[rY])
                    parent[rY] = parent[rX];
                else {
                    parent[rY] = parent[rX];
                    rank[rY]++;
                }
            }
        }

        public boolean sameComponent(int x, int y) {        // Checking if the two nodes are in the same component
            return find(x) == find(y);
        }
    }
}
