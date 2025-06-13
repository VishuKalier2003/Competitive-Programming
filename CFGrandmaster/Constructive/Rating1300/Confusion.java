// https://codeforces.com/problemset/problem/1603/A

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Confusion {
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
        }, "Divisible-confusion", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.nextInt();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final int n = fr.nextInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fr.nextInt();
            output.append(solve(n, nums)).append("\n");
        }
        wr.write(output.toString());
        wr.flush();
    }

    public static String solve(final int n, final int nums[]) {
        boolean possible = true;
        // Only need to check positions i=0..min(n-1,21)
        int limit = Math.min(n - 1, 21);
        for (int i = 0; i <= limit; i++) {
            boolean foundNonDivisor = false;
            // check all d = 2 .. (i+1)
            for (int d = 2; d <= i + 2; d++) {
                if (nums[i] % d != 0) {
                    foundNonDivisor = true;
                    break;
                }
            }
            if (!foundNonDivisor) {
                possible = false;
                break;
            }
        }
        return possible ? "Yes" : "No";
    }
}
