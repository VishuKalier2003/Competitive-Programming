import java.io.*;
import java.util.*;

public class QuesC {
    // Direction vectors: East, South, West, North
    static final int[] DR = { 0, 1,  0, -1 };
    static final int[] DC = { 1, 0, -1,  0 };

    public static void main(String[] args) throws IOException {
        BufferedReader  in  = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter  out = new BufferedWriter(new OutputStreamWriter(System.out));

        int t = Integer.parseInt(in.readLine().trim());
        while (t-- > 0) {
            int n = Integer.parseInt(in.readLine().trim());
            int[][] A = new int[n][n];
            boolean[][] seen = new boolean[n][n];
            Deque<int[]> q = new ArrayDeque<>();

            // 1) Seed BFS with the center cell(s), in clockwise order:
            if (n % 2 == 1) {
                int c = n/2;
                q.addLast(new int[]{c, c});
                seen[c][c] = true;
            } else {
                // even n: central 2×2 block -- enqueue E→S→W→N
                int r0 = n/2 - 1, c0 = n/2;
                q.addLast(new int[]{r0,   c0  }); seen[r0][c0]   = true;  // East
                q.addLast(new int[]{r0+1, c0  }); seen[r0+1][c0] = true;  // South
                q.addLast(new int[]{r0+1, c0-1}); seen[r0+1][c0-1] = true; // West
                q.addLast(new int[]{r0,   c0-1}); seen[r0][c0-1]   = true; // North
            }

            // 2) BFS: visit in increasing manhattan‐distance, clockwise within each ring
            int cnt = 0;
            while (!q.isEmpty()) {
                int[] cell = q.pollFirst();
                int r = cell[0], c = cell[1];
                A[r][c] = cnt++;

                // enqueue neighbors in E, S, W, N order
                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < n && nc >= 0 && nc < n && !seen[nr][nc]) {
                        seen[nr][nc] = true;
                        q.addLast(new int[]{nr, nc});
                    }
                }
            }

            // 3) Output the filled grid
            for (int i = 0; i < n; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < n; j++) {
                    row.append(A[i][j]).append(' ');
                }
                out.write(row.toString().trim());
                out.newLine();
            }
        }
        out.flush();
    }
}
