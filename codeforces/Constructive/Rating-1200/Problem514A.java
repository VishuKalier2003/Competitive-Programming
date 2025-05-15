// Ques- https://codeforces.com/problemset/problem/514/A

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem514A {
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
        System.out.print(solve(fast.next()));
    }

    public static StringBuilder solve(final String s) {
        final StringBuilder output = new StringBuilder();
        int n = s.length();
        for(int i = 0; i < n; i++) {
            int digit = Integer.parseInt(s.charAt(i)+"");
            if(digit == 9 && i == 0)
                output.append(9);
            else if(digit >= 5)
                output.append(9-digit);
            else
                output.append(digit);
        }
        return output;
    }
}
