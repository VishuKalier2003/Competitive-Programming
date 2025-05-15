import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ChipRibbon {
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
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            int n = fast.nextInt();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            builder.append(minOperations(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static long minOperations(final int n, final long nums[]) {
        long operations = nums[0]-1;        // Firstly we need to make chips at 0th index if more than 1
        for(int i = 1; i < n; i++)
            // IMP- Then we find the difference in adjacent values
            operations += Math.max(0, nums[i]-nums[i-1]);
        return operations;
    }
}
