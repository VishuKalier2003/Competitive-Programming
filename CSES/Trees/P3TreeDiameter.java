import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class P3TreeDiameter {
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
        tree = new ArrayList<>();
        final int n = fast.nextInt();
        tree.add(new ArrayList<>());
        for(int i = 1; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int node1 = fast.nextInt(), node2 = fast.nextInt();
            tree.get(node1).add(node2);
            tree.get(node2).add(node1);
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        output.append(solve(n));
        writer.write(output+"");
        writer.flush();
    }

    public static List<List<Integer>> tree;

    public static int solve(final int n) {
        return bfs(bfs(1, new boolean[n+1])[0], new boolean[n+1])[1]-1;
    }

    public static int[] bfs(int source, boolean visited[]) {
        Queue<Integer> q = new LinkedList<>();
        q.add(source);
        int leaf = 1;
        int level = 0;
        while(!q.isEmpty()) {
            int size = q.size();
            level++;
            for(int i = 0; i < size; i++) {
                int node = q.poll();
                visited[node] = true;
                for(int child : tree.get(node))
                    if(!visited[child]) {
                        q.add(child);
                        leaf = child;
                    }
            }
        }
        return new int[]{leaf, level};
    }
}
