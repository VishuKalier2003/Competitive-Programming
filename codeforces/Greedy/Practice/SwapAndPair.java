import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SwapAndPair {
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
            final int n = fast.nextInt();
            int nums[][] = new int[2][n];
            for(int i = 0; i < n; i++)
                nums[0][i] = fast.nextInt();
            for(int i = 0; i < n; i++)
                nums[1][i] = fast.nextInt();
            builder.append(maxPathCost(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static int maxPathCost(final int n, final int mat[][]) {
        int sumMax = 0;
        int bestBonus = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int maxVal = Math.max(mat[0][i], mat[1][i]);
            int minVal = Math.min(mat[0][i], mat[1][i]);
            sumMax += maxVal;
            bestBonus = Math.max(bestBonus, minVal);
        }
        return sumMax + bestBonus;
    }
}
