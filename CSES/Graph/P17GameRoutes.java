import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class P17GameRoutes {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {     // reading byte
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

        public int nextInt() throws IOException {       // reading int
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

        public long nexLong() throws IOException {      // reading long
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

        public String next() throws IOException {           // reading string (whitespace exclusive)
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

        public String nextLine() throws IOException {       // reading string (whitespace inclusive)
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

    // Micro-optimisation: creating new thread for allocating larger memory hence not hitting MLE (Memory Limit Exceeded)
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Game-Routes",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables, hence immutable and are automatically stored in cache redcuing call time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        g = new ArrayList<>();
        final int n = fr.nextInt(), m = fr.nextInt();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        final int edge[] = new int[2], inDegree[] = new int[n+1];
        final long dp[] = new long[n+1];
        for (int j = 0; j < m; j++) {
            edge[0] = fr.nextInt(); // node u
            edge[1] = fr.nextInt(); // node v
            g.get(edge[0]).add(edge[1]);
            inDegree[edge[1]]++;
        }
        fw.attachOutput(solve(n, dp, inDegree));
        fw.printOutput();
    }

    private static List<List<Integer>> g;
    private static final int MOD = 1_000_000_007;

    public static StringBuilder solve(final int n, final long dp[], final int inDegree[]) {
        dp[1] = 1L;
        // Micro-optimisation: using ArrayDeque which is faster than normal Queue
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for(int i = 1; i <= n; i++)
            if(inDegree[i] == 0)
                q.add(i);
        while(!q.isEmpty()) {
            int node = q.poll();
            for(int child : g.get(node)) {
                dp[child] = (dp[child] + dp[node]);
                // Micro-optimisation: subtracting instead of dividing, hence saving time
                if(dp[child] >= MOD)
                    dp[child] -= MOD;
                // Micro-optimisation: pre-decrement operation, reducing code size
                if(--inDegree[child] == 0)
                    q.add(child);
            }
        }
        return new StringBuilder().append(dp[n]);
    }
}
