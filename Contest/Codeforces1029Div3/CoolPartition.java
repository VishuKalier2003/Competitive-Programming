import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

public class CoolPartition {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
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

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "C-Cool-Partition", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder out = new StringBuilder();
        while (t-- > 0) {
            final int n = fr.readInt();
            final int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fr.readInt();
            out.append(solve(n, nums)).append("\n");
        }
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(out.toString());
        wr.flush();
    }

    public static int solve(final int n, int nums[]) {
        // Count frequency of each number in the whole array
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : nums) freq.put(x, freq.getOrDefault(x, 0) + 1);

        Set<Integer> currSet = new HashSet<>();
        Set<Integer> remainSet = new HashSet<>(freq.keySet());
        int seg = 0;
        for (int i = 0; i < n; i++) {
            currSet.add(nums[i]);
            freq.put(nums[i], freq.get(nums[i]) - 1);
            if (freq.get(nums[i]) == 0) remainSet.remove(nums[i]);
            // If currSet and remainSet are disjoint, we can split here
            if (remainSet.isEmpty() || Collections.disjoint(currSet, remainSet)) {
                seg++;
                currSet.clear();
            }
        }
        return seg;
    }
}