// IMP- Bit Manipulation (Max bit set)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Construction {
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
        while(tests-- > 0) {        // For each test case
            int n = fast.nextInt();
            long k = fast.nextLong();
            builder.append(maxBitConstruction(n, k)).append("\n");
        }
        System.out.print(builder);
    }

    public static String maxBitConstruction(int n, long k) {
        if(n == 1)      // when n is 1, then we need to take k itself
            return k+" "+"0 ".repeat(n-1);
        int x = 0;      // Since the k is long, we will traverse till 64
        for(int i = 0; i < 64; i++)
            if(exp(2, i) > k) {     // IMP- The max cap of x, if we increase power further the sum will be always greater than k
                x = i-1;    // We want to find the max possible, as soon as we cross, break with storing i-1
                break;
            }
        StringBuilder array = new StringBuilder();
        // IMP- one number with max possible set bits will be exp(2,x)-1, which will have x set bits and the other will the difference between k and the given number
        array.append((exp(2,x)-1)+" ").append((k-(exp(2, x)-1))+" ");
        n -= 2;
        while(n-- > 0)      // Rest numbers can be zeros
            array.append("0 ");
        return array.toString();
    }

    public static long exp(int a, int b) {      // Fast Exponentiation
        long result = 1L;
        while(b > 0) {
            if((b & 1) == 1)        // If current bit is 1
                result = result * a;        // Multiply result by a, to raise its power
            a = a * a;
            b >>= 1;
        }
        return result;
    }
}
