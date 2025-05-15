import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MaxMedian {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(); final long k = fast.nextLong();
        long nums[] = new long[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextLong();
        System.out.print(solve(n, k, nums));
    }

    public static long solve(final int n, final long k, long nums[]) {
        Arrays.sort(nums);
        long left = 0, right = 0, ans = 0;
        right = ans = nums[n-1] + k;        // Imp- The max value can be the largest element expanded k times
        final int medianIndex = n/2;
        while(left <= right) {      // Lower bound binary search
            long mid = left + ((right - left) >> 1);
            if(canMedian(n, medianIndex, mid, k, nums)) {       // If median possible
                ans = mid;
                left = mid+1;       // Shift right boundary
            }   else    right = mid-1;
        }
        return ans;
    }

    public static boolean canMedian(final int n, final int medianIndex, final long requiredMedian, final long k, final long nums[]) {
        long operations = 0l;
        for(int i = medianIndex; i < n; i++)
            if(nums[i] < requiredMedian)        // If thr right element of median needs to be increased
                operations += requiredMedian-nums[i];
        return operations <= k;     // If the operation count is within range
    }
}
