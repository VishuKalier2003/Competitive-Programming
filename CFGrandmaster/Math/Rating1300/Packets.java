// https://codeforces.com/problemset/problem/1037/A

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Packets {
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
        }, "Packets", 1 << 26);
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
        output.append(solve(fr.nextInt()));     // input taken
        wr.write(output.toString());
        wr.flush();
    }

    public static long solve(final long n) {
        long sum = 0, i = 1;
        while(sum <= n) {
            // finding geometric sum of series of 1,2,4,8... till sum does not exceed n
            long t = geometricSum(1, 2, i);
            if(t <= n) {
                sum = t;
                i++;
            }
            else
                break;
        }
        // If sum equals n, then we do not need any extra element to fill the remainder
        return sum == n ? i-1 : i;
    }

    public static long geometricSum(long g1, long r, long n) {
        if(r == 1)
            return g1 * n;
        // When common ratio is not equal to 1
        return (g1 * (exp(r, n)-1L)) / (r-1L);
    }

    public static long exp(long a, long b) {        // Binary (Power) exponentiation
        long res = 1L;
        while(b > 0) {
            if((b & 1) == 1)
                res *= a;
            a *= a;
            b >>= 1;
        }
        return res;
    }
}
