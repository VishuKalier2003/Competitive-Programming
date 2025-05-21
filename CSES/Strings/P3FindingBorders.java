import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class P3FindingBorders {
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
        // Hack: LPS stores the length of longest proper prefix that i also a suffix till current index, prefix[i..0] and suffix[i..0]
        int lps[] = new int[n];
        for(int i = 1, j = 0; i < n; i++) {
            // Info: If characters don't match fall back to previous matching position
            while(j > 0 && text.charAt(i) != text.charAt(j))
                j = lps[j-1];   // Fall back to the previous border (matching position)
            if(text.charAt(i) == text.charAt(j))        // If match increment the length of current match, here j stores the length of max match
                j++;
            lps[i] = j;     // Info: Update at the current index
        }
        final StringBuilder output = new StringBuilder();
        List<Integer> borders = new ArrayList<>();
        for(int k = lps[n-1]; k > 0; k = lps[k-1])      // Note: Iteratively from last jump to the border
            borders.add(k);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        for(int i = borders.size()-1; i >= 0; i--)
            output.append(borders.get(i)).append(" ");
        writer.write(output.toString());
        writer.flush();
    }
}
