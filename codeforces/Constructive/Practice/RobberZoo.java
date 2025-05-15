import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class RobberZoo {
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
        int n = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        builder.append(swapOperations(nums, n));
        System.out.print(builder);
    }

    public static int allowedSteps = 20000;

    public static StringBuilder swapOperations(int nums[], int n) {
        StringBuilder segments = new StringBuilder();
        int sorted[] = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);
        for(int i = 0; i < n; i++) {
            int sortedPos = find(sorted[i], nums, n);
            int displacement = sortedPos - i;
            nums[sortedPos] = -1;
            if(displacement > 0) {
                if(allowedSteps - displacement < 0)
                    return new StringBuilder("-1");
                int temp = sortedPos;
                while(displacement-- > 0) {
                    segments.append((temp)+" "+(temp+1)).append("\n");
                    int swapTemp = nums[temp];
                    nums[temp] = nums[temp-1];
                    nums[temp-1] = swapTemp;
                    temp--;
                }
            }
        }
        return segments;
    }

    public static int find(int num, int nums[], int n) {
        for(int i = 0; i < n; i++)
            if(nums[i] == num)
                return i;
        return -1;
    }
}
