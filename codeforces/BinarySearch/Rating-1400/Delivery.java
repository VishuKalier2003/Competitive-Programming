// Imp- https://codeforces.com/problemset/problem/1443/C
// Score- 18/100

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Delivery {
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
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            long courier[] = new long[n], purchase[] = new long[n];
            for(int i = 0; i < n; i++)
                courier[i] = fast.nextLong();
            for(int j = 0; j < n; j++)
                purchase[j] = fast.nextLong();
            output.append(solve(n, courier, purchase)).append("\n");
        }
        System.out.print(output);
    }

    public static long solve(final int n, long courier[], long purchase[]) {
        long ansMin = 1, ansMax = 0, result = 0;
        for(int i = 0; i < n; i++)
            ansMax += Math.max(courier[i], purchase[i]);
        while(ansMin <= ansMax) {
            long middle = ansMin + ((ansMax - ansMin) >> 1);
            if(greedy(n, courier, purchase, middle)) {
                result = middle;
                ansMax = middle-1;
            }
            else    ansMin = middle+1;
        }
        return result;
    }

    public static boolean greedy(final int n, final long courier[], final long purchase[], final long X) {
        long time = 0l;
        for(int i = 0; i < n; i++) {
            if(courier[i] > X)
                time += purchase[i];
            if(time > X)
                return false;
        }
        return true;
    }
}
