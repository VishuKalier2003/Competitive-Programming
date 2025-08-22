import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class LakesInBerland {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nexLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException { // reading string (whitespace exclusive)
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
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

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "https://codeforces.com/problemset/problem/723/D",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), m = fr.nextInt(), k = fr.nextInt();
        int map[][] = new int[n][m];
        for(int i = 0; i < n; i++) {
            String s = fr.next();
            for(int j = 0; j < m; j++)
                map[i][j] = s.charAt(j) == '*' ? 1 : 0;
        }
        fw.attachOutput(solve(n, m, k, map));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int m, final int k, final int map[][]) {
        vis = new boolean[n][m];
        // Check streams from the borders (since they are not lakes)
        for(int i = 0; i < n; i++) {    // Check left and right margins
            if(map[i][0] == 0 && !vis[i][0])
                dfs(i, 0, n, m, map);
            if(map[i][m-1] == 0 && !vis[i][m-1])
                dfs(i, m-1, n, m, map);
        }
        for(int j = 0; j < m; j++) {    // Check top and bottom margins
            if(map[0][j] == 0 && !vis[0][j])
                dfs(0, j, n, m, map);
            if(map[n-1][j] == 0 && !vis[n-1][j])
                dfs(n-1, j, n, m, map);
        }
        PriorityQueue<int[]> lakes = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        for(int i = 1; i < n-1; i++)
            for(int j = 1; j < m-1; j++) {
                if(map[i][j] == 0 && !vis[i][j]) {
                    lakeSize = 0;
                    dfs(i, j, n, m, map);
                    lakes.offer(new int[]{i, j, lakeSize});
                }
            }
        int count = 0;
        while(lakes.size() != k) {
            int lake[] = lakes.poll();
            count += lake[2];
            fillDfs(lake[0], lake[1], n, m, map);
        }
        final StringBuilder output = new StringBuilder().append(count).append("\n");
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++)
                output.append((map[i][j] == 0 ? "." : "*"));
            if(i < n-1)
                output.append("\n");
        }
        return output;
    }

    private static boolean vis[][];
    private static int lakeSize;

    public static void dfs(int i, int j, final int n, final int m, final int map[][]) {
        if(i < 0 || i >= n || j < 0 || j >= m || vis[i][j] || map[i][j] == 1)
            return;
        vis[i][j] = true;
        lakeSize++;
        dfs(i+1, j, n, m, map);
        dfs(i-1, j, n, m, map);
        dfs(i, j-1, n, m, map);
        dfs(i, j+1, n, m, map);
    }

    public static void fillDfs(int i, int j, final int n, final int m, final int map[][]) {
        if(i < 0 || i >= n || j < 0 || j >= m || map[i][j] == 1)
            return;
        map[i][j] = 1;
        fillDfs(i+1, j, n, m, map);
        fillDfs(i-1, j, n, m, map);
        fillDfs(i, j+1, n, m, map);
        fillDfs(i, j-1, n, m, map);
    }
}
