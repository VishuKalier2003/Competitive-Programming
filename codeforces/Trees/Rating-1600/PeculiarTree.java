// https://codeforces.com/problemset/problem/930/A

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class PeculiarTree {
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

    public static List<List<Integer>> tree;

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 2; i <= n; i++)
            tree.get(fast.nextInt()).add(i);
        solve(n);
    }

    public static void solve(final int n) {
        int apples[] = new int[n+1];
        bfs(1, apples);
        System.out.print(Arrays.stream(apples).sum());
    }

    public static void bfs(final int source, final int apples[]) {
        Queue<Integer> q = new ArrayDeque<>();
        q.add(source);
        int d = 0;
        while(!q.isEmpty()) {
            d++;
            int s = q.size();
            for(int i = 0; i < s; i++) {
                int node = q.poll();
                for(int child : tree.get(node))
                    q.add(child);
            }
            apples[d] += s % 2;
        }
    }
}
