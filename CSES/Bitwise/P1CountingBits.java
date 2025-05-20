
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P1CountingBits {
    public static class FastReader {
        private static final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static void main(String args[]) {
        Thread t1 = new Thread(null,
        () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, 
        "counting-set-bits",
        1 << 26);
        t1.start();
        try {
            t1.join();
        } catch(InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.nextLong());
    }

    public static void solve(final long n)  {
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(f(n));
        writer.write(output.toString());
        writer.flush();
    }

    public static long f(long n) {
        if(n == 0)
            return 0;
        long xBit = Long.highestOneBit(n), temp = xBit;
        int index = -1;
        while(xBit != 0) {
            xBit >>= 1;
            index++;
        }
        long reduce = 1L << index-1;
        return f(n - temp) + (index * reduce) + (n - temp + 1);
    }

    public static long exp(int a, int b) {
        long result = 1L;
        while(b != 0) {
            if((b & 1) == 1)
                result = result * a;
            a = a * a;
            b >>= 1;
        }
        return result;
    }
}
