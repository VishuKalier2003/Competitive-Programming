import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class StoneAge {
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

    public record Query(int type, int index, long value) {}     // IMP- final variable class defined

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        // Use hashmap instead of array, as we need to update indices multiple times and maintain indices not yet updated
        indexMap = new HashMap<>();
        sum = 0l;
        for(int i = 0; i < n; i++) {
            indexMap.put(i, fast.nextLong());
            sum += indexMap.get(i);     // Sum the values
        }
        Query queries[] = new Query[q];
        for(int i = 0; i < q; i++) {
            int type = fast.nextInt();
            if(type == 1)       // If type 1 query
                queries[i] = new Query(type, fast.nextInt(), fast.nextLong());
            else                // If type 2 query, no index passed
                queries[i] = new Query(type, -1, fast.nextLong());
        }
        System.out.print(compute(n, q, queries));
    }

    public static Map<Integer, Long> indexMap;
    public static long sum;

    public static StringBuilder compute(final int n, final int q, Query queries[]) {
        final StringBuilder result = new StringBuilder();
        long prevQueryII = -1;
        for(Query query : queries) {
            if(query.type == 1)     // Subtract 1 from index (reduce to 0-based indexing)
                sum = solveQueryI(query.index-1, query.value, prevQueryII);
            else {
                sum = solveQueryII(n, query.value);
                prevQueryII = query.value;      // Update the previous query value (last query II value passed in the entire array)
            }
            result.append(sum).append("\n");
        }
        return result;
    }

    public static long solveQueryI(final int index, final long value, final long prevQueryII) {
        if(indexMap.containsKey(index))     // If the current index is previously modified
            sum = sum + value - indexMap.get(index);
        else
        // IMP- Otherwise we take the last queryII value stored (since all elements were replaced by a similar value)
            sum = sum + value - prevQueryII;
        indexMap.put(index, value);         // update the index
        return sum;
    }

    public static long solveQueryII(final int n, final long value) {
        // IMP- A better approach than cleaning (resetting the hashmap as new unmodified data filled with last query2 values)
        indexMap = new HashMap<>();
        return value * n;           // return sum as the mathematical formula
    }
}
