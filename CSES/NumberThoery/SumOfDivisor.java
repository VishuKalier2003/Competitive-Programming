import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SumOfDivisor {
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
        System.out.print(solve(fast.nextLong()));
    }

    public static List<Integer> sieve;
    public static final int MOD = 1_000_000_007;

    public static void sumSieve(final long n) {
        sieve = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            sieve.add(0);
        for(int num = 1; num <= n; num++)       // Imp- O(n)
            for(int multiple = num; multiple <= n; multiple += num) {       // Imp- O(log n)
                sieve.set(multiple, sieve.get(multiple) + num);
                if(sieve.get(multiple) >= MOD)
                    sieve.set(multiple, sieve.get(multiple) - MOD);
            }
    }

    public static long solve(final long n) {
        sumSieve(n);
        long sum = 0l;
        for(int i = 1; i <= n; i++) {
            sum += sieve.get(i);
            if(sum >= MOD)
                sum -= MOD;
        }
        return sum;
    }
}
