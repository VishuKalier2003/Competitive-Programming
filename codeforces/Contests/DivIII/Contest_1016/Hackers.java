import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Hackers {

    // FastReader for fast input.
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        String next() {
            while(st == null || !st.hasMoreTokens()){
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        int nextInt(){
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int t = in.nextInt();
        while(t-- > 0){
            int n = in.nextInt();
            int m = in.nextInt();

            // Read the desired final array a[0...n-1]
            String[] a = new String[n];
            for (int j = 0; j < n; j++) {
                a[j] = in.next();
            }

            // b[i][j] for i=0..m-1, j=0..n-1
            String[][] b = new String[m][n];
            for (int i = 0; i < m; i++){
                for (int j = 0; j < n; j++){
                    b[i][j] = in.next();
                }
            }

            // Check necessary condition:
            // For each position j, there must exist some i such that b[i][j] equals a[j].
            boolean possible = true;
            for (int j = 0; j < n; j++){
                boolean found = false;
                for (int i = 0; i < m; i++){
                    if(b[i][j].equals(a[j])){
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    possible = false;
                    break;
                }
            }
            if(!possible) {
                out.println(-1);
                continue;
            }

            // For each network i, count how many positions j satisfy b[i][j]==a[j].
            int best = 0;
            for (int i = 0; i < m; i++){
                int cnt = 0;
                for (int j = 0; j < n; j++){
                    if(b[i][j].equals(a[j])){
                        cnt++;
                    }
                }
                best = Math.max(best, cnt);
            }

            // Minimum operations required:
            // n operations (to “fill” every cell arbitrarily)
            // plus 2 operations (removal + controlled network op) for every wrong cell.
            // In worst-case the number of wrong cells is n - best.
            int ops = n + 2 * (n - best);
            out.println(ops);
        }
        out.flush();
        out.close();
    }
}
