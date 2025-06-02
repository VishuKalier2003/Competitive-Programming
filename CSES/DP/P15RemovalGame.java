import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P15RemovalGame {
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
        }, "minimal-grid-path", 1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    private static long dp[][];
    private static boolean seen[][];

    public static void solve(final int n, final int nums[]) {
        dp = new long[n][n];
        seen = new boolean[n][n];
        final StringBuilder output = new StringBuilder();
        long score = helper(0, n - 1, nums), sum = 0l;
        for (int num : nums)
            sum += num;
        output.append((score + sum) / 2);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static long helper(int i, int j, final int nums[]) {
        if (i == j)
            return nums[i];
        if (seen[i][j])
            return dp[i][j];
        seen[i][j] = true;
        long left = nums[i] - helper(i + 1, j, nums);
        long right = nums[j] - helper(i, j - 1, nums);
        return dp[i][j] = Math.max(left, right);
    }
}
