import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MinimumCostRemoval {

    // FastReader class for fast I/O
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException  e) {
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
        while (t-- > 0) {
            String n = fast.next();
            solve(n);
        }
    }

    public static void solve(String n) {
        // We want a subsequence of the form: (any number of zeros) followed by a nonzero digit.
        // The total digits kept in such a subsequence will be: (# zeros chosen) + 1.
        // To minimize removals, maximize the count of digits kept.

        int totalDigits = n.length();
        int prefixZeroCount = 0;  // count of zeros seen so far
        int maxKept = 0;  // maximum digits we can keep

        // traverse each digit in n
        for (int i = 0; i < totalDigits; i++) {
            char c = n.charAt(i);
            if (c == '0') {
                // if it's a zero, we can potentially add it to our kept prefix (if used before a final nonzero digit)
                prefixZeroCount++;
            } else {
                // When we see a nonzero digit, we can form a subsequence by keeping all zeros before it plus this digit.
                int candidate = prefixZeroCount + 1;
                if (candidate > maxKept) {
                    maxKept = candidate;
                }
            }
        }

        // It is always possible to have a cost of 1 because n is positive and thus must contain a nonzero digit.
        // The minimum number of removals = total digits - maximum digits kept.
        int removals = totalDigits - maxKept;
        System.out.println(removals);
    }
}
