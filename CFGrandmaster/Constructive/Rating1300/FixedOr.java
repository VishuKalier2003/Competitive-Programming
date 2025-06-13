// https://codeforces.com/problemset/problem/1988/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class FixedOr {
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
        }, "Increasing-sequence-with-fixed-OR", 1 << 26);
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
        while(t-- > 0) {
            output.append(solve(fr.nextLong())).append("\n");
        }
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(final long num) {
        final StringBuilder s = new StringBuilder();
        int count = 0;      // count the elements in sequence
        // Finding th index of MSB
        for(int i = 63-Long.numberOfLeadingZeros(num); i >= 0; i--) {
            // We flip 1 set bit at a time from MSB to LSB to maintain OR property
            if((num & (1L << i)) != 0 && num != 1L) {
                long x = num ^ (1L << i);
                if(x != 0L) {       // Make sure the number is not 0
                    s.append(x).append(" ");
                    count++;
                }
            }
        }
        s.append(num);
        count++;
        return new StringBuilder().append(count).append("\n").append(s);
    }
}
