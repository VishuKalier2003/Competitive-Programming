// Note- No AI assist
// Ques - Shaslisk and Cooking - https://codeforces.com/problemset/problem/1040/B


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Shaslisk {
    public static class FastReader {            // Reading input
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
    public static void main(String[] args) {        // Main function
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), k = fast.nextInt();
        System.out.print(new StringBuilder().append(turnedHeaters(n, k)));
    }

    public static String turnedHeaters(final int n, final int k) {
        if(k >= n)      // If the range is greater than total heaters, we need only 1 press
            return "1\n 1";
        List<Integer> turnedPos = new ArrayList<>();
        // IMP- Create a list of positions which starts from 1 and have a difference of 2k between them
        for(int i = 1; i <= n; i = i + (2*k)+1)
            turnedPos.add(i);
        int size = turnedPos.size(), last = turnedPos.get(size-1);
        if(n-last > k) {        // If the last heater cannot cover all the toasts
            int shift = n-last-k;       // Shift all the heaters by the number of toasts left uncovered
            for(int i = 0; i < size; i++)
                turnedPos.set(i, turnedPos.get(i)+shift);
        // IMP- Since we start from 1, we can shift the heaters to a range of k (since the control is flowed in both directions)
        }
        final StringBuilder result = new StringBuilder();
        result.append(size+"\n");           // append result
        for(int index : turnedPos)
            result.append(index+" ");
        return result.toString();
    }
}
