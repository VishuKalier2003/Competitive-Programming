// https://www.spoj.com/problems/LOSTNSURVIVED/

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class LostAndSurvived {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nexLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException { // reading string (whitespace exclusive)
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
    public static class FastWriter {
        public PrintWriter pw;
        public StringBuilder sb;

        public FastWriter() {
            this.pw = new PrintWriter(new OutputStreamWriter(System.out));
            this.sb = new StringBuilder();
        }

        public void attachOutput(StringBuilder s) {
            sb.append(s);
        }

        public void printOutput() {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Spoj-Lost-and-Survived",
                1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), m = fr.nextInt();
        List<int[]> edges = new ArrayList<>();
        for(int i = 0; i < m; i++)
            edges.add(new int[]{fr.nextInt(), fr.nextInt()});
        fw.attachOutput(solve(n, edges));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final List<int[]> edges) {
        UnionFind uf = new UnionFind(n);
        final StringBuilder output = new StringBuilder();
        for(int e[] : edges) {
            final int u = e[0], v = e[1];
            uf.union(u, v);     // Union components
            output.append(uf.operation()).append("\n");
        }
        return output;
    }

    public static class UnionFind {
        // Storing the size of smallest component, keys to store the size and set to denote the components of the given size
        private final SortedMap<Integer, Set<Integer>> smallest;        // TreeMap implementation hence O(log n) time
        // parent and rank array, here union by rank (size)
        private final int parent[], rank[];
        private final int size;
        private int maxComponent;       // variable to store the max component

        public UnionFind(int n) {
            // Inilializing all variables
            this.size = n+1;
            this.parent = new int[size];
            this.rank = new int[size];
            this.maxComponent = 1;
            this.smallest = new TreeMap<>();
            smallest.put(1, new HashSet<>());       // All components initially of size 1
            for(int i = 1; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
                smallest.get(1).add(i);
            }
        }

        public int find(int x) {        // Path compression
            if(parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void removeComponent(int size, int node) {   // when removing a component, remove from map also
            smallest.get(size).remove(node);
            if(smallest.get(size).isEmpty())    // If there are no components of given size, remove the size key
                smallest.remove(size);
        }

        public void addComponent(int size, int node) {      
            if(!smallest.containsKey(size))     // If there is no key of corresponding size, create one
                smallest.put(size, new HashSet<>());
            smallest.get(size).add(node);
        }

        public int operation() {        // Operation as per question
            return maxComponent-smallest.firstKey();
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);   // finding roots
            if(rootX != rootY) {
                if(rank[rootX] < rank[rootY]) {
                    // remove components, since they will be merged
                    removeComponent(rank[rootX], rootX);
                    removeComponent(rank[rootY], rootY);
                    parent[rootX] = rootY;
                    rank[rootY] += rank[rootX];
                    // add the new component after merging as per the size
                    addComponent(rank[rootY], rootY);
                    maxComponent = Math.max(maxComponent, rank[rootY]);     // evaluate the max component
                } else {
                    // remove components. since they will be merged
                    removeComponent(rank[rootX], rootX);
                    removeComponent(rank[rootY], rootY);
                    parent[rootY] = rootX;
                    rank[rootX] += rank[rootY];
                    // add the new component after merging as per the size
                    addComponent(rank[rootX], rootX);
                    maxComponent = Math.max(maxComponent, rank[rootX]);     // evaluate the max component
                }
            }
        }
    }
}
