import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class RockScissor {
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
        }, "Rock-Paper-Scissor", 1 << 26);
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
        String s1 = fr.next(), s2 = fr.next();
        output.append(solve(n, s1, s2));
        wr.write(output.toString());
        wr.flush();
    }

    public static String solve(final int n, final String s1, final String s2) {
        int m = s1.length(), k = s2.length();

        int lcm = lcm(m, k);
        int[] total = new int[2]; // total[0] for Nike, total[1] for Poly

        for (int i = 0; i < lcm; i++) {
            char c1 = s1.charAt(i % m), c2 = s2.charAt(i % k);
            if (c1 == c2)
                continue;
            if (winsOver(c1, c2))
                total[1]++;
            else
                total[0]++;
        }
        int cycles = n / lcm;
        int remainder = n % lcm;

        int[] remainderTotal = new int[2];
        for (int i = 0; i < remainder; i++) {
            char c1 = s1.charAt(i % m), c2 = s2.charAt(i % k);
            if (c1 == c2)
                continue;
            if (winsOver(c1, c2))
                remainderTotal[1]++;
            else
                remainderTotal[0]++;
        }
        int nike = total[0] * cycles + remainderTotal[0];
        int poly = total[1] * cycles + remainderTotal[1];
        return nike + " " + poly;
    }

    private static boolean winsOver(char a, char b) {
        return (a == 'R' && b == 'S') ||
                (a == 'S' && b == 'P') ||
                (a == 'P' && b == 'R');
    }

    private static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    private static int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }

}
