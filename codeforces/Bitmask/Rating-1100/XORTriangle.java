import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class XORTriangle {
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
        int tests = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(tests-- > 0) {
            long num = fast.nextLong();
            long mask = (Long.highestOneBit(num) << 1)-1;       // IMP- Make a mask of all 1's up to largest One bit, since x-1 property
            boolean allOnes = (mask == num);        // If all ones or only a single 1 as the largest power
            boolean isPowerOf2 = (num == Long.highestOneBit(num));
            if(allOnes || isPowerOf2)
                builder.append("-1").append("\n");
            else {
                // IMP- We take all the turned off bits of num using (mask ^ num)
                long notPresentBit = mask ^ num,
                presentBit = Long.lowestOneBit(num);        // We take the single on bit of num
                builder.append(String.valueOf(notPresentBit | presentBit)).append("\n");
            }
        }
        System.out.print(builder);
    }
}
