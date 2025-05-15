import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Frog {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int N = fast.nextInt();
        int nums[] = new int[N];
        for(int i = 0; i < N; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(N, nums));
    }

    public static int solve(final int n, final int nums[]) {
        int dp[] = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        if(n >= 2)
            dp[1] = Math.abs(nums[0] - nums[1]);
        for(int i = 2; i < n; i++)
            dp[i] = Math.min(dp[i], Math.min(Math.abs(nums[i]-nums[i-1]) + dp[i-1], Math.abs(nums[i]-nums[i-2]) + dp[i-2]));
        return dp[n-1];
    }
}
