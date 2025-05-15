import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


// Imp- Important question of gcd where we check multiples of a gcd by jumping
public class CommonDivisors {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int t = fast.nextInt();
        int max = 0;
        int nums[] = new int[t];
        for(int i = 0; i < t; i++) {
            nums[i] = fast.nextInt();
            max = Math.max(max, nums[i]);
        }
        System.out.print(solve(t, max, nums));
    }

    // Imp- Instead of dividing we check for multiples of a gcd and count the numbers who are the multiples of current gcd
    public static int solve(final int n, final int max, final int nums[]) {
        int freq[] = new int[max+1];
        for(int num : nums)
            freq[num]++;
        // We start from highest gcd possible and start decrementing it
        for(int gcd = max; gcd > 0; gcd--) {        // Imp- O(n)
            int temp = gcd, count = 0;
            // We check for multiples for each gcd
            while(temp <= max) {        // Imp- O(log n)
                // Count the numbers in the array that are multiples of the current gcd
                count += freq[temp];
                temp += gcd;        // Move to the next multiple (gcd*2, gcd*3 and so on)
            }
            // If for ay gcd the count is more than 1 that means there are two numbers who share the current gcd
            // since they are multiples of the same gcd
            if(count > 1)
                // Imp- Since we are going from max to 1, the first gcd will be our maximum
                return gcd;
        }
        return 1;
    }
}
