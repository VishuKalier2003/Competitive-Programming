import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PashaAndPrimeNumbers {

    // FastReader class for fast input
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    // Check prime by trial division
    public static boolean isPrime(long n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n % 2 == 0) return false;
        long r = (long)Math.sqrt(n);
        for(long i = 3; i <= r; i += 2)
            if(n % i == 0)
                return false;
        return true;
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        while(t-- > 0) {
            // read input values
            long x = Long.parseLong(fast.next());
            int k = fast.nextInt();

            // If k == 1, then y = x. Check x for primality.
            if(k == 1) {
                if(isPrime(x)) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
            } else {
                // When k > 1:
                // If x > 1, then y = x * R and both x and R are >1, so y is composite.
                if(x > 1) {
                    System.out.println("NO");
                } else {
                    // x is 1.
                    // y becomes a repunit: y = (10^k - 1) / 9.
                    long repunit = (long)((Math.pow(10, k) - 1) / 9);
                    if(isPrime(repunit))
                        System.out.println("YES");
                    else
                        System.out.println("NO");
                }
            }
        }
    }
}
