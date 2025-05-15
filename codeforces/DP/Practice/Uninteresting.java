import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Uninteresting {
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
        while(t-- > 0)
            builder.append(isInteresting(fast.next()));
        System.out.print(builder);
    }

    public static String isInteresting(String num) {
        // IMP- Since we can square digits only till 0,1,2,3 so we have only two options of 2 and 3 which change to 4 and 9
        long sum = 0L;
        final int length = num.length();
        int count2 = 0, count3 = 0;
        for(int i = 0; i < length; i++) {
            char digit = num.charAt(i);
            sum += Long.parseLong(""+digit);        // Find the sum
            // Count the number of two's and three's
            if(digit == '2')
                count2++;
            else if(digit == '3')
                count3++;
        }
        if(sum % 9 == 0)        // If the sum is divisible by 9
            return "Yes\n";
        // IMP- we take the min of count and 8, as the max count that we want to check is 8 (digit DP)
        for(int i = 0; i <= Math.min(count2, 8); i++)
            for(int j = 0; j <= Math.min(count3, 8); j++)
                // Changing sum from 2 to 4 increases by 2 and from 3 to 9 increases by 9
                if((sum + (i*2) + (j*6)) % 9 == 0)
                    return "Yes\n";
        // If no valid possibility found, return No or -1
        return "No\n";
    }
}
