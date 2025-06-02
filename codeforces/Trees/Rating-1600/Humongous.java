import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Humongous {
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

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "humongous", 1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static List<List<Integer>> tree;

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final int n = fast.nextInt();
            List<int[]> ranges = new ArrayList<>();
            ranges.add(new int[] { -1, -1 });
            for (int i = 0; i < n; i++)
                ranges.add(new int[] { fast.nextInt(), fast.nextInt() });
            tree = new ArrayList<>();
            for (int i = 0; i <= n; i++)
                tree.add(new ArrayList<>());
            for (int i = 0; i < n - 1; i++) {
                int u = fast.nextInt(), v = fast.nextInt();
                tree.get(u).add(v);
                tree.get(v).add(u);
            }
            output.append(solve(n, ranges)).append("\n");
        }
        writer.write(output.toString());
        writer.flush();
    }

    public static long dp[][];

    public static long solve(final int n, final List<int[]> ranges) {
        // n+1 for nodes, 2 is the decision of either left(0) or right(1) value of the
        // segment of current node
        dp = new long[n + 1][2];
        dfs(1, 0, ranges);
        return Math.max(dp[1][0], dp[1][1]);
    }

    public static void dfs(int root, int parent, List<int[]> ranges) {
        dp[root][0] = 0l;
        dp[root][1] = 0l;
        long lRoot = ranges.get(root)[0], rRoot = ranges.get(root)[1];
        for (int child : tree.get(root))
            if (child != parent) {
                dfs(child, root, ranges);
                long lChild = ranges.get(child)[0], rChild = ranges.get(child)[1];
                // If root picks left, then child can either pick left or right
                long pL = Math.max(
                        dp[child][0] + Math.abs(lRoot - lChild), // pick left child
                        dp[child][1] + Math.abs(lRoot - rChild) // pick right child
                );
                dp[root][0] += pL;
                // If root picks right, then child can either pick left or right
                long pR = Math.max(
                        dp[child][0] + Math.abs(rRoot - lChild), // pick left child
                        dp[child][1] + Math.abs(rRoot - rChild) // pick right child
                );
                dp[root][1] += pR;
            }
    }
}
