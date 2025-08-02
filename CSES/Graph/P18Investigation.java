import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class P18Investigation {
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

    // Micro-optimisation: creating new thread for allocating larger memory hence
    // not hitting MLE (Memory Limit Exceeded)
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Investigation",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables, hence immutable and are
    // automatically stored in cache redcuing call time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        g = new ArrayList<>();
        final int n = fr.nextInt(), m = fr.nextInt();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        final int edge[] = new int[3];
        for (int i = 0; i < m; i++) {
            edge[0] = fr.nextInt();
            edge[1] = fr.nextInt();
            edge[2] = fr.nextInt();
            g.get(edge[0]).add(new Node(edge[1], edge[2]));
        }
        final long dist[] = new long[n + 1], maxF[] = new long[n + 1], minF[] = new long[n + 1],
                count[] = new long[n + 1];
        fw.attachOutput(solve(n, m, dist, maxF, minF, count));
        fw.printOutput();
    }

    private static List<List<Node>> g;
    private static final long INF = Long.MAX_VALUE / 2, INF_NEG = 2 * Long.MIN_VALUE, MOD = 1_000_000_007;

    // Micro-optimisation: can use record here instead of class for reducing
    // boilerplate
    private static class Node {
        private final int data;
        private final long cost;

        public Node(int node, long c) {
            this.data = node;
            this.cost = c;
        }

        // Getter functions
        public int getNode() {
            return data;
        }

        public long getCost() {
            return cost;
        }
    }

    // Note: This is dijkstra algorithm with state augmentation
    public static StringBuilder solve(final int n, final int m, final long dist[], final long maxF[], final long minF[], final long count[]) {
        Arrays.fill(dist, INF);     // Filling initial values into the arrays
        Arrays.fill(maxF, INF_NEG);
        Arrays.fill(minF, INF);
        // Priority Queue for handling edges first with smaller weight
        PriorityQueue<Node> minHeap = new PriorityQueue<>((a, b) -> Long.compare(a.cost, b.cost));      // comparing by smaller weight
        dist[1] = maxF[1] = minF[1] = 0L; count[1] = 1L;
        minHeap.add(new Node(1, 0L));       // initialising heap by source 1, with distance 0
        while(!minHeap.isEmpty()) {
            Node node = minHeap.poll();
            if(dist[node.data] < node.cost)     // If the edge is larger than the current distance, skip it
                continue;
            // Iterating for each edge (neighbor) of the current node
            for(Node neighbor : g.get(node.data)) {
                long relaxedCost = dist[node.data] + neighbor.cost;
                // Info: If a better cost path is found
                if(relaxedCost < dist[neighbor.data]) {
                    dist[neighbor.data] = relaxedCost;      // update the neighbor data
                    count[neighbor.data] = count[node.data];        // store the count (exact same as previous node count)
                    // A new path is found, hence we will increment the ways by 1
                    minF[neighbor.data] = minF[node.data] + 1L;
                    maxF[neighbor.data] = maxF[node.data] + 1L;
                    // Info: Adding the lower distance edge into the minHeap
                    minHeap.offer(new Node(neighbor.data, dist[neighbor.data]));
                // Info: If the cost is exactly same as the distance, then we have found another way to reach the neighbor with the current cost
                } else if(relaxedCost == dist[neighbor.data]) {
                    // Update the count of neighbor by adding the count (no. of ways) of node
                    count[neighbor.data] = count[neighbor.data] + count[node.data];
                    if(count[neighbor.data] >= MOD)     // Micro-optimisation: performing subtraction instead of division
                        count[neighbor.data] -= MOD;
                    // Update the arrays as the min and max of the ways
                    minF[neighbor.data] = Math.min(minF[neighbor.data], minF[node.data] + 1L);
                    maxF[neighbor.data] = Math.max(maxF[neighbor.data], maxF[node.data] + 1L);
                }
            }
        }
        return new StringBuilder().append(dist[n]).append(" ").append(count[n]).append(" ").append(minF[n]).append(" ").append(maxF[n]);
    }
}
