import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class DinicNetworkFlow {
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
        }, "Download-speed-(Dinic-Flow)",
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
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        level = new int[n+1];       // Level array to store the level of each node via bfs
        edgeIndex = new int[n+1];
        for(int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            long c = fr.nexLong();
            addEdge(u, v, c);
        }
        fw.attachOutput(solve(n));
        fw.printOutput();
    }

    private static List<List<Edge>> g;
    // Micro-optimisation: edgeIndex is used to extract the edge that needs to be processed instead of scanning all edges of the current node
    private static int level[], edgeIndex[];     // Info: edgeIndex store the index of the next neighbor of the ith node to explore

    // Note: the edge class to store nextNode, the index of the reverse edge, current flow and the maximum capacity
    private static class Edge {
        final int nextNode, revIndex;
        long flow, capacity;

        public Edge(int to, int rev, long cap) {
            this.nextNode = to; this.revIndex = rev;
            this.capacity = cap; this.flow = 0;
        }

        public long residual() {return capacity-flow;}      // Evaluating residual flow possible
        public void flow(long x) {this.flow = x;}

        public int nextNode() {return this.nextNode;}
        public int revIndex() {return this.revIndex;}
        public long flow() {return this.flow;}
        public long capacity() {return this.capacity;}
    }

    // Info: addEdge function to add forward and reverse edge
    private static void addEdge(final int u, final int v, final long c) {
        // We add the edge from u to v and the index of the edge will be size of current list with flow +ve c
        g.get(u).add(new Edge(v, g.get(v).size(), c));
        // We add the reverse edge from v to u and the index of the edge will be the previous one but since an edge has been added so size()-1 with flow 0
        g.get(v).add(new Edge(u, g.get(u).size()-1, 0L));
    }

    public static StringBuilder solve(final int n) {
        long flow = 0L;
        // Using bfs to level the nodes
        while(bfs(1, n)) {
            Arrays.fill(edgeIndex, 0);
            long pushed;
            while((pushed = dfs(1, n, Long.MAX_VALUE)) > 0) {
                flow += pushed;
            }
        }
        return new StringBuilder().append(flow);
    }

    public static boolean bfs(final int source, final int sink) {
        Arrays.fill(level, -1);     // Fill with default -1 values
        level[source] = 0;      // source level as 0
        Queue<Integer> q = new ArrayDeque<>();
        q.add(source);
        while(!q.isEmpty()) {
            int node = q.poll();
            for(Edge e : g.get(node)) {
                // Levels only the nodes that are not yet visited and have flow possible are not full yet
                if(level[e.nextNode] == -1 && e.residual() > 0) {
                    level[e.nextNode] = level[node] + 1;        // Increment level of next node by 1
                    q.add(e.nextNode);
                }
            }
        }
        // Info: If level of sink is updated, then an augmented path is found from source to sink
        return level[sink] >= 0;        
    }

    // Note: reverse edges are used to allow algorithm to take flow back, if better route is found and for Flow Conservation Principle (flow in = flow out)
    public static long dfs(int source, int sink, long pushed) {
        // Micro-optimisation: If sink is reached or a blocking is reached (no extra flow possible from current node)
        if(source == sink || pushed == 0L)  
            return pushed;
        // For every edge from the current node
        for(; edgeIndex[source] < g.get(source).size(); edgeIndex[source]++) {  // Info: Incrementing to move to the next edge
            // Micro-optimisation: edgeIndex is used to directly fetch the edge that needs to be processed
            Edge edge = g.get(source).get(edgeIndex[source]);
            // If the next node is deeper (higher level) and there is flow possible
            if(level[edge.nextNode] == level[source] + 1 && edge.residual() > 0) {
                // Run dfs again, with maintaining the min flow through the augmented path
                long transferredFlow = dfs(edge.nextNode, sink, Math.min(pushed, edge.residual()));
                if(transferredFlow > 0) {       // If flow can be transferred
                    edge.flow += transferredFlow;       // Update flow by incrementing it in forward edge
                    g.get(edge.nextNode).get(edge.revIndex).flow -= transferredFlow;    // Update flow by decrementing in reverse edge
                    return transferredFlow;
                }
            }
        }
        return 0L;      // When no flow pushed return 0
    }
}
