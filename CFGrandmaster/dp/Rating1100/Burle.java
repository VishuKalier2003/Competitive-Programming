// https://codeforces.com/problemset/problem/1958/A

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Burle {
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
        }, "1-3-5", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fr.nextInt();
        while (t-- > 0)
            output.append(solve(fr.nextInt())).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    public static int solve(int n) {
        dp = new int[n+1];
        Arrays.fill(dp, INF);
        return helper(n);
    }

    public static final int INF = 1000;
    public static int dp[];

    public static int helper(int num) {
        if(num < 3 && num > -1)
            return num;
        if(num < 0)
            return INF;
        if(dp[num] != INF)
            return dp[num];
        int min = INF;
        min = Math.min(min, Math.min(helper(num-3), helper(num-5)));
        return dp[num] = min;
    }
}