import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StaticSum {
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
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n, q, nums, queries);
    }

    public static void solve(final int n, final int q, final int nums[], final List<int[]> queries) {
        long prefix[] = new long[n+1];      // Imp- Use long to prevent Integer overflow
        for(int i = 0; i < n; i++)
            prefix[i+1] = prefix[i] + nums[i];
        final StringBuilder output = new StringBuilder();
        for(int query[] : queries)
            output.append(prefix[query[1]] - prefix[query[0]-1]).append("\n");
        System.out.print(output);
    }
}
