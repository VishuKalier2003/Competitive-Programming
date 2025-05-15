// Queen - https://codeforces.com/problemset/problem/1143/C

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;

public class Queen {
    public static class FastReader {
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

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        respectMap = new HashMap<>();
        for(int i = 0; i < 2; i++)
            respectMap.put(i, new HashSet<>());
        for(int i = 1; i <= n; i++) {
            int node = fast.nextInt(), respect = fast.nextInt();
            if(node == -1)
                root = i;
            else
                tree.get(node).add(i);
            respectMap.get(respect).add(i);
        }
        solve(n);
    }

    public static List<List<Integer>> tree;
    public static int root;
    public static Map<Integer, Set<Integer>> respectMap;

    public static void solve(final int n) throws IOException {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        dfs(root, minHeap);
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        if(minHeap.isEmpty())
            output.append(-1);
        else
            while(!minHeap.isEmpty())
                output.append(minHeap.poll()).append(" ");
        writer.write(output.toString());
        writer.flush();
    }

    public static void dfs(int node, PriorityQueue<Integer> minHeap) {
        if(tree.get(node).isEmpty()) {
            if(respectMap.get(1).contains(node) && node != root)
                minHeap.add(node);
            return;
        }
        boolean evil = respectMap.get(1).contains(node) && node != root;
        for(int child : tree.get(node)) {
            if(!respectMap.get(1).contains(child)) {
                evil = false;
                break;
            }
        }
        if(evil)
            minHeap.add(node);
        for(int child : tree.get(node))
            dfs(child, minHeap);
        return;
    }
}
