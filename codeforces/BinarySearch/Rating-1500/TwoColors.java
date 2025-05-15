import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TwoColors {
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
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), m = fast.nextInt();
            int nums[] = new int[m];
            for(int i = 0; i < m; i++)
                nums[i] = fast.nextInt();
            builder.append(countWays(n, m, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static long countWays(final int n, final int m, int nums[]) {
        Arrays.sort(nums);
        long countWays = 0;
        long prefix[] = new long[m+1];
        for(int i = 0; i < m; i++)
            prefix[i+1] = prefix[i] + nums[i];
        for(int i = 0; i < m-1; i++) {
            final int x = nums[i], target = n-x;
            int pos = findWaysWithCurrentColor(i+1, m, nums, target);
            if(pos == m)
                continue;
            int count = m - pos;
            long waysForColorI = (x-n+1) * count + (prefix[m] - prefix[pos]);
            waysForColorI *= 2;
            countWays += waysForColorI;
        }
        return countWays;
    }

    public static int findWaysWithCurrentColor(final int start, final int n, final int nums[], final int target) {
        int left = start, right = n-1, ans = n-1;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            if(nums[mid] >= target) {
                ans = mid;
                right = mid-1;
            }   else    left = mid+1;
        }
        return ans;
    }
}
