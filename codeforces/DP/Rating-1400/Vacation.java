import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Vacation {
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
        final int n = fast.nextInt();
        final int dp[][] = new int[n][3];
        for(int row[] : dp)
            Arrays.fill(row, -1);
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(n, nums, dp));
    }

    public static StringBuilder solve(final int n, final int nums[], final int dp[][]) {
        return new StringBuilder().append(n - helper(0, 0, n, nums, dp));
    }

    public static int helper(int i, int prev, final int n, final int nums[], final int dp[][]) {
        if(i == n)
            return 0;
        if(dp[i][prev] != -1)
            return dp[i][prev];
        int best = helper(i+1, 0, n, nums, dp);
        if(prev != 1 && (nums[i] == 1 || nums[i] == 3))
            best = Math.max(best, 1 + helper(i+1, 1, n, nums, dp));
        if(prev != 2 && (nums[i] == 2 || nums[i] == 3))
            best = Math.max(best, 1 + helper(i+1, 2, n, nums, dp));
        return dp[i][prev] = best;
    }
}
