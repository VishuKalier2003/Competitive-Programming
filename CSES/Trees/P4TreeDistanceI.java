import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class P4TreeDistanceI {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
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

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        tree.add(new ArrayList<>());
        for(int i = 1; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {     // Imp Technique- convert tree to graph for upward traversals
            int node1 = fast.nextInt(), node2 = fast.nextInt();
            tree.get(node1).add(node2);
            tree.get(node2).add(node1);
        }
        solve(n);
    }

    static List<List<Integer>> tree;

    static int leftDist[], rightDist[];

    public static void solve(final int n) throws IOException {
        // Imp Technique- start bfs from extreme nodes (left and right) to find the distance from those nodes and use it to compute the max distance since the farthest nodes will have the max distance from any node, we need to find the max of left most and right most node
        leftDist = new int[n+1];
        rightDist = new int[n+1];
        boolean visited[] = new boolean[n+1];       // Micro- Create a single boolean array and use it, do not create multiple
        int rightmost = bfs(1, visited, leftDist);
        Arrays.fill(visited, false);
        int leftmost = bfs(rightmost, visited, rightDist);
        Arrays.fill(visited, false);
        bfs(leftmost, visited, leftDist);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        for(int i = 1; i <= n; i++)
            output.append(Math.max(leftDist[i], rightDist[i])).append(" ");
        writer.write(output.toString());        // Micro- use toString() function instead of output + ""
        writer.flush();     // Micro- flush() instead of close()
    }

    public static int bfs(int source, boolean visited[], int dist[]) {
        Queue<Integer> q = new ArrayDeque<>();      // Micro- Use ArrayDeque which is faster
        q.add(source);
        int level = 0, leaf = source;       // Micro- Mark leaf as source for better control
        while(!q.isEmpty()) {
            int s = q.size();
            for(int i = 0; i < s; i++) {
                int node = q.poll();
                leaf = node;
                visited[node] = true;
                dist[node] = level;
                for(int child : tree.get(node))
                    if(!visited[child])
                        q.add(child);
            }
            level++;
        }
        return leaf;
    }
}
