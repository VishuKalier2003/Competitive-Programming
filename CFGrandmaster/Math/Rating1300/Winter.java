import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Winter {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored in single System.in.read()
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer);       // Stores the entire buffer data in read
                if(len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int readInt() throws IOException {
            int x = 0, c;
            while((c = read()) <= ' ')      // While whitespace is not provided
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long readLong() throws IOException {
            long x = 0l, c;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String readString() throws IOException {
            int c;
            while((c = read()) <= ' ')      // Read until whitespace
                if(c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
            } while((c = read()) > ' ');
            return sb.toString();
        }

        public String readLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if(c < 0)
                return null;
            while(c != '\n' && c >= 0)
                if(c != '\r')
                    sb.append((char)c);
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread math1300 = new Thread(null, () -> {
            try {callMain(args);}
            catch(IOException e) {e.getLocalizedMessage();}
        }, "winter-forever", 1 << 26);
        math1300.start();
        try {math1300.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    private static List<List<Integer>> tree;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.readInt();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while(t-- > 0) {
            final int n = fr.readInt(), m = fr.readInt();
            tree = new ArrayList<>();
            for(int i = 0; i <= n; i++)
                tree.add(new ArrayList<>());
            int deg[] = new int[n+1];
            for(int i = 0; i < m; i++) {
                int u = fr.readInt(), v = fr.readInt();
                tree.get(u).add(v);
                tree.get(v).add(u);
                deg[u]++;
                deg[v]++;
            }
            output.append(solve(n, deg)).append("\n");
        }
        wr.write(output.toString());        // printwriter to print output
        wr.flush();
    }

    public static String solve(final int n, final int deg[]) {
        int first = 0, second;
        Set<Integer> s = new HashSet<>();
        for(int i = 1; i <= n; i++)
            if(deg[i] == 1) {
                s.add(tree.get(i).get(0));
                first++;
            }
        second = s.size();
        return second+" "+(first/second);
    }
}
