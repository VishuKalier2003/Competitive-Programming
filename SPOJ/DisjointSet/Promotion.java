import java.io.*;
import java.util.*;

public class Promotion {
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }
        int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') if (c == -1) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do { x = x * 10 + (c - '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        Map<Integer,Integer> count = new HashMap<>();
        
        long total = 0;
        
        for (int day = 0; day < n; day++) {
            int k = fr.nextInt();
            for (int i = 0; i < k; i++) {
                int val = fr.nextInt();
                minHeap.add(val);
                maxHeap.add(val);
                count.put(val, count.getOrDefault(val, 0) + 1);
            }
            
            // Get max
            int high;
            while (true) {
                high = maxHeap.poll();
                if (count.getOrDefault(high, 0) > 0) break;
            }
            count.put(high, count.get(high) - 1);
            
            // Get min
            int low;
            while (true) {
                low = minHeap.poll();
                if (count.getOrDefault(low, 0) > 0) break;
            }
            count.put(low, count.get(low) - 1);
            
            total += (high - low);
        }
        
        System.out.println(total);
    }
}
