import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P9ShortestPathII {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single
        // System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
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

        public int readInt() throws IOException {
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

        public long readLong() throws IOException {
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

        public String readString() throws IOException {
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

        public String readLine() throws IOException {
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

    public static void main(String[] args) throws IOException {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "floyd-warshall-bidirectional-roads",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static long dist[][];
    public static List<int[]> edges, queries;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final int n = fr.readInt(), m = fr.readInt(), q = fr.readInt();
        edges = new ArrayList<>();
        queries = new ArrayList<>();
        for(int i = 0; i < m; i++)
            edges.add(new int[]{fr.readInt(), fr.readInt(), fr.readInt()});
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fr.readInt(), fr.readInt()});
        solve(n, m);
    }

    public static final long INF = (long)2e12;

    public static void solve(final int n, final int m) {
        dist = new long[n+1][n+1];
        for(long r[] : dist)
            Arrays.fill(r, INF);     // value more than weight but lesser than MAX
        for(int i = 1; i <= n; i++)
            dist[i][i] = 0;
        for(int edge[] : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            dist[u][v] = Math.min(dist[u][v], w);     // bidirectional
            dist[v][u] = Math.min(dist[v][u], w);
        }
        for(int k = 1; k <= n; k++) {
            for(int i = 1; i <= n; i++) {
                if(dist[i][k] == INF)
                    continue;
                for(int j = 1; j <= n; j++) {
                    if(dist[k][j] == INF)
                        continue;
                    if(dist[i][j] > dist[i][k] + dist[k][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
        final StringBuilder builder = new StringBuilder();
        final PrintWriter pr = new PrintWriter(new OutputStreamWriter(System.out));
        for(int q[] : queries) {
            long value = dist[q[0]][q[1]];
            if(value == INF)
                value = -1;
            builder.append(value).append("\n");
        }
        pr.write(builder.toString());
        pr.flush();
    }
}
