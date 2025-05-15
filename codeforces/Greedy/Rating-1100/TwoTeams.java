// IMP- Longest Similar Subarray (LSS)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class TwoTeams {
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
        while(t-- > 0) {        // for each test case
            final int n = fast.nextInt();
            int max = -1;
            int nums[] = new int[n];
            for(int i = 0; i < n; i++) {
                nums[i] = fast.nextInt();
                max = Math.max(max, nums[i]);
            }
            builder.append(twoTeamsComposing(n, nums, max)).append("\n");
        }
        System.out.print(builder);
    }

    public static int twoTeamsComposing(final int n, int nums[], final int max) {
        nums = countingSort(n, nums, max);
        // Modified Kadane algorithm (greedy)
        int longestSubarray = 0, prev = Integer.MIN_VALUE, subarray = 0;
        Set<Integer> unique = new HashSet<>();
        for(int i = 0; i < n; i++) {
            if(prev != nums[i]) {       // If the subarray changes
                prev = nums[i];     // update and reset values
                subarray = 0;
            }
            subarray++;
            unique.add(nums[i]);        // Add to set to count distinct values
            longestSubarray = Math.max(subarray, longestSubarray);      // IMP- Maximize the longest subarray
        }
        // When the sizes are same, one member from second team goes to first team, hence the result decreases by 1
        return unique.size() == longestSubarray ? longestSubarray-1 : Math.min(unique.size(), longestSubarray);
    }

    public static int[] countingSort(final int n, final int nums[], final int max) {
        // IMP- Counting sort algorithm for linear complexity
        int bucket[] = new int[max+1];
        for(int num : nums)
            bucket[num]++;
        int sorted[] = new int[n];
        int idx = 0;
        for(int i = 1; i <= max; i++)
            while(bucket[i]-- > 0)
                sorted[idx++] = i;
        return sorted;
    }
}
