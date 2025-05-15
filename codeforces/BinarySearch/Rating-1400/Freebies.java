import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Freebies {
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

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int max = 0;
        int nums[] = new int[n];
        for(int i = 0; i < n; i++) {
            nums[i] = fast.nextInt();
            max = Math.max(max, nums[i]);
        }
        final int T = fast.nextInt();
        System.out.print(solve(n, max, T, nums));
    }

    public static int solve(final int n, final int max, final int T, final int nums[]) {
        int prefix[] = new int[max+T+1];
        for(int num : nums)
            prefix[num]++;
        for(int i = 1; i < prefix.length; i++)
            prefix[i] = prefix[i-1] + prefix[i];
        int maxHelp = 0;
        for(int num : nums)
            maxHelp = Math.max(maxHelp, prefix[num+T] - prefix[num-1]);
        return maxHelp;
    }
}
