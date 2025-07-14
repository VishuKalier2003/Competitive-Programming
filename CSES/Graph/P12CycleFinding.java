import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class P12CycleFinding {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
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
        }, "Bellman-Ford-Cycle-Finding",
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
        FastWriter fw = new FastWriter();
        g = new ArrayList<>();
        int n = fr.readInt(), m = fr.readInt();
        parent = new int[n + 1];
        for (int i = 0; i < m; i++) {
            int u = fr.readInt(), v = fr.readInt();
            long w = fr.readLong();
            g.add(new Node(u, v, w));
        }
        fw.attachOutput(solve(n, 1));
        fw.printOutput();
    }

    public static List<Node> g;
    /* parent array used to store the parents u of node v, so that it can be traced later */
    public static int parent[];

    public static class Node {
        public int u, v;
        public long weight;

        public Node(int v1, int v2, long w) {
            this.u = v1;
            this.v = v2;
            this.weight = w;
        }
    }

    public static final long INF = Long.MAX_VALUE / 2;

    public static StringBuilder solve(final int n, final int source) {
        long dist[] = new long[n + 1];      // Distance array
        Arrays.fill(dist, INF);
        dist[1] = 0L;       // source node set
        /** Step 1 : Relax the edges n-1 times (shortest path finding relaxations) */
        for (int pass = 1; pass < n; pass++) {
            boolean relaxed = false;
            for (Node edge : g) {
                if (dist[edge.u] + edge.weight < dist[edge.v]) {
                    dist[edge.v] = dist[edge.u] + edge.weight;
                    // recording parents along the path
                    parent[edge.v] = edge.u;        // updating parent as well
                    relaxed = true;
                }
            }
            if (!relaxed)
                break;
        }
        /** Step 2 : Relax the edges nth time, if relaxed we have a cycle with negative weight in the simple path (negative cycle detection relaxation) */
        int relaxed = -1;
        for (Node edge : g) {
            // Since it is negative cycle detection, we updatethe parents as well
            if (dist[edge.u] + edge.weight < dist[edge.v]) {
                parent[edge.v] = edge.u;        // Update the parent array as the relaxed parent of node u
                relaxed = edge.v;
            }
        }
        if (relaxed == -1)
            return new StringBuilder().append("NO");
        /** Step 3 : Reach the parents by moving n times up the relaxed edges */
        // Move to parent nodes n times, to reach a node present in cycle, since we have already relaxed it n times
        for (int i = 0; i < n; i++)
            // If current node is relaxed one that means it is affected by negative cycle, so we need to reach its nth parent
            relaxed = parent[relaxed];
        List<Integer> cycle = new ArrayList<>();
        /** Step 4 : Start from current node, then traverse until the visited node is not found */
        int current = relaxed;
        do {
            cycle.add(current);
            current = parent[current];
        } while (current != relaxed);
        cycle.add(relaxed); // Complete the cycle
        Collections.reverse(cycle); // To match correct direction
        StringBuilder sb = new StringBuilder("YES\n");
        for (int v : cycle)
            sb.append(v).append(" ");
        return sb;
    }
}
