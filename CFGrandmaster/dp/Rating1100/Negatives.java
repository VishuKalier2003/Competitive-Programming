// https://codeforces.com/problemset/problem/1791/E

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Negatives {
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
        }, "Negatives-and-positives", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fr.nextInt();
        while (t-- > 0) {
            final int n = fr.nextInt();
            long nums[] = new long[n];
            int odd = 0;
            for (int i = 0; i < n; i++) {
                nums[i] = fr.nextLong();
                odd += nums[i] < 0 ? 1 : 0;     // counting odds
            }
            output.append(solve(n, nums, odd)).append("\n");
        }
        wr.write(output.toString());
        wr.flush();
    }

    public static long solve(final int n, final long nums[], final int odd) {
        long sum = 0L;
        for(int i = 0; i < n; i++) {
            if(nums[i] < 0)
                nums[i] *= -1;      // reversing sign of the number by multiplying with -1
            sum += nums[i];
        }
        Arrays.sort(nums);      // sorting the smallest num
        // If odd number of negatives, subtract smallest negative absolute
        if((odd & 1) == 1)
            sum -= 2L * nums[0];
        return sum;
    }
}