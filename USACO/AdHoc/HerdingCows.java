import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class HerdingCows {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {this.buffer = new BufferedReader(new FileReader("herding.in"));}

        @SuppressWarnings("CallToPrintStackTrace")
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

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        try (PrintWriter printer = new PrintWriter(new FileWriter("herding.out"))) {
            printer.print(solve(fast.nextInt(), fast.nextInt(), fast.nextInt()));
        }
    }

    public static StringBuilder solve(final int a, final int b, final int c) {
        int cows[] = new int[]{a, b, c};
        Arrays.sort(cows);
        final int gap1 = cows[1]-cows[0], gap2 = cows[2]-cows[1];
        int min;
        if(gap1 == 1 && gap2 == 1) {
            min = 0;
        }
        if(gap1 <= 2 || gap2 <= 2)
            min = 1;
        else
            min = 2;
        int max = Math.max(Math.abs(gap1), Math.abs(gap2))-1;
        return new StringBuilder().append(min).append("\n").append(max);
    }
}
