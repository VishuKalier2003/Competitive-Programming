import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class DijsktraNode {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Dijkstra-Node", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public record Node(int data, int weight) {}
    public static List<List<Node>> g;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        final int n = fr.nextInt(), m = fr.nextInt();
        g = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for(int j = 0; j < m; j++) {
            int n1 = fr.nextInt(), n2 = fr.nextInt(), w = fr.nextInt();
            g.get(n1).add(new Node(n2, w));
            g.get(n2).add(new Node(n1, w));
        }
        dijkstra(n, m);
        wr.write(output.toString());
        wr.flush();
    }

    public static void dijkstra(final int n, final int m) {
        int dist[] = new int[n+1];
        Arrays.fill(dist, Integer.MAX_VALUE);       // fill distances
        // define minHeap
        PriorityQueue<Node> minHeap = new PriorityQueue<>((n1, n2) -> Integer.compare(n1.weight, n2.weight));
        final int source = 1;
        dist[source] = 0;       // initialize dist of source to 0 and add to heap
        minHeap.add(new Node(source, 0));
        while(!minHeap.isEmpty()) {     // poll while empty
            Node node = minHeap.poll();
            if(node.weight > dist[node.data])   // skip condition when weight greater than distance
                continue;
            for(Node neighbor : g.get(node.data)) {     // travel to all neighbors
                // If adding the edge can reduce distance, update distance and add into the heap
                if(dist[node.data] + neighbor.weight < dist[neighbor.data]) {
                    dist[neighbor.data] = dist[node.data] + neighbor.weight;
                    minHeap.add(new Node(neighbor.data, dist[neighbor.data]));
                }
            }
        }
    }
}
