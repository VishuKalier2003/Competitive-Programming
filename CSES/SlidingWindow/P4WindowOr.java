import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P4WindowOr {
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
            while(c != '\n' && c >= 0) {
                if(c != '\r')
                    sb.append((char)c);
                c = read();
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {callMain(args);}
            catch(IOException e) {e.getLocalizedMessage();}
        }, "window-or", 1 << 26);
        t.start();
        try {t.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    private static int f[];
    public static long res;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final int n = fr.readInt(), k = fr.readInt();
        long nums[] = new long[n];
        f = new int[64];
        final int x = fr.readInt(), a = fr.readInt(), b = fr.readInt(), c = fr.readInt();
        nums[0] = x;
        res = 0l;
        long or;
        fUpdate(nums[0]);
        for(int i = 1; i < k; i++) {
            nums[i] = ((nums[i-1] * a) + b) % c;
            fUpdate(nums[i]);
        }
        or = res;
        for(int i = k; i < n; i++) {
            fNegate(nums[i-k]);
            nums[i] = ((nums[i-1] * a) + b) % c;
            fUpdate(nums[i]);
            or ^= res;
        }
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        out.append(or);
        wr.write(out.toString());
        wr.flush();
    }

    public static void fUpdate(long num) {
        while(num != 0) {
            // Gives the index of the LSB (since it counts the number of trailing zeros from LSB side)
            int bit = Long.numberOfTrailingZeros(num);
            f[bit]++;
            if(f[bit] == 1L)
                res |= (1L << bit);
            // Subtracting 1 from num removes the LSB
            num &= (num - 1L);
        }
    }

    public static void fNegate(long num) {
        while(num != 0) {
            int bit = Long.numberOfTrailingZeros(num);
            f[bit]--;
            if(f[bit] == 0L)
                res ^= (1L << bit);
            num &= (num - 1L);
        }
    }
}
