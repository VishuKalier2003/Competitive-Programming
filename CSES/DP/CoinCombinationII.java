import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class CoinCombinationII {
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
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), x = fast.nextInt();
        final int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(n, x, nums));
    }

    public static final int MOD = 1_000_000_007;

    public static int solve(int n, int x, int[] coins) {
        Arrays.sort(coins);                       // sort once
        final int MOD = 1_000_000_007;            // static final helps JIT :contentReference[oaicite:1]{index=1}
        int[] dp = new int[x+1];
        dp[0] = 1;

        // Imp- 1-D rollout: for each coin, add its contribution, converting 2d dp to 1d dp
        for (int c : coins) {
            // Imp- We start from the current coin all the way to money
            for (int s = c; s <= x; s++) {
                dp[s] += dp[s - c];     // Since we are looking at coin c, so we check for dp[s-c]
                if (dp[s] >= MOD)
                    dp[s] -= MOD;
            }
        }
        return dp[x];
    }

    // Imp- Memonization will give TLE
    public static int helper(int num, final int n, int index, final int nums[]) {
        if(num == 0)
            return 1;
        if(num < 0 || index == n)
            return 0;
        int ways = 0;
        for(int i = index; i < n; i++)
            ways = (ways + helper(num-nums[i], n, i, nums)) % MOD;
        return ways;
    }
}
