import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Spiders {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }
    public static void main(String args[]) {
        FastReader fast = new FastReader();
        final int spiders = fast.nextInt();     // N graphs (or trees)
        int totalLength = 0;
        for(int i = 0; i < spiders; i++) {
            final int threads = fast.nextInt();
            Map<Integer, Set<Integer>> spider = new HashMap<>();
            for(int j = 1; j <= threads; j++)
                spider.put(j, new HashSet<>());
            for(int j = 0; j < threads-1; j++) {
                int node1 = fast.nextInt(), node2 = fast.nextInt();     // Provided connections for each tree
                spider.get(node1).add(node2);
                spider.get(node2).add(node1);
            }
            int length = diameter(threads, spider);     // Find the max distance between any two nodes (those nodes can be anchors)
            if(length > 0)
                length--;   // base case when a single node in a tree
            totalLength += length;
        }
        System.out.print(totalLength);
    }

    public static int diameter(final int n, final Map<Integer, Set<Integer>> graph) {
        // Perform a bfs starting at 1, reach the farthest node and start another bfs from the farthest node and return the length
        return bfs(bfs(1, n, graph)[0], n, graph)[1];       // IMP compute diameter of a tree in two pass
    }

    public static int[] bfs(final int source, final int n, final Map<Integer, Set<Integer>> graph) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        int last = source, level = 0;       // IMP- variables to store length and level at each instance
        boolean visited[] = new boolean[n+1];
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                int node = queue.poll();
                visited[node] = true;
                for(int neighbor : graph.get(node))
                    if(!visited[neighbor]) {
                        queue.add(neighbor);
                        last = neighbor;
                    }
            }
            level++;
        }
        return new int[]{last, level};      // First index as farthest node, and second as the level reached
    }
}
