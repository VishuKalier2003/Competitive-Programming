import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;

public class Tshirt {
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
        final int t = fast.nextInt();       // t t-shirts
        long p[] = new long[t], a[] = new long[t], b[] = new long[t];
        for(int i  = 0; i < t; i++)
            p[i] = fast.nextLong();
        for(int i = 0; i < t; i++)
            a[i] = fast.nextLong();
        for(int i = 0; i < t; i++)
            b[i] = fast.nextLong();
        final int n = fast.nextInt();       // n guests
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        System.out.print(solve(t, p, a, b, n, nums));
    }

    public static StringBuilder solve(final int tshirts, final long p[], final long a[], final long b[], final int n, final int nums[]) {
        Map<Integer, PriorityQueue<Long>> groupMap = new HashMap<>();       // Imp- Create color groups using maps (atmost 3)
        for(int i = 1; i <= 3; i++)
            groupMap.put(i, new PriorityQueue<>());
        for(int i = 0; i < tshirts; i++) {      // A tshirt can be placed in at most two colors (can have different colors)
            groupMap.get((int)a[i]).add(p[i]);
            groupMap.get((int)b[i]).add(p[i]);
        }
        Set<Long> purchased = new HashSet<>();
        final StringBuilder result = new StringBuilder();
        for(int num : nums) {       // For each guests arrived in the shop
            PriorityQueue<Long> tempHeap = groupMap.get(num);       // Desired color pool of t-shirts chosen
            boolean taken = false;      // Imp- Flag to store whether the guest buys any t-shirt
            while(!tempHeap.isEmpty()) {
                long price = tempHeap.poll();
                if(!purchased.contains(price)) {        // If a shirt of lowest price is not yet purchased
                    purchased.add(price);
                    result.append(price+" ");
                    taken = true;
                    break;
                }
            }
            if(!taken)      // If the guest cannot buy any t-shirt then return -1
                result.append("-1 ");
        }
        return result;
    }
}
