import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EdgyTrees {
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
        final int n = fast.nextInt(), k = fast.nextInt();
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 0; i < n - 1; i++) {
            int u = fast.nextInt(), x = fast.nextInt(), c = fast.nextInt();
            if (c == 0) {
                tree.get(u).add(x);
                tree.get(x).add(u);
            }
        }
        output.append(solve(n, k));
        writer.write(output.toString());
        writer.flush();
    }

    public static final int MOD = 1_000_000_007;
    public static boolean v[];
    public static int count;

    public static long solve(final int n, final int k) {
        long total = exp(n, k), rem = 0l;
        v = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            if (!v[i]) {
                count = 0;
                dfs(i);
                rem += exp(count, k);
            }
        }
        rem %= MOD;
        return (total - rem + MOD) % MOD;
    }

    public static void dfs(int root) {
        v[root] = true;
        count++;
        for (int child : tree.get(root))
            if (!v[child])
                dfs(child);
    }

    public static long exp(long a, long b) {
        long res = 1L;
        while (b > 0) {
            if ((b & 1) == 1)
                res = (res * a) % MOD;
            a = (a * a) % MOD;
            b >>= 1;
        }
        return res;
    }

}
