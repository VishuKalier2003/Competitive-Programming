// Ques - https://codeforces.com/problemset/problem/1669/H

// Note- storing the required operation count for each bit index(0, 30)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Problem1669H {
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
            final int n = fast.nextInt(), k = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            output.append(solve(n, k, nums)).append("\n");
        }
        System.out.print(output);
    }

    public static int solve(final int n, int k, final int nums[]) {
        return greedySum(setBitCounts(nums, n), k);
    }

    public static int greedySum(final int bits[], int k) {
        int sum = 0;
        for(int bit = 30; bit >= 0; bit--) {
            if(bits[bit] <= k) {
                k -= bits[bit];
                sum |= 1 << bit;
            }
        }
        return sum;
    }

    public static int[] setBitCounts(final int nums[], final int n) {
        int bits[] = new int[31];
        Arrays.fill(bits, n);
        for(int num : nums) {
            for(int i = 0; i < 31; i++) {
                if((num & 1 << i) != 0)
                    bits[i]--;
            }
        }
        return bits;
    }
}
