import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Checkposts {
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
        }, "https://codeforces.com/problemset/problem/427/C", 1 << 26);
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
        g = new ArrayList<>();
        gRev = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
            gRev.add(new ArrayList<>());
        }
        long cost[] = new long[n + 1];
        for (int i = 1; i <= n; i++)
            cost[i] = fr.nextLong();
        final int m = fr.nextInt();
        for (int j = 0; j < m; j++) {
            final int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
            gRev.get(v).add(u);
        }
        fw.attachOutput(solve(n, cost));
        fw.printOutput();
    }

    private static List<List<Integer>> g, gRev, SCC;
    private static List<Integer> component;
    private static Stack<Integer> stack;
    private static boolean vis[];
    private static final int MOD = 1_000_000_007;
    private static int minState;

    public static StringBuilder solve(final int n, final long cost[]) {
        SCC = new ArrayList<>();
        component = new ArrayList<>();
        vis = new boolean[n+1];
        stack = new Stack<>();
        minState = Integer.MAX_VALUE;
        Kosaraju(n, cost);
        long minCost = 0L, ways = 1L;
        for(List<Integer> comp : SCC) {
            int size = comp.size(), min = comp.get(size-1), count = 0;
            minCost += min;
            for(int j = 0; j < size-1; j++) {
                if(cost[comp.get(j)] == min)
                    count++;
            }
            ways = (ways * (count % MOD)) % MOD;
        }
        return new StringBuilder().append(minCost).append(" ").append(ways);
    }

    public static void Kosaraju(int n, long cost[]) {
        for (int i = 1; i <= n; i++)
            if (!vis[i])
                firstDfs(i);
        Arrays.fill(vis, false);
        while (!stack.isEmpty()) {
            while (!stack.isEmpty() && vis[stack.peek()])
                stack.pop();
            if (stack.isEmpty())
                break;
            int node = stack.pop();
            minState = Integer.MAX_VALUE;
            secondDfs(node, cost);
            SCC.add(new ArrayList<>());
            component.add(minState);
            SCC.get(SCC.size() - 1).addAll(component);
            component = new ArrayList<>();
        }
    }

    public static void firstDfs(int node) {
        if (vis[node])
            return;
        vis[node] = true;
        for (int neighbor : g.get(node))
            if (!vis[neighbor])
                firstDfs(neighbor);
        stack.push(node);
    }

    public static void secondDfs(int node, long cost[]) {
        if (vis[node])
            return;
        component.add(node);
        minState = Math.min(minState, (int) cost[node]);
        vis[node] = true;
        for (int neighbor : gRev.get(node))
            if (!vis[neighbor])
                secondDfs(neighbor, cost);
    }

}
