// https://codeforces.com/problemset/problem/919/B

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class PerfectNumber {
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
        }, "Perfect-Number", 1 << 26);
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
        output.append(solve(fr.nextInt()));
        wr.write(output.toString());
        wr.flush();
    }

    public static long solve(int n) {
        long num = 1L;
        while(n > 0) {
            if(digitSum(num) == 10) {
                n--;
                if(n == 0)
                    return num;
            }
            num++;
        }
        return num;
    }

    public static int digitSum(long num) {
        int s = 0;
        while(num > 0) {
            long d = num % 10;
            s += (int)d;
            num /= 10;
        }
        return s;
    }
}
