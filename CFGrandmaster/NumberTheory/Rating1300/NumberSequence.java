// https://codeforces.com/problemset/problem/1454/D

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class NumberSequence {
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
        }, "number-to-sequence", 1 << 26);
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
            output.append(solve(fr.readLong())).append("\n");
        wr.write(output.toString());        // printwriter to print output
        wr.flush();
    }

    // When a sequence product is a number, then all the numbers of sequence are factors of the given number
    public static StringBuilder solve(long n) {
        // Find the prime factors of the number
        Map<Long, Integer> fMap = new HashMap<>();
        for(long i = 2; i*i <= n; i++)
            while(n % i == 0) {
                fMap.put(i, fMap.getOrDefault(i, 0)+1);
                n /= i;
            }
        if(n > 1)
            fMap.put(n, 1);
        int maxF = 0; long maxNum = 0;
        // The longest sequence with divisibility will be the sequence of max occurence of prime
        for(Map.Entry<Long, Integer> e : fMap.entrySet())
            if(e.getValue() > maxF) {
                maxF = e.getValue();
                maxNum = e.getKey();
            }
        long last = maxNum;
        // The last num stores the product of leftovers
        for(Map.Entry<Long, Integer> e : fMap.entrySet()) {
            long x = e.getKey(), f = e.getValue();
            if(x != maxNum)
                last *= exp(x, f);
        }
        final StringBuilder s = new StringBuilder();
        s.append(maxF).append("\n");
        for(int i = 0; i < maxF-1; i++)
            s.append(maxNum).append(" ");
        return s.append(last);
    }

    // Exponentiation
    public static long exp(long a, long b) {
        long res = 1L;
        while(b > 0) {
            if((b & 1) == 1)
                res = (res * a);
            a = (a * a);
            b >>= 1;
        }
        return res;
    }
}
