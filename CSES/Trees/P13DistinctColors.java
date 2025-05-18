import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P13DistinctColors {
    public static class FastReader {
        private static final byte buffer[] = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if(ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if(len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0l;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = x * 10 + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }
    }

    public static void main(String[] args) {
        Thread distinctThread = new Thread(null,
        () -> {
            try {
                callMain(args);
            } catch(IOException e) {
                e.getLocalizedMessage();
            }
        },
        "distinct-colors",
        1 << 26);
        distinctThread.start();
        try {
            distinctThread.join();
        } catch(InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static List<List<Integer>> tree;

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        tree = new ArrayList<>();
        final int n = fast.nextInt();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        int color[] = new int[n+1];
        for(int i = 1; i <= n; i++)
            color[i] = fast.nextInt();
        for(int i = 1; i <= n-1; i++) {
            int n1 = fast.nextInt(), n2 =fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        solve(n, color);
    }

    public static void solve(final int n, final int color[]) {
        EulerTour eulerTour = new EulerTour(n+1, color);
        List<int[]> queries = new ArrayList<>();
        for(int i = 1; i <= n; i++)
            queries.add(new int[]{eulerTour.inTime[i], eulerTour.outTime[i], i});
        Mo mo = new Mo(n, eulerTour.flattened, queries);
        final StringBuilder output = new StringBuilder();
        int res[] = mo.solveQueries();
        for(int i = 1; i <= n; i++)
            output.append(res[i]).append(" ");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static final class EulerTour {
        private final int inTime[], outTime[], flattened[];
        private int time;

        public EulerTour(final int n, final int property[]) {
            this.inTime = new int[n+1];
            this.outTime = new int[n+1];
            this.flattened = new int[n+1];
            this.time = 1;
            dfsIterative(property);
        }

        public void dfs(int root, int parent, final int color[]) {
            this.inTime[root] = time;
            // Hack: We only update the values at in-time since each node will be traversed once, so the array needs to only account for in time
            this.flattened[time] = color[root];
            this.time++;
            for(int child : tree.get(root))
                if(child != parent)
                    dfs(child, root, color);
            this.outTime[root] = this.time-1;
        }

        public void dfsIterative(final int color[]) {
            ArrayDeque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{1, -1, 0});
            while(!stack.isEmpty()) {
                int data[] = stack.pop();
                int root = data[0], parent = data[1], phase = data[2];
                if(phase == 0) {
                    this.inTime[root] = time;
                    this.flattened[time] = color[root];
                    this.time++;
                    stack.push(new int[]{root, parent, 1});
                    for(int child : tree.get(root))
                        if(child != parent)
                            stack.push(new int[]{child, root, 0});
                } else if(phase == 1) 
                    this.outTime[root] = this.time-1;
            }
        }
    }

    public static final class Mo {
        private int curL, curR, distinct;
        private final Map<Integer, Integer> freq;
        private List<int[]> queries;
        private final int array[];
        private final int BLOCK;

        public Mo(final int n, final int flattened[], final List<int[]> queries) {
            this.BLOCK = (int)Math.max(1, Math.sqrt(n));
            this.freq = new HashMap<>();
            this.array = flattened;
            this.queries = queries;
            Collections.sort(queries, (int q1[], int q2[]) -> {
                // Info: If they lie in same bucket of size sqrt(N)
                if(q1[0]/BLOCK == q2[0]/BLOCK)      // Checking the left boundary and the corresponding bucket in which it will lie
                    return Integer.compare(q1[1], q2[1]);
                return Integer.compare(q1[0], q2[0]);
            });
            this.curL = queries.get(0)[0];
            this.curR = queries.get(0)[0]-1;
            this.distinct = 0;
        }

        public void addOnCurrent(int index) {
            int color = this.array[index];
            this.freq.put(color, this.freq.getOrDefault(color, 0) + 1);
            if(this.freq.get(color) == 1)
                this.distinct++;
        }

        public void removeOnCurrent(int index) {
            int color = this.array[index];
            this.freq.put(color, this.freq.get(color)-1);
            if(this.freq.get(color) == 0)
                this.distinct--;
        }

        public int[] solveQueries() {
            int result[] = new int[this.queries.size()+1];      // 1 based indexing
            for(int[] q : queries) {
                while(this.curL > q[0]) {
                    this.curL--;
                    addOnCurrent(this.curL);
                }
                while(this.curL < q[0]) {
                    removeOnCurrent(this.curL);
                    this.curL++;
                }
                while(this.curR > q[1]) {
                    removeOnCurrent(this.curR);
                    this.curR--;
                }
                while(this.curR < q[1]) {
                    this.curR++;
                    addOnCurrent(this.curR);
                }
                result[q[2]] = this.distinct;
            }
            return result;
        }
    }
}