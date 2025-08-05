import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class P27CoinCollector {
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
        }, "Coin Collector",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), m = fr.nextInt();
        coins = new long[n+1];
        graph = new ArrayList<>();
        graphReverse = new ArrayList<>();
        // Creating original and reversed graph for Kosaraju's implementation
        for(int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
            graphReverse.add(new ArrayList<>());
        }
        for(int i = 1; i <= n; i++)
            coins[i] = fr.nexLong();
        // Micro-optimisation: Storing edges so can be used later
        int edges[][] = new int[m][2];
        for(int i = 0 ;i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            edges[i][0] = u;    // edge between  edge[i][0] -> edge[i][1]
            edges[i][1] = v;
            graph.get(u).add(v);
            graphReverse.get(v).add(u);
        }
        fw.attachOutput(solve(n, edges));
        fw.printOutput();
    }

    private static List<List<Integer>> graph, graphReverse;
    // Note: SuperGraph represents the entire SCC as one node and then establishes connections between SCC
    private static List<List<Integer>> superGraph;
    private static int inDegree[];      // Indegree array for topo sort

    private static long coins[];

    public static StringBuilder solve(final int n, final int edges[][]) {
        long weights[] = Kosaraju(n, edges);        // Info: Kosaraju algorithm for providing coins in each SCC
        final int k = weights.length;
        long dp[] = new long[k];        // dp array for storing the max coins taken when ending at ith state (SCC)
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for(int i = 1; i < k; i++) {
            if(inDegree[i] == 0)        // Adding pendent SCC to the deque
                q.offer(i);
            // Micro-optimisation: updating dp array within this loop and not creating a separate loop
            dp[i] = weights[i];     
        }
        while(!q.isEmpty()) {
            int superNode = q.poll();
            for(int neighbor : superGraph.get(superNode)) {     // For adjancency list
                // Note: Dp transition - we either include the next SCC or end at current SCC (dp relaxation)
                if(dp[superNode] + weights[neighbor] > dp[neighbor])    // Info: If larger number of coins can be taken, we take that
                    dp[neighbor] = dp[superNode] + weights[neighbor];
                if(--inDegree[neighbor] == 0)
                    q.add(neighbor);        // When the neighbor (adjacent SCC) becomes the pendant vertex
            }
        }
        long max = 0l;
        // Info: we can start and end at any vertex, so best to take the max among all SCC
        for(int i = 1; i < k; i++)
            max = Math.max(dp[i], max);
        return new StringBuilder().append(max);
    }

    public static long[] Kosaraju(final int n, final int edges[][]) {
        boolean visited[] = new boolean[n+1];
        int sccIndex = 1;
        superGraph = new ArrayList<>();
        int sccNodes[] = new int[n+1];      // Array to store the index of SCC (SCC component) to which th ith node belongs
        component = new ArrayList<>();
        stack = new Stack<>();
        for(int i = 1; i <= n; i++) {       // Incase the graph is disconnected
            if(!visited[i])
                firstDfs(i, visited);
        }
        Arrays.fill(visited, false);        // Resetting the visited array
        while(!stack.isEmpty()) {
            int x = stack.pop();
            component.clear();      // Resetting component value
            if(visited[x])
                continue;
            secondDfs(x, visited);
            for(int node : component)       // Info: Marking the SCC Index of all the nodes in the component
                sccNodes[node] = sccIndex;
            sccIndex++;
        }
        // Micro-optimisation: initialize superGraph with the size of sccIndex, because only that many super nodes will be there
        for(int i = 0; i < sccIndex; i++)
            superGraph.add(new ArrayList<>());
        inDegree = new int[sccIndex];       // Micro-optimisation: similarly mark the size of inDegree as well
        for(int edge[] : edges) {
            int n1 = edge[0], n2 = edge[1];
            if(sccNodes[n1] != sccNodes[n2]) {      // Info: If the two nodes, belong to different SCC
                // Note: Create a connection in superGraph from superNode 1 to superNode 2, where the index of superNodes are stored in sccNodes array
                superGraph.get(sccNodes[n1]).add(sccNodes[n2]);     
                inDegree[sccNodes[n2]]++;
            }
        }
        long weight[] = new long[sccIndex];
        for(int i = 1; i <= n; i++)
            weight[sccNodes[i]] += coins[i];        // Update the weight (sum of coins for all nodes) for each SCC
        return weight;
    }

    private static List<Integer> component;
    private static Stack<Integer> stack;

    public static void firstDfs(int node, boolean visited[]) {
        if(visited[node])       // If visited, backtrack
            return;
        visited[node] = true;
        for(int neighbor : graph.get(node))
            if(!visited[neighbor])
                firstDfs(neighbor, visited);
        stack.push(node);       // adding in post order fashion
    }

    // We add nodes in post order to ensure that the popped node is node whose entire SCC is reachable in reverse graph
    public static void secondDfs(int node, boolean visited[]) {
        if(visited[node])       // If visited, backtrack
            return;
        visited[node] = true;
        component.add(node);        // adding the node to component
        for(int neighbor : graphReverse.get(node))      // Traversing the reverseGraph
            if(!visited[neighbor])
                secondDfs(neighbor, visited);
    }
}
