import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Gellyfish {
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
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

        public int readInt() throws IOException {
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

        public long readLong() throws IOException {
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

        public String readString() throws IOException {
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

        public String readLine() throws IOException {
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
            sb.setLength(0);
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Gellyfish-and-Pheony",
                1 << 26);
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
        int t = fr.readInt();
        while (t-- > 0) {
            final int n = fr.readInt();
            int nums[] = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = fr.readInt();
            fw.attachOutput(solve(n, nums));
        }
        fw.printOutput();
    }

    public static int dp[];
    public static boolean seen[];
    public static int g;

    public static final int INF = 1_000_000;

    public static StringBuilder solve(final int n, int nums[]) {
        g = nums[0];
        int max = 0;
        for (int num : nums) {
            g = gcd(g, num);
            max = Math.max(max, num);
        }
        dp = new int[max + 1];
        seen = new boolean[max+1];
        Arrays.fill(dp, INF);
        for (int num : nums) {
            dp[num] = 0;
            seen[num] = true;
        }
        if(dp[g] == 0)
            return new StringBuilder().append(Arrays.stream(nums).filter(x -> x != g).count()).append("\n");
        int steps = bfs(nums, g);
        return new StringBuilder().append(steps+n-1).append("\n");
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // Treat operations as graph when we are given a multi-source operation
    public static int bfs(int nums[], int g) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int num : nums)
            q.add(num);
        int steps = 0;
        while (!q.isEmpty()) {
            int sz = q.size();
            for(int i = 0; i < sz; i++) {
                int x = q.poll();
                if(x == g)
                    return steps;
                for(int a : nums) {
                    int k = gcd(x, a);
                    if(!seen[k]) {
                        seen[k] = true;
                        q.offer(k);
                    }
                }
            }
            steps++;
        }
        return INF;
    }
}
