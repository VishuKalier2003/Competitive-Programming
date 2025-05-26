// Ques - https://codeforces.com/problemset/problem/839/C

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Journey {
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
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10*x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {}
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
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
                } catch(IOException e) {
                    e.getLocalizedMessage();
                }
            },
            "journey",
            1 << 26
        );
        t1.start();
        try {
            t1.join();
        } catch(InterruptedException IE) {IE.getLocalizedMessage();}
    }

    private static List<List<Integer>> tree;

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 0; i < n-1; i++) {
            int u = fast.nextInt(), v = fast.nextInt();
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        solve(n);
    }

    public static void solve(final int n) {
        double result = dfs(1, 0);
        final StringBuilder output = new StringBuilder();
        output.append(String.format("%.15f", result));
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static double dfs(int node, int parent) {
        // Count children (unvisited neighbors)
        int children = 0;
        for(int neighbor : tree.get(node))
            if(neighbor != parent)
                children++;
        // If this is a leaf node (no children), expected length is 0
        if(children == 0)
            return 0.0;        
        // Calculate expected length from this node
        double sum = 0.0;
        for(int child : tree.get(node))
            if(child != parent)
                sum += 1.0 + dfs(child, node);
        return sum / children;
    }
}