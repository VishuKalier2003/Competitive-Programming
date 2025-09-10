import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Queue;

public class IgorMuseum {
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

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ') if (c < 0) return null;
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
        public void attachOutput(StringBuilder s) { sb.append(s); }
        public void printOutput() { pw.write(sb.toString()); pw.flush(); }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Igor-in-the-Museum-(https://codeforces.com/problemset/problem/598/D)", 1 << 26);
        t.start();
        try { t.join(); } catch (InterruptedException iE) { iE.getLocalizedMessage(); }
    }

    private static final int[][] dir = {{1,0},{-1,0},{0,1},{0,-1}};
    private static final StringBuilder output = new StringBuilder();

    public static void callMain(String[] args) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        int n = fr.nextInt(), m = fr.nextInt(), q = fr.nextInt();
        char[][] grid = new char[n+1][m+1];
        for (int i = 1; i <= n; i++) {
            String s = fr.next();
            for (int j = 1; j <= m; j++) {
                grid[i][j] = s.charAt(j-1);
            }
        }

        int[][] compId = new int[n+1][m+1];
        int[] compPaintings = new int[n*m + 5];
        int compCounter = 0;

        // BFS for each component
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (grid[i][j] == '.' && compId[i][j] == 0) {
                    compCounter++;
                    int paintings = bfs(i, j, compCounter, compId, grid, n, m);
                    compPaintings[compCounter] = paintings;
                }
            }
        }

        // Answer queries
        for (int i = 0; i < q; i++) {
            int x = fr.nextInt(), y = fr.nextInt();
            output.append(compPaintings[compId[x][y]]).append("\n");
        }

        fw.attachOutput(output);
        fw.printOutput();
    }

    private static int bfs(int si, int sj, int compNum, int[][] compId, char[][] grid, int n, int m) {
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{si, sj});
        compId[si][sj] = compNum;
        int paintings = 0;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int i = cur[0], j = cur[1];

            for (int[] d : dir) {
                int x = i + d[0], y = j + d[1];
                if (x < 1 || y < 1 || x > n || y > m) continue;
                if (grid[x][y] == '*') {
                    paintings++; // wall picture
                } else if (compId[x][y] == 0) {
                    compId[x][y] = compNum;
                    q.add(new int[]{x, y});
                }
            }
        }
        return paintings;
    }
}
