import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class WeirdSum {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), m = fast.nextInt();
        int mat[][] = new int[n][m];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                mat[i][j] = fast.nextInt();
        System.out.print(solve(n, m, mat));
    }

    public static long solve(final int n, final int m, int mat[][]) {
        Map<Integer, List<int[]>> groupMap = new HashMap<>();
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= m; j++) {
                int color = mat[i-1][j-1];
                groupMap.putIfAbsent(color, new ArrayList<>());
                groupMap.get(color).add(new int[]{i, j});
            }
        long sum = 0l;
        for(List<int[]> group : groupMap.values()) {
            int size = group.size();
            Collections.sort(group, new Comparator<int[]>() {
                @Override
                public int compare(int[] a, int[] b) {
                    if(a[0] == b[0])
                        return Integer.compare(a[1], b[1]);
                    return Integer.compare(a[0], b[0]);
                }
            });
            for(int[] cell : group)
                sum += ((cell[0] + cell[1]) * size--);
        }
        return sum;
    }
}
