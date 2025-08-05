import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class P32Solution {
    // Fast I/O
    public static class FastReader {
        private static final byte[] buf = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private int read() throws IOException {
            if (ptr >= len) {
                len = System.in.read(buf);
                ptr = 0;
                if (len < 0) return -1;
            }
            return buf[ptr++];
        }
        public int nextInt() throws IOException {
            int ch, x = 0;
            do { ch = read(); if (ch < 0) return -1; } while (ch <= ' ');
            boolean neg = ch=='-';
            if (neg) ch = read();
            for (; ch>='0' && ch<='9'; ch=read()) {
                x = x*10 + (ch-'0');
            }
            return neg ? -x : x;
        }
    }

    public static class FastWriter {
        private final PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        private final StringBuilder sb = new StringBuilder();
        public void println(String s) { sb.append(s).append('\n'); }
        public void printBoard(int[][] board) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    sb.append(board[i][j]).append(' ');
                }
                sb.append('\n');
            }
        }
        public void flush() { pw.write(sb.toString()); pw.flush(); }
    }

    // Knight move deltas
    private static final int[] DX = {-2,-2,-1,-1,1,1,2,2};
    private static final int[] DY = {-1,1,-2,2,-2,2,-1,1};

    private static class Cell {
        int x,y,deg;
        Cell(int x,int y){this.x=x;this.y=y;}
    }

    private static final int[][] board = new int[9][9];
    private static final boolean[][] used = new boolean[9][9];

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        int sx = fr.nextInt(), sy = fr.nextInt();
        // Mark start
        used[sx][sy] = true;
        board[sx][sy] = 1;
        // Build tour
        backtrack(sx, sy, 1);
        // Output
        fw.printBoard(board);
        fw.flush();
    }

    // Return true once full 64-length tour is built
    private static boolean backtrack(int x, int y, int move) {
        if (move == 64) return true;
        // Gather unvisited neighbors
        List<Cell> cand = new ArrayList<>();
        for (int k = 0; k < 8; k++) {
            int nx = x + DX[k], ny = y + DY[k];
            if (nx<1||nx>8||ny<1||ny>8||used[nx][ny]) continue;
            // compute onward-degree
            int d=0;
            for (int j = 0; j < 8; j++) {
                int tx=nx+DX[j], ty=ny+DY[j];
                if (tx>=1&&tx<=8&&ty>=1&&ty<=8&&!used[tx][ty]) d++;
            }
            Cell c = new Cell(nx,ny);
            c.deg = d;
            cand.add(c);
        }
        // Warnsdorff: sort ascending by deg
        cand.sort(Comparator.comparingInt(c->c.deg));
        // Try each
        for (Cell c : cand) {
            used[c.x][c.y] = true;
            board[c.x][c.y] = move+1;
            if (backtrack(c.x, c.y, move+1)) return true;
            used[c.x][c.y] = false;
        }
        return false;
    }
}
