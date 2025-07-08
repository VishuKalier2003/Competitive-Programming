// https://codeforces.com/problemset/problem/2118/C

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class MakeBeautiful {
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
        Thread greedy1900 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Make-beautiful", 1 << 26);
        greedy1900.start();
        try {
            greedy1900.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.nextInt();
        FastWriter fw = new FastWriter();
        while(t-- > 0) {
            final int n = fr.nextInt();
            final long k = fr.nextLong();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fr.nextLong();
            fw.add(solve(n, nums, k));
        }
        fw.flushMemory();
    }

    public static String solve(final int n, long nums[], long k) {
        int sum = 0;
        for(long num : nums)
            sum += Long.bitCount(num);
        // For flipping a bit x by just increasing v by +1 each time, we need 2^i operations
        for(int i = 0; i <= 60; i++) {
            // as per contraint 60 is the last bit
            long budget = (1L << i);
            for(long num : nums) {
                if((((num >> i) & 1L) != 1) && k >= budget) {
                    sum++;
                    k -= budget;
                }
            }
        }
        return new StringBuilder().append(sum).append("\n").toString();
    }
}
