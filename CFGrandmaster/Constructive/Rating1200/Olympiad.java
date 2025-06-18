import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Olympiad {
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

    public static void main(String[] args) throws IOException {
        Thread cons1200 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Training-before-Olympiad", 1 << 26);
        cons1200.start();
        try {
            cons1200.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.nextInt();
        final StringBuilder output = new StringBuilder();
        while (t-- > 0) {
            final int n = fr.nextInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fr.nextInt();
            output.append(solve(n, nums)).append("\n");
        }
        PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(final int n, final int[] nums) {
        StringBuilder sb = new StringBuilder();
        long sum = 0;
        int odd = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            if ((nums[i] & 1) == 1)
                odd++;
            int k = i + 1;
            long ans;
            if (k == 1) {
                // Only one element → game ends immediately with that value
                ans = nums[0];
            } else {
                int r = odd % 3;
                int cnt3 = odd / 3;
                if (r == 1) {
                    // If exactly one odd left modulo 3, the second player will get one more “sum−1” move
                    ans = sum - cnt3 - 1L;
                } else {
                    // r == 0 or r == 2
                    ans = sum - cnt3;
                }
            }
            sb.append(ans);
            if (i + 1 < n)
                sb.append(' ');
        }
        return sb;
    }
}
