import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class P2Hamming {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ')
                if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do { x = 10 * x + (c - '0'); } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ') if (c < 0) return null;
            StringBuilder sb = new StringBuilder();
            do { sb.append((char)c); } while ((c = read()) > ' ');
            return sb.toString();
        }
    }

    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;
        public FastWriter() { this.pw = new PrintWriter(new OutputStreamWriter(System.out)); this.sb = new StringBuilder(); }
        public void attachOutput(StringBuilder s) { sb.append(s); }
        public void printOutput() { pw.write(sb.toString()); pw.flush(); }
    }

    private static final StringBuilder output = new StringBuilder();

    public static void main(String[] args) throws IOException {
        Thread t = new Thread(null, () -> {
            try { callMain(args); } catch (IOException e) { e.getLocalizedMessage(); }
        }, "Hamming-Distance", 1 << 26);
        t.start();
        try { t.join(); } catch (InterruptedException iE) { iE.getLocalizedMessage(); }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), len = fr.nextInt();
        long nums[] = new long[n];
        Set<Long> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            nums[i] = Long.parseLong(fr.next(), 2);
            if (!set.add(nums[i])) {  // duplicate -> distance 0
                output.append(0);
                fw.attachOutput(output);
                fw.printOutput();
                return;
            }
        }
        solve(n, len, nums, fw);
    }

    public static void solve(int n, int len, long nums[], FastWriter fw) {
        int minDist = len; // max Hamming distance = len
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = Long.bitCount(nums[i] ^ nums[j]);
                if (dist < minDist) minDist = dist;
            }
        }
        output.append(minDist);
        fw.attachOutput(output);
        fw.printOutput();
    }
}
