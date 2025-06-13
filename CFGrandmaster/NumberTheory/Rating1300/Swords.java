import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Swords {
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
        }, "Swords", 1 << 26);
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
        final int n = fr.nextInt();
        long nums[] = new long[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextLong();
        output.append(solve(n, nums)).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(final int n, final long[] nums) {
        // 1. Find the maximum remaining swords (initial x)
        long x = 0;
        for (long v : nums)
            x = Math.max(x, v);
        // 2. Compute z = gcd of all (x - ai)
        long z = 0;
        for (long v : nums)
            z = gcd(z, x - v);
        // 3. Compute y = total number of thieves
        long y = 0;
        for (long v : nums)
            y += (x - v) / z;
        // 4. Format output
        return new StringBuilder().append(y).append(" ").append(z);
    }

    // Euclidean gcd
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

}
