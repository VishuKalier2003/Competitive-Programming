// https://codeforces.com/problemset/problem/1536/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Dulic {
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
        while(t-- > 0) {
            final int n = fast.nextInt();
            output.append(solve(n, fast.next())).append("\n");
        }
        System.out.print(output);
    }

    public static StringBuilder solve(final int n, final String s) {
        int prefixD[] = new int[n+1], prefixK[] = new int[n+1];
        Map<String, Integer> freqMap = new HashMap<>();
        final StringBuilder result = new StringBuilder();
        for(int i = 1; i <= n; i++) {
            char current = s.charAt(i-1);
            prefixD[i] = current == 'D' ? prefixD[i-1] + 1 : prefixD[i-1];
            prefixK[i] = current == 'K' ? prefixK[i-1] + 1 : prefixK[i-1];
            String ratio = ratio(prefixD[i], prefixK[i]);
            freqMap.put(ratio, freqMap.getOrDefault(ratio, 0)+1);
            result.append(freqMap.get(ratio)).append(" ");
        }
        return result;
    }

    public static String ratio(int d, int k) {
        if(d == 0)
            return "0:K";
        if(k == 0)
            return "D:0";
        int gcd = gcd(d, k);
        d /= gcd;
        k /= gcd;
        return d+":"+k;
    }

    public static int gcd(int a, int b) {
        if(b == 0)
            return a;
        return gcd(b, a % b);
    }
}
