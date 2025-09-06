import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LinkCutCentroids {
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
        }, "Link-Cut-Centroids-(https://codeforces.com/problemset/problem/1406/C)", 1 << 26);
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
        int t = fr.nextInt();
        while(t-- > 0) {
            final int n = fr.nextInt();
            g = new ArrayList<>();
            for(int i = 0; i <= n; i++)
                g.add(new ArrayList<>());
            for(int i = 0; i < n-1; i++) {
                final int u = fr.nextInt(), v = fr.nextInt();
                g.get(u).add(v);
                g.get(v).add(u);
            }
            solve(n);
        }
        fw.attachOutput(output);
        fw.printOutput();
    }

    public static final StringBuilder output = new StringBuilder();
    public static List<List<Integer>> g;

    public static void solve(final int n) {
        int subtree[] = new int[n+1];
        subtree(1, -1, subtree);
        int c1 = centroid(1, -1, n, subtree), c2 = -1;
        for(int neighbor : g.get(c1)) {
            if(subtree[neighbor] == n/2) {
                c2 = neighbor;
                break;
            }
        }
        if(c2 == -1) {
            int next = g.get(c1).get(0);
            output.append(c1).append(" ").append(next).append("\n");
            output.append(c1).append(" ").append(next).append("\n");
            return;
        }
        int next = 0;
        for(int neighbor : g.get(c1))
            if(neighbor != c2) {
                next = neighbor;
                break;
            }
        output.append(c1).append(" ").append(next).append("\n");
        output.append(c2).append(" ").append(next).append("\n");
    }

    public static void subtree(int root, int parent, final int subtree[]) {
        subtree[root] = 1;
        for(int child : g.get(root))
            if(child != parent) {
                subtree(child, root, subtree);
                subtree[root] += subtree[child];
            }
    }

    public static int centroid(int root, int parent, final int n, final int subtree[]) {
        for(int child : g.get(root)) {
            if(child != parent) {
                if(subtree[child] > n/2)
                    return centroid(child, root, n, subtree);
            }
        }
        return root;
    }
}
