// IMP- Greedy and Math (max)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Quests {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();
        StringBuilder builder = new StringBuilder();
        while(tests-- > 0) {
            int n = fast.nextInt(), k = fast.nextInt();     // n and k values
            int nums[] = new int[n], again[] = new int[n];
            for(int i = 0; i < n; i++)      // first input array
                nums[i] = fast.nextInt();
            for(int i = 0; i < n; i++)      // second input array
                again[i] = fast.nextInt();
            builder.append(maxHealth(nums, again, n, k)).append("\n");      // Builder call
        }
        System.out.print(builder);
    }

    public static int maxHealth(final int nums[], final int again[], final int n, final int k) {
        // Variables to store maxHealth, max of again till i index and sum of nums till i index
        int maxHealth = 0, maxAgain = 0, prefixSum = 0;
        for(int i = 0; i < Math.min(n, k); i++) {
            prefixSum += nums[i];       // Updating the sum
            // IMP- It is best to work on quests which have max again values for the left over k
            maxAgain = Math.max(maxAgain, again[i]);        // Updating the max of again
            // IMP- For every index i, we will have left over k as k-i-1 and then we greedily find the max health
            maxHealth = Math.max(maxHealth, prefixSum+(k-i-1)*maxAgain);
        }
        return maxHealth;
    }
}
