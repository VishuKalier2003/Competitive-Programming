// https://codeforces.com/problemset/problem/1538/C

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Pairs {
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
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            long c, x = 0L;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1L;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread math1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Shovels", 1 << 26);
        math1300.start();
        try {
            math1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static List<Long> factors;

    public static void callMain(String args[]) throws IOException {
        final FastReader fr = new FastReader();
        int t = fr.nextInt();
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final int n = fr.nextInt();
            final long l = fr.nextLong(), r = fr.nextLong();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fr.nextInt();
            out.append(solve(n, l, r, nums)).append("\n");
        }
        wr.write(out.toString());
        wr.flush();
    }

    public static long solve(final int n, final long l, final long r, int nums[]) {
        // Info: Sort the array, as we can always keep i < j since if we get any time j <= i, reverse the pointers
        Arrays.sort(nums);
        // countL stores the number of pairs, which have sum less than or equal to l-1
        long countL = 0l;
        // countR stores the number of pairs, which have sum less than or equal to r
        long countR = 0l;
        // Hack: This count is like a sum property and to get sums under a range we can do prefix sums (sum(r) - sum(l-1))
        for(int i = 0; i < n-1; i++)        // binary search for each element with target l-1
            countL += binarySearch(i, n, nums, l-1);
        for(int i = 0; i < n-1; i++)        // binary search for each element with target r
            countR += binarySearch(i, n, nums, r);
        return countR-countL;       // prefix sum formula
    }

    public static int binarySearch(int left, int n, int nums[], long target) {
        // Note: define ans appropriately, since if no value is found, the result should be 0 so we keep ans as left
        int right = n-1, ans = left, temp = left;       // right as the last index
        left++;
        while(left <= right) {
            int mid = (left+right) >>> 1;
            if(nums[temp] + nums[mid] <= target) {
                ans = mid;
                left = mid+1;
            } else
                right = mid-1;
        }
        return ans-temp;
    }
}
