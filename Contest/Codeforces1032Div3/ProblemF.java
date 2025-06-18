import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ProblemF {
    public static class FastReader {
        private static final byte[] buf = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buf);
                if (len <= 0)
                    return -1;
            }
            return buf[ptr++] & 0xff;
        }

        public long readLong() throws IOException {
            long x = 0;
            int c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static void main(String[] args) {
        @SuppressWarnings("CallToPrintStackTrace")
        Thread t = new Thread(null, () -> {
            try {
                callMain();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "problem-F", 1 << 26);
        t.start();
    }

    public static void callMain() throws IOException {
        FastReader fr = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        int T = (int) fr.readLong();
        while (T-- > 0) {
            int n = (int) fr.readLong();
            long S = fr.readLong(), X = fr.readLong();
            long[] a = new long[n];
            for (int i = 0; i < n; i++)
                a[i] = fr.readLong();
            out.println(solve(n, S, X, a));
        }
        out.flush();
    }

    // Count all subarrays in arr[l..r) whose sum is target. Standard prefix-sum +
    // hash-map.
    private static long countSubarrays(long[] arr, int l, int r, long target) {
        Map<Long, Integer> freq = new HashMap<>();
        freq.put(0L, 1);
        long sum = 0, res = 0;
        for (int i = l; i < r; i++) {
            sum += arr[i];
            long want = sum - target;
            Integer f = freq.get(want);
            if (f != null)
                res += f;
            freq.put(sum, freq.getOrDefault(sum, 0) + 1);
        }
        return res;
    }

    public static long solve(int n, long s, long x, long[] nums) {
        long answer = 0;
        int start = 0;
        // Process each block of nums[start..end) where all elements <= x
        for (int i = 0; i <= n; i++) {
            if (i == n || nums[i] > x) {
                // block = [start..i)
                if (start < i) {
                    long total = countSubarrays(nums, start, i, s);
                    long gapSum = 0;
                    int prev = start;
                    for (int j = start; j < i; j++) {
                        if (nums[j] == x) {
                            // gap = [prev..j)
                            if (prev < j) {
                                gapSum += countSubarrays(nums, prev, j, s);
                            }
                            prev = j + 1;
                        }
                    }
                    // last gap after the final x
                    if (prev < i) {
                        gapSum += countSubarrays(nums, prev, i, s);
                    }

                    answer += (total - gapSum);
                }
                start = i + 1;
            }
        }

        return answer;
    }
}
