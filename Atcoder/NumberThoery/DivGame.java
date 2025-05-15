// https://atcoder.jp/contests/abc169/tasks/abc169_d

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class DivGame {
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
        System.out.print(solve(fast.nextLong()));
    }

    public static long solve(long N) {
        Map<Long, Long> freqMap = new HashMap<>();
        for(long factor = 2; factor*factor <= N; factor++)
            while(N % factor == 0) {
                freqMap.put(factor, freqMap.getOrDefault(factor, 0l)+1l);
                N /= factor;
            }
        if(N > 1)
            freqMap.put(N, 1l);
        long moves = 0l;
        for(long frequency : freqMap.values()) {
            long count = 1l;
            while(frequency >= count) {
                frequency -= count;
                moves++;
                count++;
            }
        }
        return moves;
    }
}
