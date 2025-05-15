import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MoveToEnd {
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
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            output.append(solve(n, nums)).append("\n");
        }
        System.out.print(output);
    }

    public static StringBuilder solve(final int n, long nums[]) {
        final StringBuilder result = new StringBuilder();
        long prefixMax[] = new long[n], suffixSum[] = new long[n+1];
        prefixMax[0] = nums[0];
        suffixSum[n-1] = nums[n-1];
        for(int i = 1; i < n; i++)
            prefixMax[i] = Math.max(prefixMax[i-1], nums[i]);
        for(int i = n-1; i >= 0; i--)
            suffixSum[i] = suffixSum[i+1] + nums[i];
        for(int i = 1; i <= n; i++) {
            long optionA = suffixSum[n-i];
            long optionB = prefixMax[n-i] + suffixSum[n-i+1];
            result.append(Math.max(optionA, optionB)).append(" ");
        }
        return result;
    }
}
