import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Scuza {
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
        while(t-- > 0) {
            final int n = fast.nextInt(), k = fast.nextInt();
            long nums[] = new long[n], query[] = new long[k];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            for(int i = 0; i < k; i++)
                query[i] = fast.nextLong();
            builder.append(solveJumpQuery(n, k, nums, query));
        }
        System.out.print(builder);
    }

    public static StringBuilder solveJumpQuery(final int n, final int k, final long nums[], final long queries[]) {
        long prefixMax[] = new long[n], prefixSum[] = new long[n];
        prefixMax[0] = prefixSum[0] = nums[0];
        for(int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i-1] + nums[i];
            prefixMax[i] = Math.max(prefixMax[i-1], nums[i]);
        }
        final StringBuilder result = new StringBuilder();
        for(long query : queries) {
            int index = binarySearchLowerBound(n, prefixMax, query);
            if(index == -1)
                result.append(0+" ");
            else
                result.append(prefixSum[index]+" ");
        }
        return result.append("\n");
    }

    public static int binarySearchLowerBound(final int n, final long nums[], final long query) {
        int left = 0, right = n-1, ans = -1;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            if(nums[mid] <= query) {
                ans = mid;
                left = mid+1;
            }   else
                right = mid-1;
        }
        return ans;
    }
}
