import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Exam {
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
        if(n > 4) {
            builder.append(n).append("\n");
            for(int i = 1; i <= n; i += 2)
                builder.append(i+" ");
            for(int i = 2; i <= n; i += 2)
                builder.append(i+" ");
        }
        else if(n == 4)
            builder.append("4").append("\n").append("3 1 4 2");
        else if(n == 3)
            builder.append("2").append("\n").append("1 3");
        else
            builder.append("1").append("\n").append("1");
        System.out.print(builder);
    }
}
