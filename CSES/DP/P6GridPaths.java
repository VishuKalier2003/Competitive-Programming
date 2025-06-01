import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class P6GridPaths {
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
        final int n = fast.nextInt();
        String board[] = new String[n];
        for (int i = 0; i < n; i++)
            board[i] = fast.next();
        System.out.print(solve(n, board));
    }

    public static final int MOD = 1_000_000_007;

    public static int solve(final int n, final String board[]) {
        final int dp[][] = new int[n][n];
        dp[0][0] = board[0].charAt(0) == '.' ? 1 : 0;
        for (int i = 1; i < n; i++)
            dp[i][0] = board[i].charAt(0) == '.' ? dp[i - 1][0] : 0;
        for (int j = 1; j < n; j++)
            dp[0][j] = board[0].charAt(j) == '.' ? dp[0][j - 1] : 0;
        for (int i = 1; i < n; i++)
            for (int j = 1; j < n; j++) {
                if (board[i].charAt(j) == '*') {
                    dp[i][j] = 0;
                    continue;
                }
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                if (dp[i][j] >= MOD)
                    dp[i][j] -= MOD;
            }
        return dp[n - 1][n - 1];
    }
}
