import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class QuesA {
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

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        for(int i = 0; i < n; i++)
            solve(fast.nextInt(), fast.nextInt(), fast.nextInt(), fast.nextInt());
    }

    public static void solve(final int n, final int m, final int p, final int q) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        if(n % p != 0)
            output.append("Yes\n");
        else {
            if(m == (n/p) * q)
                output.append("Yes\n");
            else
                output.append("No\n");
        }
        writer.write(output.toString());
        writer.flush();
    }
}
