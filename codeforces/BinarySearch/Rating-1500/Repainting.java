import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Repainting {
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
            final int n = fast.nextInt(), k = fast.nextInt();
            String s = fast.next();
            long nums[] = new long[n];
            long max = 0;
            for(int i = 0; i < n; i++) {
                nums[i] = fast.nextLong();
                max = Math.max(nums[i], max);       // Finding max element (penalty) of the array
            }
            builder.append(solve(n, k, max, s, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static long solve(final int n, final int k, final long max, final String s, final long nums[]) {
        long left = 0, right = max, ans = 0;
        while(left <= right) {      // Use lower bound binary search
            long mid = left + ((right - left) >> 1);
            if(penaltyEvaluate(mid, n, k, s, nums)) {
                ans = mid;      // Store the mid in the ans variable
                right = mid-1;      // Shift right backwards, since we need to find the minimum penalty
            }   else
                left = mid+1;
        }
        return ans;
    }

    public static boolean penaltyEvaluate(final long allowedPenalty, final int n, final int k, final String s, final long nums[]) {
        int segments = 0;
        int i = 0;
        while(i < n) {
            if(s.charAt(i) == 'B' && nums[i] > allowedPenalty) {        // If current cell needs to be painted and has larger penalty
                segments++;
                int j = i;
                while(j < n) {      // Iterate over the segment from start to end
                    char current = s.charAt(j);
                    // IMP- If cell is blue, then we paint is along with the segment and if red then we first check the penalty and then paint it
                    if(current == 'B' || (current == 'R' && nums[j] <= allowedPenalty))
                        j++;
                    else
                        break;
                }
                i = j;      // Two pointer approach
            } else
                i++;        // IMP- If cell is not blue and higher penalty (cell is not critical)
        }
        return segments <= k;
    }
}
