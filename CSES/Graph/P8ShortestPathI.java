import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class P8ShortestPathI {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer);       // Stores the entire buffer data in read
                if(len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while((c = read()) <= ' ')      // While whitespace is not provided
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long readLong() throws IOException {
            long x = 0l, c;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
            int c;
            while((c = read()) <= ' ')      // Read until whitespace
                if(c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
            } while((c = read()) > ' ');
            return sb.toString();
        }

        public String readLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if(c < 0)
                return null;
            while(c != '\n' && c >= 0)
                if(c != '\r')
                    sb.append((char)c);
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        Thread t = new Thread(null, () -> {
            try {callMain(args);}
            catch(IOException e) {e.getLocalizedMessage();}
        }, "dijkstra",
        1 << 26);
        t.start();
        try {t.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    public static class Node {
        final int data;
        final long weight;

        public Node(int d, long w) {
            this.data = d; this.weight = w;
        }

        public int getData() {return this.data;}
        public long getWeight() {return this.weight;}
    }

    public static List<List<Node>> g;

    public static void callMain(String args[]) throws IOException {
        g = new ArrayList<>();
        FastReader fr = new FastReader();
        final int n = fr.readInt(), m = fr.readInt();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for(int i = 0; i < m; i++) {
            int u = fr.readInt(), v = fr.readInt(), w = fr.readInt();
            g.get(u).add(new Node(v, w));
        }
        solve(n, m);
    }

    public static void solve(final int n, final int m) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        // Define the comparator, to compare by weights
        PriorityQueue<Node> maxHeap = new PriorityQueue<>((a, b) -> {
            if(a.weight == b.weight)
                return Integer.compare(a.data, b.data);
            return Long.compare(a.weight, b.weight);
        });
        long dist[] = new long[n+1];        // distance array defined
        Arrays.fill(dist, Long.MAX_VALUE);      // fill with MAX value
        maxHeap.add(new Node(1, 0));        // add node to heap as start node s with distance 0
        dist[1] = 0L;       // set the distance of the start node as 0
        while(!maxHeap.isEmpty()) {
            Node node = maxHeap.poll();
            if(dist[node.data] < node.weight)
                continue;
            for(Node neighbor : g.get(node.data)) {
                // If current node distance + edge weight of next node is lesser than distance of neigbhor, that means we can reach the neighbor through the current node more quickly (lesser resources)
                if(dist[node.data] + neighbor.weight < dist[neighbor.data]) {
                    dist[neighbor.data] = dist[node.data] + neighbor.weight;        // update neighbor
                    maxHeap.add(new Node(neighbor.data, dist[neighbor.data]));      // add the updated data into the heap
                }
            }
        }
        final StringBuilder output = new StringBuilder();
        for(int i = 1; i <= n; i++)
            output.append(dist[i]).append(" ");
        pw.write(output.toString());
        pw.flush();
    }
}
