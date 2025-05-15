// https://codeforces.com/problemset/problem/979/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class KuroWalk {
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

    private static List<List<Integer>> tree;        // Tree data structure
    private static int garden[];        // storing the attribute of each garden (whether closer to beeptopia or florisa)
    private static boolean visited[];           // Marking visited gardens during bfs

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final long n = fast.nextInt();
        final int f = fast.nextInt(), b = fast.nextInt();
        tree = new ArrayList<>();
        garden = new int[(int)n+1];
        visited = new boolean[(int)n+1];
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 0; i < n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        System.out.print(solve(n, f, b));
    }

    public static long solve(final long n, final int f, final int b) {
        bfs(f, 1, b);       // Source as florisa garden, excluding beetopia
        bfs(b, 2, f);       // Source as beetopia garden, excluding florisa
        long florisa = 0, beetopia = 0;
        for(int i = 1; i <= n; i++) {
            if(garden[i] == 1)
                florisa++;
            if(garden[i] == 2)
                beetopia++;
        }
        // The invalid ways are reaching beetopia after florisa (subtree nodes of florisa * subtree nodes of beetopia)
        return (n*(n-1)) - (florisa*beetopia);
    }

    public static void bfs(int source, int value, int special) {
        Queue<Integer> q = new ArrayDeque<>();
        Arrays.fill(visited, false);
        q.add(source);
        while(!q.isEmpty()) {
            int s = q.size();
            for(int i = 0; i < s; i++) {
                int node = q.poll();
                visited[node] = true;
                garden[node] += value;
                for(int child : tree.get(node))
                    if(!visited[child] && child != special)
                        q.add(child);
            }
        }
    }
}
