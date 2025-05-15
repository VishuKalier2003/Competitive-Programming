import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Knights {
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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int n = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < n; i++) {
            if(i % 2 == 0)
                for(int j = 0; j < n; j++)
                    builder.append(j % 2 == 0 ? "W" : "B");
            else
                for(int j = 0; j < n; j++)
                    builder.append(j % 2 == 0 ? "B" : "W");
            builder.append("\n");
        }
        System.out.print(builder);
    }
}
