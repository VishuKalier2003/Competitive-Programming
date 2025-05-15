import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class JulyaCalendar {
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
        int num = fast.nextInt();
        int dp[] = new int[num+1];
        Arrays.fill(dp, Integer.MAX_VALUE);     // Filling the values as necessary
        System.out.println(helperCalendar(num, dp));
    }

    public static int helperCalendar(int num, int dp[]) {
        if(num == 0)        // When number reduces to zero, base case
            return 0;
        if(dp[num] != Integer.MAX_VALUE)    // When state is already computed
            return dp[num];
        int temp = num, ans = Integer.MAX_VALUE;
        while(temp != 0) {
            int digit = temp % 10;      // Extracting digit
            if(digit > 0)
                // IMP- for each state, we find the minimum steps from all possible paths reaching that state
                ans = Math.min(ans, helperCalendar(num-digit, dp)+1);       // Function call and adding cost 1 to the reached state
            temp /= 10;
        }
        dp[num] = ans;      // Updating the dp[state] with minimum cost
        return dp[num];
    }
}
