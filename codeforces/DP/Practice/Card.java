// Note:- Solved on Own (AI Assists - 0)
// Ques - Game of Credit Cards - https://codeforces.com/problemset/problem/777/B

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Card {
    public static class FastReader {
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() { return Integer.parseInt(next()); }
        public long nextLong() { return Long.parseLong(next()); }
    }

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        final StringBuilder builder = new StringBuilder();
        final int n = fast.nextInt();
        final String row1 = fast.next(), row2 = fast.next();
        builder.append(bothWayResult(n, row1, row2));
        System.out.print(builder);
    }

    public static String bothWayResult(final int n, final String Sherlock, final String Montry) {
        return sherlockOffense(new int[10], n, Sherlock, Montry)+"\n"+montryOffense(new int[10], n, Sherlock, Montry);
    }

    public static int sherlockOffense(int montry[], final int n, final String Sherlock, final String Montry) {
        for(int i = 0; i < n; i++)      // Create the frequency map
            montry[Montry.charAt(i)-'0']++;
        int defense = 0;                // Defense count (time Montry saves himself)
        for(int i = 0; i < n; i++) {
            int currentValue = Sherlock.charAt(i)-'0';      // We tend to maintain equilibrium
            if(montry[currentValue] > 0)        // IMP- choose the number exact same to keep the rest numbers to counter other values
                montry[currentValue]--;
            else {
                boolean saved = false;
                for(int j = currentValue; j < 10; j++)
                    if(montry[j] > 0) {     // If needed to be saved by higher value
                        montry[j]--;
                        saved = true;
                        break;
                    }
                if(!saved) {        // When cannot be saved
                    defense++;
                    for(int j = 0; j < currentValue; j++)
                        if(montry[j] > 0) {
                            montry[j]--;        // decrease the value
                            break;
                        }
                }
            }
        }
        return defense;
    }

    public static int montryOffense(int montry[], final int n, final String Sherlock, final String Montry) {
        int offense = 0;
        for(int i = 0; i < n; i++)
            montry[Montry.charAt(i)-'0']++;     // Create frequency map
        for(int i = 0; i < n; i++) {
            int currentValue = Sherlock.charAt(i)-'0';
            boolean attack = false;
            for(int j = currentValue+1; j < 10; j++)        // IMP- For attack we find the next greater value
                if(montry[j] > 0) {
                    montry[j]--;
                    attack = true;
                    offense++;      // Increase attack points
                    break;
                }
            if(!attack) {           // If cannot be attacked, use the smallest available value
                for(int j = 0; j <= currentValue; j++)
                    if(montry[j] > 0) {
                        montry[j]--;
                        break;
                    }
            }
        }
        return offense;
    }
}
