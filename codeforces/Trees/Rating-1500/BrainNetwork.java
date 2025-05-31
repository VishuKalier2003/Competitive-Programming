// https://codeforces.com/problemset/problem/690/C2

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrainNetwork {
    public static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static void main(String[] args) {
        Thread b1500 = new Thread(
                null, () -> {
                    try {
                        callMain(args);
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                },
                "brain-network",
                1 << 26);
        b1500.start();
        try {
            b1500.join();
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        }
    }

    private static List<List<Integer>> tree;

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), m = fast.nextInt();
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 0; i < m; i++) {
            final int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        solve(n);
    }

    public static void solve(final int n) {
        boolean v[] = new boolean[n + 1];
        final StringBuilder output = new StringBuilder();
        output.append(bfs(bfs(1, v)[0], v)[1] - 1);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static int[] bfs(int s, boolean v[]) {
        final ArrayDeque<Integer> q = new ArrayDeque<>();
        q.offer(s);
        int lvl = 0, leaf = s;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int node = q.poll();
                v[node] = true;
                for (int child : tree.get(node))
                    if (!v[child]) {
                        q.offer(child);
                        leaf = child;
                    }
            }
            lvl++;
        }
        Arrays.fill(v, false);
        return new int[] { leaf, lvl };
    }
}
