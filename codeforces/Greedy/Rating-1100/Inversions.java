// IMP- Inversion (Combinatorics & Greedy)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Inversions {
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
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(maxInversions(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static long maxInversions(int n, int nums[]) {
        long originalInversion = inversion(n, nums);
        // IMP- dpOne[i] will be the number of ones to the left of index i.
        // IMP- dpZero[i] will be the number of zeros to the right of index i.
        int dpOne[] = new int[n];
        int dpZero[] = new int[n];
        dpOne[0] = 0;
        dpZero[n-1] = 0;
        // Compute cumulative ones from the left.
        for (int i = 1; i < n; i++)
            dpOne[i] = dpOne[i-1] + (nums[i-1] == 1 ? 1 : 0);
        // Compute cumulative zeros from the right.
        for (int j = n - 2; j >= 0; j--)
            dpZero[j] = dpZero[j+1] + (nums[j+1] == 0 ? 1 : 0);
        long bestIncrease = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            long candidate;
            if (nums[i] == 1)
                candidate = dpOne[i] - dpZero[i]; // 1 -> 0 flip
            else
                candidate = dpZero[i] - dpOne[i]; // 0 -> 1 flip
            bestIncrease = Math.max(bestIncrease, candidate);
        }
        // IMP- If the best candidate is negative, do no flip (since we have option to not flip)
        bestIncrease = Math.max(bestIncrease, 0);
        return originalInversion + bestIncrease;
    }

    // Count the number of inversions in the binary array.
    // An inversion is a pair (i,j) with i < j and nums[i] == 1 and nums[j] == 0.
    public static long inversion(int n, int nums[]) {
        long inversion = 0, count0 = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (nums[i] == 0)
                count0++;
            else
                inversion += count0;
        }
        return inversion;
    }
}
