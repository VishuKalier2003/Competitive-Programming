import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class P26GinatPizza {
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
        }, "Giant-Pizza",
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
        impGraph = new ArrayList<>();
        impGraphReverse = new ArrayList<>();
        final int n = fr.nextInt(), m = fr.nextInt();
        for (int i = 0; i <= 2*m+1; i++) {
            impGraph.add(new ArrayList<>());
            impGraphReverse.add(new ArrayList<>());
        }
        // Micro-optimisation: creating 2m nodes and storing first m as +ve and next m as -ve
        for (int i = 0; i < n; i++) {
            char ch1 = fr.next().charAt(0);
            int x = fr.nextInt();
            char ch2 = fr.next().charAt(0);
            int y = fr.nextInt();
            // Info: The distance between x and ~x is m
            int u = (ch1 == '+') ? x : x + m;
            int v = (ch2 == '+') ? y : y + m;
            // If u is greater than m, then I need to shift u to left by m (u-m), else right by m (u+m)
            int notU = (u > m) ? u - m : u + m;     // shifting to get the opposite sign
            int notV = (v > m) ? v - m : v + m;
            // Info: If the clause is (a or b) then it can be implied into ~a -> b and ~b -> a (two implications)
            impGraph.get(notU).add(v);      // Original Implication graph
            impGraph.get(notV).add(u);
            impGraphReverse.get(v).add(notU);       // reverse Implication graph
            impGraphReverse.get(u).add(notV);
        }
        // Info: All these clauses are merged by AND into a Conjunctive Normal Form (CNF) via the constraints
        fw.attachOutput(solve(n, m));
        fw.printOutput();
    }

    private static List<List<Integer>> impGraph, impGraphReverse;
    private static Stack<Integer> stack;
    private static boolean sccFlag;

    public static StringBuilder solve(final int n, final int m) {
        stack = new Stack<>();
        sccFlag = true;     // Flag to check if all SCC formed are valid
        int sccNodes[] = Kosaraju(m);       // Running the Kosraju algorithm to define all SCC
        if(!sccFlag)
            return new StringBuilder().append("IMPOSSIBLE");
        char result[] = new char[m+1];
        // Now for every variable (1 to m)
        for(int i = 1; i <= m; i++) {
            // Get the SCC indexes for x and ~x
            int positive = sccNodes[i];     // Taking
            int negative = sccNodes[i+m];   // Not Taking
            // Note: Intuitively, the later the literal appears in topological order, the more freely we can take it (Assign a +ve), since it has lesser constraints
            result[i] = positive > negative ? '+' : '-';
        }
        final StringBuilder output = new StringBuilder();
        for(int i = 1; i <= m; i++)
            output.append(result[i]).append(" ");
        return new StringBuilder().append(output);
    }

    // Note: A higher SCC index means that node is coming later than other node in Original graph, since Kosaraju processes then in topological order
    public static int[] Kosaraju(final int m) {
        boolean visited[] = new boolean[2*m+1];     // Creating visited array (2m+1) nodes for 1 based indexing
        int sccNodes[] = new int[2*m+1];
        component = new HashSet<>();
        // Assuming the graph is disconnected, running dfs for each component
        for (int i = 1; i <= 2*m; i++) {
            if (!visited[i])
                firstDfs(i, visited);
        }
        Arrays.fill(visited, false);        // resetting the visited array
        sccIndex = 1;
        while(!stack.isEmpty()) {
            int x = stack.pop();
            if(visited[x])      // If visited, skip it
                continue;
            component.clear();      // resetting the current SCC (ready to make new one)
            if(!secondDfs(x, m, visited)) {     // running second dfs on reverse Implication graph
                sccFlag = false;
                return sccNodes;
            }
            // Note: Kosraju porcesses SCC in such a way that lower Id SCC are sinks and higher Id SCC are sources
            for(int v : component)      // Info: giving the current SCC nodes the same SCC index
                sccNodes[v] = sccIndex;
            sccIndex++;
        }
        return sccNodes;
    }

    private static Set<Integer> component;
    private static int sccIndex = 1;        // Index for indexing the SCC

    public static void firstDfs(int node, boolean visited[]) {
        if (visited[node])      // If visited node found backtrack
            return;
        visited[node] = true;
        for (int neighbor : impGraph.get(node))
            if (!visited[neighbor])
                firstDfs(neighbor, visited);        // Traversing unvisited paths
        stack.push(node);
    }

    // We add nodes in post order to ensure that the popped node is node whose entire SCC is reachable in reverse graph
    public static boolean secondDfs(int node, int m, boolean visited[]) {
        if(visited[node])       // If visited node found backtrack
            return true;
        visited[node] = true;
        component.add(node);
        boolean isValid = true;
        int opposite = node > m ? node-m : node+m;
        // Info: SCC is not valid if x and ~x are in same SCC because Implications always implies True, and in SCC we can reach from u to v and v to u, so x and ~x cannot be same
        if(component.contains(opposite))
            return false;
        for(int neighbor : impGraphReverse.get(node))
            if(!visited[neighbor])
                // AND operator to ensure each node is valid in SCC
                isValid &= secondDfs(neighbor, m, visited);     // Traversing unvisited paths
        return isValid;
    }
}
