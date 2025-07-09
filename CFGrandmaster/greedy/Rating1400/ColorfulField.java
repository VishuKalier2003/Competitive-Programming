import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ColorfulField {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static class FastWriter {
        public StringBuilder builder;
        public PrintWriter pr;

        public FastWriter() throws FileNotFoundException {
            this.builder = new StringBuilder();
            this.pr = new PrintWriter(new OutputStreamWriter(System.out));
        }

        public void add(String s) {
            this.builder.append(s);
        }

        public void flushMemory() {
            this.pr.write(builder.toString());
            this.pr.flush();
        }
    }

    public static void main(String[] args) {
        Thread greedy1400 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Colorful-Field", 1 << 26);
        greedy1400.start();
        try {
            greedy1400.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static Map<Integer, String> mp;
    public static List<int[]> barren, queries;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        mp = new HashMap<>();
        final int n = fr.nextInt(), m = fr.nextInt(), k = fr.nextInt(), t = fr.nextInt();
        barren = new ArrayList<>();
        queries = new ArrayList<>();
        for (int i = 0; i < k; i++)
            barren.add(new int[] { fr.nextInt(), fr.nextInt() });
        for (int j = 0; j < t; j++)
            queries.add(new int[] { fr.nextInt(), fr.nextInt() });
        fw.add(solve(n, m, k));
        fw.flushMemory();
    }

    public static String solve(final int n, final int m, final int k) {
        // 1) sort waste cells in true row-major order
        Collections.sort(barren, (a, b) -> {
            if (a[0] != b[0])
                return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });
        // 2) helper to compare int[] pairs lexicographically
        Comparator<int[]> cmp = (a, b) -> {
            if (a[0] != b[0])
                return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        };
        StringBuilder sb = new StringBuilder();
        for (int[] q : queries) {
            int i = q[0], j = q[1];
            // binarySearch: if found, returns idx>=0; else returns (-insertionPoint-1)
            int pos = Collections.binarySearch(barren, q, cmp);
            if (pos >= 0) {
                // exactly matches a waste cell
                sb.append("Waste\n");
                continue;
            }
            // insertion point = -(pos+1) = number of waste cells < (i,j)
            int wastesBefore = -pos - 1;
            // 3) compute overall index among cultivated cells
            int linearIndex = (i - 1) * m + (j - 1);
            int cultivatedIdx = linearIndex - wastesBefore;
            // 4) cycle through [Carrots, Kiwis, Grapes]
            switch (cultivatedIdx % 3) {
                case 0 -> sb.append("Carrots\n");
                case 1 -> sb.append("Kiwis\n");
                default -> sb.append("Grapes\n");
            }
        }
        return sb.toString();
    }

}
