// https://codeforces.com/problemset/problem/1294/C

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductOfThree {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer);       // Stores the entire buffer data in read
                if(len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while((c = read()) <= ' ')      // While whitespace is not provided
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long readLong() throws IOException {
            long x = 0l, c;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
            int c;
            while((c = read()) <= ' ')      // Read until whitespace
                if(c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
            } while((c = read()) > ' ');
            return sb.toString();
        }

        public String readLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if(c < 0)
                return null;
            while(c != '\n' && c >= 0)
                if(c != '\r')
                    sb.append((char)c);
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread math1300 = new Thread(null, () -> {
            try {callMain(args);}
            catch(IOException e) {e.getLocalizedMessage();}
        }, "Product-of-three", 1 << 26);
        math1300.start();
        try {math1300.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while(t-- > 0)
            output.append(solve(fr.readLong())).append("\n");       // read input and output
        wr.write(output.toString());        // printwriter to print output
        wr.flush();
    }

    public static List<Long> factors;

    public static StringBuilder solve(long n) {
        Map<Long, Integer> fMap = new HashMap<>();      // Creates factor map
        long temp = n;
        factors = new ArrayList<>();
        for(long i = 2; i*i <= n; i++) {
            while(n % i == 0) {
                fMap.put(i, fMap.getOrDefault(i, 0)+1);
                n /= i;
            }
        }
        if(n > 1)
            fMap.put(n, fMap.getOrDefault(n, 0)+1);
        // Factorize using prime factors (10^12 can have atmost 8 prime factors)
        piFactors(new ArrayList<>(fMap.keySet()), new ArrayList<>(fMap.values()), 0, 1L);
        return twoSum(temp);
    }

    // Note: Finding factors using prime factors
    public static void piFactors(List<Long> primes, List<Integer> powers, int idx, long curr) {
        // Use combinatorial and recursion to find the factors
        if(idx == primes.size()) {
            if(curr != 1)
                factors.add(curr);
            return;
        }
        long base = 1L;
        for(int j = 0; j <= powers.get(idx); j++) {     // For all powers [0..j] of current prime factor
            piFactors(primes, powers, idx+1, curr * base);          // recurse to next
            base *= primes.get(idx);        // multiply base with the ith prime [0..idx] prime
        }
    }

    // Note: Take one factor as first and use two-sum to find second and third factor
    public static StringBuilder twoSum(long n) {
        n /= factors.get(0);
        long n1 = factors.get(0);
        int s = factors.size();
        Set<Long> comp = new HashSet<>();       // set to store complement
        for(int i = 1; i < s; i++)
            comp.add(n/factors.get(i));
        for(int i = 1; i < s; i++)
            // checking if complement exists and complement and the factor are not same
            if(comp.contains(factors.get(i)) && factors.get(i) != n/factors.get(i)) {
                long n2 = factors.get(i), n3 = n/factors.get(i);
                return new StringBuilder().append("YES\n").append(n1).append(" ").append(n2).append(" ").append(n3);
            }
        return new StringBuilder().append("NO");        // case when two-sum evaluates to false
    }
}