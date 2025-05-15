import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MortalCombat {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            output.append(solve(n, nums)).append("\n");
        }
        System.out.print(output);
    }

    public static StringBuilder solve(final int n, final int nums[]) {
        int dp[][] = new int[2][n+1];
        for(int j = 0; j <= n; j++) {
            dp[0][j] = Integer.MAX_VALUE;
            dp[1][j] = Integer.MAX_VALUE;
        }
        dp[1][0] = 0;
        for(int j = 0; j < n; j++) {
            if(dp[1][j] < Integer.MAX_VALUE)
                dp[0][j+1] = Math.min(dp[0][j+1], dp[1][j] + nums[j]);
            dp[1][j+1] = Math.min(dp[1][j+1], dp[0][j]);
            if(j < n-1) {
                if(dp[1][j] < Integer.MAX_VALUE)
                    dp[0][j+2] = Math.min(dp[0][j+2], dp[1][j] + nums[j] + nums[j+1]);
                dp[1][j+2] = Math.min(dp[1][j+2], dp[0][j]);
            }
        }
        return new StringBuilder().append(Math.min(dp[0][n], dp[1][n]));
    }
}
