import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P5BuildingTeams {
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
        Thread t = new Thread(null, () -> {
            try {callMain(args);}
            catch(IOException e) {e.getLocalizedMessage();}
        }, "building-teams",
        1 << 26);
        t.start();
        try {t.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    private static List<List<Integer>> g;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final int n = fr.readInt(), m = fr.readInt();
        g = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for(int i = 0; i < m; i++) {
            int x = fr.readInt(), y = fr.readInt();
            g.get(x).add(y);
            g.get(y).add(x);
        }
        solve(n, m);
    }

    public static boolean v[];
    public static int color[];

    public static void solve(final int n, final int m) {
        v = new boolean[n+1];
        color = new int[n+1];
        Arrays.fill(color, 1);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for(int i = 1; i <= n; i++)
            if(!v[i])
                bfs(i, q);
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        for(int i = 1; i <= n; i++) {
            int c = color[i];
            for(int child : g.get(i))
                if(c == color[child]) {
                    System.out.println("IMPOSSIBLE");
                    return;
                }
            out.append(c).append(" ");
        }
        wr.write(out.toString());
        wr.flush();
    }

    public static void bfs(int s, Deque<Integer> q) {
        q.add(s);
        v[s] = true;
        int lvl = 0;
        while(!q.isEmpty()) {
            int size = q.size();
            for(int i = 0; i < size; i++) {
                int node = q.poll();
                v[node] = true;
                color[node] = lvl % 2 == 0 ? 1 : 2;
                for(int child : g.get(node))
                    if(!v[child]) {
                        v[child] = true;
                        q.add(child);
                    }
            }
            lvl++;
        }
    }
}
