import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeirdSum {
    // FastReader for quick input
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    // FastWriter for batched output
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
        }, "Weird-Sum-(https://codeforces.com/problemset/problem/1648/A)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    private static Map<Integer, List<Integer>> rowMap, colMap;
    private static final StringBuilder output = new StringBuilder();

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), m = fr.nextInt();

        rowMap = new HashMap<>();
        colMap = new HashMap<>();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int c = fr.nextInt();
                rowMap.putIfAbsent(c, new ArrayList<>());
                colMap.putIfAbsent(c, new ArrayList<>());
                rowMap.get(c).add(i);
                colMap.get(c).add(j);
            }
        }

        long total = 0L;
        for (int color : rowMap.keySet()) {
            total += sumDistances(rowMap.get(color));
            total += sumDistances(colMap.get(color));
        }

        output.append(total).append("\n");
        fw.attachOutput(output);
        fw.printOutput();
    }

    // Compute sum of absolute differences in O(k log k) for a list of positions
    private static long sumDistances(List<Integer> list) {
        Collections.sort(list);
        long prefix = 0L, ans = 0L;
        for (int i = 0; i < list.size(); i++) {
            long val = list.get(i);
            ans += val * i - prefix;
            prefix += val;
        }
        return ans;
    }
}
