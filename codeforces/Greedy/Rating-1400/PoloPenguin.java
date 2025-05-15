import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class PoloPenguin {
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
        final int n = fast.nextInt(), m = fast.nextInt(), d = fast.nextInt();
        int mat[][] = new int[n][m];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                mat[i][j] = fast.nextInt();
        System.out.print(solve(n, m, d, mat));
    }

    public static StringBuilder solve(final int n, final int m, final int d, final int mat[][]) {
        List<Integer> cells = new ArrayList<>();
        final int MOD = mat[0][0] % d;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++) {
                if(mat[i][j] % d != MOD)
                    return new StringBuilder().append(-1);
                cells.add(mat[i][j]);
            }
        Collections.sort(cells);
        int sum = 0, k = n*m;
        for(int i = 0; i < k; i++)
            sum += Math.abs(cells.get(i) - cells.get(k/2-1));
        return new StringBuilder().append(sum/d);
    }
}
