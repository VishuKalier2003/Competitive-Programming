import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class EpicNovel {
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
        }, "Epic-novel", 1 << 26);
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
        int t = fr.nextInt();
        while(t-- > 0) {
            final int n = fr.nextInt(), a = fr.nextInt(), va = fr.nextInt(), c = fr.nextInt(), vc = fr.nextInt(), b = fr.nextInt();
            output.append(solve(n, a, va, c, vc, b)).append("\n");
        }
        wr.write(output.toString());
        wr.flush();
    }

    public static int solve(final int n, int a, int va, int c, int vc, final int b) {
        int nums[] = new int[101];
        nums[a] = va; nums[c] = vc;
        int idx = 101;
        while(vc > va) {
            nums[c] = vc--;
            idx = c--;
        }
        while(a < idx)
            nums[a++] = va;
        return nums[b];
    }
}
