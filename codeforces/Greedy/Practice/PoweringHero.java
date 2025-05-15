import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class PoweringHero {
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
            int n = fast.nextInt();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            builder.append(maxHeroPower(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static long maxHeroPower(int n, long nums[]) {
        PriorityQueue<Long> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        long power = 0L;
        for(int i = 0; i < n; i++) {
            if(nums[i] != 0)
                maxHeap.add(nums[i]);
            else if(!maxHeap.isEmpty())
                power += maxHeap.poll();
        }
        return power;
    }
}
