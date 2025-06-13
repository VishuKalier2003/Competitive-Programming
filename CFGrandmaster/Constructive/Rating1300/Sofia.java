// https://codeforces.com/problemset/problem/1980/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Sofia {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
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

    public static void main(String[] args) {
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "sofia-and-lost-operations", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        int t = fr.nextInt();
        final StringBuilder out = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        while (t-- > 0) {
            final int n = fr.nextInt();
            int a[] = new int[n], b[] = new int[n];
            for (int i = 0; i < n; i++)
                a[i] = fr.nextInt();
            for (int j = 0; j < n; j++)
                b[j] = fr.nextInt();
            final int m = fr.nextInt();
            int d[] = new int[m];
            for (int j = 0; j < m; j++)
                d[j] = fr.nextInt();
            out.append(solve(n, m, a, b, d)).append("\n");
        }
        wr.write(out.toString());
        wr.flush();
    }

    // Need to check only the final state, so we check all the states that will lead to false
    public static String solve(final int n, final int m, final int a[], final int b[], final int d[]) {
        Map<Integer, Integer> fMap = new HashMap<>(), sfMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i])   // When the elements are different, they need atleast 1 operation
                fMap.put(b[i], fMap.getOrDefault(b[i], 0) + 1);
            else        // When two elements are same, they need atleast 0 operation
                sfMap.put(b[i], sfMap.getOrDefault(b[i], 0) + 1);
        }
        // If the last element to be updated is not in any of the two maps, then we can never create the array from the sequence
        if (!fMap.containsKey(d[m - 1]) && !sfMap.containsKey(d[m - 1]))
            return "No";
        for (int j = m-1; j >= 0; j--) {
            // Prune the frequency map one by one
            if (fMap.containsKey(d[j])) {
                fMap.put(d[j], fMap.get(d[j]) - 1);
                if(fMap.get(d[j]) == 0)
                    fMap.remove(d[j]);
            }
        }
        // Check if map becomes empty
        return fMap.isEmpty() ? "Yes" : "No";
    }
}