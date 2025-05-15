// Ques- https://codeforces.com/problemset/problem/1355/A

// Note- Find logic of recurring
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem1355A {
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
            output.append(solve(fast.nextLong(), fast.nextLong())).append("\n");
        System.out.print(output);
    }

    public static long solve(long n, long k) {
        while(k > 1) {
            int d[] = extractMaxMin(n);
            n = n + (d[0] * d[1]);      // Perform the formula operation
            if(d[0] == 0)       // If the min digit is found 0, then next each operation will make the same number again and again
                break;
            k--;            // Decrease k since it might be attained before any 0 digit found
        }
        return n;
    }

    public static int[] extractMaxMin(long n) {     // extract min and max digit
        int minMax[] = new int[2];
        minMax[0] = 9;
        minMax[1] = 0;
        while(n > 0) {
            int digit = (int)(n % 10);
            // max and min digit
            minMax[1] = Math.max(minMax[1], digit);
            minMax[0] = Math.min(minMax[0], digit);
            n /= 10;
        }
        return minMax;
    }
}
