package QueueHeaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class SlidingWindowMax {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try{tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
    }

    public static void main(String[] args) {
        int nums[];
        int k;
        input: {
            FastReader fastReader = new FastReader();
            nums = new int[fastReader.nextInt()];
            k = fastReader.nextInt();
            for(int i = 0; i < nums.length; i++)
                nums[i] = fastReader.nextInt();
            break input;
        } output: {
            System.out.println(Arrays.toString(slidingWindowMaximum(nums, k)));
            break output;
        }
    }

    public static class Pair<K, V> {        // Generic pair class
        K data; V index;
        public Pair(K data, V index) {
            this.data = data; this.index = index;
        }

        public K getData() {return this.data;}
        public V getIndex() {return this.index;}
    }

    public static int[] slidingWindowMaximum(int nums[], int k) {
        int n = nums.length, idx = 0;
        int result[] = new int[n-k+1];
        // In (a,b) when we use a-b it is ascending and b-a is descending
        PriorityQueue<Pair<Integer, Integer>> maxHeap = new PriorityQueue<>((a, b) -> b.data - a.data);
        for(int i = 0; i < k; i++)
            maxHeap.add(new Pair<Integer, Integer>(nums[i], i));
        result[idx++] = maxHeap.peek().getData();
        for(int i = 1, j = k; j < n; i++, j++) {
            maxHeap.add(new Pair<Integer, Integer>(nums[j], j));
            while(maxHeap.peek().getIndex() < i)
                maxHeap.poll();
            result[idx++] = maxHeap.peek().getData();
        }
        return result;
    }
}
