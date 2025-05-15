// IMP- Parenthesis Balance (Invariant Consistency)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class EvenPositions {
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
    }
    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(tests-- > 0) {        // For each test case
            int n = fast.nextInt();
            String s = fast.next();
            builder.append(minCost(n, s)).append("\n");
        }
        System.out.print(builder);
    }

    public static int minCost(int n, String s) {        // Greedy Approach
        // IMP- Invariant Consistency- performing dynamic decisions on the basis of values (these values are updated dynamically while traversal)
        int balance = 0;
        StringBuilder completeString = new StringBuilder();
        for(int i = 0; i < n; i++) {
            char bracket = s.charAt(i);
            if(i % 2 != 0) {        // The positions that are already given
                completeString.append(bracket);
                if(bracket == ')')  // Update running value (invariant consistency)
                    balance--;
                else    balance++;
            }
            else {      // When the index value is empty
                // IMP- Fill values as per the running (value) and update the invariant simultaneously
                if(balance > 0) {
                    completeString.append(')');
                    balance--;
                }
                else {
                    completeString.append('(');
                    balance++;
                }
            }
        }
        return findCost(completeString.toString(), n);
    }

    public static int findCost(String s, int n) {
        int costOpen = 0, costClose = 0;        // Cost of closed and open parenthesis
        for(int i = 0; i < n; i++) {
            if(s.charAt(i) == '(')
                costOpen += i;
            else    costClose += i;
        }
        return costClose-costOpen;      // Closed parenthesis sum will always be larger
    }
}
