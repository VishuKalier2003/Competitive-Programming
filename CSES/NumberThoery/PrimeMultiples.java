import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Imp- Inclusion Exclusion principle, it automatically prunes if a state is counted twice in a mutual events, regions or segments
// Important tool for Competitive Programming (CF-1600)
public class PrimeMultiples {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

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
        final long n = fast.nextLong();
        final int k = fast.nextInt();
        long nums[] = new long[k];
        for (int i = 0; i < k; i++)
            nums[i] = fast.nextLong();
        System.out.print(solve(n, k, nums));
    }

    private static long result = 0;
    private static long n;
    private static int k;
    private static long[] primes;

    public static long solve(long N, int K, long[] A) {
        n = N;
        k = K;
        primes = A;
        result = 0;
        dfs(0, 1L, 0);
        return result;
    }

    private static void dfs(int idx, long currentLcm, int depth) {
        for (int i = idx; i < k; i++) {
            long p = primes[i];
            long g = gcd(currentLcm, p);
            // Overflow-safe check: would (currentLcm/g)*p exceed n?
            if (currentLcm / g > n / p) {
                continue; // prune: contribution would be zero or overflow
            }
            long nextLcm = (currentLcm / g) * p;
            long contrib = n / nextLcm;
            // inclusion–exclusion sign: + when depth even, – when odd
            result += (depth % 2 == 0 ? contrib : -contrib);
            dfs(i + 1, nextLcm, depth + 1);
        }
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

}
