import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P2Labyrinth {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
            int c;
            while ((c = read()) <= ' ')
                if (c < 0) return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    static int n, m;
    static int[][] grid;
    static int[] Ai = new int[1], Aj = new int[1], Bi = new int[1], Bj = new int[1];

    public static void main(String[] args) {
        @SuppressWarnings("CallToPrintStackTrace")
        Thread t = new Thread(null, () -> {
            try {
                callMain();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "labyrinth", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ignored) {
        }
    }

    public static void callMain() throws IOException {
        FastReader fr = new FastReader();
        n = fr.readInt();
        m = fr.readInt();
        grid = new int[n][m];

        for (int i = 0; i < n; i++) {
            String s = fr.readString();
            for (int j = 0; j < m; j++) {
                char ch = s.charAt(j);
                if (ch == '#') {
                    grid[i][j] = 0;
                } else {
                    grid[i][j] = 1;
                    if (ch == 'A') {
                        Ai[0] = i;
                        Aj[0] = j;
                    } else if (ch == 'B') {
                        Bi[0] = i;
                        Bj[0] = j;
                    }
                }
            }
        }

        solve();
    }

    public static void solve() {
        boolean[][] visited = new boolean[n][m];
        int[][] pi = new int[n][m], pj = new int[n][m];
        char[][] pmove = new char[n][m];

        for (int[] row : pi) Arrays.fill(row, -1);
        for (int[] row : pj) Arrays.fill(row, -1);

        // Directions: 'L', 'R', 'U', 'D'
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        char[] dch = {'L', 'R', 'U', 'D'};

        int[] qi = new int[n * m];
        int[] qj = new int[n * m];
        int head = 0, tail = 0;

        int si = Ai[0], sj = Aj[0];
        visited[si][sj] = true;
        qi[tail] = si;
        qj[tail] = sj;
        tail++;

        @SuppressWarnings("unused")
        boolean found = false;
        while (head < tail) {
            int i = qi[head];
            int j = qj[head];
            head++;
            if (i == Bi[0] && j == Bj[0]) {
                found = true;
                break;
            }
            for (int d = 0; d < 4; d++) {
                int ni = i + dx[d];
                int nj = j + dy[d];
                if (ni < 0 || nj < 0 || ni >= n || nj >= m) continue;
                if (!visited[ni][nj] && grid[ni][nj] == 1) {
                    visited[ni][nj] = true;
                    pi[ni][nj] = i;
                    pj[ni][nj] = j;
                    pmove[ni][nj] = dch[d];
                    qi[tail] = ni;
                    qj[tail] = nj;
                    tail++;
                }
            }
        }

        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        if (!visited[Bi[0]][Bj[0]]) {
            out.println("NO");
            out.flush();
            return;
        }

        // Reconstruct path
        StringBuilder path = new StringBuilder();
        int ci = Bi[0], cj = Bj[0];
        while (!(ci == si && cj == sj)) {
            char c = pmove[ci][cj];
            path.append(c);
            int ti = pi[ci][cj];
            int tj = pj[ci][cj];
            ci = ti;
            cj = tj;
        }
        path.reverse();
        out.println("YES");
        out.println(path.length());
        out.println(path.toString());
        out.flush();
    }
}
