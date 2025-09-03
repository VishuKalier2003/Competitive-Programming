import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P1MeetInTheMiddle {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException { // reading string (whitespace exclusive)
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

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Meet-In-The-Middle-(https://cses.fi/problemset/task/1628)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt();
        final long x = fr.nextLong();
        long nums[] = new long[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextLong();
        fw.attachOutput(solve(n, x, nums));
        fw.printOutput();
    }

    private static long[] mp1, mp2;

    public static StringBuilder solve(final int n, final long x, final long nums[]) {
        meetInTheMiddle(n, nums);
        long ways = 0L;
        Arrays.sort(mp2);
        for(long s1 : mp1) {
            long target = x - s1;
            int high = upperBound(mp2, target), low = lowerBound(mp2, target);
            ways += (high - low);
        }
        return new StringBuilder().append(ways);
    }

    public static void meetInTheMiddle(final int n, final long nums[]) {
        List<Long> nums1 = new ArrayList<>(), nums2 = new ArrayList<>();
        int mid = n / 2;
        for (int i = 0; i < mid; i++)
            nums1.add(nums[i]);
        for (int j = mid; j < n; j++)
            nums2.add(nums[j]);
        mp1 = subsetSum(nums1.size(), nums1);
        mp2 = subsetSum(nums2.size(), nums2);
    }

    public static long[] subsetSum(final int n, final List<Long> nums) {
        int total = 1 << n;
        long sums[] = new long[total];
        for(int mask = 0; mask < total; mask++) {
            long sum = 0l;
            for(int i = 0; i < n; i++)
                if((mask & (1 << i)) != 0)
                    sum += nums.get(i);
            sums[mask] = sum;
        }
        return sums;
    }

    public static int lowerBound(final long nums[], final long val) {
        int low = 0, high = nums.length;
        while(low < high) {
            int mid = (low + high) >>> 1;
            if(nums[mid] < val)
                low = mid+1;
            else
                high = mid;
        }
        return low;
    }

    public static int upperBound(final long nums[], final long val) {
        int low = 0, high = nums.length;
        while(low < high) {
            int mid = (low + high) >>> 1;
            if(nums[mid] <= val) 
                low = mid+1;
            else
                high = mid;
        }
        return low;
    }
}
