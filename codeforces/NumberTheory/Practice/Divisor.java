import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Divisor {
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
        final int n = fast.nextInt();
        int nums[] = new int[n];
        int firstNum = -1;
        for(int i = 0; i < n; i++) {
            nums[i] = fast.nextInt();
            // IMP- Since the divisors (1 and n) are also there, the max element is either x or y
            firstNum = Math.max(firstNum, nums[i]);
        }
        final StringBuilder builder = new StringBuilder();
        // We then find the second element
        builder.append(findSecondNumber(n, nums, firstNum)).append(" "+firstNum);
        System.out.print(builder);
    }

    public static int findSecondNumber(int n, int nums[], int firstNum) {
        Set<Integer> firstDivisors = new HashSet<>();
        for(int i = 0; i < n; i++)
            // Marking only one instance of the divisors
            if(firstNum % nums[i] == 0 && firstDivisors.add(nums[i]))   // Element not present, then add it
                nums[i] = -1;
        // IMP- Finding the max unmarked element as the second element
        return Arrays.stream(nums).max().orElse(Integer.MIN_VALUE);
    }
}
