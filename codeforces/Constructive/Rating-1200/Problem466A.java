// Ques- https://codeforces.com/problemset/problem/466/A

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem466A {
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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        System.out.print(solve(fast.nextInt(), fast.nextInt(), fast.nextInt(), fast.nextInt()));
    }

    public static int solve(final int n, final int m, final int a, final int b) {
        if((double)(b/m) < a)       // Update for an edge case
            return (n/m)*b + Math.min((n%m)*a, b);      // We can either buy leftover a tickets or a single m ticket
        return n*a;
    }
}
