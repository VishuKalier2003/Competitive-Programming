import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P11CollectingNumbers {
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
        }, "collecting-numbers-I", 1 << 26);
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
        long sum = 0l;
        for(int i = 0; i < n; i++) {
            nums[i] = fast.nextInt();
            sum += nums[i];
        }
        solve(n, nums, sum);
    }

    // Hack: Run Detection Algorithm in a Permutation
    public static void solve(final int n, final int nums[], long sum) {
        int pos[] = new int[n+1];
        for(int i = 0; i < n; i++)
            pos[nums[i]] = i;
        int count = 1;  // Info: Stores the number of times we need to traverse a permutation to read all elements in ancending order
        for(int i = 2; i <= n; i++)
            if(pos[i] < pos[i-1])       // For every bump, we increase the run by 1
                count++;
        final StringBuilder output = new StringBuilder();
        output.append(count);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}
