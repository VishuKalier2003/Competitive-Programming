import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionFindLevel2 {
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
            } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
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
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
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
        }, "Union-Find(Segment Tree + Rollback DSU)", 1 << 26);
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
            edges.add(new int[]{fr.nextInt(), fr.nextInt(), fr.nextInt()});
        fw.attachOutput(solve(n, m, edges));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int m, final List<int[]> edges) {
        Map<String, Integer> edgeMap = new HashMap<>();
        // Info: edgeList stores all the unique type 1 queries
        List<int[]> edgeList = new ArrayList<>();
        // for each unique edge, they are for marking the start time and the end time upon which the segment tree will be built
        int start[] = new int[m], end[] = new int[m];
        Arrays.fill(end, m);
        for(int rangeIndex = 0; rangeIndex < m; rangeIndex++) {
            int type = edges.get(rangeIndex)[0], u = edges.get(rangeIndex)[1], v = edges.get(rangeIndex)[2];
            if(u > v) {     // Removing duplicacies by converting (2,1) to (1,2)
                int temp = u; u = v; v = temp;
            }
            String key = u + "," + v;
            // for each edge we will know its start time (inclusive) and end time (exclusive)
            if(type == 1) {
                int sz = edgeList.size();
                edgeList.add(new int[]{u, v});
                // Info: passing sz here not i, since the list on which segment tree will work is edgeList and not all queries
                edgeMap.put(key, sz);
                start[sz] = rangeIndex;
            } else if(type == 2) {
                // The key corresponds to the insertion index of the query (key)
                int id = edgeMap.get(key);
                end[id] = rangeIndex;
            }
        }
        SegmentTree segtree = new SegmentTree(n);
        // Note: Segment tree is created on edgeList (the addition queries) and not all queries
        for(int id = 0; id < edgeList.size(); id++)
            segtree.addInterval(1, 0, m, start[id], end[id], id);
        UnionFind uf = new UnionFind(n, m);
        int ans[] = new int[m];
        segtree.dfs(1, 0, m, uf, edges, ans, edgeList.toArray(new int[0][0]));
        final StringBuilder output = new StringBuilder();
        for(int i = 0; i < m; i++)
            if(edges.get(i)[0] == 3)
                output.append((ans[i] == 1 ? "Yes" : "No")).append(" ");
        return output;
    }

    public static class UnionFind {
        private final int[] parent, rank;       // usual UF arrays
        // history arrays to store the previous states, here parent and rank
        private final int[] historyParent, historyRank;     
        private int historyPtr;

        public UnionFind(int n, int m) {
            this.parent = new int[n+1];
            this.rank = new int[n+1];
            for(int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
            // Micro-optimisation: giving extra query stack just to be safe (in case of duplicate values)
            this.historyParent = new int[2 * m];
            this.historyRank = new int[2 * m];
            this.historyPtr = 0;        // initialising history pointer to 0
        }

        // Note: find() without path compression for easy rollback O(log n)
        public int find(int x) {
            while(parent[x] != x)       // no path compression
                x = parent[x];
            return x;
        }

        public boolean union(int x, int y) {
            int rX = find(x), rY = find(y);
            if(rX == rY)        // In case of nodes are in same component
                return false;
            else {
                // Note: The history is always maintained of smaller node, since that is the one being merged according to union by rank
                if(rank[rX] < rank[rY]) {   // The smaller component as x
                    historyParent[historyPtr] = rX;     // parent history as node x
                    historyRank[historyPtr] = rank[rX];
                    rank[rY] += rank[rX];
                    parent[rX] = rY;
                } else {    // The smaller component as y
                    historyParent[historyPtr] = rY;     // parent history as node y
                    historyRank[historyPtr] = rank[rY];
                    rank[rX] += rank[rY];
                    parent[rY] = rX;
                }
                historyPtr++;
                return true;
            }
        }

        // return snapshot as the number of valid union operations (stack size) done so far
        public int snapshot() {return historyPtr;}

        // Info: rolling back till to the ith operation (given as parameter)
        public void rollback(int snap) {
            while(historyPtr > snap) {
                historyPtr--;
                int b = historyParent[historyPtr];
                int size = historyRank[historyPtr];
                // Info: rollback step
                int a = parent[b];
                parent[b] = b;      // self looping the parent, node B, (breaking the edge)
                rank[a] = size;     // restoring size of node A with the stored size
            }
        }

        // Note: The operation that we want to check (can be metadata too)
        public boolean operation(int x, int y) {    
            return find(x) == find(y);
        }
    }

    // Note: Here we created the open interval segment tree [start, end) type, the changes are only in considering mid -> [start, mid) to [mid, end)
    public static class SegmentTree {
        // Segment tree as array and each cell will be a list
        private final List<Integer>[] tree;
        private final int n;

        public SegmentTree(int n) {
            this.n = n;
            this.tree = new ArrayList[4 * this.n];      // Iterative segment tree
            for(int i = 0; i < 4 * this.n; i++)
                tree[i] = new ArrayList<>();
        }

        public void addInterval(int index, int rl, int rr, int ql, int qr, int edgeIdx) {
            if(ql >= rr || qr <= rl)        // no overlap
                return;
            if(ql >= rl && qr <= rr) {      // complete overlap
                // Micro-optimisation: each edge is stored in minimum number of nodes, such that an edge in full overlap node will influence all nodes below it
                tree[index].add(edgeIdx);   // Info: add the edgeIndex and backtrack
                return;
            }
            // partial overlap
            int mid = (rl + rr) >>> 1;
            addInterval(index << 1, rl, mid, ql, qr, edgeIdx);
            addInterval(index << 1 | 1, mid, rr, ql, qr, edgeIdx);
        }

        public void dfs(int index, int l, int r, UnionFind uf, List<int[]> queries, final int res[], final int edges[][]) {
            int snap = uf.snapshot();   // gather the snapshot
            // For each edgeIndex that is a full overlap for this node
            for(int edgeIdx : tree[index]) {
                int u = edges[edgeIdx][0], v = edges[edgeIdx][1];
                uf.union(u, v);     // perform the union operation of the two nodes
            }
            // Note: Operations in the dfs are checked on leaf nodes
            if(r-l == 1) {
                if(queries.get(l)[0] == 3) {
                    final int u = queries.get(l)[1], v = queries.get(l)[2];
                    // check the operation (connectivity, components count, etc.)
                    res[l] = uf.operation(u, v) ? 1 : -1;
                }
            } else {
                int mid = (l+r) >>> 1;
                dfs(index << 1, l, mid, uf, queries, res, edges);       // left subtree
                dfs(index << 1 | 1, mid, r, uf, queries, res, edges);   // right subtree
            }
            // Info: In post order fashion re-roll the edges added when passing out of the current node
            uf.rollback(snap);
        }
    }
}
