import java.io.*;
import java.util.*;

public class BricksBas {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        @SuppressWarnings("CallToPrintStackTrace")
        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try { st = new StringTokenizer(br.readLine()); }
                catch (IOException e) { e.printStackTrace(); }
            }
            return st.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
    }

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        int t = fr.nextInt();
        StringBuilder out = new StringBuilder();
        while (t-- > 0) {
            int n = fr.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = fr.nextInt();
            Arrays.sort(a);

            long maxScore = 0;

            // Case 1: (a[i] - a[i-1]) + (a[i] - a[0]) for i in [2, n-1]
            for (int i = 2; i < n; i++) {
                long score = (long)(a[i] - a[i - 1]) + (a[i] - a[0]);
                maxScore = Math.max(maxScore, score);
            }

            // Case 2: (a[i+1] - a[i]) + (a[n-1] - a[i]) for i in [0, n-3]
            for (int i = 0; i <= n - 3; i++) {
                long score = (long)(a[i + 1] - a[i]) + (a[n - 1] - a[i]);
                maxScore = Math.max(maxScore, score);
            }

            out.append(maxScore).append('\n');
        }
        System.out.print(out);
    }
}
