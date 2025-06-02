import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class InfectedTree {
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
                "tree-infection",
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
            final int n = fast.nextInt();
            tree = new ArrayList<>();
            for (int i = 0; i <= n; i++)
                tree.add(new ArrayList<>());
            for (int i = 0; i < n - 1; i++) {
                int u = fast.nextInt(), v = fast.nextInt();
                tree.get(u).add(v);
                tree.get(v).add(u);
            }
            output.append(solve(n)).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static int size[], dp[];

    public static int solve(final int n) {
        size = new int[n + 1];
        dp = new int[n + 1];
        if (tree.get(1).size() == 1)
            return n - 2;
        dfs(1, 0);
        dfsDp(1, 0);
        return dp[1];
    }

    public static int dfs(int root, int parent) {
        size[root] += 1;
        for (int child : tree.get(root))
            if (child != parent) {
                int s = dfs(child, root);
                size[root] += s;
            }
        return size[root];
    }

    public static void dfsDp(int root, int parent) {
        List<Integer> children = new ArrayList<>();
        for (int v : tree.get(root))
            if (v != parent)
                children.add(v);
        for (int child : children)
            dfsDp(child, root);
        if (children.isEmpty())
            dp[root] = 0;
        else if (children.size() == 1) {
            int c0 = children.get(0);
            dp[root] = size[c0] - 1;
        } else {
            int c0 = children.get(0), c1 = children.get(1);
            int option1 = dp[c0] + (size[c1] - 1);
            int option2 = dp[c1] + (size[c0] - 1);
            dp[root] = Math.max(option1, option2);
        }
    }
}
