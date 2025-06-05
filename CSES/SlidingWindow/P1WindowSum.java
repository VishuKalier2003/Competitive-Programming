
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P1WindowSum {
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
        Thread t = new Thread(null, () -> {
            try {callMain(args);}
            catch(IOException e) {e.getLocalizedMessage();}
        }, "window-sum", 1 << 26);
        t.start();
        try {t.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final int n = fr.readInt(), k = fr.readInt();
        long pr[] = new long[n+1], nums[] = new long[n];
        final int x = fr.readInt(), a = fr.readInt(), b = fr.readInt(), c = fr.readInt();
        nums[0] = x;
        pr[1] = x;
        for(int i = 1; i < n; i++) {
            nums[i] = ((nums[i-1] * a) + b) % c;
            pr[i+1] = pr[i] + nums[i];
        }
        solve(n, k, nums, pr);
    }

    public static void solve(final int n, final int k, final long nums[], final long pr[]) {
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        long xor = 0l;
        for(int i = k; i < n+1; i++)
            // Xor sum combined with prefix sums to find window sum and xor of all windows
            xor ^= (pr[i] - pr[i-k]);
        out.append(xor);
        wr.write(out.toString());
        wr.flush();
    }
}
