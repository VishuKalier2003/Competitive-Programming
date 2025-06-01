import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P20NestedRangeCheck {
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
        Thread josephus = new Thread(
                null, () -> {
                    try {
                        callMain(args);
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                },
                "josephus-problem-ii",
                1 << 26);
        josephus.start();
        try {
            josephus.join();
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int nums[][] = new int[n][2];
        for (int i = 0; i < n; i++) {
            nums[i][0] = fast.nextInt();
            nums[i][1] = fast.nextInt();
        }
        solve(n, nums);
    }

    public static class Node {
        final int s, e, idx;

        public Node(int s, int e, int idx) {
            this.s = s;
            this.e = e;
            this.idx = idx;
        }
    }

    public static void solve(final int n, final int nums[][]) {
        List<Node> l = new ArrayList<>();
        for (int i = 0; i < n; i++)
            l.add(new Node(nums[i][0], nums[i][1], i));
        Collections.sort(l, (Node n1, Node n2) -> {
            if (n1.s == n2.s)
                return Integer.compare(n2.e, n1.e);
            return Integer.compare(n1.s, n2.s);
        });
        boolean isContained[] = new boolean[n];
        int maxRight = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (l.get(i).e <= maxRight)
                isContained[l.get(i).idx] = true;
            maxRight = Math.max(maxRight, l.get(i).e);
        }
        Collections.sort(l, (Node n1, Node n2) -> {
            if (n1.s == n2.s)
                return Integer.compare(n1.e, n2.e);
            return Integer.compare(n2.s, n1.s);
        });
        boolean containOther[] = new boolean[n];
        int minRight = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (l.get(i).e >= minRight)
                containOther[l.get(i).idx] = true;
            minRight = Math.min(minRight, l.get(i).e);
        }
        final StringBuilder output = new StringBuilder();
        for (boolean b : containOther)
            output.append(b ? 1 : 0).append(" ");
        output.append("\n");
        for (boolean b : isContained)
            output.append(b ? 1 : 0).append(" ");
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}
