import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class FoxBox {
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
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        fw.add(solve(n, nums));
        fw.flushMemory();
    }

    public static List<List<Integer>> g;

    public static String solve(final int n, final int nums[]) {
        Arrays.sort(nums);
        List<List<Integer>> piles = new ArrayList<>();

        for (int x : nums) {
            boolean placed = false; // 1) reset flag for **this** box
            for (List<Integer> pile : piles) {
                if (pile.size() <= x) {
                    pile.add(x);
                    placed = true;
                    break; // 2) stop after first valid placement
                }
            }
            if (!placed) {
                List<Integer> newPile = new ArrayList<>();
                newPile.add(x);
                piles.add(newPile);
            }
        }

        return String.valueOf(piles.size());
    }
}
