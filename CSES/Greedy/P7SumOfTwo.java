import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class P7SumOfTwo {
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
        Thread t3 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "sum-of-two", 1 << 26);
        t3.start();
        try {
            t3.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), target = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, target, nums);
    }

    public static void solve(final int n, final int target, final int nums[]) {
        final StringBuilder output = new StringBuilder();
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            // Check if complement exists in our map
            if(valueToIndex.containsKey(complement)) {
                // Found a pair! Output the positions (1-indexed)
                output.append(valueToIndex.get(complement) + 1).append(" ").append(i + 1);
                break;
            }
            // Store current value with its index
            valueToIndex.put(nums[i], i);
        }
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        if(output.length() > 0)
            writer.write(output.toString());
        else
            writer.write("IMPOSSIBLE");
        writer.flush();
    }
}