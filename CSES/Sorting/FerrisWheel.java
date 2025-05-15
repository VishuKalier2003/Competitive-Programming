import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class FerrisWheel {
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
        final int n = fast.nextInt(), x = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(n, x, nums));
    }

    public static int solve(final int n, final int x, int nums[]) {
        Arrays.sort(nums);
        int left = 0, right = n-1, ans = 0;
        while(left <= right) {
            if(left < right && nums[left] + nums[right] <= x) {
                left++;
                right--;
            } else
                right--;
            ans++;
        }
        return ans;
    }
}
