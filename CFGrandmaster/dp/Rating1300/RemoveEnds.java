import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class RemoveEnds {
    public static class FastReader {
        BufferedReader buffer;
        StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(buffer.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }

    public static class FastWriter {
        StringBuilder sb = new StringBuilder();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        public void add(String s) {
            sb.append(s);
        }

        public void flush() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread worker = new Thread(null, () -> {
            try {
                run();
            } catch (IOException ignored) {}
        }, "RemoveEnds", 1 << 26);
        worker.start();
        worker.join();
    }

    private static void run() throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        int t = fr.nextInt();
        while (t-- > 0) {
            int n = fr.nextInt();
            long[] nums = new long[n];
            for (int i = 0; i < n; i++) {
                nums[i] = fr.nextLong();
            }
            fw.add(solve(n, nums));
        }

        fw.flush();
    }

    private static String solve(int n, long[] nums) {
        long[] pre = new long[n + 1];
        long[] suf = new long[n + 1];

        // pre[i] = sum of |nums[j]| for j in [0..i-1] where nums[j] < 0
        for (int j = 0; j < n; j++) {
            pre[j + 1] = pre[j] + (nums[j] < 0 ? nums[j] : 0L);
        }

        // suf[i] = sum of nums[j] for j in [i..n-1] where nums[j] > 0
        for (int j = n - 1; j >= 0; j--) {
            suf[j] = suf[j + 1] + (nums[j] > 0 ? (long) nums[j] : 0L);
        }

        long answer = 0L;
        for (int i = 0; i <= n; i++) {
            answer = Math.max(answer, pre[i] + suf[i]);
        }

        return answer + "\n";
    }
}
