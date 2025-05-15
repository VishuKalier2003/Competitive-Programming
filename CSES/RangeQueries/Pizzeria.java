import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Pizzeria {
    public static class FastReader {        // FastReader
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
                    e.printStackTrace();
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

    public static class FastWriter {        // Fast Writer class
        BufferedWriter writer;

        public FastWriter() {this.writer = new BufferedWriter(new OutputStreamWriter(System.out));}

        public void write(String s) throws IOException {
            this.writer.write(s);
        }

        public void close() throws IOException {this.writer.close();}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        long nums[] = new long[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextLong();
        List<long[]> queries = new ArrayList<>();
        for(int j = 0; j < q; j++)
            queries.add(new long[]{fast.nextInt(), fast.nextInt(), fast.nextInt()});
    }

    public static void solve(final int n, final int q, final long nums[], final List<long[]> queries) {}
}
