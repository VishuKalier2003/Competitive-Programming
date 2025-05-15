// Note- Editorial

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Brightness {
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
        while(t-- > 0)
            builder.append(kthNonPerfectNumber(fast.nextLong())).append("\n");
        System.out.print(builder);
    }

    public static long kthNonPerfectNumber(final long k) {
        long left = 1, right = (long)2e18, ans = -1;
        // For all non-perfect numbers the bulbs will remain on
        while(left <= right) {
            long mid = left + ((right - left) >> 1);
            // IMP- for given k, there are sqrt(n) perfect squares, and so there are n-sqrt(n), non-prefect numbers
            long nonPerfects = mid - (long)Math.floor(Math.sqrt(mid));
            if(nonPerfects >= k) {
                ans = mid;          // Update value for lower bound
                right = mid-1;      // IMP- When checking for largest k not exceeding (lower bound)
            } else
                left = mid+1;
        }
        return ans;
    }
}
