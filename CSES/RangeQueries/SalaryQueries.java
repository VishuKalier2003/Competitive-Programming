import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class SalaryQueries {
    public static class FastReader {        // FastReader
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

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

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static class FastWriter {        // Fast Writer class
        BufferedWriter writer;

        public FastWriter() {this.writer = new BufferedWriter(new OutputStreamWriter(System.out));}

        public void print(String s) throws IOException {
            this.writer.write(s);
        }

        public void close() throws IOException {this.writer.close();}
    }

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        List<Long> nums = new ArrayList<>();
        for(int i = 0; i < n; i++)
            nums.add(fast.nextLong());
        List<long[]> queries = new ArrayList<>();
        for(int i = 0; i < q; i++) {
            String type = fast.next();
            if(type.equals("!"))
                queries.add(new long[]{1l, fast.nextLong(), fast.nextLong()});
            else
                queries.add(new long[]{2l, fast.nextLong(), fast.nextLong()});
        }
        solve(n, q, nums, queries);
    }

    public static Map<Long, Integer> indexMap;      // Imp- Store the indices of each unique sorted coordinate
    public static TreeSet<Long> coordinates;

    public static void solve(final int n, final int q, final List<Long> nums, List<long[]> queries) throws IOException {
        coordinateCompression(nums, queries);       // Note- Coordinate Compression technique
        Fenwick fenwick = new Fenwick(coordinates.size());      // Imp- Fenwick Tree of coordinates size, then only we can count for each possible unique query
        // Use a current array to store the previous value that needs to removed when updated
        long current[] = new long[n];
        for(int i = 0; i < n; i++) {
            current[i] = nums.get(i);
            int index = indexMap.get(current[i]);       // Get the index from map
            fenwick.update(index, 1);       // Increase frequency by 1
        }
        FastWriter writer = new FastWriter();
        for(long query[] : queries) {
            if(query[0] == 2) {         // If the query is of type 2
                // Imp- Extract the indices from the map first
                int left = indexMap.get(query[1]), right = indexMap.get(query[2]);
                writer.print(fenwick.rangeCount(left, right)+"\n");
            }
            else {
                int idx = (int)query[1]-1;      // Find the index of the nums array that we are targeting
                int oldIndex = indexMap.get(current[idx]);
                fenwick.update(oldIndex, -1);       // Decrement old value
                current[(int)query[1]-1] = query[2];        // Imp- Update the current array with the new value
                int newIndex = indexMap.get(current[idx]);
                fenwick.update(newIndex, 1);        // Increment new value
            }
        }
        writer.close();
    }

    public static void coordinateCompression(List<Long> nums, List<long[]> queries) {
        indexMap = new HashMap<>();     // Use map to store the indices of each unique coordinate (1 to M)
        // Imp- We need unique sorted coordinates hence using tree set
        coordinates = new TreeSet<>();
        coordinates.addAll(nums);
        for(long query[] : queries) {
            if(query[0] == 1) {     // Here query[1] is the index (1 based)
                coordinates.add(nums.get((int)query[1]-1));
                coordinates.add(query[2]);      // updated value
            }
            else {
                coordinates.add(query[1]);      // lower boundary
                coordinates.add(query[2]);      // upper boundary
            }
        }
        int i = 1;      // Imp- Fenwick trees are 1 based indexed thus we start indexing from 1
        for(long coordinate : coordinates)
            indexMap.put(coordinate, i++);      // Mapping coordinates with indexes to count frequencies (rangeCount and prefixCount)
    }

    public static class Fenwick {
        public int tree[];
        public int n;

        public Fenwick(int n) {
            this.n = n;
            this.tree = new int[n+1];
        }

        public void update(int index, int delta) {      // Updating by delta value
            while(index <= this.n) {
                this.tree[index] += delta;
                index += lowBit(index);     // Low to high (Add)
            }
        }

        public int count(int index) {
            int count = 0;      // Summation of frequencies (else block in solve()), uses +1 and -1 for adding and deleting (hence sum)
            while(index > 0) {
                count += this.tree[index];
                index -= lowBit(index);         // High to Low (Subtract)
            }
            return count;
        }

        public int rangeCount(int left, int right) {
            return count(right) - count(left-1);        // Prefix range technique
        }

        public int lowBit(int index) {return index & -index;}       // Evaluating low bit
    }
}
