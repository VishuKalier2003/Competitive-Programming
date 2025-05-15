import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CounterExample {
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
        System.out.print(findCounterExample(fast.nextLong(), fast.nextLong()));
    }

    public static String findCounterExample(long l, long r) {
        if (r-l+1 < 3)      // If there are less than 3 numbers in the range ⇒ can't form (a, b, c) ⇒ return -1
            return "-1";
        if (l % 2 == 0)     // If l is even, then (l, l+1, l+2) is a valid triplet
            return l+" "+(l+1)+" "+(l+2);
        if (r-l+1 > 3)  // If l is odd and there are at least 4 numbers, then (l+1, l+2, l+3) starts from even and works too
            return (l+1)+" "+(l+2)+" "+(l+3);
        return "-1";
    }
}
