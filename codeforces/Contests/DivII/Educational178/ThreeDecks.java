import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ThreeDecks {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0)
            output.append(solve(fast.nextInt(), fast.nextInt(), fast.nextInt())).append("\n");
        System.out.print(output);
    }

    public static StringBuilder solve(int a, int b, int c) {
        long sum = a + b + c;
        if(sum % 3 != 0)
            return new StringBuilder().append("NO");
        c = c - (b-a);
        long k = sum / 3;
        if(b <= k)
            return new StringBuilder().append("YES");
        return new StringBuilder().append("NO");
    }
}
