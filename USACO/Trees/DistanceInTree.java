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

public class DistanceInTree {
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
                    System.out.println(e.getLocalizedMessage());
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
        final int n = fast.nextInt(), k = fast.nextInt();
        tree = new ArrayList<>();
        tree.add(new ArrayList<>());
        for(int i = 1; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 0; i < n-1; i++) {
            int node1 = fast.nextInt(), node2 = fast.nextInt();
            tree.get(node1).add(node2);
            tree.get(node2).add(node1);
        }
        solve(n, k);
    }

    public static List<List<Integer>> tree;
    public static List<Integer> levelNodes;
    public static int leaf;
    public static Queue<Integer> q;

    public static void solve(final int n, final int k) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        if(k > n) {
            output.append(0);
            writer.write(output.toString());
            writer.flush();
            return;
        }
        boolean visited[] = new boolean[n+1];
        bfs(n, visited);
        Arrays.fill(visited, false);
        levelNodes = new ArrayList<>();
        q.clear();
        q.add(leaf);
        while(!q.isEmpty()) {
            int s = q.size();
            levelNodes.add(s);
            for(int i = 0; i < s; i++) {
                int node = q.poll();
                visited[node] = true;
                for(int child : tree.get(node))
                    if(!visited[child])
                        q.add(child);
            }
        }
        long result = 0L, s = levelNodes.size();
        for(int i = 0; i < s-k; i++) {
            long total = levelNodes.get(i) + levelNodes.get(i+k);
            result += (total*(total-1))/2;
        }
        output.append(result);
        writer.write(output.toString());
        writer.flush();
    }

    public static void bfs(final int n, final boolean visited[]) {
        q = new ArrayDeque<>();
        q.add(1);
        while(!q.isEmpty()) {
            int s = q.size();
            for(int i = 0; i < s; i++) {
                int node = q.poll();
                visited[node] = true;
                leaf = node;
                for(int child : tree.get(node))
                    if(!visited[child])
                        q.add(child);
            }
        }
    }
}
