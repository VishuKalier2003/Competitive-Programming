import java.io.*;
import java.util.*;

public class MaximiseComponent {
    static class Data {
        int rMin, rMax, cMin, cMax, sum;
        Data(int r, int c) {
            rMin = rMax = r;
            cMin = cMax = c;
            sum = 0;
        }
    }

    static void dfsIterative(int si, int sj, int n, int m, int[][] grid, boolean[][] vis, Data data) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{si, sj});
        vis[si][sj] = true;

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int i = cell[0], j = cell[1];

            data.rMin = Math.min(i, data.rMin);
            data.rMax = Math.max(i, data.rMax);
            data.cMin = Math.min(j, data.cMin);
            data.cMax = Math.max(j, data.cMax);
            data.sum++;

            if (i+1 < n && grid[i+1][j]==1 && !vis[i+1][j]) { vis[i+1][j]=true; stack.push(new int[]{i+1,j}); }
            if (i-1 >=0 && grid[i-1][j]==1 && !vis[i-1][j]) { vis[i-1][j]=true; stack.push(new int[]{i-1,j}); }
            if (j+1 < m && grid[i][j+1]==1 && !vis[i][j+1]) { vis[i][j+1]=true; stack.push(new int[]{i,j+1}); }
            if (j-1 >=0 && grid[i][j-1]==1 && !vis[i][j-1]) { vis[i][j-1]=true; stack.push(new int[]{i,j-1}); }
        }
    }

    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner();
        int t = fs.nextInt();
        while (t-- > 0) {
            int n = fs.nextInt(), m = fs.nextInt();
            int[][] grid = new int[n][m];
            boolean[][] vis = new boolean[n][m];
            int[] rDots = new int[n];
            int[] cDots = new int[m];
            for (int i = 0; i < n; i++) {
                String s = fs.next();
                for (int j = 0; j < m; j++) {
                    if (s.charAt(j) == '.') {
                        rDots[i]++;
                        cDots[j]++;
                        grid[i][j] = 0;
                    } else {
                        grid[i][j] = 1;
                    }
                }
            }

            ArrayList<Data> components = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (grid[i][j]==1 && !vis[i][j]) {
                        Data d = new Data(i, j);
                        dfsIterative(i, j, n, m, grid, vis, d);
                        components.add(d);
                    }
                }
            }

            // Build row contribution array using difference method
            int[] R = new int[n+1];
            for (Data comp : components) {
                int from = Math.max(0, comp.rMin - 1);
                int to   = Math.min(n-1, comp.rMax + 1);
                R[from] += comp.sum;
                if (to + 1 < n) R[to+1] -= comp.sum;
            }
            for (int i = 1; i < n; i++) R[i] += R[i-1];

            int ans = 0;
            for (int i = 0; i < n; i++) {
                ans = Math.max(ans, rDots[i] + R[i]);
            }

            // Build column contribution array using difference method
            int[] C = new int[m+1];
            for (Data comp : components) {
                int from = Math.max(0, comp.cMin - 1);
                int to   = Math.min(m-1, comp.cMax + 1);
                C[from] += comp.sum;
                if (to + 1 < m) C[to+1] -= comp.sum;
            }
            for (int j = 1; j < m; j++) C[j] += C[j-1];

            for (int j = 0; j < m; j++) {
                ans = Math.max(ans, cDots[j] + C[j]);
            }

            System.out.println(ans);
        }
    }

    // FastScanner for performance
    static class FastScanner {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        String next() throws IOException {
            while (st==null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }
        int nextInt() throws IOException { return Integer.parseInt(next()); }
    }
}
