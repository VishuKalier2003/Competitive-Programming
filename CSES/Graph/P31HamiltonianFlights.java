import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class P31HamiltonianFlights {
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
        }, "Hamiltonian-Flights",
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
        // Info: The dp table stores the dp[i][j] as the ways to reach node j when we take nodes of set i
        final long dp[][] = new long[1 << n][n+1];      // The set is represented by bitmask
        for(int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
        }
        fw.attachOutput(solve(n, dp));
        fw.printOutput();
    }

    private static List<List<Integer>> g;
    private static final int MOD = 1_000_000_007;

    public static StringBuilder solve(final int n, final long dp[][]) {
        dp[1][1] = 1L;      // base case
        // For every mask (entry or possible path or subset of nodes) in the graph, it means the taken subset
        for(int mask = 1; mask < (1 << n); mask++) {
            if((mask & 1) != 1)     // Micro-optimisation: if path does not start from 1, skip it
                continue;
            for(int node = 1; node <= n; node++) {
                // Micro-optimisation: if current mask and node does not cause an increase (is disconnected), we skip it
                if(dp[mask][node] == 0)     
                    continue;
                for(int neighbor : g.get(node)) {
                    int bit = 1 << (neighbor-1);        // Info: Extracting the neighbor bit
                    // If that neighbor is not yet taken into the current subset, but needs to be taken
                    if((mask & bit) == 0) {
                        int newMask = mask | bit;       // define the new mask
                        dp[newMask][neighbor] = (dp[newMask][neighbor] + dp[mask][node]) % MOD;     // perform the modulus operation
                    }
                }
            }
        }
        // Info: The best output would be when we take all nodes as subset and ways to reach node n from node 1
        return new StringBuilder().append(dp[(1 << n)-1][n]);
    }
}
