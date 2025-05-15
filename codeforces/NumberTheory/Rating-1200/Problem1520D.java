// Ques- https://codeforces.com/problemset/problem/1520/D

// Note- Counting pairs of values in n elements - (n*n-1)/2
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Problem1520D {
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
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt() - i; // Storing the index and num difference
            output.append(solve(n, nums)).append("\n");
        }
        System.out.print(output);
    }

    public static long solve(final int n, final int nums[]) {
        Map<Integer, Long> freq = new HashMap<>();
        for(int num : nums)
            freq.put(num, freq.getOrDefault(num, 0l) + 1l);
        long count = 0;
        for(long f : freq.values())     // use long to prevent overflow since n*n will exceed int and give wrong answer during multiplication
            count += (f*(f-1))/2;
        return count;
    }
}
