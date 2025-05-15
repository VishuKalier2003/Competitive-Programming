import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class CoPrime {
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

    public record Prime(int n, int[] nums) {}
    public static void main(String args[]) {
        Prime primes[];
        input: {
            FastReader fastReader = new FastReader();
            primes = new Prime[fastReader.nextInt()];
            for(int i = 0; i < primes.length; i++) {
                int n = fastReader.nextInt();
                primes[i] = new Prime(n, new int[n]);
                for(int j = 0; j < primes[i].n; j++)
                    primes[i].nums[j] = fastReader.nextInt();
            }
            break input;
        } output: {
            sieveOfErasthosthenes();
            Arrays.stream(primes).map(CoPrime::maxCoPrimeSum).forEach(System.out::println);
            break output;
        }
    }

    public static boolean sieve[] = new boolean[10000];

    public static void sieveOfErasthosthenes() {
        Arrays.fill(sieve, true);
        sieve[0] = sieve[1] = false;
        for(int i = 2; i*i < sieve.length; i++) {
            if(sieve[i])
                for(int j = 2*i; j < sieve.length; j = j+i)
                    sieve[j] = false;
        }
    }

    public static int maxCoPrimeSum(Prime prime) {
        int nums[] = prime.nums;
        PriorityQueue<Integer> maxHeap = Arrays.stream(nums).filter(num -> sieve[num]).boxed().collect(Collectors.toCollection(() -> new TreeSet<>())).stream().collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.reverseOrder())));
        return maxHeap.size() == 1 ? -1 : maxHeap.poll() + maxHeap.poll();
    }
}
