/* https://codeforces.com/problemset/problem/379/C - Greedy, sorting */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class RatingChange {
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
        final int n = fr.nextInt();
        Node nums[] = new Node[n];
        for (int i = 0; i < n; i++)
            nums[i] = new Node(fr.nextLong(), i);
        fw.add(solve(n, nums));
        fw.flushMemory();
    }

    public static class Node {
        public long num;
        public int index;

        public Node(long n, int idx) {
            this.num = n;
            this.index = idx;
        }

        public long getNum() {
            return this.num;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public static String solve(final int n, final Node nums[]) {
        Arrays.sort(nums, (a, b) -> Long.compare(a.getNum(), b.getNum()));
        long value = nums[0].getNum();
        long res[] = new long[n];
        res[nums[0].getIndex()] = value;        // result array to store values index wise
        for (int i = 1; i < n; i++) {
            if (value >= nums[i].getNum())
                res[nums[i].getIndex()] = ++value;
            else
                res[nums[i].getIndex()] = value = nums[i].getNum();
        }
        final StringBuilder sb = new StringBuilder();
        for(long r : res)
            sb.append(r).append(" ");
        return sb.toString();
    }
}
