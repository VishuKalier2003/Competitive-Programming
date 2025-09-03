import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NatsyaPotions {
    // FastReader: fast input using 1MB buffer
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

        public long nextLong() throws IOException {
            long x = 0, c;
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

        public String nextLine() throws IOException {
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

    // FastWriter: reduces flush calls
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

    private static List<List<Integer>> g;
    private static List<Set<Integer>> gRev;
    private static int deg[];

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "https://codeforces.com/problemset/problem/1851/E", 1 << 26);
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
        int t = fr.nextInt();
        while (t-- > 0) {
            final int n = fr.nextInt(), k = fr.nextInt();
            deg = new int[n + 1];
            g = new ArrayList<>();
            gRev = new ArrayList<>();
            Set<Integer> infinite = new HashSet<>(), compulsory = new HashSet<>();

            for (int i = 0; i <= n; i++) {
                g.add(new ArrayList<>());
                gRev.add(new HashSet<>());
            }

            long cost[] = new long[n + 1];
            for (int i = 1; i <= n; i++)
                cost[i] = fr.nextLong();

            for (int i = 0; i < k; i++) {
                final int x = fr.nextInt();
                cost[x] = 0;
                infinite.add(x);
            }

            for (int i = 1; i <= n; i++) {
                int req = fr.nextInt();
                if (req == 0)
                    compulsory.add(i);
                for (int j = 0; j < req; j++) {
                    int node = fr.nextInt();
                    deg[i]++;
                    g.get(node).add(i); // directed edge: prerequisite -> potion
                    gRev.get(i).add(node); // reverse edge: for summing costs
                }
            }
            fw.attachOutput(solve(n, cost, infinite, compulsory));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final long costs[],
            final Set<Integer> infinite,
            final Set<Integer> compulsory) {
        Deque<Integer> q = new ArrayDeque<>();
        long bestCost[] = new long[n + 1];
        Arrays.fill(bestCost, Long.MAX_VALUE);

        // Start with nodes that have indegree 0
        for (int i = 1; i <= n; i++) {
            if (deg[i] == 0) {
                if (infinite.contains(i)) {
                    bestCost[i] = 0;
                } else {
                    bestCost[i] = costs[i];
                }
                q.add(i);
            }
        }

        while (!q.isEmpty()) {
            int node = q.poll();
            for (int neighbor : g.get(node)) {
                deg[neighbor]--;
                if (deg[neighbor] == 0) {
                    long sum = 0L;
                    for (int pre : gRev.get(neighbor)) {
                        sum += bestCost[pre];
                    }
                    // If node is infinite, keep 0; else min(direct cost, sum)
                    if (infinite.contains(neighbor)) {
                        bestCost[neighbor] = 0;
                    } else {
                        bestCost[neighbor] = Math.min(costs[neighbor], sum);
                    }
                    q.add(neighbor);
                }
            }
        }

        StringBuilder output = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if (bestCost[i] == Long.MAX_VALUE)
                output.append(costs[i]).append(" ");
            else
                output.append(bestCost[i]).append(" ");
        }
        return output.append("\n");
    }

}
