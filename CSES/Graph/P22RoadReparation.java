import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class P22RoadReparation {
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
        }, "Road-Reparation",
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
        final int n = fr.nextInt(), m = fr.nextInt();
        g = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        // Adding bidirectional edges with weights
        for (int j = 0; j < m; j++) {
            int u = fr.nextInt(), v = fr.nextInt();
            long w = fr.nexLong();
            g.get(u).add(new Edge(v, w));
            g.get(v).add(new Edge(u, w));
        }
        fw.attachOutput(solve(n));
        fw.printOutput();
    }

    // Micro-optimisation: can use record here for production ready code
    public static class Edge {
        final int end;
        final long weight;

        public Edge(int end, long w) {
            this.end = end;
            this.weight = w;
        }

        public int getEnd() {
            return end;
        }

        public long getWeight() {
            return weight;
        }
    }

    public static List<List<Edge>> g;

    // Note: Using Prim's algorithm for converting Graph to a Minimum Spanning Tree (MST)
    public static StringBuilder solve(final int n) {
        // Info: MinHeap defined to take edges as per increasing order of the weights
        PriorityQueue<Edge> minHeap = new PriorityQueue<>((a, b) -> Long.compare(a.weight, b.weight));
        // Micro-optimisation: use boolean array instead of set since set performing hashing which takes time
        boolean taken[] = new boolean[n+1];
        long sum = 0L;
        int count = 0;      // counting the number of added edges into the tree
        taken[1] = true;
        for(Edge e : g.get(1))      // Adding all outgoing edges from node 1 (source)
            minHeap.offer(e);
        while (!minHeap.isEmpty()) {
            Edge e = minHeap.poll();        // Extracting lowest edge from the heap
            if(taken[e.end])    // Info: If that node is already taken in MST then skip it
                continue;
            taken[e.end] = true;        // Marking the edge end as true, if not taken in MST
            sum += e.weight;
            count++;
            // Micro-optimisation: If prematurely the edges count become n-1, we need not process the rest
            if(count == n-1)
                break;
            // Info: For the current node, add all edges into the heap whose ends are not taken
            for(Edge neighbor : g.get(e.end))
                if(!taken[neighbor.end])
                    minHeap.offer(neighbor);
        }
        if(count < n-1)     // If the graph is disconnected
            return new StringBuilder().append("IMPOSSIBLE");
        return new StringBuilder().append(sum);
    }
}
