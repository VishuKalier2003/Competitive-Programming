import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class P18JosephusProblem {
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
                "josephus-problem",
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
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = i + 1;
        solve(n, nums);
    }

    public static void solve(final int n, final int nums[]) {
        int log = 32 - Integer.numberOfLeadingZeros(n);
        int cycle[] = new int[n * (log + 1)];
        for (int k = 0; k < (log + 1); k++) {
            int offset = k * n;
            System.arraycopy(nums, 0, cycle, offset, n);
        }
        final StringBuilder output = new StringBuilder();
        int len = cycle.length;
        boolean kill = false;
        Set<Integer> dead = new HashSet<>();
        for (int i = 0; i < len; i++) {
            int c = cycle[i];
            if (dead.contains(c))
                continue;
            if (kill) {
                dead.add(c);
                output.append(c).append(" ");
                kill = false;
            } else
                kill = true;
        }
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}
