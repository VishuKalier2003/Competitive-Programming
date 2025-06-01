import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class P19JosephusProblemII {
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
        final int n = fast.nextInt(), k = fast.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = i + 1;
        solve(n, k, nums);
    }

    public static void solve(final int n, final int jump, final int nums[]) {
        int log = 32 - Integer.numberOfLeadingZeros(n);
        int cycle[] = new int[n * (log + 1)];
        for (int k = 0; k <= log; k++) {
            int offset = k * n;
            System.arraycopy(nums, 0, cycle, offset, n);
        }
        int req = jump % n, currentJump = 0, len = cycle.length;
        final StringBuilder output = new StringBuilder();
        Set<Integer> dead = new HashSet<>();
        for (int i = 0; i < len; i++) {
            int c = cycle[i];
            if (dead.contains(c))
                continue;
            if (currentJump == req) {
                output.append(cycle[i]).append(" ");
                dead.add(cycle[i]);
                if (n == dead.size())
                    req = 0;
                else
                    req = jump % (n - dead.size());
                currentJump = 0;
            } else
                currentJump++;
        }
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}
