import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class P5RemoveDigits {
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
        System.out.print(solve(fast.nextInt()));
    }

    public static int dp[];

    public static int solve(int num) {
        dp = new int[num + 1];
        Arrays.fill(dp, 1_000_000_000); // We take a large value, not MAX since MAX+1 will be underflow
        dp[0] = 0; // Imp- 0 operations to make 0
        for (int i = 1; i <= num; i++) {
            int mask = mask(i); // extracting the mask of available digits
            for (int j = 0; j < 10; j++, mask >>= 1) {
                if ((mask & 1) == 1) // Imp- the current (0-9) digit is present
                    dp[i] = Math.min(dp[i], dp[i - j] + 1); // Store the min among all states
            }
        }
        return dp[num]; // Find the dp of last state
    }

    // Imp- Memonization gave TLE in 3 cases
    public static int helper(int num) {
        if (num == 0)
            return 0;
        if (dp[num] != -1)
            return dp[num];
        int mask = mask(num); // extracting the digits in the current number
        int count = Integer.MAX_VALUE;
        for (int i = 0; i < 10; i++, mask >>= 1) {
            if ((mask & 1) == 1) // If current bit is 1 (rightmost bit)
                count = Math.min(count, 1 + helper(num - i));
        }
        return dp[num] = count;
    }

    public static int mask(int num) {
        int mask = 0;
        while (num > 0) {
            int digit = num % 10;
            if (digit != 0)
                mask |= 1 << digit;
            num /= 10;
        }
        return mask;
    }
}
