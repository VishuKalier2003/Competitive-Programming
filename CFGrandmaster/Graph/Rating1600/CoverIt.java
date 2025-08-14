import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoverIt {
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
        }, "https://codeforces.com/problemset/problem/1176/E",
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
        while (t-- > 0) {
            final int n = fr.nextInt(), m = fr.nextInt();
            g = new ArrayList<>();
            for(int i = 0; i <= n; i++)
                g.add(new ArrayList<>());
            for(int j = 0; j < m; j++) {
                final int u = fr.nextInt(), v = fr.nextInt();
                g.get(u).add(v);
                g.get(v).add(u);
            }
            fw.attachOutput(solve(n));
        }
        fw.printOutput();
    }

    private static List<List<Integer>> g;

    public static StringBuilder solve(final int n) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        Set<Integer> red = new HashSet<>(), white = new HashSet<>();
        q.add(1);
        int lvl = 0;
        boolean vis[] = new boolean[n+1];
        vis[1] = true;
        while(!q.isEmpty()) {
            int sz = q.size();
            for(int i = 0; i < sz; i++) {
                int node = q.poll();
                if(lvl % 2 == 0)
                    red.add(node);
                else 
                    white.add(node);
                for(int neighbor : g.get(node))
                    if(!vis[neighbor]) {
                        q.offer(neighbor);
                        vis[neighbor] = true;
                    }
            }
            lvl++;
        }
        final int half = n/2;
        final StringBuilder output = new StringBuilder();
        if(red.size() <= half) {
            output.append(red.size()).append("\n");
            for(int e : red)
                output.append(e).append(" ");
            output.append("\n");
        }
        else {
            output.append(white.size()).append("\n");
            for(int e : white)
                output.append(e).append(" ");
            output.append("\n");
        }
        return output;
    }
}
