import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class TwoSet {
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
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nexLong() throws IOException { // reading long
            long x = 0l, c;
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
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
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
        }, "Two-Sets",
                1 << 26);
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
        final int n = fr.nextInt(), a = fr.nextInt(), b = fr.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        fw.attachOutput(solve(n, a, b, nums));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int a, final int b, final int nums[]) {
        StringBuilder out = new StringBuilder();
        Map<Integer, Integer> idxMap = new HashMap<>();
        for (int i = 0; i < n; i++)
            idxMap.put(nums[i], i);

        int[] set = new int[n];
        Arrays.fill(set, -1); // -1 = unassigned

        for (int i = 0; i < n; i++) {
            if (set[i] != -1)
                continue; // already assigned

            Queue<Integer> q = new ArrayDeque<>();
            set[i] = 0; // arbitrary start, we’ll adjust if constraints force otherwise
            q.add(i);

            boolean ok = true;
            while (!q.isEmpty() && ok) {
                int u = q.poll();
                int curSet = set[u];

                // Partner for sum a
                int pa = a - nums[u];
                Integer paIdx = idxMap.get(pa);
                if (paIdx == null) {
                    // partner missing: must NOT be in set 0
                    if (curSet == 0) {
                        ok = false;
                        break;
                    }
                } else {
                    if (set[paIdx] == -1) {
                        set[paIdx] = 0;
                        q.add(paIdx);
                    } else if (set[paIdx] != 0) {
                        ok = false;
                        break;
                    }
                }

                // Partner for sum b
                int pb = b - nums[u];
                Integer pbIdx = idxMap.get(pb);
                if (pbIdx == null) {
                    // partner missing: must NOT be in set 1
                    if (curSet == 1) {
                        ok = false;
                        break;
                    }
                } else {
                    if (set[pbIdx] == -1) {
                        set[pbIdx] = 1;
                        q.add(pbIdx);
                    } else if (set[pbIdx] != 1) {
                        ok = false;
                        break;
                    }
                }
            }

            if (!ok) {
                return out.append("NO\n");
            }
        }

        out.append("YES\n");
        for (int i = 0; i < n; i++) {
            out.append(set[i]).append(i == n - 1 ? "\n" : " ");
        }
        return out;
    }

}
