// Ques -

// Note- DSU to create components
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Problem893C {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static class DisjointSet {
        public int n;
        public int rank[], parent[];

        public DisjointSet(int value) {
            this.n = value;
            this.rank = new int[n];
            this.parent = new int[n];
            for(int i = 0; i < n; i++)
                this.parent[i] = i;
        }

        public int find(int x) {
            if(parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if(rootX != rootY) {
                if(rank[rootX] < rank[rootY])
                    parent[rootX] = rootY;
                else if(rank[rootY] < rank[rootX])
                    parent[rootY] = rootX;
                else {
                    parent[rootX] = rootY;
                    rank[rootY]++;
                }
            }
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), m = fast.nextInt();
        long nums[] = new long[n+1];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextLong();
        List<int[]> connections = new ArrayList<>();
        for(int i = 0; i < m; i++)
            connections.add(new int[]{fast.nextInt(), fast.nextInt()});
        System.out.println(solve(n+1, m, nums, connections));
    }

    public static long solve(final int n, final int m, final long nums[], final List<int[]> connections) {
        DisjointSet dsu = new DisjointSet(n);
        componentSplit(n, connections, dsu);
        Map<Integer, Long> dataMap = new HashMap<>();
        for(int i = 1; i < n; i++) {
            int root = dsu.find(i);
            dataMap.put(root, Math.min(dataMap.getOrDefault(root, Long.MAX_VALUE), nums[i-1]));
        }
        return dataMap.values().stream().mapToLong(Long::longValue).sum();
    }

    public static void componentSplit(final int n, final List<int[]> connections, DisjointSet dsu) {
        for(int[] edge : connections)
            dsu.union(edge[0], edge[1]);
        return;
    }
}
