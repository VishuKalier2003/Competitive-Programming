import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
public class P14RoundTripII {
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
        }, "Round-Trip-Cycle-Detect",
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
        final int n = fr.readInt(), m = fr.readInt();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for(int j = 0; j < m; j++) {
            int u = fr.readInt(), v = fr.readInt();
            g.get(u).add(v);
        }
        fw.attachOutput(solve(n, m, new int[n+1]));
        fw.printOutput();
    }
 
    private static List<List<Integer>> g;
    private static int parent[];
    private static final StringBuilder output = new StringBuilder();

    /**
    * We use vis[] states to check if node and child have same value 1 (currently being visited), then there is a back edge and all checked values are given 2 and unvisited are given 1
    @param n the number of nodes
    @param m the number of edges
    @param vis the visited array to store states (0, 1 and 2)
    @return string output containg the length of cycle alongwith the cycle
    */
    public static StringBuilder solve(final int n, final int m, final int vis[]) {
        parent = new int[n+1];      // creating a parent array for backtracking edges
        // Create a visited array of int (0 for unvisited, 1 for currently visiting, 2 for already visited)
        for(int i = 1; i <= n; i++) {
            if(vis[i] == 0 && recurse(i, vis))
                return new StringBuilder().append(output);
        }
        output.append("IMPOSSIBLE");        // In case not found
        return new StringBuilder().append(output);
    }

    public static boolean recurse(int node, final int vis[]) {
        vis[node] = 1;      // mark node as currently visiting
        for(int child : g.get(node)) {
            if(vis[child] == 0) {       // If child is not yet visited
                // update parent array for backtracing the cycle
                parent[child] = node;
                if(recurse(child, vis))     // recursion
                    return true;
            // Note: Both node and child have the visited state as 1 (being visited currently)
            } else if(vis[child] == 1) {    // If child is currently being visiting, a cycle edge found
                List<Integer> cycle = new ArrayList<>();        // Storing and checking cycles
                cycle.add(child); 
                int curr = node;
                while(curr != child) {
                    cycle.add(curr);
                    curr = parent[curr];
                }
                cycle.add(child);
                Collections.reverse(cycle);     // reversing cycle
                // Output storing
                output.append(cycle.size()).append("\n");
                for(int e : cycle)
                    output.append(e).append(" ");
                return true;        // Info: return true such that the cycle is found
            }
        }
        vis[node] = 2;
        return false;       // Info: return false such that current node is not part of any cycle
    }
}