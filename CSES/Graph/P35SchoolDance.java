import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class P35SchoolDance {
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
        }, "School-Dance",
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
        final int n = fr.nextInt(), m = fr.nextInt(), k = fr.nextInt();
        g = new ArrayList<>();
        // Micro-optimisation: we define the start set, if boy keep it n, if girl keep it m
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        boyMatching = new int[n+1];     // N boys - where ith boy matches with the girl number boy[i], 0 otherwise
        girlMatching = new int[m+1];    // M girls - where ith girl matches with the boy number girl[i], 0 otherwise
        dist = new int[n+1];
        // Note: Creating a Bipartite graph, directed edge from boy to girl
        for(int i = 0; i < k; i++) {
            int boy = fr.nextInt(), girl = fr.nextInt();
            g.get(boy).add(girl);       // Directed edge
        }
        fw.attachOutput(solve(n));
        fw.printOutput();
    }

    private static List<List<Integer>> g;
    // Distance array to store the depth of the node in the Bipartite graph
    private static int[] boyMatching, girlMatching, dist;

    // Note: Hopkroft-Karp defines the maximal matching pairs between two sets with the pairs
    public static StringBuilder solve(final int n) {
        int matching = hopkroftKarp(n);     
        final StringBuilder output = new StringBuilder();
        output.append(matching).append("\n");
        for(int boy = 1; boy <= n; boy++)
            if(boyMatching[boy] != 0)   // If there is a girl for a boy, there is an existing edge
                output.append(boy).append(" ").append(boyMatching[boy]).append("\n");
        return output;
    }

    // Note: An Augmenting path is path from source to sink, with alternating present (matched) and absent (unmatched) edges
    public static int hopkroftKarp(final int n) {
        int matching = 0;
        // Info: Run bfs to find whether there exists an augmenting path in the current iteration
        while(bfs(n)) {
            for(int boy = 1; boy <= n; boy++) {
                if(boyMatching[boy] == 0) {     // If there is a free node (boy with no matchings)
                    if(dfs(boy))        // Run dfs, and if an augmenting path found, increment the matching
                        matching++;
                }
            }
        }
        return matching;
    }

    private static final int SINK = 0;      // Here SINK (virtual sink) refers to the end node that doesn't have any matching (taken as node 0)
    // Note: In augmenting path, we can break matched edge into two adjacent unmatched edges, thereby assigning more valid pairs
    private static final int INF = Integer.MAX_VALUE;       // The absent (unmatched) edges are denoted by INF

    private static boolean bfs(int n) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        dist[SINK] = INF;   // Assuming, we might not be able to reach the sink
        for(int boy = 1; boy <= n; boy++) {
            if(boyMatching[boy] == 0) {     // If there is no matching for current boy
                dist[boy] = 0;      // mark the level of current boy
                q.add(boy);
            } else
                dist[boy] = INF;    // Else, mark the distance as visited (unreachable)
        }
        while(!q.isEmpty()) {
            int boy = q.poll();     // Poll the boy
            // Micro-optimisation: If the boy level is smaller than sink level, then we can go deeper
            if(dist[boy] < dist[SINK]) {
                for(int girl : g.get(boy)) {    // For every possible girl for current boy
                    int nextBoy = girlMatching[girl];  
                    // Access the next boy, and if it is unmatched (unvisited)     
                    if(dist[nextBoy] == INF) {
                        dist[nextBoy] = dist[boy] + 1;      // Increment depth of the boy by 1
                        q.add(nextBoy);
                    }
                }
            }
        }
        // Info: If we are able to reach the sink, then an augmenting path is present
        return dist[SINK] != INF;
    }

    private static boolean dfs(int boy) {       // Info: Used to find the Augmented path
        if(boy == SINK)     // If we reached the sink
            return true;
        for(int girl : g.get(boy)) {
            int nextBoy = girlMatching[girl];       // Accessing the next boy
            // Micro-optimisation: We will recurse only when the next boy is deeper than the current boy in graph levels
            if(dist[nextBoy] == dist[boy] + 1) {
                if(dfs(nextBoy)) {      // Perform path flipping - flipping all matched edges with unmatched ones
                    // Note: Maintains a bijection, boy is matched to girl and girl is matched to boy (double booking)
                    boyMatching[boy] = girl;        // update boy matching
                    girlMatching[girl] = boy;       // update girl matching
                    return true;
                }
            }
        }
        // Micro-optimisation: We set dist to INF, greater than dist[boy]+1, so that we don't 
        // traverse the path from current boy again, since no augmenting path found in this layering
        dist[boy] = INF;
        return false;
    }
}
