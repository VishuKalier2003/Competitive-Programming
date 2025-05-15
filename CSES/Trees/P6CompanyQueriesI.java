
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class P6CompanyQueriesI {
    public static class FastReader {
        private final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) >= '0' && c <= '9');
            return x;
        }
    }

    public static List<List<Integer>> tree;
    public static List<int[]> queries;

    public static void main(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        tree = new ArrayList<>();
        queries = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        for(int i = 0; i < q; i++)
            queries.add(new int[]{fast.nextInt(), fast.nextInt()});
        solve(n);
    }

    public static void solve(final int n) {}

    public static class SparseTable {
        public int table[][];
        public int log[];
        public int maxLog;

        public SparseTable(int n) {
            this.maxLog = 32 - Integer.numberOfLeadingZeros(n);
            this.table = new int[maxLog+1][n+1];
            this.log = new int[n+1];
            log[1] = 0;
            for(int i = 1; i <= n; i++)
                this.log[i] = this.log[i/2] + 1;
        }
    }
}
