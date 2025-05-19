import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSESPlanetQueries {
    public static class FastReader {            // Note: Fastest Input reader for Java (working on byte and not String)
        private final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;        // Note: Converting to ASCII character between 0 to 255 (Hexadecimal code 0xff)
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
                x = x * 10 + (c-'0');
            } while((c = read()) >= '0' && c <= '9');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0;
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

    public static Map<Integer, Integer> tree;
    public static List<int[]> queries;
    public static int maxK;

    public static void main(String[] args) throws IOException {     // Input reading main function
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        tree = new HashMap<>();
        for(int i = 1; i <= n; i++)
            tree.put(i, -1);
        int teleporter[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            teleporter[i] = fast.nextInt();
        queries = new ArrayList<>();
        maxK = 0;
        for(int i = 0; i < q; i++) {
            int x = fast.nextInt(), k = fast.nextInt();
            queries.add(new int[]{x, k});
            maxK = Math.max(maxK, k);
        }
        solve(n, teleporter);
    }

    public static void solve(final int n, final int teleporter[]) {
        for(int i = 1; i <= n; i++)
            tree.put(i, teleporter[i]);
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        BinaryLifting binaryLifting = new BinaryLifting(n, teleporter);
        for(int query[] : queries)
            output.append(binaryLifting.lifting(query[0], query[1])).append("\n");
        writer.write(output.toString());
        writer.flush();
    }

    public static final class BinaryLifting {
        private final int[][] lift;
        private final int maxLog, n;

        public BinaryLifting(final int n, final int nums[]) {
            this.n = n;
            this.maxLog = 32 - Integer.numberOfLeadingZeros(maxK);      // The max jump can be the max k value present there
            this.lift = new int[this.maxLog+1][this.n+1];
            for(int row[] : this.lift)
                Arrays.fill(row, -1);
            binaryLiftIterative(nums);
        }

        public void binaryLiftIterative(final int nums[]) {
            for(int i = 1; i <= this.n; i++)
                this.lift[0][i] = nums[i];
            for(int j = 1; j <= this.maxLog; j++)
                for(int i = 1; i <= n; i++)
                this.lift[j][i] = this.lift[j-1][this.lift[j-1][i]];
        }

        public int lifting(int root, int k) {
            int ancestor = root;
            for(int i = 0; i < this.maxLog; i++)
                if((k & (1 << i)) != 0) {
                    ancestor = this.lift[i][ancestor];
                    if(ancestor == -1)
                        break;
                }
            return ancestor;
        }
    }
}
