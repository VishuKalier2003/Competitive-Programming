import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ForestQueries {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

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

    public static class Writer {        // Writer class
        BufferedWriter writer;

        public Writer() {this.writer = new BufferedWriter(new OutputStreamWriter(System.out));}

        public void print(String s) throws IOException {
            this.writer.write(s);
        }

        public void close() throws IOException {this.writer.close();}
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        String lines[] = new String[n];
        for(int i = 0; i < n; i++)
            lines[i] = fast.next();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt(), fast.nextInt(), fast.nextInt()});
        solve(n, lines, queries);
    }

    public static void solve(final int n, final String lines[], final List<int[]> queries) throws IOException {
        Writer writer = new Writer();
        // Dp and nums matrix defined
        int dp[][] = new int[n+1][n+1], nums[][] = new int[n+1][n+1];
        dp[1][1] = nums[1][1];      // Base case fill top left cell (1 based indexing)
        // Start filling nums table
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                nums[i+1][j+1] = lines[i].charAt(j) == '.' ? 0 : 1;
        // Start filling dp table
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++)
                // Imp- one section [i-1][j-1] is added twice by [i-1][j] and [i][j-1] hence we subtract it once [i-1][j-1] (Inclusion Exclusion)
                dp[i][j] = nums[i][j] + dp[i-1][j] + dp[i][j-1] - dp[i-1][j-1];
        for(int query[] : queries) {
            // extract coordinates
            int y1 = query[0], x1 = query[1], y2 = query[2], x2 = query[3];
            // Imp- one section [i-1][j-1] is subtracted twice by [i-1][j] and [i][j-1] hence we add it once [i-1][j-1] (Inclusion Exclusion)
            writer.print(String.valueOf(dp[y2][x2] - dp[y1-1][x2] - dp[y2][x1-1] + dp[y1-1][x1-1])+"\n");
        }
        writer.close();     // close the writer to prevent memory leak
    }
}
