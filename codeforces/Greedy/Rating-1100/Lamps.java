// IMP- Grouping and Priority (Greedy)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Lamps {
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
            int a[] = new int[n];
            long b[] = new long[n];
            for(int i = 0; i < n; i++) {
                a[i] = fast.nextInt();
                b[i] = fast.nextLong();
            }
            builder.append(maxLampsScore(n, a, b)).append("\n");
        }
        System.out.print(builder);
    }

    public static long maxLampsScore(final int n, final int a[], final long b[]) {
        Map<Integer, PriorityQueue<Long>> heapMap = new HashMap<>();        // IMP- Map of max heaps
        for(int i = 0; i < n; i++) {
            if(!heapMap.containsKey(a[i]))
                heapMap.put(a[i], new PriorityQueue<Long>(Collections.reverseOrder()));
            heapMap.get(a[i]).add(b[i]);
        }
        long score = 0L;
        for(int key : heapMap.keySet()) {       // For each group (key)
            int count = key;
            PriorityQueue<Long> heap = heapMap.get(key);
            while(!heap.isEmpty() && count-- > 0)
                score += heap.poll();       // IMP- Extract the max count elements
        }
        return score;
    }
}
