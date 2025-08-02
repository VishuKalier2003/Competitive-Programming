
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class P15CourseSchedule {
    public static class FastReader {        // FastReader class for quick input reading
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {     // Reading at current buffer index
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {       // reading int
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {     // reading long
            long x = 0L, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
        }

        public String next() throws IOException {       // reading string without whitespace
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

        public String nextLine() throws IOException {   // reading string (whitespace inclusive)
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

    public static class FastWriter {        // FastWriter class for quick output printing
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
        }, "Course-Schedule",
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
        final int n = fr.nextInt(), m = fr.nextInt();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        int inDegree[] = new int[n + 1];
        for (int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
            inDegree[v]++;
        }
        fw.attachOutput(solve(n, m, inDegree));
        fw.printOutput();
    }

    private static List<List<Integer>> g;

    // Note: Algorithm for topological sorting
    public static StringBuilder solve(final int n, final int m, final int inDegree[]) {
        final ArrayDeque<Integer> q = new ArrayDeque<>();       // Queue for processing nodes
        int count = 0;      // count and output as variables for printing
        final StringBuilder output = new StringBuilder();
        // Initially checking all pendant nodes (nodes with inDegree as 0)
        for(int i = 1; i <= n; i++)
            if(inDegree[i] == 0) {      // If degree becomes 0, then that node can be reached at current
                q.add(i);
                output.append(i).append(" ");
                count++;
            }
        // Performing topological operation till the queue becomes empty
        while(!q.isEmpty()) {
            int node = q.poll();        // Polling head
            // For each node that is the successor (next task) of the current node
            for(int child : g.get(node)) {
                inDegree[child]--;      // Info: Remove the edge, hence the inDegree
                if(inDegree[child] == 0) {      // If degree becomes 0, then that node can be reached at current
                    q.add(child);
                    output.append(child).append(" ");
                    count++;
                }
            }
        }
        if(count != n)      // If all nodes cannot be processed then the topological sort cannot be completed
            return new StringBuilder().append("IMPOSSIBLE");
        return output;
    }
}