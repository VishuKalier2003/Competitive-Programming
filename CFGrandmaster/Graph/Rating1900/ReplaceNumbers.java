import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplaceNumbers {
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
        }, "https://codeforces.com/problemset/problem/1620/E", 1 << 26);
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
        List<int[]> queries = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            int type = fr.nextInt();
            if(type == 1)
                queries.add(new int[]{type, fr.nextInt()});
            else
                queries.add(new int[]{type, fr.nextInt(), fr.nextInt()});
        }
        fw.attachOutput(solve(n, queries));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final List<int[]> queries) {
        int index = 0;
        for(int query[] : queries) {
            int type = query[0];
            if(type == 1) {
                addQuery(query[1], index);
                index++;
            } else
                updateQuery(query[1], query[2]);
        }
        final int ans[] = new int[index];
        for(Map.Entry<Integer, List<Integer>> e : uf.entrySet()) {
            int val = e.getKey();
            if(!e.getValue().isEmpty())
                for(int idx : e.getValue())
                    ans[idx] = val;
        }
        final StringBuilder output = new StringBuilder();
        for(int i = 0; i < index; i++)
            output.append(ans[i]).append(" ");
        return output;
    }

    private static final Map<Integer, List<Integer>> uf = new HashMap<>();

    private static void addQuery(int num, int index) {
        if(!uf.containsKey(num))
            uf.put(num, new ArrayList<>());
        uf.get(num).add(index);
    }

    private static void updateQuery(int before, int after) {
        if(before == after)
            return;
        if(!uf.containsKey(before))
            uf.put(before, new ArrayList<>());
        if(!uf.containsKey(after))
            uf.put(after, new ArrayList<>());
        List<Integer> lstBefore = uf.get(before), lstAfter = uf.get(after);
        if(lstBefore.size() > lstAfter.size()) {
            lstBefore.addAll(lstAfter);
            uf.put(after, lstBefore);
        } else {
            lstAfter.addAll(lstBefore);
            uf.put(after, lstAfter);
        }
        uf.put(before, new ArrayList<>());
    }
}
