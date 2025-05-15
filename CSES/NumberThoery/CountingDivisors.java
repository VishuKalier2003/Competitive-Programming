import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Imp- Using sieve to count the number of divisors
public class CountingDivisors {
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
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        countingSieve();
        while(t-- > 0)
            output.append(solve(fast.nextInt())).append("\n");
        System.out.print(output);
    }

    public static final int bound = 1000001;
    public static final int sieve[] = new int[bound];

    public static void countingSieve() {
        for(int num = 1; num < bound; num++)        // O(n)
            for(int multiple = num; multiple < bound; multiple += num)      // O(d) or O(log n)
                sieve[multiple]++;      // Count the divisors
    }

    public static int solve(int num) {
        return sieve[num];      // pre computation check
    }
}
