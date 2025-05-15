// Imp- https://codeforces.com/problemset/problem/1624/D
// Score- 22/100

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PalinColoring {
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
        while(t-- > 0) {
            final int n = fast.nextInt(), k = fast.nextInt();
            String s = fast.next();
            output.append(solve(n, k, s)).append("\n");
        }
        System.out.print(output);
    }

    public static int solve(final int n, final int k, final String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray())
            freq[c - 'a']++;
        int pairs = 0, singles = 0;
        for (int f : freq) {
            pairs  += f / 2;
            singles += f % 2;
        }
        int baseEven = 2 * (pairs / k);     // even part of each palindrome
        int leftoverPairs = pairs % k;      // leftover letters after giving each palindrome (pairs/k) pairs
        int leftoverLetters = 2 * leftoverPairs + singles;
        int addCenter = (leftoverLetters >= k ? 1 : 0);     // Imp- can we give each palindrome one center?
        return baseEven + addCenter;
    }
}
