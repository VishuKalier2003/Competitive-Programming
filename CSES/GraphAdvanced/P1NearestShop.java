import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P1NearestShop {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
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
    }

    // Micro-optimisation: FastWriter class
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

    // Main entry
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Nearest-Shop-(https://cses.fi/problemset/task/3303/)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Globals
    private static final StringBuilder output = new StringBuilder();
    private static final List<List<Integer>> g = new ArrayList<>();
    private static final Set<Integer> shops = new HashSet<>();
    private static int[] nearestShop;

    // Driver
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final int n = fr.nextInt(), m = fr.nextInt(), k = fr.nextInt();
        for (int i = 0; i <= n; i++)
            g.add(new ArrayList<>());
        for (int i = 0; i < k; i++)
            shops.add(fr.nextInt());
        for (int j = 0; j < m; j++) {
            final int u = fr.nextInt(), v = fr.nextInt();
            g.get(u).add(v);
            g.get(v).add(u);
        }
        solve(n, k);
        fw.attachOutput(output);
        fw.printOutput();
    }

    // Note: The technique is processing intersection edges later after multi-source bfs (delayed processing)
    public static void solve(final int n, final int k) {
        nearestShop = new int[n + 1];       // Nearest shop
        int dist[] = bfs(n);
        int shopAns[] = new int[n+1];
        Arrays.fill(shopAns, Integer.MAX_VALUE);        // result array for each node the nearest shop (exclusively for shops)
        for(int node = 1; node <= n; node++) {
            for(int neighbor : g.get(node)) {
                // Info: If the nearest shop for both nodes exist and is different, intersection is found
                if(nearestShop[node] != -1 && nearestShop[neighbor] != -1 && nearestShop[node] != nearestShop[neighbor]) {
                    int cand = dist[node] + dist[neighbor] + 1;     // candidate defined
                    int shopA = nearestShop[node], shopB = nearestShop[neighbor];
                    shopAns[shopA] = Math.min(shopAns[shopA], cand);        // min distance computed
                    shopAns[shopB] = Math.min(shopAns[shopB], cand);        // min distance computed
                }
            }
        }
        // The result is built using shopAns and dist array
        for(int i = 1; i <= n; i++) {
            if(shops.contains(i))       // shopAns used exclusively for shops
                output.append(shopAns[i] == Integer.MAX_VALUE ? -1 : shopAns[i]).append(" ");
            else
                output.append(dist[i] == Integer.MAX_VALUE ? -1 : dist[i]).append(" ");
        }
    }

    // Note: Multi-source bfs runs in O(n)
    public static int[] bfs(final int n) {
        int dist[] = new int[n + 1];
        boolean vis[] = new boolean[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(nearestShop, -1);
        Deque<Integer> q = new ArrayDeque<>();
        for(int shop : shops) {     // Info: All source nodes collectively pushed into the deque
            q.offer(shop);
            dist[shop] = 0;
            nearestShop[shop] = shop;       // marking the nearest shop for each node, becomes easier to preprocess later
            vis[shop] = true;
        }
        while(!q.isEmpty()) {
            int u = q.poll();
            for(int v : g.get(u)) {     // For each neighbor
                // Info: Ensures that each node is visited by only one source
                if(!vis[v]) {
                    vis[v] = true;
                    dist[v] = dist[u] + 1;      // distance is updated on unit basis
                    nearestShop[v] = nearestShop[u];        // updating the node with the nearest shop from bfs
                    q.offer(v);
                }
            }
        }
        return dist;
    }
}
