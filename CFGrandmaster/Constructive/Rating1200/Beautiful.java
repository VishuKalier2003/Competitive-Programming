// https://codeforces.com/problemset/problem/2041/E

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Beautiful {
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
        }, "Beautiful-Array", 1 << 26);
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
        output.append(solve(fr.nextInt(), fr.nextInt())).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    // Create an array of [b, b, 3a-2b]
    public static StringBuilder solve(final int a, final int b) {
        final StringBuilder s = new StringBuilder().append(3).append("\n");
        s.append(b).append(" ").append(b).append(" ").append((3*a) - (2*b));
        return s;
    }
}
