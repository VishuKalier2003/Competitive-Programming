import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class P3CoinCombinationI {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), x = fast.nextInt();
        final int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(n, x, nums));
    }

    public static final int MOD = 1_000_000_007;

    public static int solve(final int n, final int money, final int nums[]) {
        final int dp[] = new int[money + 1];
        Arrays.sort(nums); // Imp- Sort the numbers to reduce the inner loop operations per i
        dp[0] = 1;
        for (int i = 1; i <= money; i++) {
            for (int num : nums) {
                if (num > i) // If current num exceeds i then we cannot evaluate all the other states (i-num)
                    break;
                dp[i] += dp[i - num]; // add the state value
                if (dp[i] >= MOD) // Imp- If the dp exceeds MOD, perform subtraction
                    dp[i] -= MOD;
            }
        }
        return dp[money];
    }
}
