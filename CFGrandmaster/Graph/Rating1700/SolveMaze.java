import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;

public class SolveMaze {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "SolveMazeThread", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        int t = fr.nextInt();
        while (t-- > 0) {
            final int n = fr.nextInt(), m = fr.nextInt();
            char[][] grid = new char[n][m];
            for (int i = 0; i < n; i++) {
                String s = fr.next();
                for (int j = 0; j < m; j++) {
                    grid[i][j] = s.charAt(j);
                }
            }
            fw.attachOutput(solve(n, m, grid));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int m, char[][] grid) {
        final StringBuilder output = new StringBuilder();

        // Directions for 4-neighbour traversal
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // Step 1: Block around bad people
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'B') {
                    for (int d = 0; d < 4; d++) {
                        int ni = i + dx[d];
                        int nj = j + dy[d];
                        if (ni < 0 || nj < 0 || ni >= n || nj >= m) continue;
                        if (grid[ni][nj] == 'G') {
                            // A good person is adjacent to bad person, impossible
                            output.append("No\n");
                            return output;
                        }
                        if (grid[ni][nj] == '.') {
                            grid[ni][nj] = '#'; // block empty cell
                        }
                    }
                }
            }
        }

        // Step 2: BFS from exit cell (n-1, m-1)
        boolean[][] vis = new boolean[n][m];
        ArrayDeque<int[]> q = new ArrayDeque<>();
        if (grid[n-1][m-1] != '#') {
            q.add(new int[]{n-1, m-1});
            vis[n-1][m-1] = true;
        }
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];
            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];
                if (nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                if (!vis[nx][ny] && grid[nx][ny] != '#') {
                    vis[nx][ny] = true;
                    q.add(new int[]{nx, ny});
                }
            }
        }

        // Step 3: Verify conditions
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'G' && !vis[i][j]) {
                    output.append("No\n");
                    return output;
                }
                if (grid[i][j] == 'B' && vis[i][j]) {
                    output.append("No\n");
                    return output;
                }
            }
        }

        output.append("Yes\n");
        return output;
    }
}
