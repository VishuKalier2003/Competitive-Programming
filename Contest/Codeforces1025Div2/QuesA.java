import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class QuesA {
    // Hack: Fastest Input Output reading in Java
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;        // Reading data between 0 to 255 characters
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
                x = x * 10 + (c-'0');
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
                x = x * 10 + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String[] args) {
        Thread extendThread = new Thread(null, 
        ()-> {
            try {
                callMain(args);
            } catch(IOException e) {
                e.getLocalizedMessage();
            }
        },
        "quesA",
        1 << 26);
        extendThread.start();
        try {
            extendThread.join();
        } catch(InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            output.append(solve(n, nums)).append("\n");
        }
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static StringBuilder solve(final int n, final int nums[]) {
        int count0 = 0, prev = -1;
        for(int i = 0; i < n; i++) {
            count0 += nums[i] == 0 ? 1 : 0;
            if(prev == 0 && nums[i] == 0)
                return new StringBuilder().append("Yes");
            prev = nums[i];
        }
        if(count0 == 0)
            return new StringBuilder().append("Yes");
        return new StringBuilder().append("No");
    }
}
