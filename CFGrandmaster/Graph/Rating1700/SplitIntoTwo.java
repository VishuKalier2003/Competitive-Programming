import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SplitIntoTwo {
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
        }, "https://codeforces.com/problemset/problem/1702/E",
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
        int t = fr.nextInt();
        while (t-- > 0) {
            final int n = fr.nextInt();
            List<int[]> queries = new ArrayList<>();
            for (int i = 0; i < n; i++)
                queries.add(new int[] { fr.nextInt(), fr.nextInt() });
            fw.attachOutput(solve(n, queries));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final List<int[]> queries) {
        // Graph adjacency list
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] degree = new int[2 * n + 5]; // numbers can go up to 2*n in worst case

        // Build graph & check immediate fail conditions
        for (int[] q : queries) {
            int u = q[0], v = q[1];
            if (u == v) // self-loop
                return new StringBuilder("No\n");
            degree[u]++;
            degree[v]++;
            if (degree[u] > 2 || degree[v] > 2) // degree constraint
                return new StringBuilder("No\n");
            graph.computeIfAbsent(u, _ -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, _ -> new ArrayList<>()).add(u);
        }

        // Bipartite check via BFS
        Map<Integer, Integer> color = new HashMap<>();
        for (int node : graph.keySet()) {
            if (!color.containsKey(node)) {
                Queue<Integer> q = new ArrayDeque<>();
                q.add(node);
                color.put(node, 0);
                while (!q.isEmpty()) {
                    int cur = q.poll();
                    for (int nei : graph.get(cur)) {
                        if (!color.containsKey(nei)) {
                            color.put(nei, color.get(cur) ^ 1);
                            q.add(nei);
                        } else if (color.get(nei).equals(color.get(cur))) {
                            return new StringBuilder("No\n");
                        }
                    }
                }
            }
        }
        return new StringBuilder("Yes\n");
    }

}
