import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CountingPairs {
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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            final long x = fast.nextLong(), y = fast.nextLong();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            builder.append(countPairs(n, nums, x, y)).append("\n");
        }
        System.out.print(builder);
    }

    public static int countPairs(final int n, final long nums[], final long x, final long y) {
        long prefix[] = new long[n+1];
        for(int i = 1; i <= n; i++)
            prefix[i] = prefix[i-1] + nums[i-1];
        int totalPairs = 0;
        for(int i = 1; i < n; i++) {
            int count = 0;
            int upper = binarySearchUpperBound(i, n, prefix, prefix[i-1], y), lower = binarySearchLowerBound(i, n, prefix, prefix[i-1], x);
            count = Math.max(0, upper-lower);
            totalPairs += count;
        }
        return totalPairs;
    }

    public static int binarySearchLowerBound(final int idx, final int n, final long nums[], final long prefix, final long x) {
        int left = idx, right = n, ans = n+1;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            long checkpoint = nums[mid] - prefix;
            if(checkpoint >= x) {
                ans = mid;
                right = mid-1;
            } else
                left = mid+1;
        }
        return ans;
    }

    public static int binarySearchUpperBound(final int idx, final int n, final long nums[], final long prefix, final long y) {
        int left = idx, right = n, ans = n+1;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            long checkpoint = nums[mid] - prefix;
            if(checkpoint <= y) {
                ans = mid;
                left = mid+1;
            } else
                right = mid-1;
        }
        return ans;
    }
}
