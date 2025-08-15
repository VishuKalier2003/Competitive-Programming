import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class XOUR {
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
        }, "https://codeforces.com/problemset/problem/1971/G",
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
        int t = fr.nextInt();
        while(t-- > 0) {
            final int n = fr.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fr.nextInt();
            fw.attachOutput(solve(n, nums));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int nums[]) {
        Map<Integer, PriorityQueue<int[]>> sortedMap = new HashMap<>();
        for(int i = 0; i < n; i++) {
            int num = nums[i] & ~3;
            if(!sortedMap.containsKey(num))
                sortedMap.put(num, new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])));
            sortedMap.get(num).add(new int[]{nums[i], i});
        }
        final StringBuilder output = new StringBuilder();
        int ans[] = new int[n];
        for(PriorityQueue<int[]> heap : sortedMap.values()) {
            // Note: Unique technique to sort by nums and swap in O(n log n)
            List<Integer> indexes = new ArrayList<>(), numbers = new ArrayList<>();
            // nums would arrive in sorted order because of the heap
            while(!heap.isEmpty()) {
                // Insert indexes and numbers in separate lists
                int x[] = heap.poll();
                indexes.add(x[1]);
                numbers.add(x[0]);
            }
            int sz = indexes.size();
            // Sort indexes and that would lead to placing smallest element in specified indexes
            Collections.sort(indexes);
            for(int i = 0; i < sz; i++)
                // map indexes (sorted) with the values into an array
                ans[indexes.get(i)] = numbers.get(i);
        }
        for(int i = 0; i < n; i++)
            output.append(ans[i]).append(" ");
        output.append("\n");
        return output;
    }
}
