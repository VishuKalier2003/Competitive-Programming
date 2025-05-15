import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Matryoska {
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
        final StringBuilder result = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();      // Array input
            result.append(countNestedDolls(n, nums)).append("\n");
        }
        System.out.print(result);
    }

    public static int countNestedDolls(final int n, final long nums[]) {
        // IMP- Instead of sorting, we can directly use a treemap, that stores keys in sorted order and from there we can greedily start generating the sequence from the smallest avilable
        TreeMap<Long, Integer> sortedFreqMap = new TreeMap<>();
        for(long num : nums)
            sortedFreqMap.put(num, sortedFreqMap.getOrDefault(num, 0)+1);
        int nestedCage = 0;     // Number of sequences generated (nested cage)
        while(!sortedFreqMap.isEmpty()) {
            long current = sortedFreqMap.firstKey();
            while(sortedFreqMap.containsKey(current)) {     // While the current element exists in the treemap
                sortedFreqMap.put(current, sortedFreqMap.get(current)-1);
                if(sortedFreqMap.get(current) == 0)     // If the current element is exhausted, remove it
                    sortedFreqMap.remove(current);
                current++;      // Increase current by 1 to add the next element to the nested doll
            }
            nestedCage++;       // Increment the nested cage
        }
        return nestedCage;
    }
}
