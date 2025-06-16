// https://codeforces.com/problemset/problem/2056/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Subsequence {
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
                    e.getLocalizedMessage();
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
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Palindromic-subsequences", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fr.nextInt();
        while (t-- > 0)
            output.append(solve(fr.nextInt())).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(int n) {
        StringBuilder sb = new StringBuilder(n * 2);
        if (n == 6) {
            // Hard‐coded seed for n=6
            sb.append("1 1 2 3 1 2");
        } else {
            // General case: 1,2,3,...,n−2, 1,2
            for (int i = 1; i <= n - 2; i++) {
                sb.append(i).append(' ');
            }
            sb.append(1).append(' ');
            sb.append(2);
        }
        return sb;
    }
}
