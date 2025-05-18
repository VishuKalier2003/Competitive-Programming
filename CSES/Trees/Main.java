import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static List<Integer>[] tree;
    static int[] color, compColor;
    static int[] subSize, heavy, answer;
    static int[] freq;
    static int distinctCount;

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        n = fr.nextInt();

        // Read and compress colors
        tree = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) tree[i] = new ArrayList<>();
        color = new int[n+1];
        Integer[] tmp = new Integer[n];
        for (int i = 1; i <= n; i++) {
            tmp[i-1] = fr.nextInt();
        }
        Integer[] sorted = tmp.clone();
        Arrays.sort(sorted);
        Map<Integer,Integer> comp = new HashMap<>();
        int idx = 0;
        for (int x : sorted) {
            if (!comp.containsKey(x)) comp.put(x, idx++);
        }
        compColor = new int[n+1];
        for (int i = 1; i <= n; i++) {
            compColor[i] = comp.get(tmp[i-1]);
        }

        // Build tree
        for (int i = 1; i < n; i++) {
            int u = fr.nextInt(), v = fr.nextInt();
            tree[u].add(v);
            tree[v].add(u);
        }

        subSize = new int[n+1];
        heavy = new int[n+1];
        answer = new int[n+1];
        freq = new int[n];
        distinctCount = 0;

        // Iterative computation of subtree sizes & heavy child
        computeSubtreeSizesIterative();
        // Iterative DSU-on-tree
        dsuOnTreeIterative();

        // Output
        PrintWriter pw = new PrintWriter(System.out);
        for (int i = 1; i <= n; i++) {
            pw.print(answer[i]);
            pw.print(' ');
        }
        pw.flush();
    }

    // Iterative dfs to compute subSize and heavy arrays
    static void computeSubtreeSizesIterative() {
        int[] parent = new int[n+1];
        boolean[] vis = new boolean[n+1];
        Stack<Integer> dfs = new Stack<>();
        Stack<Integer> post = new Stack<>();

        dfs.push(1);
        parent[1] = -1;
        while (!dfs.isEmpty()) {
            int u = dfs.pop();
            vis[u] = true;
            post.push(u);
            for (int v : tree[u]) {
                if (!vis[v]) {
                    parent[v] = u;
                    dfs.push(v);
                }
            }
        }
        while (!post.isEmpty()) {
            int u = post.pop();
            subSize[u] = 1;
            int maxSz = 0;
            for (int v : tree[u]) {
                if (v == parent[u]) continue;
                subSize[u] += subSize[v];
                if (subSize[v] > maxSz) {
                    maxSz = subSize[v];
                    heavy[u] = v;
                }
            }
        }
    }

    // Iterative DSU-on-tree
    static void dsuOnTreeIterative() {
        Map<Integer,Integer> cnt = new HashMap<>();
        Stack<DsuState> stack = new Stack<>();
        stack.push(new DsuState(1, -1, true));

        while (!stack.isEmpty()) {
            DsuState s = stack.pop();
            int u = s.u, p = s.p;
            boolean keep = s.keep;
            // light children
            for (int v : tree[u]) {
                if (v == p || v == heavy[u]) continue;
                stack.push(new DsuState(v, u, false));
            }
            // heavy child
            if (heavy[u] != 0) stack.push(new DsuState(heavy[u], u, true));
            // merge lights
            for (int v : tree[u]) {
                if (v == p || v == heavy[u]) continue;
                addSubtree(v, u, cnt, +1);
            }
            // add self
            cnt.put(compColor[u], cnt.getOrDefault(compColor[u], 0) + 1);
            if (cnt.get(compColor[u]) == 1) distinctCount++;
            answer[u] = distinctCount;
            // remove if !keep
            if (!keep) addSubtree(u, p, cnt, -1);
        }
    }

    static void addSubtree(int start, int par, Map<Integer,Integer> cnt, int delta) {
        Stack<int[]> stk = new Stack<>();
        stk.push(new int[]{start, par});
        while (!stk.isEmpty()) {
            int[] pr = stk.pop();
            int u = pr[0], p = pr[1];
            int c = compColor[u];
            int val = cnt.getOrDefault(c, 0) + delta;
            if (val == 0) cnt.remove(c);
            else cnt.put(c, val);
            for (int v : tree[u]) {
                if (v == p) continue;
                stk.push(new int[]{v, u});
            }
        }
    }

    static class DsuState {
        int u, p;
        boolean keep;
        DsuState(int u, int p, boolean keep) { this.u = u; this.p = p; this.keep = keep; }
    }

    static class FastReader {
        final BufferedInputStream bis = new BufferedInputStream(System.in);
        int read() throws IOException { return bis.read(); }
        int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') ;
            do { x = x * 10 + (c - '0'); } while ((c = read()) >= '0' && c <= '9');
            return x;
        }
    }
}