import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class P10EditDistance {
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
        final String s1 = fast.next(), s2 = fast.next();
        System.out.print(solve(s1, s2, s1.length(), s2.length()));
    }

    public static int dp[][];

    public static int solve(final String s1, final String s2, final int n, final int m) {
        dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++)
            dp[i][0] = i;
        for (int j = 0; j <= m; j++)
            dp[0][j] = j;
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
            }
        return dp[n][m];
    }

    public static int helper(int i, int j, final int n, final int m, final String s1, final String s2) {
        if (i == n)
            return m - j;
        if (j == m)
            return n - i;
        if (dp[i][j] != -1)
            return dp[i][j];
        int cost;
        if (s1.charAt(i) == s2.charAt(j))
            cost = helper(i + 1, j + 1, n, m, s1, s2);
        else
            cost = Math.min(helper(i + 1, j + 1, n, m, s1, s2),
                    Math.min(helper(i + 1, j, n, m, s1, s2), helper(i, j + 1, n, m, s1, s2))) + 1;
        return cost;
    }
}
