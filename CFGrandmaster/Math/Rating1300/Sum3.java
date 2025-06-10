// https://codeforces.com/problemset/problem/1692/F

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Sum3 {
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
        }, "3-sum", 1 << 26);
        math1300.start();
        try {math1300.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while(t-- > 0) {
            final int n = fr.readInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fr.readInt();
            output.append(solve(n, nums)).append("\n");
        }
        wr.write(output.toString());        // printwriter to print output
        wr.flush();
    }

    // Evaluate 3sum by sorting and then by fixing first value at i, checking the remaining sum by two pointers
    public static String solve(final int n, final int nums[]) {
        int digit[] = new int[10];
        for(int num : nums)
            digit[num%10]++;
        List<Integer> lst = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            int f = Math.min(digit[i], 3);
            for(int j = 0; j < f; j++)
                lst.add(i);
        }
        int s = lst.size();
        for(int i = 0; i < s-2; i++)
            for(int j = i+1; j < s-1; j++)
                for(int k = j+1; k < s; k++) {
                    int x = lst.get(i) + lst.get(j) + lst.get(k);
                    if(x > 9)
                        x %= 10;
                    if(x == 3)
                        return "Yes";
                }
        return "No";
    }
}
