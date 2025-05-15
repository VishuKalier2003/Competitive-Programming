import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class TreeJumps {
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
            Map<Integer, Set<Integer>> tree = new HashMap<>();
            for(int i = 1; i <= n; i++)
                tree.put(i, new HashSet<>());
            for(int i = 2; i <= n; i++)
                tree.get(fast.nextInt()).add(i);
            builder.append(countValidSequences(n, tree)).append("\n");
        }
        System.out.print(builder);
    }

    public static final int MOD = 998244353;

    public static int countValidSequences(final int n, Map<Integer, Set<Integer>> tree) {
        Map<Integer, Integer> levelRegions = bfsLayering(1, tree);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        int level = 1, result = 0;      // Variable to store result
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                int node = queue.poll();
                if(node == 1)       // If root node encountered
                    result = levelRegions.get(level+1) % MOD;
                else {
                    int children = tree.get(node).size();       // Extract the number of children
                    // IMP- find all the next level nodes that are not children of the current node
                    result = (result + (levelRegions.get(level+1) - children) % MOD) % MOD;
                }
                for(int child : tree.get(node))     // For each child
                    queue.add(child);
            }
            level++;
        }
        return (result+1) % MOD;        // Increase result by 1, to store the root node itself
    }

    public static Map<Integer, Integer> bfsLayering(final int source, Map<Integer, Set<Integer>> tree) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> levelMap = new HashMap<>();       // IMP- counting number of nodes at each level
        queue.add(source);
        int level = 1;
        while(!queue.isEmpty()) {
            int size = queue.size();
            levelMap.put(level, size);      // Updating level
            for(int i = 0; i < size; i++) {
                int node = queue.poll();
                for(int neighbor : tree.get(node))
                    queue.add(neighbor);
            }
            level++;
        }
        levelMap.put(level, 0);     // IMP- Adding another level to lowest level as the decoy (dummy) level
        return levelMap;
    }
}
