import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TupliNum {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public String next() throws IOException { // reading string (whitespace exclusive)
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "https://leetcode.com/problems/queries-on-a-permutation-with-key/", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        int t = fr.nextInt();
        for(int k = 1; k <= t; k++) {
            fr.nextLine();
            final int n = fr.nextInt(), m = fr.nextInt();
            int nums[] = new int[n];
            int max = 0;
            for(int i = 0; i < n; i++) {
                nums[i] = fr.nextInt();
                max = Math.max(max, nums[i]);
            }
            List<int[]> queries = new ArrayList<>();
            for(int i = 0; i < m; i++)
                queries.add(new int[]{fr.nextInt(), fr.nextInt()});
            fw.attachOutput(solve(k, n, max, nums, m, queries));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int k, final int n, final int max, final int nums[], final int m, final List<int[]> queries) {
        final StringBuilder output = new StringBuilder();
        output.append("Case ").append(k).append(":\n");
        Fenwick fenwick = new Fenwick(max);
        int prev = -1;
        Set<Integer> s = new HashSet<>();
        for(int num : nums) {
            if(num >= prev && s.add(num)) {
                fenwick.pointUpdate(num, 1);
                prev = num;
            }
        }
        for(int q[] : queries) {
            int l = Math.min(q[0], max), r = Math.min(q[1], max);
            output.append(fenwick.rangeQuery(l, r)).append("\n");
        }
        return output;
    }

    public static class Fenwick {
        private final int tree[];
        private final int n;

        public Fenwick(int a) {
            this.n = a;
            this.tree = new int[a+1];
        }

        public void pointUpdate(int x, int delta) {
            for(int i = x; i <= n; i += i & -i)
                tree[i] += delta;
        }

        public int pointQuery(int x) {
            int sum = 0;
            for(int i = x; i > 0; i -= i & -i)
                sum += tree[i];
            return sum;
        }

        public int rangeQuery(int l, int r) {
            return pointQuery(r) - pointQuery(l-1);
        }
    }
}
