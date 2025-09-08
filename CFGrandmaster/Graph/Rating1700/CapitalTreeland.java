import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CapitalTreeland {
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
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
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
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
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
        }, "Choosing-Capital-for-Treeland-(https://codeforces.com/problemset/problem/219/D)", 1 << 26);
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
        final int n = fr.nextInt();
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 0; i < n - 1; i++) {
            final int u = fr.nextInt(), v = fr.nextInt();
            tree.get(u).add(new int[] { v, 0 });
            tree.get(v).add(new int[] { u, 1 });
        }
        solve(n);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();
    private static List<List<int[]>> tree;

    private static int[] dp; // dp[i] = reversals needed if root is i
    private static int base; // reversals if root = 1

    public static void solve(final int n) {
        dp = new int[n + 1];
        base = 0;
        // Step 1: compute base cost when root = 1
        dfs1(1, 0);
        // Step 2: rerooting to compute dp for all nodes
        dp[1] = base;
        dfs2(1, 0);
        // Step 3: find min reversals and collect capitals
        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++)
            min = Math.min(min, dp[i]);
        output.append(min).append('\n');
        for (int i = 1; i <= n; i++)
            if (dp[i] == min)
                output.append(i).append(' ');
        output.append('\n');
    }

    // DFS1: compute base reversals if root = 1
    private static void dfs1(int u, int p) {
        for (int[] e : tree.get(u)) {
            int v = e[0], cost = e[1];
            if (v == p)
                continue;
            base += cost; // count reversal if edge is v->u
            dfs1(v, u);
        }
    }

    // DFS2: rerooting step
    private static void dfs2(int u, int p) {
        for (int[] e : tree.get(u)) {
            int v = e[0], cost = e[1];
            if (v == p)
                continue;
            // If edge u->v (cost = 0): when rerooting at v, add +1
            // If edge v->u (cost = 1): when rerooting at v, add -1
            if (cost == 0) {
                dp[v] = dp[u] + 1;
            } else {
                dp[v] = dp[u] - 1;
            }
            dfs2(v, u);
        }
    }

}
