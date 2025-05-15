import java.io.*;
import java.util.*;

public class FixPermutation {
    public static void main(String[] args) throws IOException {
        // Use BufferedReader for fast I/O.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder output = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());

        while(t-- > 0) {
            int n = Integer.parseInt(br.readLine().trim());
            int[] p = new int[n+1];      // 1-indexed permutation p[1..n]
            int[] pos = new int[n+1];    // pos[v] = index where value v is located.
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= n; i++) {
                p[i] = Integer.parseInt(st.nextToken());
                pos[p[i]] = i;
            }

            // Precompute the inverse mapping for p.
            // Since p is a permutation, for each number x (1..n), there is a unique index j with p[j] = x.
            // Let inv[x] = j.
            int[] inv = new int[n+1];
            for (int i = 1; i <= n; i++) {
                inv[p[i]] = i;
            }

            int[] d = new int[n+1]; // removal order (1-indexed)
            st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= n; i++) {
                d[i] = Integer.parseInt(st.nextToken());
            }

            // inClosure[i] = true if index i is forced to be fixed.
            boolean[] inClosure = new boolean[n+1];
            // We'll update the closure incrementally. The closure size is the number of operations required.
            int closureSize = 0;

            // Use a queue to process propagation.
            Deque<Integer> queue = new ArrayDeque<>();

            // answers for each query
            int[] ans = new int[n+1];

            for (int i = 1; i <= n; i++) {
                // Remove element d[i]: the corresponding index in the original permutation becomes forced.
                int forcedIndex = pos[d[i]];
                if (!inClosure[forcedIndex]) {
                    inClosure[forcedIndex] = true;
                    closureSize++;
                    queue.add(forcedIndex);
                }

                // Propagate: for every index j not yet fixed, if p[j] (a number) is already in the closure (as an index),
                // then j must be forced as well.
                while (!queue.isEmpty()) {
                    int cur = queue.poll();
                    // Find the unique index j such that p[j] == cur.
                    // (If no such j exists, then cur is not a valid number in 1..n, but here it always is.)
                    int j = inv[cur];
                    if (!inClosure[j]) {
                        inClosure[j] = true;
                        closureSize++;
                        queue.add(j);
                    }
                }

                ans[i] = closureSize; // minimal operations = size of the closure.
            }

            // Print the answer for the test case: queries 1 to n.
            for (int i = 1; i <= n; i++) {
                output.append(ans[i]).append(" ");
            }
            output.append("\n");
        }
        System.out.print(output);
    }
}
