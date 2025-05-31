import java.io.*;
import java.util.*;

public class Subsequence {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            try {
                while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                    tokenizer = new StringTokenizer(buffer.readLine());
                }
            } catch(IOException e) {e.printStackTrace();}
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }
    public record TestCase(long[] nums) {}
    public static void main(String[] args) {
        FastReader fastReader = new FastReader();
        TestCase[] testCases = new TestCase[fastReader.nextInt()];
        for(int i = 0; i < testCases.length; i++) {
            testCases[i] = new TestCase(new long[fastReader.nextInt()]);
            for(int j = 0; j < testCases[i].nums.length; j++)
                testCases[i].nums[j] = fastReader.nextLong();
        }
        Arrays.stream(testCases).map(TestCase::nums).map(Subsequence::maxAlternatingSum).forEach(System.out::println);
    }

    public static long maxAlternatingSum(long nums[]) {
        boolean positive = nums[0] > 0;
        long max = Integer.MIN_VALUE, sum = 0;
        for(int i = 0; i < nums.length;) {
            if(positive)
                while(i < nums.length && nums[i] > 0)
                    max = Math.max(max, nums[i++]);
            else
                while(i < nums.length && nums[i] < 0)
                    max = Math.max(max, nums[i++]);
            positive = !positive;
            sum += max;
            max = Integer.MIN_VALUE;
        }
        return sum;
    }
}
