// https://codeforces.com/problemset/problem/1271/B

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Blocks {
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
        }, "Blocks", 1 << 26);
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
        output.append(solve(fr.nextInt(), fr.next()));
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(final int n, final String s) {
        char bl[] = s.toCharArray();
        char x = bl[0];     // take the first character as the character that we will fill (marked character)
        int count = 0;
        final StringBuilder out = new StringBuilder();
        for(int i = 1; i < n-1; i++) {
            if(bl[i] != x) {
                // When the element is not same as the marked
                bl[i] = x;
                bl[i+1] = bl[i+1] == 'W' ? 'B' : 'W';       // update adjacent
                out.append((i+1)).append(" ");
                count++;
            }
        }
        if(bl[n-1] == x)    // If last gets marked
            return new StringBuilder().append(count).append("\n").append(out);
        if((n-1) % 2 != 0)      // If odd characters are marked
            return new StringBuilder().append("-1");
        // Count characters at odd positions till 2nd last character
        for(int j = 0; j < n-1; j += 2) {
            count++;
            out.append((j+1)).append(" ");
        }
        return new StringBuilder().append(count).append("\n").append(out);
    }
}
