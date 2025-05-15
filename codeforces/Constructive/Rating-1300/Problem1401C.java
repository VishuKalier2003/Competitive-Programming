// https://codeforces.com/problemset/problem/1401/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Problem1401C {
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

    public final static StringBuilder output = new StringBuilder();

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            int min = (int)1e9;
            for(int i = 0; i < n; i++) {
                nums[i] = fast.nextInt();
                min = Math.min(min, nums[i]);
            }
            solve(n, nums, min);
        }
        System.out.print(output);
    }

    // Imp- Check after creating answer, since we want to swap, if the num is at same position evan after swap then we need not change, otherwise we have to change it, and if it is not a multiple of min then we cannot change it as it will not satisfy the condition of gcd(a, b) = min
    public static void solve(final int n, final int nums[], final int min) {
        // We make the answer array
        int sorted[] = Arrays.copyOf(nums, n);      // copying function
        Arrays.sort(sorted);        // Sorting
        for(int i = 0; i < n; i++)
            if(sorted[i] != nums[i] && nums[i] % min != 0) {
                output.append("NO\n");
                return;
            }
        output.append("YES\n");
    }
}
