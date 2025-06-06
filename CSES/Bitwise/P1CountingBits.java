
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P1CountingBits {
    public static class FastReader {        // Note: FastReader for taking inputs
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

        public int nextInt() throws IOException {       // Integer input
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

        public long nextLong() throws IOException {     // Long input
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
        // Info: Threading for faster memory allocation and provision of larger memory 1 << 26 bytes
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
        output.append(f(n));        // Note: Recursive call
        writer.write(output.toString());
        writer.flush();
    }

    public static long f(long n) {
        if(n == 0)
            return 0;
        long xBit = Long.highestOneBit(n), temp = xBit;
        int index = -1;     // Info: Indexing from LSB starts with 0, hence we take -1 as empty case
        while(xBit != 0) {
            xBit >>= 1;
            index++;
        }
        // Note: The number of 1 bits between 1 to 2^x is 2 * (2^(x-1)), recursive formula
        long reduce = index * 1L << index-1;
        // There are a few step bits that contribute for 2^x bit
        return f(n - temp) + reduce + (n - temp + 1);
    }
}
