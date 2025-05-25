import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P23FactoryMachines {
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
        }, "factory-machines", 1 << 26);
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
        final long x = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, x, nums);
    }

    public static void solve(final int n, final long products, final int nums[]) {
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        Arrays.sort(nums);
        long left = nums[0], right = nums[n-1] * products, ans = right;
        while(left <= right) {
            long mid = (left + right) >>> 1;
            if(greedy(nums, n, mid, products)) {
                ans = mid;
                right = mid-1;
            }
            else
                left = mid+1;
        }
        output.append(ans);
        writer.write(output.toString());
        writer.flush();
    }

    public static boolean greedy(final int nums[], final long n, final long givenTime, long products) {
        for(int num : nums) {
            products -= (givenTime / num);
            if(products <= 0)
                return true;
        }
        return false;
    }
}
