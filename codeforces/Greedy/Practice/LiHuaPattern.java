import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LiHuaPattern {
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
            final int n = fast.nextInt(); final long k = fast.nextInt();
            int mat[] = new int[n*n];
            for(int i = 0; i < n*n; i++)
                mat[i] = fast.nextInt();
            builder.append(greedyOptimization(n, k, mat));
        }
        System.out.print(builder);
    }

    public static String greedyOptimization(final int n, long k, final int mat[]) {
        for(int i = 0, j = n*n-1; i < j; i++, j--) {
            if(mat[i] != mat[j]) {
                if(k > 0)
                    k--;
                else    return "No\n";
            }
        }
        if(k % 2 == 0)
            return "Yes\n";
        if(k % 2 != 0 && n % 2 != 0)
            return "Yes\n";
        return "No\n";
    }
}
