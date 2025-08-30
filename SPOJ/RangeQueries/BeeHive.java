import java.io.*;
import java.util.*;

public class BeeHive {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        public FastReader() { br = new BufferedReader(new InputStreamReader(System.in)); }
        String next() throws IOException {
            while (st == null || !st.hasMoreElements())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }
        int nextInt() throws IOException { return Integer.parseInt(next()); }
    }

    static class Fenwick2D {
        int[][] tree;
        int n, m;
        Fenwick2D(int n, int m) {
            this.n = n;
            this.m = m;
            tree = new int[n+1][m+1];
        }
        void update(int x, int y, int delta) {
            for (int i = x; i <= n; i += i & -i)
                for (int j = y; j <= m; j += j & -j)
                    tree[i][j] += delta;
        }
        int query(int x, int y) {
            int sum = 0;
            for (int i = x; i > 0; i -= i & -i)
                for (int j = y; j > 0; j -= j & -j)
                    sum += tree[i][j];
            return sum;
        }
        int queryRect(int x1, int y1, int x2, int y2) {
            return query(x2, y2) - query(x1-1, y2) - query(x2, y1-1) + query(x1-1, y1-1);
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        int m = fr.nextInt();
        int q = fr.nextInt();
        int[][] mat = new int[n+1][m+1];
        Fenwick2D fenwick = new Fenwick2D(n, m);

        // Read initial matrix and build Fenwick tree
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++) {
                mat[i][j] = fr.nextInt();
                fenwick.update(i, j, mat[i][j]);
            }

        // Process queries
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < q; t++) {
            int type = fr.nextInt();
            if (type == 1) {
                int i = fr.nextInt();
                int j = fr.nextInt();
                int val = fr.nextInt();
                int delta = val - mat[i][j];
                mat[i][j] = val;
                fenwick.update(i, j, delta);
            } else {
                int i1 = fr.nextInt();
                int j1 = fr.nextInt();
                int i2 = fr.nextInt();
                int j2 = fr.nextInt();
                sb.append(fenwick.queryRect(i1, j1, i2, j2)).append("\n");
            }
        }
        System.out.print(sb);
    }
}
