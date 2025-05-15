import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Jellyfish {
    public static class FastReader {
        StringTokenizer tokenizer;
        BufferedReader buffer;

        public FastReader() {
            buffer = new BufferedReader(new InputStreamReader(System.in));
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

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        StringBuilder builder = new StringBuilder();

        while (t-- > 0) {
            int n = fast.nextInt(), m = fast.nextInt(), k = fast.nextInt();
            long[] jelly = new long[n];
            long[] gelly = new long[m];

            for (int i = 0; i < n; i++) jelly[i] = fast.nextLong();
            for (int i = 0; i < m; i++) gelly[i] = fast.nextLong();

            builder.append(simulateGame(jelly, gelly, k)).append("\n");
        }

        System.out.print(builder);
    }

    public static long simulateGame(long[] jelly, long[] gelly, int k) {
        Arrays.sort(jelly);
        Arrays.sort(gelly);

        // Only 1 operation (odd) -> Jellyfish tries to improve her sum
        if (k % 2 == 1) {
            if (jelly[0] < gelly[gelly.length - 1]) {
                jelly[0] = gelly[gelly.length - 1];
                // No need to update gelly array since we only care about Jellyfish's sum
            }
        } else {
            // 2 operations (odd + even), Gellyfish can undo
            // Do Jellyfish move first
            if (jelly[0] < gelly[gelly.length - 1]) {
                long temp = jelly[0];
                jelly[0] = gelly[gelly.length - 1];
                gelly[gelly.length - 1] = temp;
            }

            // Then Gellyfish tries to undo the move
            Arrays.sort(jelly);
            Arrays.sort(gelly);

            if (gelly[0] < jelly[jelly.length - 1]) {
                jelly[jelly.length - 1] = gelly[0];
                // Final result after 2 rounds
            }
        }

        return Arrays.stream(jelly).sum();
    }
}
