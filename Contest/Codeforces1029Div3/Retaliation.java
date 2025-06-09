import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Retaliation {

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tokenizer;
            int t = Integer.parseInt(reader.readLine().trim());
            while (t-- > 0) {
                int n = Integer.parseInt(reader.readLine().trim());
                long[] a = new long[n];
                
                tokenizer = new StringTokenizer(reader.readLine());
                for (int i = 0; i < n; i++) {
                    a[i] = Long.parseLong(tokenizer.nextToken());
                }
                long denom = (long)n * n - 1;
                long numY = (long)n * a[0] - a[n - 1];
                long numX = (long)n * a[n - 1] - a[0];
                if (numY % denom != 0 || numX % denom != 0) {
                    System.out.println("NO");
                    continue;
                }
                long y = numY / denom;
                long x = numX / denom;
                if (x < 0 || y < 0) {
                    System.out.println("NO");
                    continue;
                }
                boolean feasible = true;
                for (int i = 0; i < n; i++) {
                    long expected = x * (i + 1) + y * (n - i);
                    if (a[i] != expected) {
                        feasible = false;
                        break;
                    }
                }
                System.out.println(feasible ? "YES" : "NO");
            }
        }
    }
}
