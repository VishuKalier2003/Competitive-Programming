import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class P4FindingPeriods {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {}
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t2 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "border-finding", 1 << 26);
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.next());
    }

    public static void solve(final String text) {
        int n = text.length();
        int lps[] = new int[n];
        for(int i = 1, j = 0; i < n; i++) {
            while(j > 0 && text.charAt(i) != text.charAt(j))
                j = lps[j-1];
            if(text.charAt(i) == text.charAt(j))
                j++;
            lps[i] = j;
        }
        final StringBuilder output = new StringBuilder();
        List<Integer> borders = new ArrayList<>();
        for(int k = lps[n-1]; k > 0; k = lps[k-1]) 
            borders.add(k);     // Info: Adding the index
        // Hack: If border is of length p, then period is of length n-p and vice versa
        for(int i = 0; i < borders.size(); i++)
            output.append(n - borders.get(i)).append(" ");
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(n);
        writer.write(output.toString());
        writer.flush();
    }
}
