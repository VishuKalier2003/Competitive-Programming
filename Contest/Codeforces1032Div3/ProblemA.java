import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ProblemA {
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
        }, "problem-A",
        1 << 26);
        t.start();
        try {t.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fr.readInt(), x = fr.readInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fr.readInt();
            output.append(solve(n, x, nums)).append("\n");
        }
        PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        wr.write(output.toString());
        wr.flush();
    }

    public static int solve(final int n, final int x, final int nums[]) {
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for(int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        int cost = Math.min(Math.abs(x-min), Math.abs(x-max));
        return cost + (max - min);
    }
}
