import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class JourneyPlan {
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
        final int n = fast.nextInt();
        int nums[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(n, nums));
    }

    public static long solve(final int n, final int nums[]) {
        Map<Integer, Long> sumMap = new HashMap<>();
        for(int i = 1; i <= n; i++) {
            int diff = i - nums[i];     // Imp- The difference of indices and the values must be same
            sumMap.putIfAbsent(diff, 0L);
            sumMap.replace(diff, sumMap.get(diff) + nums[i]);
        }
        long maxSum = 0;
        for(long sum : sumMap.values())
            maxSum = Math.max(maxSum, sum);
        return maxSum;
    }
}
