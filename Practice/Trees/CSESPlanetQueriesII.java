import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

public class CSESPlanetQueriesII {
    public static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0) return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ') if (c < 0) return -1;
            boolean neg = c == '-';
            if (neg) c = read();
            do { x = x * 10 + (c - '0'); } while ((c = read()) >= '0' && c <= '9');
            return neg ? -x : x;
        }
    }

    public static int[] to;
    public static List<Integer>[] rev;
    public static List<int[]> queries;
    public static int maxJump;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        int n = fast.nextInt(), q = fast.nextInt();
        to = new int[n+1];
        for (int i = 1; i <= n; i++) to[i] = fast.nextInt();
        queries = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            int a = fast.nextInt(), b = fast.nextInt();
            queries.add(new int[]{a, b});
            maxJump = Math.max(maxJump, b);
        }
        solve(n);
    }

    @SuppressWarnings("unchecked")
    public static void solve(int n) {
        // build reverse graph
        rev = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) rev[i] = new ArrayList<>();
        for (int i = 1; i <= n; i++) rev[to[i]].add(i);

        // peel non-cycle nodes
        Deque<Integer> dq = new ArrayDeque<>();
        int[] indeg = new int[n+1];
        for (int i = 1; i <= n; i++) indeg[to[i]]++;
        for (int i = 1; i <= n; i++) if (indeg[i] == 0) dq.add(i);
        while (!dq.isEmpty()) {
            int u = dq.poll();
            int v = to[u];
            if (--indeg[v] == 0) dq.add(v);
        }
        List<Integer> cycle = new ArrayList<>();
        for (int i = 1; i <= n; i++) if (indeg[i] > 0) cycle.add(i);

        // depth to cycle
        int[] depth = new int[n+1];
        boolean[] seen = new boolean[n+1];
        dq = new ArrayDeque<>(cycle);
        for (int u: cycle) seen[u] = true;
        while (!dq.isEmpty()) {
            int v = dq.poll();
            for (int u: rev[v]) {
                if (!seen[u]) {
                    seen[u] = true;
                    depth[u] = depth[v] + 1;
                    dq.add(u);
                }
            }
        }

        // cycle components
        int[] comp = new int[n+1], pos = new int[n+1];
        List<Integer> clenList = new ArrayList<>();
        Arrays.fill(seen, false);
        for (int u: cycle) if (!seen[u]) {
            int cur=u, idx=0;
            while (!seen[cur]) {
                seen[cur]=true;
                comp[cur]=clenList.size();
                pos[cur]=idx++;
                cur=to[cur];
            }
            clenList.add(idx);
        }
        int[] cycleLen = clenList.stream().mapToInt(x->x).toArray();

        // binary lifting
        int LOG = 32 - Integer.numberOfLeadingZeros(maxJump);
        int[][] up = new int[LOG+1][n+1];
        for (int i = 1; i <= n; i++) up[0][i] = to[i];
        for (int j = 1; j <= LOG; j++) {
            for (int i = 1; i <= n; i++) up[j][i] = up[j-1][ up[j-1][i] ];
        }

        // answer
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        for (int[] qu: queries) {
            int a=qu[0], b=qu[1], ans;
            if (depth[b]>0) {
                if (depth[a]<depth[b]) ans=-1;
                else {
                    int d = depth[a]-depth[b], u=a;
                    for (int j=0; j<=LOG; j++) if ((d&(1<<j))!=0) u=up[j][u];
                    ans = (u==b?d:-1);
                }
            } else {
                int da=depth[a], u=a;
                for (int j=0; j<=LOG; j++) if ((da&(1<<j))!=0) u=up[j][u];
                if (comp[u]!=comp[b]) ans=-1;
                else {
                    int clen=cycleLen[comp[b]];
                    int dp=(pos[b]-pos[u]+clen)%clen;
                    ans=da+dp;
                }
            }
            out.println(ans);
        }
        out.flush();
    }
}
