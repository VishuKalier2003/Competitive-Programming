import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Binomial {
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
        compute();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0)
            output.append(solve(fast.nextInt(), fast.nextInt())).append("\n");
        System.out.print(output);
    }

    public static long fact[], factInverse[];

    public static void compute() {
        fact = new long[MAX];
        factInverse = new long[MAX];
        fact[1] = 1;
        fact[0] = 1;
        for(int i = 2; i < MAX; i++)
            fact[i] = (fact[i-1] * i) % MOD;
        factInverse[MAX-1] = exp(fact[MAX-1], MOD-2) % MOD;
        for(int i = MAX-2; i >= 0; i--)
            factInverse[i] = (factInverse[i+1] * (i+1)) % MOD;
    }

    public static final int MOD = 1_000_000_007, MAX = 1_000_001;

    public static long solve(final int a, final int b) {
        return (((fact[a] * factInverse[b]) % MOD) * factInverse[a-b]) % MOD;
    }

    public static int exp(long base, int b) {
        long result = 1;
        while(b > 0) {
            if((b & 1) == 1)
                result = (result * base) % MOD;
            base = (base * base) % MOD;
            b >>= 1;
        }
        return (int)result;
    }
}
