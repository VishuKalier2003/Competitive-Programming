import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

public class MEXRepitition {
    public static class FastReader { // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch(IOException e) {
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
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            final long k = fast.nextLong();
            // Create a set with 0,1,...,n
            LinkedHashSet<Integer> present = new LinkedHashSet<>();
            for (int i = 0; i <= n; i++)
                present.add(i);
            int nums[] = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = fast.nextInt();
                present.remove(nums[i]); // remove numbers that appear
            }
            // Get the MEX (the first remaining number)
            int MEX = present.stream().findFirst().orElse(0);
            builder.append(mexRepeated(n, k, nums, MEX)).append("\n");
        }
        System.out.print(builder);
    }

    public static String mexRepeated(int n, long k, int nums[], int MEX) {
        // Build the permutation of length n+1: [a1, a2, ..., an, MEX]
        int len = n + 1;
        int state[] = new int[len];
        for (int i = 0; i < n; i++)
            state[i] = nums[i];
        state[n] = MEX; // appended MEX
        // Calculate the effective right shift
        int r = (int)(k % len);
        // Build the result after cyclic right shift by r positions.
        // For an array of length len, the new array at index i is the old array at index (i - r + len) % len.
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len-1; i++) {
            int index = (i - r + len) % len;
            result.append(state[index]).append(" ");
        }
        return result.toString().trim();
    }
}
