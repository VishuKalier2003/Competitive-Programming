import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P9StickLength {
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
        }, "greedy-costing", 1 << 26);
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
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    public static void solve(final int n, final int nums[]) {
        Arrays.sort(nums);
        int bestHeight;
        if((n & 1) == 1)
            bestHeight = nums[n/2];
        else
            bestHeight = (nums[n/2] + nums[(n/2)-1])/2;
        long minCost = 0l;
        for(int i = 0; i < n; i++)
            minCost += Math.abs(nums[i] - bestHeight);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(minCost);
        writer.write(output.toString());
        writer.flush();
    }
}
