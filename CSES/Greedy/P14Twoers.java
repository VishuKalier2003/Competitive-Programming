import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class P14Twoers {
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
        }, "towers", 1 << 26);
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
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    // Hack: Patience Sorting, where we just not place values greedily, we check for any min or max constraint before placing these blocks
    public static void solve(final int n, final int nums[]) {
        // Note: Patience sorting either uses (greedy + binary search) or (sliding window + binary search)
        List<Integer> bases = new ArrayList<>(); 
        for(int block : nums) {
            if(bases.isEmpty())     // In case of empty
                bases.add(block);
            else if(bases.get(bases.size()-1) <= block)     // When current block exceeds all sizes
                bases.add(block);
            else {
                // Info: binary search the index of smallest sized block that is larger than current
                int index = binarySearch(bases.size(), block, bases);
                bases.set(index, block);
            }
        }
        // StringBuilder defined
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(bases.size());
        writer.write(output.toString());
        writer.flush();
    }

    public static int binarySearch(int r, int target, List<Integer> bases) {
        int left = 0, right = r-1, ans = r-1;
        while(left <= right) {
            int mid = (left + right) >>> 1;
            // Finding the last true case
            if(bases.get(mid) > target) {       // Note: k Optimization technique
                ans = mid;
                right = mid-1;
            } else
                left = mid+1;
        }
        return ans;
    }
}
