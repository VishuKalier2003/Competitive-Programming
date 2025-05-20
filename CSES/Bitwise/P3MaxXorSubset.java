import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P3MaxXorSubset {
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
        "max-xor-subset",
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
        final int n = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    public static void solve(final int n, final int nums[])  {
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        int gauss[] = new int[32];
        for(int i = 0; i < n; i++) {
            for(int k = 31; k >= 0; k--)
                if((nums[i] >> k & 1) == 1)
                    gauss[k]++;
        }
        int max = 0;
        for(int k = 0; k < 32; k++)
            if(gauss[k] % 2 != 0)
                max |= 1 << k;
        final StringBuilder output = new StringBuilder();
        output.append(max);
        writer.write(output.toString());
        writer.flush();
    }
}
