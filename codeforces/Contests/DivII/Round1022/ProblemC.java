import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ProblemC {
    public static class FastReader {
        private BufferedReader buffer;
        private StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() { return Integer.parseInt(next()); }
        public long nextLong() { return Long.parseLong(next()); }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final StringBuilder output = new StringBuilder();
        int t = fast.nextInt();
        while(t-- > 0) {
            final int n = fast.nextInt();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            output.append(solve(n, nums)).append("\n");
        }
        System.out.print(output);
    }

    public static int solve(final int n, long nums[]) {
        Map<Long, List<Integer>> indexMap = new HashMap<>();
        boolean marked[] = new boolean[n];
        for(int i = 0; i < n; i++) {        // Creating a map to store the indices of similar elements
            if(!indexMap.containsKey(nums[i]))
                indexMap.put(nums[i], new ArrayList<>());
            indexMap.get(nums[i]).add(i);
        }
        Arrays.sort(nums);      // Sort the weights array
        int index = n-1, count = 0;     // Start from descending order
        while(index >= 0) {
            for(int marker : indexMap.get(nums[index])) {
                // For each index of the same value
                if(!isMarked(marked, marker, n))     // check whether it can be marked
                    count++;            // If it cannot be marked create a clone there and mark it
                marked[marker] = true;
            }
            index -= indexMap.get(nums[index]).size();      // Subtract the group size to get to the next group
        }
        return count;
    }

    public static boolean isMarked(boolean marked[], int index, int n) {
        if(index > 0 && marked[index-1])        // Left check
            return true;
        else if(index < n-1 && marked[index+1])     // Right check
            return true;
        return false;       // If both check fails then it cannot be marked currently
    }
}
