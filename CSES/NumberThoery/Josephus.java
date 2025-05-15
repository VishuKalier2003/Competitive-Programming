// https://cses.fi/problemset/task/2164/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Josephus {
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
        return cyclicQuery(n, k);
    }

    public static long cyclicQuery(long n, long k) {
        if(n == 1)
            return 1;
        long removed = n/2;
        if(k <= removed)
            return 2*k;
        long newN = n-removed, newK = k-removed;
        long jumps = cyclicQuery(newN, newK);
        if((n & 1) == 0)
            return 2*jumps-1;
        else {
            long shift = ((jumps + removed - 1) % newN) + 1;
            return 2*shift - 1;
        }
    }
}
