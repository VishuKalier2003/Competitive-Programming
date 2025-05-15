import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class SubsequenceUpdate {
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
        int tests = fast.nextInt();
        StringBuilder builder = new StringBuilder();
        while(tests-- > 0) {
            int n = fast.nextInt(), l = fast.nextInt(), r = fast.nextInt();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            builder.append(minSumAfterReverse(nums, n, l-1, r-1)).append("\n");
        }
        System.out.print(builder);
    }

    public static long minSumAfterReverse(long nums[], int n, int l, int r) {
        // IMP- heaps to store the data (extracting k length max sum subsequence)
        PriorityQueue<Long> leftHeap = new PriorityQueue<>(), rightHeap = new PriorityQueue<>();
        int k = r-l+1;
        long lefSum = 0L, rightSum = 0L;
        for(int i = 0; i < n; i++) {
            // Taking left subsequence (1, r)
            if(i <= r)
                leftHeap.add(nums[i]);
            // Taking right subsequence (l, n)
            if(i >= l)
                rightHeap.add(nums[i]);
        }
        while(k-- > 0) {
            lefSum += leftHeap.poll();
            rightSum += rightHeap.poll();
        }
        return Math.min(rightSum, lefSum);  // Taking min of the two subsequence sums
    }
}
