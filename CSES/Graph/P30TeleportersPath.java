import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class P30TeleportersPath {
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
        }, "Teleporters-Path",
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
        final int n = fr.nextInt(), m = fr.nextInt();
        g = new ArrayList<>();
        graph = new ArrayList<>();
        revGraph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            g.add(new Stack<>());       // Micro-optimisation: creating a stack at each index
            graph.add(new ArrayList<>());
            revGraph.add(new ArrayList<>());
        }
        inDegree = new int[n + 1];
        outDegree = new int[n + 1];
        for (int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).push(v);
            graph.get(u).add(v);
            revGraph.get(v).add(u);
            outDegree[u]++;
            inDegree[v]++;
        }
        fw.attachOutput(solve(n, m));
        fw.printOutput();
    }

    private static List<Stack<Integer>> g;
    private static List<List<Integer>> graph, revGraph;
    private static int inDegree[], outDegree[];
    private static final String IMPOSSIBLE = "IMPOSSIBLE";

    public static StringBuilder solve(final int n, final int m) {
        boolean[] visF = new boolean[n + 1], visR = new boolean[n + 1];
        dfsForward(1, visF);    // Fill visF[] — reachable from 1
        dfsReverse(n, visR);    // Fill visR[] — can reach n
        for (int i = 1; i <= n; i++) {
            // All nodes having any indegree or outdegree should be reachable
            if ((inDegree[i] > 0 || outDegree[i] > 0) && (!visF[i] || !visR[i])) {
                return new StringBuilder().append(IMPOSSIBLE);
            }
        }
        int startNode = 1, endNode = n;
        // If start node does not have extra outgoing edge
        if (outDegree[startNode] - inDegree[startNode] != 1)
            return new StringBuilder().append(IMPOSSIBLE);
        // If end node does not have xtra incmoing edge
        if (inDegree[endNode] - outDegree[endNode] != 1)
            return new StringBuilder().append(IMPOSSIBLE);
        for (int i = 2; i < n; i++)
            // If rest of the nodes do not have extra outgoing and incoming edges
            if (inDegree[i] != outDegree[i])
                return new StringBuilder().append(IMPOSSIBLE);
        return new StringBuilder().append(hierholzer(n, m));
    }

    public static StringBuilder hierholzer(final int n, final int m) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        List<Integer> eulerWalk = new ArrayList<>();        // List to store euler walk
        stack.push(1);      // pushing start node into the stack
        while (!stack.isEmpty()) {
            int u = stack.peek();
            // Info: If there is any untraversed edge left
            if (!g.get(u).isEmpty()) {      // Micro-optimisation: used stack here so redundant calls to size() is reduced
                int v = g.get(u).pop();
                stack.push(v);      // Push the end node of the edge into the stack
            } else {
                int node = stack.pop();     // Info: Pop the node when all its outgoing edges are traversed 
                eulerWalk.add(node);
            }
        }
        Collections.reverse(eulerWalk);     // reverse the walk
        final StringBuilder output = new StringBuilder();
        for (int node : eulerWalk)
            output.append(node).append(" ");
        return output;
    }

    public static int dfsForward(int u, boolean[] vis) {
        if (vis[u])
            return 0;
        vis[u] = true;
        int cnt = 1;
        for (int v : graph.get(u))      // Dfs on forward (original graph)
            cnt += dfsForward(v, vis);
        return cnt;
    }

    public static int dfsReverse(int u, boolean[] vis) {
        if (vis[u])
            return 0;
        vis[u] = true;
        int cnt = 1;
        for (int v : revGraph.get(u))       // Dfs on reversed graph
            cnt += dfsReverse(v, vis);
        return cnt;
    }

}
