import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class NewYear {
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
        }, "New-Year", 1 << 26);
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
        final int n = fr.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        output.append(solve(n, nums)).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(final int n, final int nums[]) {
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n-1; i++) {
            if(nums[i] == 0)
                sb.append("R");
            else {
                sb.append("PRL".repeat(nums[i]));
                sb.append("R");
            }
        }
        if(nums[n-1] != 0)
            sb.append("PLR".repeat(nums[n-1]));
        return sb;
    }
}
