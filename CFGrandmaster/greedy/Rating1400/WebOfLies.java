import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class WebOfLies {
    public static class FastReader {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastReader() {
            reader = new BufferedReader(new InputStreamReader(System.in), 1 << 20);
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    String line = reader.readLine();
                    if (line == null) return null;
                    tokenizer = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException("I/O error in FastReader", e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        PrintWriter out = new PrintWriter(System.out, false);

        // Read initial parameters
        int n = fr.nextInt();   // number of nobles
        int m = fr.nextInt();   // number of initial friendships

        // Core state: for each noble i (1…n), track:
        //   • strongerCount[i] = number of friends > i
        //   • degree[i]        = total number of friends (unused in decision but kept for completeness)
        int[] strongerCount = new int[n + 1];
        int[] degree        = new int[n + 1];

        // All nobles begin alive (no stronger neighbor)
        int alive = n;

        // Ingest initial edges, updating alive & strongerCount in O(1) each
        for (int i = 0; i < m; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            degree[u]++; degree[v]++;
            if (u < v) {
                if (strongerCount[u] == 0) alive--;
                strongerCount[u]++;
            } else if (v < u) {
                if (strongerCount[v] == 0) alive--;
                strongerCount[v]++;
            }
        }

        // Process queries: type 1 = add, type 2 = remove, type 3 = report
        int q = fr.nextInt();
        while (q-- > 0) {
            int type = fr.nextInt();
            if (type == 3) {
                // Enterprise‑grade constant‑time report
                out.println(alive);
            } else {
                int u = fr.nextInt(), v = fr.nextInt();
                boolean add = (type == 1);

                // Only the weaker endpoint’s strongerCount matters
                if (u < v) {
                    if (add) {
                        degree[u]++; degree[v]++;
                        if (strongerCount[u] == 0) alive--;
                        strongerCount[u]++;
                    } else {
                        degree[u]--; degree[v]--;
                        strongerCount[u]--;
                        if (strongerCount[u] == 0) alive++;
                    }
                } else if (v < u) {
                    if (add) {
                        degree[u]++; degree[v]++;
                        if (strongerCount[v] == 0) alive--;
                        strongerCount[v]++;
                    } else {
                        degree[u]--; degree[v]--;
                        strongerCount[v]--;
                        if (strongerCount[v] == 0) alive++;
                    }
                }
            }
        }
        out.flush();
    }
}
