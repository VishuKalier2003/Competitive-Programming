import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class P28MailDelivery {
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
        }, "Mail-Delivery",
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
        for (int i = 0; i <= n; i++) {      // Creating graphs
            // Micro-optimisation: creating stack for easier popping of last values (no redundant calls to size())
            g.add(new Stack<>());
            graph.add(new ArrayList<>());
        }
        degree = new int[n + 1];
        // Info: The ith edge stores the edgeStart[i] (node u) and edgeEnd[i] (node v)
        edgeStart = new int[m + 1];
        edgeEnd = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            degree[u]++;        // Updating degrees
            degree[v]++;
            // We store the connection with the edge ID, so that the edgeID can be used to access the u and v nodes
            g.get(u).push(i);
            g.get(v).push(i);
            graph.get(u).add(v);
            graph.get(v).add(u);
            edgeStart[i] = u;       // Storing nodes of ith edge as edgeStart (u) and edgeEnd (v   )
            edgeEnd[i] = v;
        }
        fw.attachOutput(solve(n, m));
        fw.printOutput();
    }

    private static List<Stack<Integer>> g;
    private static List<List<Integer>> graph;
    private static int degree[], edgeStart[], edgeEnd[];

    // Note: For directed graphs, the abs(indegree[i] - outdegree[i]) == 1 for atmost one vertex and all other vertices should have 0
    public static StringBuilder solve(final int n, final int m) {
        for (int i = 1; i <= n; i++)
            // Info: If any node has indegree odd, that means there is an extra edge, hence eulerian circuit is impossible
            if (degree[i] % 2 == 1)
                return new StringBuilder().append("IMPOSSIBLE");
        int required = 0;
        for (int i = 1; i <= n; i++)        
            if (degree[i] > 0)      // A node with a positive non-zero degree needs to be traversed (required)
                required++;
        int needed = dfs(1, new boolean[n+1]);  // Counting the number of nodes that can be reached from post office (node 1)
        if (needed < required)  // Info: If the nodes reachable and less than the required nodes
            return new StringBuilder().append("IMPOSSIBLE");
        return new StringBuilder().append(hierholzer(m));
    }

    public static StringBuilder hierholzer(final int m) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();     // Creating a stack for traversal
        List<Integer> lst = new ArrayList<>();
        // Info: The visited array is defined by number of edges, not number of vertices
        boolean visited[] = new boolean[m+1];
        stack.push(1);
        while(!stack.isEmpty()) {
            int node = stack.peek();    // Peeking the top element of the stack
            // Micro-optimisation: Used List<Stack> for the reason of reducing redundant calls to the size() function of lists
            while(!g.get(node).isEmpty() && visited[g.get(node).peek()]) {
                g.get(node).pop();      // removing the head in O(1)
            }
            if(!g.get(node).isEmpty()) {        // While a node is not visited
                int nodeID = g.get(node).pop();     // Pop the node from the graph
                visited[nodeID] = true;
                // Info: If the current node is the startNode of the edge, other node would be the end and vice versa
                int endNode = edgeStart[nodeID] == node ? edgeEnd[nodeID] : edgeStart[nodeID];
                stack.push(endNode);        // Pushing the end node
            } else {
                // If all the edges of the top node are exhausted, pop the node out of the stack    
                lst.add(stack.pop());   
            }
        }
        final StringBuilder output = new StringBuilder();
        for(int i = lst.size()-1; i >= 0; i--)      // Info: Traverse the list in reverse to get the Eulerian Circuit
            output.append(lst.get(i)).append(" ");
        return new StringBuilder().append(output);
    }

    public static int dfs(int node, boolean visited[]) {
        visited[node] = true;
        int count = 1;
        for(int neighbor : graph.get(node)) {       // traversing for the count operation
            if(!visited[neighbor])
                count += dfs(neighbor, visited);
        }
        return count;
    }
}
