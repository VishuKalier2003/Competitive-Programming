import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StrongVertices {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            long a[] = new long[n], b[] = new long[n];
            for(int i = 0; i < n; i++)
                a[i] = fast.nextLong();
            for(int i = 0; i < n; i++)
                b[i] = fast.nextLong();
            final long c[] = new long[n];
            for(int i = 0; i < n; i++)
                c[i] = a[i] - b[i];
            long max = Long.MIN_VALUE;
            for(int i = 0; i < n; i++)
                max = Math.max(max, c[i]);
            List<Integer> result = new ArrayList<>();
            for(int i = 0; i < n; i++)
                if(c[i] == max)
                    result.add(i+1);
            builder.append(result.size()).append("\n");
            for(int vertex : result)
                builder.append(vertex+" ");
            builder.append("\n");
        }
        System.out.print(builder);
    }
}
