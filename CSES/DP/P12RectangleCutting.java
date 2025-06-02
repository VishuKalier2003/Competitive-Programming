import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class P12RectangleCutting {
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
        System.out.print(solve(fast.nextInt(), fast.nextInt()));
    }

    public static int solve(int a, int b) {
        int dp[][] = new int[a + 1][b + 1];
        for (int i = 1; i <= a; i++)
            for (int j = 1; j <= b; j++) {
                if (i == j)
                    dp[i][j] = 0;
                else {
                    int bestCut = Integer.MAX_VALUE;
                    // For each i x j dimension, we check the best cut
                    for (int k1 = 1; k1 < i; k1++)
                        // Imp- cut cost defined as the sum of cuts of states formed after cutting + 1
                        bestCut = Math.min(bestCut, dp[k1][j] + dp[i - k1][j] + 1);
                    for (int k2 = 1; k2 < j; k2++)
                        // Imp- cut cost defined as the sum of cuts of states formed after cutting + 1
                        bestCut = Math.min(bestCut, dp[i][k2] + dp[i][j - k2] + 1);
                    dp[i][j] = bestCut; // update the dp table
                }
            }
        return dp[a][b];
    }
}
