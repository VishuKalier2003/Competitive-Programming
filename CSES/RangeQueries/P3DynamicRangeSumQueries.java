import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P3DynamicRangeSumQueries {
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
        }, "Dynamic-Range-Sum-Queries-(https://cses.fi/problemset/task/1648)", 1 << 26);
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
        final int n = fr.nextInt(), q = fr.nextInt();
        long nums[] = new long[n+1];
        // for fenwick tree numbers are arranged in 1-based indexing, stored from [1, n+1)
        for(int i = 1; i <= n; i++)
            nums[i] = fr.nextLong();
        int queries[][] = new int[q][3];
        for(int i = 0; i < q; i++) {
            queries[i][0] = fr.nextInt();
            queries[i][1] = fr.nextInt();
            queries[i][2] = fr.nextInt();
        }
        solve(n, nums, q, queries);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final int n, final long nums[], final int q, final int queries[][]) {
        // Fenwick tree created with parameter n and will be of size n+1 with 1 based indexing
        Fenwick fen = new Fenwick(n);
        // Initial updates to be done
        for(int i = 1; i <= n; i++)
            fen.queryUpdate(i, nums[i]);
        for(int qry[] : queries) {
            int type = qry[0];
            if(type == 1) {
                int idx = qry[1], val = qry[2];
                // The point update is done on basis of delta (difference), not just simple swap
                long delta = val - nums[idx];
                fen.queryUpdate(idx, delta);
                nums[idx] = val;        // storing new value in the array for later comparison
            } else
                output.append(fen.queryRange(qry[1], qry[2])).append("\n");
        }
    }

    public static class Fenwick {
        // Tree queries are inclusive (all)
        private final long bit[];       // tree
        private final int n;

        public Fenwick(int size) {
            // The size is assigned with n, never keep n as size+1
            this.n = size;      
            // The tree is assigned with size n+1
            this.bit = new long[n+1];
        }

        public void queryUpdate(int idx, long delta) {
            // Updates are from x to n, so add lsb
            for(int i = idx; i <= n; i += i & -i)
                bit[i] += delta;
        }

        public long queryPoint(int idx) {
            long sum = 0L;
            // Query are from x to 0, so subtract lsb
            for(int i = idx; i > 0; i -= i & -i)
                sum += bit[i];
            return sum;
        }

        public long queryRange(int left, int right) {
            // Range query of range [l,r] inclusive, so prefix[r] - prefix[l-1]
            return queryPoint(right) - queryPoint(left-1);
        }
    }
}