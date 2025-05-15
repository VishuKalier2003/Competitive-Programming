import java.io.*;
import java.util.*;

public class MainAndMEX {

    // Main function that reads input and writes output.
    public static void main(String[] args) throws IOException {
        // Use BufferedReader and PrintWriter for fast I/O.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        // Number of test cases.
        int t = Integer.parseInt(br.readLine());
        while (t-- > 0) {
            // Read n (length of array) and k (number of segments)
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            // Read the array.
            int[] a = new int[n];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++){
                a[i] = Integer.parseInt(st.nextToken());
            }

            // Binary search for the maximum feasible x.
            // We search in range [0, n+1) (n+1 is a safe upper bound).
            int lo = 0, hi = n + 1;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (feasible(a, mid, k))
                    lo = mid + 1;
                else
                    hi = mid;
            }
            // Maximum feasible x is lo - 1.
            out.println(lo - 1);
        }
        out.flush();
        out.close();
    }

    // Returns true if we can partition the array `a` into at least k segments
    // such that in each segment the MEX is at least x.
    static boolean feasible(int[] a, int x, int k) {
        // If x == 0, then every segment has MEX >= 0 (vacuously true)
        if (x == 0) return true;

        // Count of valid segments formed so far.
        int count = 0;
        // Boolean array to mark which numbers in [0, x-1] have been found in the current segment.
        boolean[] seen = new boolean[x];
        int foundCount = 0;
        for (int value : a) {
            // Only numbers less than x matter for our condition.
            if (value < x) {
                if (!seen[value]) {
                    seen[value] = true;
                    foundCount++;
                }
            }
            // Once we have seen all numbers in [0, x-1], we can cut a segment.
            if (foundCount == x) {
                count++;
                if (count >= k) return true;
                // Reset for the next segment.
                Arrays.fill(seen, false);
                foundCount = 0;
            }
        }
        return false;
    }
}
