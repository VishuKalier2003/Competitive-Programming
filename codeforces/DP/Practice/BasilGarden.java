import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BasilGarden {
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
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(minTime(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static long minTime(final int n, final long nums[]) {
        long time = nums[n-1];
        for(int i = n-2; i >= 0; i--)
            time = Math.max(time+1, nums[i]);
        return time;
    }
}
