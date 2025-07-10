import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MaximDiscounts {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
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

    public static class FastWriter {
        public StringBuilder builder;
        public PrintWriter pr;

        public FastWriter() throws FileNotFoundException {
            this.builder = new StringBuilder();
            this.pr = new PrintWriter(new OutputStreamWriter(System.out));
        }

        public void add(String s) {
            this.builder.append(s);
        }

        public void flushMemory() {
            this.pr.write(builder.toString());
            this.pr.flush();
        }
    }

    public static void main(String[] args) {
        Thread greedy1400 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Maxim-and-Discounts", 1 << 26);
        greedy1400.start();
        try {
            greedy1400.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int q = fr.nextInt();
        int discounts[] = new int[q];
        for (int i = 0; i < q; i++)
            discounts[i] = fr.nextInt();
        final int n = fr.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        fw.add(solve(q, discounts, n, nums));
        fw.flushMemory();
    }

    public static String solve(final int q, final int discounts[], final int n, final int nums[]) {
        // 1) sort ascending
        Arrays.sort(nums);

        // 2) find best (smallest) q*
        int qStar = Arrays.stream(discounts).min().orElse(0);

        long sum = 0L;
        int idx = n - 1; // start from most expensive

        // 3) process in blocks of size (q* + 2)
        while (idx >= 0) {
            // pay for q* items
            for (int paid = 0; paid < qStar && idx >= 0; paid++) {
                sum += nums[idx];
                idx--;
            }
            // skip up to 2 items free
            idx -= 2;
        }

        return Long.toString(sum);
    }

}
