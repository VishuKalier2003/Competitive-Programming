import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class P11Flights {
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
        }, "Dijkstra-k-Weighted",
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
        int n = fr.readInt(), m = fr.readInt();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for (int j = 0; j < m; j++) {
            int u = fr.readInt(), v = fr.readInt();
            long w = fr.readLong();
            g.get(u).add(new Node(v, w));
        }
        fw.attachOutput(solve(n));
        fw.printOutput();
    }

    public static List<List<Node>> g;

    // Some extra states are maintained in the Node class
    public static class Node {
        int data; // node index
        boolean state; // State storing whether we used the coupon on the current flight or not
        long weight; // edge weight

        public Node(int d, long w) {
            this.data = d;
            this.weight = w;
            this.state = false; // default set to false (haven't used the coupon)
        }

        public Node(int d, long w, boolean state) { // Parametrized constructor
            this.data = d;
            this.weight = w;
            this.state = state;
        }
    }

    public static final long INF = Long.MAX_VALUE / 2; // final long variable

    public static StringBuilder solve(final int n) {
        long dist[][] = new long[n + 1][2];
        for (long row[] : dist)
            Arrays.fill(row, INF); // filling the dist array
        // Comparator defined minHeap
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> Long.compare(a.weight, b.weight));
        minHeap.add(new Node(1, 0L, false)); // Initially we will start with city 0 without using coupon
        dist[1][0] = 0L; // update the dist for node 1
        while (!minHeap.isEmpty()) {
            Node node = minHeap.poll();
            int s = node.state ? 1 : 0;
            if (dist[node.data][s] < node.weight) // Prune the paths which will always increase cost
                continue;
            for (Node neighbor : g.get(node.data)) { // for each neighbor
                // If the coupon is not used (state as false)
                if (!node.state) {
                    // If it is possible to relax without using coupon
                    if (dist[node.data][0] + neighbor.weight < dist[neighbor.data][0]) {
                        dist[neighbor.data][0] = dist[node.data][0] + neighbor.weight;
                        minHeap.add(new Node(neighbor.data, dist[neighbor.data][0], false)); // Keep the coupon
                    }
                    // If it is possible to relax with using the coupon, reduce flight cost by half
                    if (dist[node.data][0] + neighbor.weight / 2L < dist[neighbor.data][1]) {
                        dist[neighbor.data][1] = dist[node.data][0] + (neighbor.weight / 2L);
                        minHeap.add(new Node(neighbor.data, dist[neighbor.data][1], true)); // Use the coupon
                    }
                    // If we have already used the coupon, we cannot go to the false state and can
                    // only transition to true
                } else {
                    // If it is possible to relax without any coupon
                    if (dist[node.data][1] + neighbor.weight < dist[neighbor.data][1]) {
                        dist[neighbor.data][1] = dist[node.data][1] + neighbor.weight;
                        minHeap.add(new Node(neighbor.data, dist[neighbor.data][1], true)); // Used the coupon earlier
                    }
                }
            }
        }
        return new StringBuilder().append(dist[n][1]); // Minimum cost from node 1 to node n
    }
}
