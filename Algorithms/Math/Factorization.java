

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Factorization {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.getLocalizedMessage();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String args[]) {
        FastReader fr = new FastReader();
        System.out.println(solve(fr.nextInt()));
    }

    public static StringBuilder solve(int n) {
        Map<Integer, Integer> fMap = new HashMap<>();
        for(int i = 2; i*i <= n; i++) {
            while(n % i == 0) {
                fMap.put(i, fMap.getOrDefault(i, 0) + 1);
                n /= i;
            }
        }
        if(n > 1)
            fMap.put(n, 1);
        return new StringBuilder().append(factorize(fMap));
    }

    public static List<Integer> factorize(Map<Integer, Integer> fMap) {
        List<Integer> lst = new ArrayList<>();
        lst.add(1);
        for(Map.Entry<Integer, Integer> e : fMap.entrySet()) {
            int p = e.getKey(), exp = e.getValue();     // extract base and power
            int size = lst.size();      // current list size
            int pow[] = new int[exp];
            pow[0] = p;
            for(int i = 1; i < exp; i++)        // find powers of p till exp
                pow[i] = pow[i-1] * p;
            for(int i = 0; i < size; i++) {     // for each element in list
                int base = lst.get(i);
                for(int f = 1; f <= exp; f++) { // multiply by each power of p and add in the list
                    lst.add(base * pow[f-1]);
                }
            }
        }
        return lst;
    }
}
