import java.io.*;
import java.util.*;

public class ChildToy {

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        int m = fr.nextInt();

        int[] v = new int[n + 1]; // 1-indexed
        for (int i = 1; i <= n; i++) {
            v[i] = fr.nextInt();
        }

        long totalEnergy = 0;
        for (int i = 0; i < m; i++) {
            int u = fr.nextInt();
            int w = fr.nextInt();
            totalEnergy += Math.min(v[u], v[w]);
        }

        System.out.println(totalEnergy);
    }

    // FastReader for faster input
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreElements())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
