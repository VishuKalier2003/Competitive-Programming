// https://cses.fi/problemset/task/3420/

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class P16DistinctValueSubarrays {
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
        Thread t1 = new Thread(null,
                () -> {
                    try {
                        callMain(args);
                    } catch (IOException e) {
                        e.getLocalizedMessage();
                    }
                },
                "Distinct-subarrays",
                1 << 26);
        t1.start();
        try {
            t1.join();
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

    public static void solve(final int n, final int nums[]) {
        Set<Integer> repeat = new HashSet<>();
        int l = 0, r = 0; // Info: l deontes left boundary and r denotes right boundary
        long count = 0l;
        while (l < n) {
            while (r < n && !repeat.contains(nums[r])) {
                repeat.add(nums[r]);
                r++;
            }
            count += r - l;
            // Hack: Removing l also removes the occurence of same value at r, so after
            // removing l we add r
            repeat.remove(nums[l]);
            l++;
        }
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(count);
        writer.write(output.toString());
        writer.flush();
    }
}
