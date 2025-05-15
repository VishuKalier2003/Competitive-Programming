import java.io.*;
import java.util.*;

public class RookDefense {
    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        String next() {
            while (st==null||!st.hasMoreTokens()) {
                try { st=new StringTokenizer(br.readLine()); }
                catch(IOException e){ throw new RuntimeException(e); }
            }
            return st.nextToken();
        }
        int nextInt(){ return Integer.parseInt(next()); }
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt(), q = in.nextInt();
        FenwickTree bitRow = new FenwickTree(n);
        FenwickTree bitCol = new FenwickTree(n);
        int[] rowCount = new int[n], colCount = new int[n];

        for (int i = 0; i < q; i++) {
            int t = in.nextInt();
            if (t == 1) {
                int x = in.nextInt()-1, y = in.nextInt()-1;
                if (++rowCount[x] == 1) bitRow.pointUpdate(x+1, +1);
                if (++colCount[y] == 1) bitCol.pointUpdate(y+1, +1);
            }
            else if (t == 2) {
                int x = in.nextInt()-1, y = in.nextInt()-1;
                if (--rowCount[x] == 0) bitRow.pointUpdate(x+1, -1);
                if (--colCount[y] == 0) bitCol.pointUpdate(y+1, -1);
            }
            else {
                int x1 = in.nextInt()-1, y1 = in.nextInt()-1;
                int x2 = in.nextInt()-1, y2 = in.nextInt()-1;
                int rows = bitRow.rangeSum(x1+1, x2+1);
                int cols = bitCol.rangeSum(y1+1, y2+1);
                boolean full = (rows == x2 - x1 + 1)
                            || (cols == y2 - y1 + 1);
                out.println(full ? "Yes" : "No");
            }
        }
        out.flush();
    }

    // FenwickTree as above
    static class FenwickTree {
        private final int n;
        private final int[] tree;
        FenwickTree(int size) {
            this.n = size;
            this.tree = new int[n+1];
        }
        private static int lowbit(int x) { return x & -x; }
        void pointUpdate(int pos, int delta) {
            for (int i = pos; i <= n; i += lowbit(i))
                tree[i] += delta;
        }
        int prefixSum(int pos) {
            int s = 0;
            for (int i = pos; i > 0; i -= lowbit(i))
                s += tree[i];
            return s;
        }
        int rangeSum(int l, int r) {
            return prefixSum(r) - prefixSum(l-1);
        }
    }
}
