// https://codeforces.com/problemset/problem/478/B

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem478B {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        @SuppressWarnings("CallToPrintStackTrace")
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

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        System.out.print(solve(fast.nextLong(), fast.nextLong()));
    }

    public static StringBuilder solve(final long n, final long m) {
        // Max was evaluated correctly
        long max = n-(m-1), min = n/m, extra = n%m;
        return new StringBuilder().append(
            // Need to distribute teams as evenly as possible, so we will add 1 from extra left to each team (otherwise we will maximize value in one team)
            count(min)*(m-extra) + count(min+1)*extra
        ).append(" ").append(count(max));
    }

    public static long count(final long n) {
        return (n * (n-1))/2;
    }
}
