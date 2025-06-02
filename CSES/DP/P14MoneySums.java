import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Imp- Subset Sum algorithm - O(n^2)
public class P14MoneySums {
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int nums[] = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            nums[i] = fast.nextInt();
            sum += nums[i];
        }
        System.out.print(solve(n, sum, nums));
    }

    public static StringBuilder solve(final int n, final int sum, final int nums[]) {
        boolean dp[] = new boolean[sum + 1];
        dp[0] = true; // Initial state as 0
        // Imp- We perform operations as reverse, for addition we do subtraction from
        // right to left and for multiplication we do division
        for (int num : nums) // Imp- O(n)
            // We make a range for each number from sum to num, and check if state-num is
            // true, if yes, then it implies that state-num is already valid and adding num
            // to that state will make the state also valid
            for (int state = sum; state >= num; state--) // Imp- O(n)
                if (dp[state - num]) // If state-num is valid
                    dp[state] = true; // state is also valid
        final StringBuilder output = new StringBuilder();
        int count = 0;
        for (int i = 1; i <= sum; i++)
            if (dp[i]) {
                output.append(i).append(" ");
                count++;
            }
        return new StringBuilder().append(count).append("\n").append(output);
    }
}
