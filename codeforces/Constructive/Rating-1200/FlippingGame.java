import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FlippingGame {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
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
        final int n = fast.nextInt();
        int nums[] = new int[n];
        int count1 = 0;
        for(int i = 0; i < n; i++) {
            int value = fast.nextInt();
            count1 += value == 1 ? 1 : 0;
            nums[i] = value == 1 ? -1 : 1;
        }
        System.out.print(solve(n, nums)+count1);
    }

    public static int solve(final int n, final int nums[]) {
        return kadane(n, nums);
    }

    public static int kadane(final int n, final int nums[]) {
        int maxEndingHere = nums[0], maxSoFar = nums[0];
        for(int i = 1; i < n; i++) {
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        return maxSoFar;
    }
}
