import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

// Imp- Good question of Inclusion-Exclusion principle with finding all factors of a number using bitmask subset and prime factorization
public class CoprimePairs {
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
        final int n = fast.nextInt();
        int nums[] = new int[n];
        int max = 0;
        for(int i = 0; i < n; i++) {
            nums[i] = fast.nextInt();
            max = Math.max(max, nums[i]);
        }
        System.out.print(solve(n, max, nums));
    }

    public static long solve(final int n, final int max, final int nums[]) {
        int factorFreq[] = new int[max+1];
        long pairs = 0l;
        for(int i = 0; i < n; i++) {
            List<Integer> primes = primeFactorization(nums[i]);     // Finding the list of primes
            int primeList = 1 << primes.size();
            long nonCoprimes = 0l;
            for(int mask = 1; mask < primeList; mask++) {
                int factorsTaken = Integer.bitCount(mask);      // counting number of bits
                int factor = maskedFactor(mask, primes, primes.size());
                // Imp- Using Inclusion-Exclusion, since a factor can be a product of other factors, hence union to count only once
                // When the subset is of odd size we add, inclusion
                if(factorsTaken % 2 != 0)
                    nonCoprimes += factorFreq[factor];
                else        // Else we exclude, exclusion
                    nonCoprimes -= factorFreq[factor];
            }
            pairs += i - nonCoprimes;
            // Update factors of previous values later, we do not want to consider the current element itself
            for(int mask = 1; mask < primeList; mask++) {
                int factor = maskedFactor(mask, primes, primes.size());
                factorFreq[factor]++;
            }
        }
        return pairs;
    }

    public static List<Integer> primeFactorization(int num) {
        List<Integer> primes = new ArrayList<>();
        for(int i = 2; i*i <= num; i++) {
            if(num % i == 0) {
                primes.add(i);      // Add the prime once only
                while(num % i == 0)     // reduce until not divisible
                    num /= i;
            }
        }
        if(num > 1)
            primes.add(num);
        return primes;
    }

    public static int maskedFactor(int mask, List<Integer> primes, int size) {
        // For primes there are atmost 8 primes till 10^6 so feasible enough in O(1) time
        int factor = 1;
        // Imp- Use masking to find the factors as the products, since all masks of 2^n-1 lie in range 2^n
        for(int i = 0; i < size; i++, mask >>= 1)
            if((mask & 1) == 1)
                factor *= primes.get(i);        // Extract the number corresponding to the ith bit
        return factor;
    }
}
