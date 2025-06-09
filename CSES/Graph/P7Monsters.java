import java.io.IOException;
import java.util.Arrays;

public class P7Monsters {
    // FastReader remains unchanged
    static class FastReader {
        private static final byte[] buf = new byte[1 << 20];
        private int ptr, len;
        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buf);
                if (len <= 0) return -1;
            }
            return buf[ptr++] & 0xff;
        }
        int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = (c == '-');
            if (neg) c = read();
            do { x = x * 10 + (c - '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
        String next() throws IOException {
            int c;
            while ((c = read()) <= ' ') if (c < 0) return null;
            StringBuilder sb = new StringBuilder();
            do { sb.append((char)c); }
            while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    // Directions
    static final int[] DX = {-1,1,0,0}, DY = {0,0,-1,1};
    static final char[] DC = {'U','D','L','R'};

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        int n = fr.nextInt(), m = fr.nextInt();
        char[][] G = new char[n][m];

        // Read grid, track monster sources & player pos
        int startX=0, startY=0, monsterCount=0;
        for (int i=0;i<n;i++) {
            String line = fr.next();
            for (int j=0;j<m;j++) {
                G[i][j] = line.charAt(j);
                if (G[i][j]=='M') monsterCount++;
                else if (G[i][j]=='A') { startX=i; startY=j; }
            }
        }

        // Preallocate BFS queues of size n*m
        int maxQ = n*m;
        int[] qx = new int[maxQ], qy = new int[maxQ], qt = new int[maxQ];
        boolean[][] seen = new boolean[n][m];
        int[][] monsterTime = new int[n][m];
        for (int[] row: monsterTime) Arrays.fill(row, Integer.MAX_VALUE);

        // Phase 1: Multi-source BFS from monsters
        int head=0, tail=0;
        for (int i=0, c=0;i<n;i++){
            for (int j=0;j<m;j++){
                if (G[i][j]=='M') {
                    qx[tail]=i; qy[tail]=j; qt[tail++]=0;
                    seen[i][j]=true;
                }
            }
        }
        while (head<tail) {
            int x=qx[head], y=qy[head], t=qt[head++];
            monsterTime[x][y] = t;
            for (int d=0;d<4;d++){
                int nx=x+DX[d], ny=y+DY[d];
                if (nx>=0 && ny>=0 && nx<n && ny<m
                 && !seen[nx][ny] && G[nx][ny]!='#') {
                    seen[nx][ny]=true;
                    qx[tail]=nx; qy[tail]=ny; qt[tail++]=t+1;
                }
            }
        }

        // Phase 2: Single-source BFS from A
        for (boolean[] row: seen) Arrays.fill(row,false);
        head=tail=0;
        qx[tail]=startX; qy[tail]=startY; qt[tail++]=0;
        seen[startX][startY]=true;

        // Parent pointers: encode prev coord + incoming direction
        // Use packed ints: high 16 bits for x, low 16 bits for y, separate dir[].
        int[][] parent = new int[n][m];
        char[][] dirTaken = new char[n][m];
        boolean escaped=false;
        int ex= -1, ey= -1;

        while (head<tail) {
            int x=qx[head], y=qy[head], t=qt[head++];
            // Check border
            if (x==0||y==0||x==n-1||y==m-1) {
                escaped=true; ex=x; ey=y; break;
            }
            for (int d=0;d<4;d++){
                int nx=x+DX[d], ny=y+DY[d], nt=t+1;
                if (nx>=0&&ny>=0&&nx<n&&ny<m
                 && !seen[nx][ny]
                 && G[nx][ny]!='#'
                 && nt<monsterTime[nx][ny]) {

                    seen[nx][ny]=true;
                    parent[nx][ny] = (x<<16)|y;
                    dirTaken[nx][ny] = DC[d];
                    qx[tail]=nx; qy[tail]=ny; qt[tail++]=nt;
                }
            }
        }

        if (!escaped) {
            System.out.println("NO");
            return;
        }

        // Reconstruct path into fixed buffer
        char[] path = new char[n*m];
        int pathLen=0, cx=ex, cy=ey;
        while (!(cx==startX && cy==startY)) {
            path[pathLen++] = dirTaken[cx][cy];
            int p = parent[cx][cy];
            cx = p>>>16; cy = p & 0xFFFF;
        }
        // Reverse in-place
        for (int i=0, j=pathLen-1; i<j; i++, j--) {
            char tmp = path[i]; path[i]=path[j]; path[j]=tmp;
        }

        // Output
        System.out.println("YES");
        System.out.println(pathLen);
        // Convert char[] to byte[] for output
        byte[] outBytes = new byte[pathLen];
        for (int i = 0; i < pathLen; i++) outBytes[i] = (byte) path[i];
        System.out.write(outBytes, 0, pathLen);
        System.out.println();
    }
}
