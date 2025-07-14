import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class P13FlightRoutes {
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
        }, "Dijkstra-k-Shortest-Paths",
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
        int n = fr.readInt(), m = fr.readInt(), k = fr.readInt();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for (int j = 0; j < m; j++) {
            int u = fr.readInt(), v = fr.readInt();
            long w = fr.readLong();
            g.get(u).add(new Node(v, w));
        }
        fw.attachOutput(solve(n, k, 1));
        fw.printOutput();
    }

    public static List<List<Node>> g;

    // Node class to store data
    public static class Node {
        public int data;
        public long weight;

        public Node(int dataNode, long weight) {        // Contructor
            this.data = dataNode;
            this.weight = weight;
        }
    }

    public static final long INF = Long.MAX_VALUE / 2;      // final variable defined

    public static StringBuilder solve(final int n, final int k, final int source) {
        /** 1. We define the matrix and the user defined comparator heap */
        // distance matrix for each node and storing k reached paths (reaching or visiting node atmost k times)
        long dist[][] = new long[n + 1][k + 1];
        for (long row[] : dist)
            Arrays.fill(row, INF);
        // Custom comparator heap based on weights (smaller weights given more priority)
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> Long.compare(a.weight, b.weight));
        minHeap.add(new Node(source, 0L));       // start with source s with distance 0
        dist[source][0] = 0L;       // Mark the node distance
        // Array to store the number of visits for each node
        int count[] = new int[n + 1];
        List<Long> res = new ArrayList<>();
        /** 2. Performing relaxation via minHeap */
        while (!minHeap.isEmpty()) {
            Node node = minHeap.poll();
            if (count[node.data] >= k)      // If a node is reached more than k times, skip it
                continue;
            // Store the distance of ith node and jth visit as the weight computed so far
            dist[node.data][count[node.data]] = node.weight;
            count[node.data]++;         // update count of the current node
            // If node is the target node
            if (node.data == n) {
                res.add(node.weight);
                if (res.size() == k)        // If the target node is visited k times, exit the loop (prematurely)
                    break;
            }
            for (Node neighbor : g.get(node.data)) {
                // If the node is not reached k times
                if (count[neighbor.data] < k)
                    // Relax the path by adding the neighbor weight to the current node
                    minHeap.add(new Node(neighbor.data, node.weight + neighbor.weight));
            }
        }
        Collections.sort(res);      // sorting the answer as required by the question
        final StringBuilder sb = new StringBuilder();
        for(long r : res)
            sb.append(r).append(" ");
        return new StringBuilder().append(sb);
    }
}
