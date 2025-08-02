import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P16LongestFlightRoute {
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
            do { x = 10 * x + (c - '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static class FastWriter {
        private final PrintWriter pw;
        private final StringBuilder sb;
        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }
        public void append(StringBuilder s) { sb.append(s); }
        public void println() { sb.append('\n'); }
        public void flush() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Graph data structures
    private static List<List<Integer>> g;
    private static List<List<Integer>> gr;  // reverse graph
    private static boolean[] canReach;
    private static int[] dp, nextCity;
    private static final int NEG_INF = Integer.MIN_VALUE / 2;

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain();
            } catch (IOException e) {
                // Fail silently
            }
        }, "Longest-Flight-Route", 1 << 26);
        t.start();
        try { t.join(); }
        catch (InterruptedException ignored) {}
    }

    private static void callMain() throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        int n = fr.nextInt();
        int m = fr.nextInt();

        // 1) Build forward and reverse adjacency
        g  = new ArrayList<>(n+1);
        gr = new ArrayList<>(n+1);
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
            gr.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int u = fr.nextInt();
            int v = fr.nextInt();
            g.get(u).add(v);
            gr.get(v).add(u);
        }

        // 2) Reverse-BFS from target to mark all that can reach n
        canReach = new boolean[n+1];
        Deque<Integer> dq = new ArrayDeque<>();
        canReach[n] = true;
        dq.add(n);
        while (!dq.isEmpty()) {
            int u = dq.removeFirst();
            for (int p : gr.get(u)) {
                if (!canReach[p]) {
                    canReach[p] = true;
                    dq.addLast(p);
                }
            }
        }

        // 3) Allocate DP and next pointers
        dp       = new int[n+1];
        nextCity = new int[n+1];
        for (int i = 1; i <= n; i++) {
            dp[i] = NEG_INF;
            nextCity[i] = -1;
        }

        // 4) Compute best path length from 1→n via memoized DFS
        int best = dfs(1, n);

        // 5) Output
        if (best < 0) {
            fw.append(new StringBuilder("IMPOSSIBLE"));
        } else {
            StringBuilder out = new StringBuilder();
            out.append(best).append("\n");
            int cur = 1;
            // walk the nextCity chain
            while (cur != -1) {
                out.append(cur).append(" ");
                cur = nextCity[cur];
            }
            fw.append(out);
        }

        fw.flush();
    }

    /**
     * Returns length of longest path from u → target (in #cities),
     * or a negative value if no route exists.
     */
    private static int dfs(int u, int target) {
        // Memoized result
        if (dp[u] != NEG_INF) {
            return dp[u];
        }
        // Base case: we've arrived
        if (u == target) {
            dp[u] = 1;
            return 1;
        }
        // If u itself cannot reach target, prune immediately
        if (!canReach[u]) {
            dp[u] = NEG_INF;
            return dp[u];
        }

        // Explore all outgoing flights, skipping dead branches
        int bestLen = NEG_INF;
        for (int v : g.get(u)) {
            if (!canReach[v]) continue;
            int len = dfs(v, target);
            if (len > 0 && len + 1 > bestLen) {
                bestLen = len + 1;
                nextCity[u] = v;
            }
        }

        dp[u] = bestLen;
        return dp[u];
    }
}
