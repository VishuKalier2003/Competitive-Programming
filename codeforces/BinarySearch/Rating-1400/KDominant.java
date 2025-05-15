import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KDominant {
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
        final String s = fast.next();
        final int n = s.length();
        System.out.print(solve(n, s));
    }

    public static int solve(final int n, final String s) {
        int left = 1, right = n, ans = n;       // We will binary search the segment itself
        while(left <= right) {      // Performing lower bound binary search (k Optimization)
            int mid = left + ((right - left) >> 1);
            if(kDominantFound(n, s, mid)) {     // Finding if dominant character exists
                ans = mid;
                right = mid-1;      // Store it and check for the further smaller character if possible
            }
            else    left = mid+1;       // Otherwise increase the left margin
        }
        return ans;
    }

    public static boolean kDominantFound(final int n, final String s, final int window) {
        int map[] = new int[26];            // Imp- frequency map of 26 characters (lowercase)
        for(int i = 0; i < window; i++)
            map[s.charAt(i)-'a']++;
        List<Integer> maskStates = new ArrayList<>();
        maskStates.add(bitMask(map));           // Store the bitmask for each segment of length window in the list
        for(int i = window; i < n; i++) {
            // Update the segment and store its bitmask in the list
            map[s.charAt(i-window)-'a']--;
            map[s.charAt(i)-'a']++;
            maskStates.add(bitMask(map));
        }
        int AND = -1;       // Imp- all 26 characters set as 1
        for(int mask : maskStates)
            AND &= mask;        // Find the common occurrences of each character among all masks
        // Imp- If AND is greater than 0, then it means there is at least one character that is present in all the bit masks
        return AND > 0;
    }

    public static int bitMask(final int map[]) {
        int mask = 0;
        for(int i = 0; i < 26; i++)
            if(map[i] > 0)      // If the current character is present in the map (has occurred at least once)
                mask |= (1 << i);       // Set its index bit to 1
        return mask;
    }
}
