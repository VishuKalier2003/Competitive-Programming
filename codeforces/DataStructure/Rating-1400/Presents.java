import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Presents {
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
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), m = fast.nextInt();
            int a[] = new int[n], b[] = new int[m];
            for(int i = 0; i < n; i++)
                a[i] = fast.nextInt();
            for(int i = 0; i < m; i++)
                b[i] = fast.nextInt();
            builder.append(solve(n, m, a, b)).append("\n");
        }
        System.out.print(builder);
    }

    public static long solve(final int n, final int m, final int presentStack[], final int presentList[]) {
        // Set (or boolean array) to mark gifts that have been "lifted" from the stack.
        Set<Integer> checked = new HashSet<>();
        int j = 0;          // Imp- j: pointer for the current position in the stack (presentStack)
        long time = 0;
        // Process each gift in the presentList in order.
        for (int i = 0; i < m; i++) {  // Imp- i is 0-indexed; it represents that i gifts have already been delivered.
            int currentGift = presentList[i];
            // If this gift is not already "checked" (removed from the stack), we need to take the extra time.
            if (!checked.contains(currentGift)) {
                // Move the stack pointer until we find the gift.
                while (j < n && presentStack[j] != currentGift) {
                    checked.add(presentStack[j]);
                    j++;
                }
                // Here, presentStack[j] == currentGift, and j is 0-indexed.
                // The 1-indexed position is (j+1), and (i) gifts have already been delivered,
                // so the cost is 1 + 2*((j+1) - (i+1)) = 1 + 2*(j - i).
                time += 1 + 2 * (j - i);
                j++; // Imp- Move j past the current gift (simulate its removal)
            } else {
                // This gift is already accessible at the top (we have already removed the gifts above it)
                time += 1;
            }
        }
        return time;
    }

}
