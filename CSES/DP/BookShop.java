import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BookShop {
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
        final int n = fast.nextInt(), cost = fast.nextInt();
        int prices[] = new int[n], pages[] = new int[n];
        for(int i = 0; i < n; i++)
            prices[i] = fast.nextInt();
        for(int i = 0; i < n; i++)
            pages[i] = fast.nextInt();
        System.out.print(solve(n, cost, prices, pages));
    }

    public static int dp[][];

    public static int solve(final int n, final int cost, final int prices[], final int pages[]) {
        dp = new int[n+1][cost+1];
        for(int i = 0; i <= cost; i++)
            dp[n][i] = 0;
        for(int i = n-1; i >= 0; i--) {     // Imp- i is increasing in memo, so we will decrease
            // Imp- We decrease cost in memo, so we will increase it here
            for(int j = 0; j <= cost; j++) {
                int maxPages = dp[i+1][j];      // To skip, we just store the previous state current without modifying
                if(j >= prices[i])
                    // Otherwise we find the max pages that we can get
                    maxPages = Math.max(maxPages, pages[i] + dp[i+1][j-prices[i]]);
                dp[i][j] = maxPages;
            }
        }
        return dp[0][cost];
    }

    public static int helper(int i, final int n, int cost, final int prices[], final int pages[]) {
        if(i == n)
            return 0;
        if(dp[i][cost] != -1)
            return dp[i][cost];
        int maxPages = helper(i+1, n, cost, prices, pages);
        if(cost >= prices[i])
            maxPages = Math.max(maxPages, pages[i]+helper(i+1, n, cost - prices[i], prices, pages));
        return dp[i][cost] = maxPages;
    }
}
