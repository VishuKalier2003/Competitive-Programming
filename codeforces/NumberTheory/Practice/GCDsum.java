import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class GCDsum {
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
            long nums[] = new long[n];
            long sum = 0L;
            for(int i = 0; i < n; i++) {
                nums[i] = fast.nextLong();
                sum += nums[i];
            }
            builder.append(maxGCDofSubarrays(n, sum, nums)).append("\n");       // Function call
        }
        System.out.print(builder);
    }

    public static long maxGCDofSubarrays(int n, long sum, long nums[]) {
        // When we want to maximize GCD it is always optimal to split the array into exactly 2 segments and not more than 2
        long prefix = nums[0], maxGCD = gcd(prefix, sum-prefix);
        for(int i = 1; i < n-1; i++) {      // Need not to take boundaries, skip 0 and n-1
            prefix += nums[i];
            // IMP- Finding max GCD of the increasing segment and the leftover sum iteratively
            maxGCD = Math.max(maxGCD, gcd(prefix, sum-prefix));
        }
        return maxGCD;
    }

    public static long gcd(long a, long b) {        // GCD function
        if(b == 0)
            return a;
        return gcd(b, a % b);
    }
}
