import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class RubrikColor {
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
        }, "Rubrik-Coloring", 1 << 26);
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
        output.append(solve(fr.nextLong()));
        wr.write(output.toString());
        wr.flush();
    }

    public static final long MOD = 1_000_000_007, PHI = MOD-1;

    public static long solve(final long n) {
        long ways = 6L;
        for(long i = 1; i < n; i++) {
            long levelWays = exp(4, exp(2, i, PHI), MOD);
            ways = (ways * levelWays) % MOD; 
        }
        return ways;
    }

    public static long exp(long a, long b, final long M) {
        long res = 1L;
        while(b > 0L) {
            if((b & 1) == 1)
                res = (res * a) % M;
            a = (a * a) % M;
            b >>= 1;
        }
        return res;
    }
}
