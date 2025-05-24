import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P8SubarraySum {
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
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread th1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "modified-kadane", 1 << 26);
        th1.start();
        try {
            th1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        long nums[] = new long[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    public static void solve(final int n, final long nums[]) {
        long maxSub = nums[0], sum = nums[0];
        for(int i = 1; i < n; i++) {
            // Info: Either start new from nums[i], or include previous nums[i] + sum
            sum = Math.max(nums[i], sum + nums[i]);
            maxSub = Math.max(maxSub, sum);     // Info: Take maximum of each subarray
        }
        final StringBuilder output = new StringBuilder();
        output.append(maxSub);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.append(output.toString());
        writer.flush();
    }
}
