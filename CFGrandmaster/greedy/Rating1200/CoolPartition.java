import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class CoolPartition {
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
        while (t-- > 0) {
            final int n = fr.nextInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fr.nextInt();
            fw.add(solve(n, nums));
        }
        fw.flushMemory();
    }

    public static String solve(final int n, final int[] nums) {
        Map<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < n; i++) {
            last.put(nums[i], i); // last occurrence of each number
        }

        int maxLast = 0;
        int count = 0;

        for (int i = 0; i < n; i++) {
            maxLast = Math.max(maxLast, last.get(nums[i]));
            if (i == maxLast) {
                count++;
            }
        }

        return count + "\n";
    }

}
