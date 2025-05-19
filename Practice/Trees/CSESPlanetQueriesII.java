
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSESPlanetQueriesII {
    public static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read();
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c =='-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static void main(String args[]) {
        Thread t1 = new Thread(null,
        () -> {
            try {
                callMain(args);
            } catch(IOException e) {
                e.getLocalizedMessage();
            }
        },
        "planet-queries-II",
        1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        }
    }

    public static Map<Integer, Integer> directedTree;
    public static Map<Integer, List<Integer>> reverseTree;
    public static List<int[]> queries;
    public static int maxJump;

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        directedTree = new HashMap<>();
        reverseTree = new HashMap<>();
        int teleporters[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            teleporters[i] = fast.nextInt();
        queries = new ArrayList<>();
        for(int i = 0; i < q; i++) {
            int x = fast.nextInt(), k = fast.nextInt();
            maxJump = Math.max(maxJump, k);
            queries.add(new int[]{x, k});
        }
        solve(n, teleporters);
    }

    public static void solve(final int n, final int teleporters[]) {
        for(int i = 1; i <= n; i++)
            reverseTree.put(i, new ArrayList<>());
        for(int i = 1; i <= n; i++) {
            directedTree.put(i, teleporters[i]);
            reverseTree.get(teleporters[i]).add(i);
        }
        BinaryLifting bl = new BinaryLifting(n, teleporters);
        bl.computeDepths(topoSort(n));
    }

    public static ArrayDeque<Integer> topoSort(final int n) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        int indegree[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            indegree[directedTree.get(i)]++;
        for(int i = 1; i <= n; i++)
            if(indegree[i] == 0)
                queue.add(i);
        while(!queue.isEmpty()) {
            int node = queue.poll();
            int nextNode = directedTree.get(node);
            indegree[nextNode]--;
            if(indegree[nextNode] == 0)
                queue.add(nextNode);
        }
        ArrayDeque<Integer> cycleNodes = new ArrayDeque<>();
        for(int i = 1; i <= n; i++)
            if(indegree[i] > 0)
                cycleNodes.add(i);
        return cycleNodes;
    }

    public static final class BinaryLifting {
        private final int lift[][];
        private final int depth[];
        private final int n, maxLog;

        public BinaryLifting(final int n, final int teleporters[]) {
            this.n = n;
            this.maxLog = 32 - Integer.numberOfLeadingZeros(maxJump);
            this.lift = new int[this.maxLog+1][this.n+1];
            this.depth = new int[this.n+1];
            for(int row[] : lift)
                Arrays.fill(row, -1);
            binaryLiftIterative(n, teleporters);
        }

        public void binaryLiftIterative(final int n, final int teleporters[]) {
            for(int i = 1; i <= n; i++)
                this.lift[0][i] = teleporters[i];
            for(int j = 1; j <= this.maxLog; j++)
                for(int i = 1; i <= n; i++)
                    this.lift[j][i] = this.lift[j-1][this.lift[j-1][i]];
        }

        public void computeDepths(ArrayDeque<Integer> queue) {
            boolean visited[] = new boolean[this.n+1];
            for(int node : queue) {
                visited[node] = true;       // Mark the cycle nodes before the queueing
                this.depth[node] = 0;       // Will be automatically handled
            }
            while(!queue.isEmpty()) {
                int node = queue.poll();
                for(int child : reverseTree.get(node)) {
                    if(!visited[child]) {
                        visited[child] = true;
                        this.depth[child] = this.depth[node] + 1;
                        queue.add(child);
                    }
                }
            }
        }

        public int lifting(int root, int k) {
            int ancestor = root;
            for(int i = 0; i <= this.maxLog; i++)
                if((k & 1 << i) != 0) {
                    ancestor = this.lift[i][ancestor];
                }
            return ancestor;
        }

        public int lca(int x, int y) {
            if(this.depth[x] == 0 || this.depth[y] == 0)
                return -1;
            if(this.depth[x] > this.depth[y])
                x = lifting(x, this.depth[x] - this.depth[y]);
            else if(this.depth[y] > this.depth[x])
                y = lifting(y, this.depth[y] - this.depth[x]);
            if(x == y && this.depth[x] != 0)
                return x;
            for(int k = this.maxLog; k >= 0; k--) {}
            return 0;
        }
    }
}
