import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Dijkstra {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
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
        Thread graph1900 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Dijkstra?", 1 << 26);
        graph1900.start();
        try {
            graph1900.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static class FastWriter {
        public StringBuilder builder;
        public PrintWriter pr;

        public FastWriter() throws FileNotFoundException {
            this.builder = new StringBuilder();
            this.pr = new PrintWriter(new OutputStreamWriter(System.out));
        }

        public void add(String s) {
            this.builder.append(s);
        }

        public void flushMemory() {
            this.pr.write(builder.toString());
            this.pr.flush();
        }
    }

    public static List<List<Node>> g;

    public static class Node {
        public final int data;
        public final long weight;

        public Node(int d, long w) {this.data = d; this.weight = w;}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        g = new ArrayList<>();
        final int n = fr.nextInt(), m = fr.nextInt();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for(int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt(), w = fr.nextInt();
            g.get(u).add(new Node(v, w));
            g.get(v).add(new Node(u, w));
        }
        FastWriter fw = new FastWriter();
        fw.add(solve(n).toString());
        fw.flushMemory();
    }

    public static StringBuilder solve(final int n) {
        long dist[] = new long[n+1];
        Arrays.fill(dist, Long.MAX_VALUE);
        PriorityQueue<Node> maxHeap = new PriorityQueue<>((a, b) -> {
            if(a.weight == b.weight)
                return Integer.compare(a.data, b.data);
            return Long.compare(b.weight, a.weight);
        });
        dist[1] = 0;
        maxHeap.add(new Node(1, 0L));
        while(!maxHeap.isEmpty()) {
            Node node = maxHeap.poll();
            if(dist[node.data] < node.weight)
                continue;
            for(Node neighbor : g.get(node.data)) {
                if(dist[node.data] + neighbor.weight < dist[neighbor.data]) {
                    dist[neighbor.data] = dist[node.data] + neighbor.weight;
                    maxHeap.add(new Node(neighbor.data, dist[neighbor.data]));
                }
            }
        }
        final StringBuilder output = new StringBuilder();
        for(int i = 1; i <= n; i++)
            output.append(dist[i]).append(" ");
        return output;
    }
}
