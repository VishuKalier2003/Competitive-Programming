import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class QueriesPermutation {
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
        final int n = fr.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        fw.attachOutput(solve(n, n, nums));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int q, final int nums[]) {
        // Note: Fenwick tree of size nums length + queries length (Order statistics format)
        int N = q + n;
        /**
         * Info: Order statistics technique
        The tree will be of size N, query updates the prefix so when we push or reorder, we will do -1 for prefix range(1, old)
        and +1 for prefix range (1, new) and push the query pointer to left at each query
        */
        Fenwick fenwick = new Fenwick(N);
        int pos[] = new int[n+1];   // The pos array will store the pos of values hence has to be of size n
        for(int i = 1; i <= n; i++) {       // Info: the indexing has to be kept 1 based indexing
            pos[i] = q + i;     // updating the index for start
            fenwick.pointUpdate(pos[i], +1);
        }
        int queryFront = q;     // Info: the query variable which will move left after each query
        final int ans[] = new int[q];
        for(int i = 0; i < q; i++) {
            int query = nums[i];
            // store the answer since I want to check the indices before (excluding current element)
            ans[i] = fenwick.pointQuery(pos[query])-1;
            fenwick.pointUpdate(pos[query], -1);        // decrementing the prefix range [1, old]
            pos[query] = queryFront--;
            fenwick.pointUpdate(pos[query], +1);        // incrementing the prefix range [1, new]
        }
        final StringBuilder output = new StringBuilder();
        for(int a : ans)
            output.append(a).append(" ");
        return output;
    }

    public static class Fenwick {
        private final int bit[];
        private final int n;

        public Fenwick(int size) {
            this.n = size;
            this.bit = new int[n+1];
        }

        public void pointUpdate(int index, int delta) {     // point updates
            while(index <= n) {
                bit[index] += delta;
                index += (index & -index);
            }
        }

        public int pointQuery(int index) {      // point queries
            int sum = 0;
            while(index > 0) {
                sum += bit[index];
                index -= (index & -index);
            }
            return sum;
        }
    }
}
