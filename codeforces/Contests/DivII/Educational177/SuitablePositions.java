import java.io.*;
import java.util.*;

public class SuitablePositions {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder output = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());

        while (t-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            long k = Long.parseLong(st.nextToken());
            long x = Long.parseLong(st.nextToken());

            long[] a = new long[n];
            st = new StringTokenizer(br.readLine());
            long S = 0;
            for (int i = 0; i < n; i++) {
                a[i] = Long.parseLong(st.nextToken());
                S += a[i];
            }

            // Total sum of array b
            long total = S * k;

            // If even the entire array b has sum less than x, no starting position works.
            if(total < x) {
                output.append("0\n");
                continue;
            }

            // Compute prefix sums for one period: A[0..n]
            long[] A = new long[n+1];
            A[0] = 0;
            for (int i = 1; i <= n; i++) {
                A[i] = A[i-1] + a[i-1];
            }

            // For each remainder r (which corresponds to A[r] for r = 0...n-1), count valid t.
            long ans = 0;
            for (int r = 0; r < n; r++) {
                // We need: t * S + A[r] <= total - x
                long remain = total - x - A[r];
                if (remain < 0) continue; // no valid t for this remainder
                long T = remain / S; // maximum t such that the inequality holds
                // t can be from 0 to k-1, so valid count is min(k, T+1)
                long validCount = Math.min(k, T + 1);
                ans += validCount;
            }

            output.append(ans).append("\n");
        }
        System.out.print(output);
    }
}
