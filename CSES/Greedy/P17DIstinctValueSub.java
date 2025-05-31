import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class P17DIstinctValueSub {
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
                "distinct-value-subsequences",
                1 << 26);
        b1500.start();
        try {
            b1500.join();
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    private static final int MOD = 1_000_000_007;

    public static void solve(final int n, final int[] nums) {
        Map<Integer, Integer> fMap = new HashMap<>();
        for (int num : nums)
            fMap.put(num, fMap.getOrDefault(num, 0) + 1);
        long count = 1;
        for (int f : fMap.values())
            count = (count * (f + 1)) % MOD;
        count = (count - 1 + MOD) % MOD; // Not considering empty subsequence
        final StringBuilder output = new StringBuilder();
        output.append(count);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}
