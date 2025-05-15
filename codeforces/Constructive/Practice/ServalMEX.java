import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ServalMEX {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        StringBuilder output = new StringBuilder();

        while (t-- > 0) {
            output.append(solve(fast));
        }

        System.out.print(output);
    }

    private static String solve(FastReader fast) {
        int n = fast.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = fast.nextInt();
        }

        // List to store each operation as an array of two integers.
        List<int[]> ops = new ArrayList<>();
        int mid = n / 2;
        int r = n;

        // If there is at least one 0 in the second half of the array.
        boolean found = false;
        for (int i = mid; i < n; i++) {
            if (a[i] == 0) {
                found = true;
                break;
            }
        }
        if (found) {
            // Operation on subarray from mid+1 to n (1-indexed)
            ops.add(new int[]{mid + 1, n});
            r -= (n - mid - 1);
        }

        // If there is at least one 0 in the first half of the array.
        found = false;
        for (int i = 0; i < mid; i++) {
            if (a[i] == 0) {
                found = true;
                break;
            }
        }
        if (found) {
            // Operation on subarray from 1 to mid (1-indexed)
            ops.add(new int[]{1, mid});
            r -= (mid - 1);
        }

        // Final operation
        ops.add(new int[]{1, r});

        // Prepare the output for this test case.
        StringBuilder sb = new StringBuilder();
        sb.append(ops.size()).append("\n");
        for (int[] op : ops) {
            sb.append(op[0]).append(" ").append(op[1]).append("\n");
        }
        return sb.toString();
    }
}
