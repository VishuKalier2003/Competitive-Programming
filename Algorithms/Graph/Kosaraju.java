import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Kosaraju {
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
        }, "Flight-Routes-Check",
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
        g = new ArrayList<>();
        revG = new ArrayList<>();
        final int n = fr.nextInt(), m = fr.nextInt();
        // Info: Creating original graph and reversed graph
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
            revG.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
            revG.get(v).add(u);
        }
        fw.attachOutput(solve(n, m));
        fw.printOutput();
    }

    private static List<List<Integer>> g, revG;
    private static boolean vis[];       // visited array to track visited nodes
    private static Stack<Integer> stack;
    private static List<List<Integer>> SCC;     // List of SCC components
    private static List<Integer> component;     // List to store a single component

    public static StringBuilder solve(final int n, final int m) {
        vis = new boolean[n + 1];
        SCC = new ArrayList<>();
        component = new ArrayList<>();
        stack = new Stack<>();
        // Running dfs even when the graph might be disconnected (preferred way)
        for (int i = 1; i <= n; i++) {
            if (!vis[i])
                helper(i, g);       // Info: First dfs to store the nodes in post order (original graph)
        }
        Arrays.fill(vis, false);    // visited array reset
        while (!stack.isEmpty()) {      // Popping the stack empty
            while (!stack.isEmpty() && vis[stack.peek()])       // removing elements that are already visited (already in SCC)
                stack.pop();
            if(stack.isEmpty())     // Micro-optimisation: ending stack early
                break;
            int node = stack.pop();
            findSCC(node, revG);        // Info: Second dfs to find the SCC from current node in reverse graph
            SCC.add(new ArrayList<>());
            // Adding the new SCC component to the list
            SCC.get(SCC.size() - 1).addAll(component);
            component.clear();
        }
        if (SCC.size() == 1)        // If entire graph is a single SCC
            return new StringBuilder().append("YES");
        return new StringBuilder().append("NO\n").append(SCC.get(1).get(0)).append(" ").append(SCC.get(0).get(0));
    }

    // We add nodes in post order to ensure that the popped node is node whose entire SCC is reachable in reverse graph
    public static void helper(int node, List<List<Integer>> graph) {
        vis[node] = true;
        for (int neighbor : graph.get(node)) {
            if (!vis[neighbor])
                helper(neighbor, graph);        // Traversing unvisited paths
        }
        stack.push(node);       // Adding nodes in the stack in post order fashion
    }

    public static void findSCC(int node, List<List<Integer>> graph) {
        if (vis[node])      // Info: If visited node found, backtrack
            return;
        vis[node] = true;
        component.add(node);        // Adding node to the SCC
        for (int neighbor : graph.get(node))
            if (!vis[neighbor])
                findSCC(neighbor, graph);       // Traversing unvsited paths
    }
}
