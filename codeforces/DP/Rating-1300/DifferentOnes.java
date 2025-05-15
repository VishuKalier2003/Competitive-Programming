import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DifferentOnes {
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
            final int n = fast.nextInt();
            int nums[] = new int[n+1];
            for(int i = 0; i < n; i++)
                nums[i+1] = fast.nextInt();
            final int query = fast.nextInt();
            List<int[]> queries = new ArrayList<>();
            for(int i = 0; i < query; i++)
                queries.add(new int[]{fast.nextInt(), fast.nextInt()});
            builder.append(solve(n+1, nums, query, queries)).append("\n");
        }
        System.out.print(builder);
    }

    public static StringBuilder solve(final int n, final int nums[], final int q, List<int[]> queries) {
        int dp[] = new int[n];
        dp[0] = 0;
        for(int i = 2; i < n; i++)
            dp[i] = nums[i] != nums[i-1] ? dp[i-1]+1 : dp[i-1];
        final StringBuilder result = new StringBuilder();
        for(int query[] : queries) {
            final int l = query[0], r = query[1];
            if(dp[l] == dp[r])
                result.append("-1 -1\n");
            else
                result.append(l+" ").append(binarySearch(l+1, r, dp[l]+1, dp)).append("\n");
        }
        return result;
    }

    public static int binarySearch(final int start, final int end, final int value, final int dp[]) {
        int left = start, right = end, ans = end;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            if(dp[mid] > value) {
                ans = mid;
                right = mid-1;
            }
            else
                left = mid+1;
        }
        return ans;
    }
}
