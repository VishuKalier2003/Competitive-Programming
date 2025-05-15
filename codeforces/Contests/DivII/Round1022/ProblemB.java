import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ProblemB {
    public static class FastReader {
        private BufferedReader buffer;
        private StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() { return Integer.parseInt(next()); }
        public long nextLong() { return Long.parseLong(next()); }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        StringBuilder output = new StringBuilder();
        while (t-- > 0) {
            long n = fast.nextLong();
            long x = fast.nextLong();
            output.append(solve(n, x)).append('\n');
        }
        System.out.print(output);
    }

    // padding cost: minimal sum of u positive numbers with XOR = 0
    private static long padCost(long u) {
        if (u == 0) return 0;
        if (u == 1) return Long.MAX_VALUE;
        return (u % 2 == 0) ? u : u + 3;
    }

    // find lowest bit position where x has 0-bit
    private static int lowestZeroBit(long x) {
        for (int b = 0; b < 63; b++) {
            if (((x >> b) & 1) == 0) return b;
        }
        return 63;
    }

    public static long solve(long n, long x) {
        if (n == 1) {
            return x == 0 ? -1 : x;
        }
        if (x == 0) {
            // just padding of size n
            long c = padCost(n);
            return (c >= Long.MAX_VALUE) ? -1 : c;
        }

        int pc = Long.bitCount(x);
        long best = Long.MAX_VALUE;

        // k = 1 core
        long u1 = n - 1;
        long c1 = padCost(u1);
        if (c1 < Long.MAX_VALUE) best = Math.min(best, x + c1);

        // k = pc core (disjoint bits)
        long u2 = n - pc;
        long c2 = padCost(u2);
        if (c2 < Long.MAX_VALUE) best = Math.min(best, x + c2);

        // k = 2 core special decomposition
        if (n >= 2) {
            int lzb = lowestZeroBit(x);
            long core2 = x + 2L * (1L << lzb);
            long u3 = n - 2;
            long c3 = padCost(u3);
            if (c3 < Long.MAX_VALUE) best = Math.min(best, core2 + c3);
        }

        return best;
    }
}
