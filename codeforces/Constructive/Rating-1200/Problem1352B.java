// Ques- https://codeforces.com/problemset/problem/1352/B

// Note- Instead of creating the array from scratch for each case, try to generate all possibilities and prune (here checking that last value is possible or not), no need to always go bottom up, try doing top down as well (think reverse)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem1352B {
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
            final int n = fast.nextInt(), k = fast.nextInt();
            output.append(solve(n, k)).append("\n");
        }
        System.out.print(output);
    }

    public static StringBuilder solve(final int n, final int k) {
        int maxEven = n-(2*(k-1)), maxOdd = n-(k-1);
        if(maxEven > 0 && maxEven % 2 == 0)
            return new StringBuilder().append("Yes\n").append("2 ".repeat(k-1)).append(maxEven);
        if(maxOdd > 0 && maxOdd % 2 != 0)
            return new StringBuilder().append("Yes\n").append("1 ".repeat(k-1)).append(maxOdd);
        return new StringBuilder().append("No");
    }
}
