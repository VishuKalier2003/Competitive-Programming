// Ques- https://codeforces.com/problemset/problem/1372/B

// Note- Smallest divisor of a number breaks into two number a and b with min LCM
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Problem1372B {
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
        while(t-- > 0)
            output.append(solve(fast.nextInt())).append("\n");
        System.out.print(output);
    }

    public static StringBuilder solve(int n) {
        if(n % 2 == 0)      // When evenly divides
            return new StringBuilder().append(n/2).append(" ").append(n/2);
        int num1 = smallestPrimeDivisor(primeFactorization(n));
        if(num1 == n)       // When number is prime itself
            return new StringBuilder().append(1).append(" ").append(n-1);
        return new StringBuilder().append(n/num1).append(" ").append(n-(n/num1));
    }

    public static List<Integer> primeFactorization(int n) {
        List<Integer> factors = new ArrayList<>();
        for(int i = 2; i*i <= n; i++)
            if(n % i == 0) {
                factors.add(i);
                while(n % i == 0)
                    n /= i;
            }
        if(n > 1)    // update
            factors.add(n);
        return factors;
    }

    public static int smallestPrimeDivisor(List<Integer> factors) {
        return factors.get(0);      // Get smallest divisor
    }
}
