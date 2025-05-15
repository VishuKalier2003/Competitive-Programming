// IMP- Counting Subsequences (Greedy)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TMT {
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
    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0)
            builder.append(possibleSubsequences(fast.nextInt(), fast.next()));
        System.out.print(builder);
    }

    public static String possibleSubsequences(final int n, final String s) {
        if(n % 3 != 0 || s.charAt(0) == 'M' || s.charAt(n-1) == 'M')
            return "No\n";
        // lists to store the indices
        List<Integer> tIndex = new ArrayList<>(), mIndex = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            if(s.charAt(i) == 'T')
                tIndex.add(i);
            else    mIndex.add(i);
        }
        int k = mIndex.size(), tSize = tIndex.size();
        if(k*2 != tSize)        // The number of Ts should be exactly double of Ms
            return "No\n";
        for(int i = 0; i < k; i++)
            // IMP- For each ith M, the ith T should be before and i+k th T should be after (greedy check)
            if(tIndex.get(i) > mIndex.get(i) || tIndex.get(i+k) < mIndex.get(i))
                return "No\n";
        return "Yes\n";
    }
}
