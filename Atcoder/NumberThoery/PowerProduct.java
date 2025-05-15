import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class PowerProduct {
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
        final int n = fast.nextInt(), k = fast.nextInt();
        long nums[] = new long[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextLong();
        System.out.print(solve(n, k, nums));
    }

    public static long solve(final int n, final int k, long nums[]) {
        Arrays.sort(nums);
        Map<Long, Integer> freqMap = new HashMap<>();
        for(long num : nums)
            freqMap.put(num, freqMap.getOrDefault(num, 0)+1);
        long upperRange = nums[n-1] * nums[n-2];
        long count = 0;
        for(long i = 1; fastExp(i, k) <= upperRange; i++) {
            long power = fastExp(i, k);
            for(long num : nums) {
                long second = power/num;
                if(freqMap.containsKey(second))
                    count += freqMap.get(second);
            }
        }
        return count;
    }

    public static long fastExp(long a, int k) {
        long result = 1l;
        while(k > 0) {
            if((k & 1) != 0)
                result = result * a;
            a = a * a;
            k >>= 1;
        }
        return result;
    }
}
