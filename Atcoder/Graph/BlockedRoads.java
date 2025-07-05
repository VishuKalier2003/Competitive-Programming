import java.io.*;
import java.util.*;

public class BlockedRoads {
    static final long INF = (long)2e12;
    static int n, m, q;
    static List<int[]> edges;
    static int[] queryType, queryA, queryB;
    static boolean[] everClosed, active;
    static long[][] dp;
    static long[] answers;

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        n = fr.readInt();
        m = fr.readInt();
        q = fr.readInt();

        edges = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            edges.add(new int[]{fr.readInt(), fr.readInt(), fr.readInt()});
        }

        queryType = new int[q];
        queryA    = new int[q];
        queryB    = new int[q];
        everClosed = new boolean[m];
        for (int i = 0; i < q; i++) {
            queryType[i] = fr.readInt();
            if (queryType[i] == 1) {
                queryA[i] = fr.readInt() - 1;  // zero‑based edge ID
                everClosed[queryA[i]] = true;
            } else {
                queryA[i] = fr.readInt();
                queryB[i] = fr.readInt();
            }
        }

        // Active roads = those never closed
        active = new boolean[m];
        for (int i = 0; i < m; i++) {
            active[i] = !everClosed[i];
        }

        // Initialize DP and run one full Floyd–Warshall
        dp = new long[n+1][n+1];
        floydInit();

        // Prepare to collect answers in reverse
        answers = new long[q];
        int ansIdx = 0;

        // Process queries in reverse
        for (int i = q-1; i >= 0; i--) {
            if (queryType[i] == 2) {
                long dist = dp[queryA[i]][queryB[i]];
                answers[ansIdx++] = (dist >= INF ? -1 : dist);
            } else {
                // reopening edge in reverse order
                int eid = queryA[i];
                if (!active[eid]) {
                    active[eid] = true;
                    incrementalInsert(eid);
                }
            }
        }

        // Output answers in original order
        StringBuilder sb = new StringBuilder();
        for (int i = ansIdx-1; i >= 0; i--) {
            sb.append(answers[i]).append('\n');
        }
        System.out.print(sb);
    }

    // Build initial dp[][] with all active edges
    static void floydInit() {
        for (int i = 1; i <= n; i++) {
            Arrays.fill(dp[i], INF);
            dp[i][i] = 0;
        }
        for (int i = 0; i < m; i++) {
            if (active[i]) {
                int[] e = edges.get(i);
                int u = e[0], v = e[1], w = e[2];
                dp[u][v] = Math.min(dp[u][v], w);
                dp[v][u] = Math.min(dp[v][u], w);
            }
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                if (dp[i][k] == INF) continue;
                for (int j = 1; j <= n; j++) {
                    long viaK = dp[i][k] + dp[k][j];
                    if (viaK < dp[i][j]) {
                        dp[i][j] = viaK;
                    }
                }
            }
        }
    }

    // When an edge eid is activated, update dp in O(N²)
    static void incrementalInsert(int eid) {
        int[] e = edges.get(eid);
        int u = e[0], v = e[1], w = e[2];
        for (int x = 1; x <= n; x++) {
            if (dp[x][u] == INF && dp[x][v] == INF) continue;
            for (int y = 1; y <= n; y++) {
                long cand1 = dp[x][u] + w + dp[v][y];
                long cand2 = dp[x][v] + w + dp[u][y];
                long best  = Math.min(dp[x][y], Math.min(cand1, cand2));
                if (best < dp[x][y]) {
                    dp[x][y] = best;
                }
            }
        }
    }

    // Fast I/O
    static class FastReader {
        private static final byte[] buf = new byte[1<<20];
        private int ptr, len;
        int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buf);
                if (len <= 0) return -1;
            }
            return buf[ptr++] & 0xFF;
        }
        int readInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = (c == '-');
            if (neg) c = read();
            do { x = x*10 + (c & 15); }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }
}
