import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPHard {
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
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
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
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
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
        }, "NP-Hard-Problem-(https://codeforces.com/problemset/problem/687/A)", 1 << 26);
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
        final int n = fr.nextInt(), m = fr.nextInt();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for(int i = 0; i < m; i++) {
            final int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
            g.get(v).add(u);
        }
        solve(n);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static List<List<Integer>> g;

    public static void solve(final int n) {
        boolean vis[] = new boolean[n+1];
        int color[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            if(!vis[i] && !bipartite(i, vis, color)) {
                output.append("-1");
                return;
            }
        // counting red and white cells
        long red = Arrays.stream(color).filter(x -> x == 1).count(), white = Arrays.stream(color).filter(x -> x == 2).count();
        output.append(red).append("\n");
        for(int i = 1; i <= n; i++)
            if(color[i] == 1)
                output.append(i).append(" ");
        output.append("\n").append(white).append("\n");
        for(int j = 1; j <= n; j++)
            if(color[j] == 2)
                output.append(j).append(" ");
    }

    private static boolean bipartite(final int source, final boolean vis[], final int color[]) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.offer(source);
        boolean f = true;
        while(!q.isEmpty()) {
            int sz = q.size();
            for(int i = 0; i < sz; i++) {
                int node = q.poll();
                vis[node] = true;
                color[node] = f ? 1 : 2;
                for(int neighbor : g.get(node)) {
                    // checking if bipartite
                    if(color[neighbor] == color[node])
                        return false;
                    if(!vis[neighbor]) {    // else add node into the queue
                        vis[neighbor] = true;
                        q.offer(neighbor);
                    }
                }
            }
            f = !f;
        }
        return true;
    }
}
