// Imp- https://codeforces.com/problemset/problem/1183/C
// Score- 22/100

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ComputerGame {
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
        final StringBuilder output = new StringBuilder();
        while(t-- > 0)
            output.append(solve(fast.nextLong(), fast.nextLong(), fast.nextLong(), fast.nextLong())).append("\n");
        System.out.print(output);
    }

    public static StringBuilder solve(final long k, final long n, final long a, final long b) {
        final StringBuilder result = new StringBuilder();
        if(n*b >= k)
            return result.append(-1);
        // Applying k Optimization on the range of a (first type)
        long ansMin = 0, ansMax = n, ans = n;
        while(ansMin <= ansMax) {
            // Evaluate mid
            long mid = ansMin + ((ansMax - ansMin) >> 1);
            if(mid*a + (n-mid)*b < k) {     // We find the max a that is acceptable
                ans = mid;
                ansMin = mid+1;
            } else
                ansMax = mid-1;
        }
        return result.append(ans);
    }
}
