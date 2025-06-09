import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class P3BuildingRoads {
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
        }, "building-roads",
        1 << 26);
        t.start();
        try {t.join();}
        catch(InterruptedException iE) {iE.getLocalizedMessage();}
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final int n = fr.readInt(), m = fr.readInt();
        DSU dsu = new DSU(n+1);
        for(int i = 0; i < m; i++)
            dsu.union(fr.readInt(), fr.readInt());
        Set<Integer> unique = new HashSet<>();
        for(int i = 1; i <= n; i++)
            unique.add(dsu.find(i));
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        int prev = -1;
        out.append(unique.size()-1).append("\n");
        for(int curr : unique) {
            if(prev != -1)
                out.append(prev).append(" ").append(curr).append("\n");
            prev = curr;
        }
        wr.write(out.toString());
        wr.flush();
    }

    public static class DSU {
        public int parent[], rank[];
        public int n;

        public DSU(int n) {
            this.n = n;
            this.parent = new int[n];
            this.rank = new int[n];
            for(int i = 0; i < n; i++)
                parent[i] = i;
        }

        public int find(int x) {
            if(x != parent[x])
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rX = find(x), rY = find(y);
            if(rX != rY) {
                if(rank[rX] < rank[rY])
                    parent[rX] = rY;
                else if(rank[rY] < rank[rX])
                    parent[rY] = rX;
                else {
                    parent[rY] = rX;
                    rank[rX]++;
                }
            }
        }
    }
}