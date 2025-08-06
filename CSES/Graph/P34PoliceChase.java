import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P34PoliceChase {
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
        }, "Police-Chase",
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
        final int n = fr.nextInt(), m = fr.nextInt();
        g = new ArrayList<>();
        edges = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        level = new int[n+1];           // Storing the level for each node
        edgeIndex = new int[n+1];       
        for(int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            addEdge(u, v, 1);       // Adding edge from u -> v
            addEdge(v, u, 1);       // Adding edge from v -> u
            // Info: The original edges are stored in separate list so it can be used for finding the min-cut edges
            edges.add(new int[]{u, v});
        }
        fw.attachOutput(solve(n));
        fw.printOutput();
    }

    private static List<List<Edge>> g;
    // Micro-optimisation: Storing the edge index of the next edge which needs to be processed (edges for a node are like 0th, 1st, 2nd...)
    private static int level[], edgeIndex[];
    private static List<int[]> edges;

    private static class Edge {     // static class for the edges
        // nextnode, reverse Index for the back edge and capacity is the max possible flow through the pipe
        private final int nextNode, revIndex, capacity;
        private int flow;

        public Edge(int next, int rev, int cap) {
            this.nextNode = next; this.revIndex = rev; this.capacity = cap; this.flow = 0;
        }

        public int residual() {return capacity-flow;}       // residual (left-over) flow possible

        public void flow(int flow) {this.flow = flow;}

        public int flow() {return flow;}
        public int nextNode() {return nextNode;}
        public int revIndex() {return revIndex;}
        public int capacity() {return capacity;}
    }

    // Info: addEdge function to add forward and reverse edge
    public static void addEdge(int u, int v, int cost) {
        // We add the edge from u to v and the index of the edge will be size of current list with flow +ve c
        g.get(u).add(new Edge(v, g.get(v).size(), cost));
        // We add the reverse edge from v to u and the index of the edge will be the previous one but since an edge has been added so size()-1 with flow 0
        g.get(v).add(new Edge(u, g.get(u).size()-1, 0));
    }

    public static StringBuilder solve(final int n) {
        int maxFlow = dinic(n);     // Dinic flow algorithm called
        markReachable(1);
        final StringBuilder output = new StringBuilder();
        output.append(maxFlow).append("\n");
        for(int uv[] : edges) {
            int u = uv[0], v = uv[1];
            boolean cut = false;
            if(reachable[u] && !reachable[v]) {
                for(Edge e : g.get(u)) {
                    if(e.nextNode == v && e.capacity == e.flow) {
                        cut = true;
                        break;
                    }
                }
            } else if(reachable[v] && !reachable[u]) {
                for(Edge e : g.get(v)) {
                    if(e.nextNode == u && e.capacity == e.flow) {
                        cut = true;
                        break;
                    }
                }
            }
            if(cut)
                output.append(u).append(" ").append(v).append("\n");
        }
        return output;
    }

    // Note: Setting the edge with capacity 1, the flow of the graph gives the number of min-cut edges
    private static int dinic(final int n) {
        int flow = 0;
        reachable = new boolean[n+1];
        // Using bfs to level the nodes
        while(bfs(1, n)) {  //  Ends when no further augmented paths are possible
            Arrays.fill(edgeIndex, 0);
            long pushed;
            // Finding the flow through the augmented path
            while((pushed = dfs(1, n, Integer.MAX_VALUE)) > 0) {
                flow += pushed;
            }
        }
        return flow;        // Provides the number of min-cut edges
    }

    private static boolean reachable[];

    private static boolean bfs(final int source, final int sink) {
        Arrays.fill(level, -1);
        level[source] = 0;
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(source);
        while(!q.isEmpty()) {
            int node = q.poll();
            for(Edge e : g.get(node)) {
                // Levels only the nodes that are not yet visited and have flow possible are not full yet
                if(level[e.nextNode] == -1 && e.residual() > 0) {
                    level[e.nextNode] = level[node] + 1;
                    q.add(e.nextNode);
                }
            }
        }
        // Info: If sink does not have flow possible, it does not gets labelled, specifying no new augmented paths possible
        return level[sink] != -1;       
    }

    // Note: reverse edges are used to allow algorithm to take flow back, if better route is found and for Flow Conservation Principle (flow in = flow out)
    private static long dfs(int source, int sink, int pushed) {
        // Micro-optimisation: If sink is reached or a blocking is reached (no extra flow possible from current node)
        if(source == sink || pushed == 0)
            return pushed;
        // For every edge from the current node
        for(;edgeIndex[source] < g.get(source).size(); edgeIndex[source]++) {       // Info: Incrementing to move to the next edge
            // Micro-optimisation: edgeIndex is used to directly fetch the edge that needs to be processed
            Edge e = g.get(source).get(edgeIndex[source]);
            // If the next node is deeper (higher level) and there is flow possible
            if(level[e.nextNode] == level[source] + 1 && e.residual() > 0) {
                // Info: Run dfs again, with maintaining the min flow through the augmented path
                long transferredFlow = dfs(e.nextNode, sink, Math.min(e.residual(), pushed));
                if(transferredFlow > 0) {       // If flow can be transferred
                    e.flow += transferredFlow;          // Update flow by incrementing it in forward edge
                    g.get(e.nextNode).get(e.revIndex).flow -= transferredFlow;      // Update flow by decrementing it in backward edge
                    return transferredFlow;
                }
            }
        }
        return 0;
    }

    // Info: Called after performing the Dinic flow algorithm
    public static void markReachable(int node) {
        reachable[node] = true;
        for(Edge e : g.get(node)) {
            // Marking all nodes reachable from source node with having some residual flow (set A)
            if(!reachable[e.nextNode] && e.residual() > 0)
                markReachable(e.nextNode);
        }
    }
}
