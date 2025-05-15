import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BlockAdventure {
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
            final int n = fast.nextInt(), m = fast.nextInt(), k = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(canWin(n, m, k, nums));
        }
        System.out.print(builder);
    }

    public static String canWin(final int n, final int m, final int k, final int nums[]) {
        int currentBlocks = m;      // Current blocks holding
        for (int i = 0; i < n - 1; i++) {
            // IMP- The blocks that he can add in the bag, need to maintain that the height does not become negative by applying k
            int requiredHeight = Math.max(0, nums[i + 1] - k);
            currentBlocks += nums[i] - requiredHeight;
            if (currentBlocks < 0)      // If blocks become negative, then it is not possible
                return "No\n";
        }
        return "Yes\n";
    }

}
