// Ques(2064B) - Longest Unique Subarray (similar to Longest Increasing Subarray)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Variety {
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
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int tests = fast.nextInt();
        StringBuilder builder = new StringBuilder();
        while(tests-- > 0) {
            int n = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(longestIncreasingSubarray(nums, n)).append("\n");    // Finding the i-th
        }
        System.out.print(builder);
    }

    public static Map<Integer, Integer> freqMap;

    public static String longestIncreasingSubarray(int nums[], int n) {
        freqMap = new HashMap<>();
        int prefix[] = new int[n];      // Prefix array where prefix[i] stores the length of the unique subarray ending at ith index
        for(int num : nums)
            freqMap.put(num, freqMap.getOrDefault(num, 0)+1);       // generating the frequency map
        prefix[0] = freqMap.get(nums[0]) == 1 ? 1 : 0;
        int subarraySize = prefix[0];       // subarray size
        for(int i = 1; i < n; i++)
            if(freqMap.get(nums[i]) == 1) {
                // IMP- updating the a[i] by the prefix[i-1], thus, ith index stores the unique subarray size ending at ith index
                prefix[i] = prefix[i-1] + 1;
                subarraySize = Math.max(subarraySize, prefix[i]);       // Maximizing the longest subarray found till ith index
            }
        if(subarraySize == 0)
            return "0";
        // IMP- Extracting the 1-based index (l,r) to (i-prefix[i]+2, i+1)
        for(int i = 0; i < n; i++)
            if(subarraySize == prefix[i])
                return (i-prefix[i]+2)+" "+(i+1);
        return null;
    }
}
