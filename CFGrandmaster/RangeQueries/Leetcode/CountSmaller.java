import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CountSmaller {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
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

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }
    }

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

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "https://leetcode.com/problems/count-of-smaller-numbers-after-self/description/", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt();
        int nums[] = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = fr.nextInt();
        fw.attachOutput(solve(n, nums));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int nums[]) {
        Map<Integer, Integer> coorMap = coordinateCompression(n, nums);
        final int len = coorMap.values().size();
        FenwickTree fenwick = new FenwickTree(len);
        final int ans[] = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int coor = coorMap.get(nums[i]);
            // Info: Store before update so it doesn't get added into the prefix range and accounting for the values <= coor so coor-1
            ans[i] = fenwick.pointQuery(coor-1);
            // Update the frequency and prefix range by +1
            fenwick.pointUpdate(coor, +1);
        }
        final StringBuilder output = new StringBuilder();
        for(int a : ans)
            output.append(a).append(" ");
        return output;
    }

    public static Map<Integer, Integer> coordinateCompression(final int n, final int a[]) {
        int nums[] = Arrays.copyOf(a, n);
        int idx = 1;    // Note: Indexing in Fenwick always start at 1 (never 0, else will hit TLE)
        int prev = Integer.MIN_VALUE;
        Arrays.sort(nums);
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if (num != prev) {
                map.put(num, idx);
                idx++;
                prev = num;
            }
        }
        return map;
    }

    public static class FenwickTree {
        private final int tree[];
        private final int n;

        public FenwickTree(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }

        public void pointUpdate(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += index & -index;
            }
        }

        public int pointQuery(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & -index;
            }
            return sum;
        }

        public int rangeQuery(int l, int r) {
            return pointQuery(r) - pointQuery(l - 1);
        }
    }
}
