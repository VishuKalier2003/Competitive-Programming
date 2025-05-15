import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class VadimFillingTable {

    // FastReader for fast input.
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        String next() {
            while(st == null || !st.hasMoreTokens()){
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        int nextInt(){
            return Integer.parseInt(next());
        }
    }

    // Function F recursively computes the number at position (x,y)
    // in a table of size 2^n x 2^n with starting offset 'off'.
    // (x, y) are 1-indexed.
    static long F(int n, long off, int x, int y) {
        if(n == 1) { // Base table 2x2:
            // According to the problem:
            // (1,1) -> off+1, (2,2) -> off+2, (2,1) -> off+3, (1,2) -> off+4.
            if(x == 1 && y == 1) return off + 1;
            if(x == 2 && y == 2) return off + 2;
            if(x == 2 && y == 1) return off + 3;
            if(x == 1 && y == 2) return off + 4;
        }
        int mid = 1 << (n-1);        // mid = 2^(n-1)
        long blockSize = 1L << (2 * (n - 1)); // each quadrant has 2^(2*(n-1)) cells

        // Determine quadrant for (x,y)
        if(x <= mid && y <= mid) {
            // Q1: top-left, offset remains off.
            return F(n - 1, off, x, y);
        } else if(x > mid && y > mid) {
            // Q2: bottom-right, new coordinates: (x - mid, y - mid)
            return F(n - 1, off + blockSize, x - mid, y - mid);
        } else if(x > mid && y <= mid) {
            // Q3: bottom-left, new coordinates: (x - mid, y)
            return F(n - 1, off + 2 * blockSize, x - mid, y);
        } else { // x <= mid && y > mid, Q4: top-right, new coordinates: (x, y - mid)
            return F(n - 1, off + 3 * blockSize, x, y - mid);
        }
    }

    // Function G recursively determines the (x, y) coordinates for number d
    // in a table of size 2^n x 2^n.
    static int[] G(int n, long d) {
        if(n == 1) { // base 2x2 table
            if(d == 1) return new int[]{1, 1};
            if(d == 2) return new int[]{2, 2};
            if(d == 3) return new int[]{2, 1};
            if(d == 4) return new int[]{1, 2};
        }
        int mid = 1 << (n - 1); // size of half dimension
        long blockSize = 1L << (2 * (n - 1));
        // Determine in which quadrant the number d falls.
        if(d <= blockSize) {
            // Q1: top-left
            int[] pos = G(n - 1, d);
            return pos; // no adjustment needed.
        } else if(d <= 2 * blockSize) {
            // Q2: bottom-right: subtract blockSize; add mid to both coordinates
            int[] pos = G(n - 1, d - blockSize);
            return new int[]{pos[0] + mid, pos[1] + mid};
        } else if(d <= 3 * blockSize) {
            // Q3: bottom-left: subtract 2*blockSize; add mid to row coordinate.
            int[] pos = G(n - 1, d - 2 * blockSize);
            return new int[]{pos[0] + mid, pos[1]};
        } else {
            // Q4: top-right: subtract 3*blockSize; add mid to column coordinate.
            int[] pos = G(n - 1, d - 3 * blockSize);
            return new int[]{pos[0], pos[1] + mid};
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        while(t-- > 0) {
            int n = fast.nextInt();   // table dimension is 2^n x 2^n
            int q = fast.nextInt();   // number of queries
            // Process each query
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < q; i++) {
                String queryType = fast.next();
                if(queryType.equals("->")) {
                    int x = fast.nextInt();
                    int y = fast.nextInt();
                    // Calculate result from function F.
                    long ans = F(n, 0, x, y);
                    sb.append(ans).append("\n");
                } else if(queryType.equals("<-")) {
                    long d = Long.parseLong(fast.next());
                    int[] pos = G(n, d);
                    sb.append(pos[0]).append(" ").append(pos[1]).append("\n");
                }
            }
            System.out.print(sb.toString());
        }
    }
}
