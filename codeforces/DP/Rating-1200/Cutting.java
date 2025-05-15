import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Cutting {
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
        final StringBuilder builder = new StringBuilder();
        final int n = fast.nextInt(), bitcoin = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        // Total even and odd cuts, will be n/2 of the array size
        builder.append(maxCuts(n, n/2, n/2, nums, bitcoin));
        System.out.print(builder);
    }

    public static String maxCuts(final int n, final int totalOdd, final int totalEven, final int nums[], int bitcoin) {
        PriorityQueue<Integer> cutCostHeap = new PriorityQueue<>();
        int segmentOdd = 0, segmentEven = 0, cuts = 0;
        for(int i = 0; i < n; i++) {
            // IMP- When a valid cut is found we will take the ith element into consideration for the next segment (the next segment starts at i)
            if(segmentEven == segmentOdd && segmentOdd > 0) {
                cutCostHeap.add(Math.abs(nums[i]-nums[i-1]));
                segmentEven = segmentOdd = 0;
            }
            // We want to take i-th element into consideration
            if(nums[i] % 2 == 0)
                segmentEven++;
            else    segmentOdd++;
        }
        // IMP- Find the top smallest cuts, which when summed are less than or equal to bitcoin
        while(!cutCostHeap.isEmpty()) {
            if(bitcoin - cutCostHeap.peek() < 0)
                break;      // When exceeds break
            bitcoin -= cutCostHeap.poll();
            cuts++;
        }
        return cuts+"\n";
    }
}
