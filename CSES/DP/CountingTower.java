import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CountingTower {
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
        final StringBuilder output = new StringBuilder();
        int t = fast.nextInt();
        while(t-- > 0)
            output.append(solve(fast.nextInt())).append("\n");
        System.out.print(output);
    }

    public static final int MOD = 1_000_000_007;

    public static long solve(final int n) {
        return helper(n);
    }

    public static long helper(int n) {
        if(n == 0)
            return 1;
        if(n == 1)
            return 2;
        long ways = 0l;
        ways = (ways + (2l* helper(n-1))) % MOD;
        ways = (ways + (4l* helper(n-2))) % MOD;
        return ways;
    }
}
