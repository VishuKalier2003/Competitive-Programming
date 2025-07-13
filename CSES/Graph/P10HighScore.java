/* In this question we need to apply Bellman Ford to find a cycle of all positve weights so we can loop infinitely to increase the value */

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P10HighScore {
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

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Bellman-Ford",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        g = new ArrayList<>();
        revG = new ArrayList<>();
        int n = fr.readInt(), m = fr.readInt();
        for (int j = 0; j < m; j++) {
            int u = fr.readInt(), v = fr.readInt();
            long w = fr.readLong();
            // Invert the weights for this question, since BF finds min but we want to
            // acheive max
            g.add(new Edge(u, v, -w));
            revG.add(new Edge(v, u, -w));
        }
        FastWriter fw = new FastWriter();
        fw.attachOutput(solve(n, 1));
        fw.printOutput();
    }

    public static List<Edge> g, revG;

    public static class Edge {
        private final int v1, v2;
        private final long w;

        public Edge(int v1, int v2, long w) {
            this.v1 = v1;
            this.v2 = v2;
            this.w = w;
        }

        public int getV1() {
            return this.v1;
        }

        public int getV2() {
            return this.v2;
        }

        public long getWeight() {
            return this.w;
        }
    }

    private static final long INF = Long.MAX_VALUE / 4;

    public static StringBuilder solve(int n, int source) {
        /** Step 1 : Initialise variables and perform n-1 relaxations */
        long dist[] = new long[n + 1];
        Arrays.fill(dist, INF);
        dist[source] = 0L; // Mark the desired source
        for (int pass = 1; pass < n; pass++) {
            boolean flag = false; // Flag to store if edge can be relaxed in ith pass
            for (Edge edge : g) {
                // If the edge can be relaxed i.e. the weight to reach that node can be reduced
                // further
                if (dist[edge.v1] != INF && dist[edge.v1] + edge.w < dist[edge.v2]) {
                    dist[edge.v2] = dist[edge.v1] + edge.w;
                    flag = true; // Mark as relaxation done
                }
            }
            // If edge cannot be relaxed in ith pass, then it cannot be relaxed in next
            // passes, hence skip
            if (!flag)
                break;
        }
        /** Step 2 : Create an array of negCycle and check for nth relaxation */
        // Array to mark nodes, influenced directly or indirectly by negative cycle
        boolean negCycle[] = new boolean[n + 1];
        for (Edge edge : g) {
            // If an edge can be relaxed in nth iteration, then it is in negative cycle
            if (dist[edge.v1] != INF && dist[edge.v1] + edge.w < dist[edge.v2]) {
                negCycle[edge.v2] = true;
            }
        }
        /**
         * Step 3 : Backpropagate from negCycle nodes via reverse graphs to mark nodes
         * that can reach the negCycle
         */
        backPropagate(n, negCycle);
        /**
         * Step 4 : Perform reachability checks from graph 1 as source and reverse graph
         * n as source
         */
        boolean forward[] = new boolean[n + 1], backward[] = new boolean[n + 1];
        // Perform dfs from source s in original graph g
        dfs(source, g, forward);
        // Perform dfs from source n in reverse graph revG
        dfs(n, revG, backward);
        for (int i = 1; i <= n; i++)
            // If ith node is in negative cycle and can be reached from both source s via
            // forward array and from node n via backward array
            if (negCycle[i] && forward[i] && backward[i])
                return new StringBuilder().append("-1");
        // since we had the source already set, just return the node whose distance is
        // to be evaluated
        return new StringBuilder().append(-dist[n]);
    }

    public static void backPropagate(int n, boolean negCycle[]) {
        Deque<Integer> q = new ArrayDeque<>();      // use bfs from the cycle vai reverse graph 
        for (int i = 1; i <= n; i++) {
            if (negCycle[i])
                q.add(i);
        }
        while (!q.isEmpty()) {
            int v = q.poll();
            for (Edge e : revG) {
                // Check for the reverse graph edge from v1 to v2
                if (e.v1 == v && !negCycle[e.v2]) {
                    negCycle[e.v2] = true;      // mark v2 (v) as the negCycle affected node
                    q.add(e.v2);        // Add the affected node into the deque
                }
            }
        }
    }

    public static void dfs(int node, List<Edge> g, boolean vis[]) {
        vis[node] = true;
        for (Edge edge : g) {
            if (edge.v1 == node && !vis[edge.v2]) {
                dfs(edge.v2, g, vis);
            }
        }
    }

}
