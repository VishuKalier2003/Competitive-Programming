// Note- Editorial
/**
 * Wasn't able to develop the mathematical integer sequence relation
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MoveTurn {
    public static class FastReader {            // Reading input
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

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
        System.out.print(new StringBuilder().append(endPositions(fast.nextInt())));
    }

    public static int endPositions(int n) {
        return n % 2 == 0 ? ((n/2)+1)*((n/2)+1) : 2*((n/2)+1)*((n/2)+2);
    }
}
