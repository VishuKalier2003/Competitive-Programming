// https://codeforces.com/problemset/problem/1946/C

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeCutting {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
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
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
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

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {
            }
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(
                null, () -> {
                    try {
                        callMain(args);
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                },
                "edgy-trees",
                1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException IE) {
            IE.getLocalizedMessage();
        }
    }

    public static List<List<Integer>> tree;

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fast.nextInt();
        while (t-- > 0) {
            final int n = fast.nextInt(), k = fast.nextInt();
            tree = new ArrayList<>();
            for (int i = 0; i <= n; i++)
                tree.add(new ArrayList<>());
            for (int i = 0; i < n - 1; i++) {
                int u = fast.nextInt(), v = fast.nextInt();
                tree.get(u).add(v);
                tree.get(v).add(u);
            }
            output.append(solve(n, k)).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static int comp;
    public static int size[];

    public static int solve(final int n, final int k) {
        int l = 1, r = n - k, ans = l;
        size = new int[n + 1];
        while (l <= r) {
            int mid = (l + r) >>> 1;
            if (kCuts(k, mid)) {
                ans = mid;
                l = mid + 1;
            } else
                r = mid - 1;
        }
        return ans;
    }

    public static boolean kCuts(final int k, final int treeSize) {
        comp = 0;
        Arrays.fill(size, 0);
        dfs(1, 0, treeSize);
        return comp >= k + 1;
    }

    public static int dfs(int root, int parent, int treeSize) {
        size[root] += 1;
        for (int child : tree.get(root)) {
            if (child != parent) {
                int s = dfs(child, root, treeSize);
                size[root] += s;
            }
        }
        // Error we must prune after we have taken all children of a current node
        if (size[root] >= treeSize) {
            comp++;
            return 0;
        }
        return size[root];
    }
}
