import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Euler {
    static class FastReader {
        private final byte[] buf = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            do {
                x = x * 10 + (c - '0');
            } while ((c = read()) >= '0' && c <= '9');
            return x;
        }

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buf);
                if (len <= 0)
                    return -1;
            }
            return buf[ptr++];
        }
    }

    public static int nodeValues[]; // Note: These are my initial values that are updated with deltas
    public static List<List<Integer>> tree;

    public static void main(String[] args) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt(), q = fast.nextInt();
        List<int[]> queries = new ArrayList<>();
        nodeValues = new int[n + 1];
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        for (int i = 0; i < n; i++)
            nodeValues[i + 1] = fast.nextInt();
        for (int i = 0; i < n - 1; i++) {
            int n1 = fast.nextInt(), n2 = fast.nextInt();
            tree.get(n1).add(n2);
            tree.get(n2).add(n1);
        }
        for (int i = 0; i < q; i++) {
            int type = fast.nextInt();
            if (type == 1)
                queries.add(new int[] { 1, fast.nextInt(), fast.nextInt() });
            else
                queries.add(new int[] { 2, fast.nextInt() });
        }
        solve(n, q, queries);
    }

    public static void solve(final int n, final int q, final List<int[]> queries) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        EulerTour eulerTour = new EulerTour(n);
        eulerTour.eulerIterative();
        // Note: The indices in flatten store the time, and when we want to query over range we simply do query over [nodeIn, nodeOut] because indirectly nodeOut will be present there because nodeOut is a time as well, and nodeIn is important because it is the entry point of subtree rooted at u
        int flatten[] = new int[n + 1];
        for (int i = 1; i <= n; i++)
            flatten[(int) eulerTour.nodeIn[i]] = nodeValues[i];
        Fenwick fenwick = new Fenwick(n);
        for (int i = 1; i <= n; i++) // Note: Flatten array is only used to bulk initialize the fenwick tree
            fenwick.update(i, flatten[i]);
        for (int query[] : queries) {
            if (query[0] == 1) {
                int s = query[1], update = query[2];
                int idx = (int) eulerTour.nodeIn[s]; // Info: Flatten index to be determined
                long delta = update - nodeValues[s];
                nodeValues[s] = update;
                fenwick.update(idx, delta);
            } else {
                int s = query[1];
                int inIndex = (int) eulerTour.nodeIn[s];
                int outIndex = (int) eulerTour.nodeOut[s];
                // Info: We use inIndex as first parameter since inIndex represents left range
                // of subtree and outIndex the right range of the subtree
                builder.append(fenwick.sumQuery(inIndex, outIndex)).append("\n");
            }
        }
        writer.write(builder.toString());
        writer.flush();
    }

    public static class EulerTour {
        public long nodeIn[], nodeOut[];
        public long time;

        public EulerTour(int n) // 0 based indexing
        {
            nodeIn = new long[n + 1]; // 1 based indexing
            nodeOut = new long[n + 1]; // 1 based indexing
            time = 1; // Info: To guarantee node-in time is always <= n and for 1 based indexing we start at 1
        }

        public void eulerDfs(int root, int parent) {
            nodeIn[root] = time++;
            for (int child : tree.get(root)) {
                if (child == parent)
                    continue;
                eulerDfs(child, root); // Time does not update between siblings, we only update time when going deeper
            }
            // Info: To fit into n+1 flatten array, else array size should be n+2
            nodeOut[root] = time - 1; // To guarantee node-out time is always <= n
        }

        public void eulerIterative() {
            Deque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[] { 1, -1, 0 });
            while (!stack.isEmpty()) {
                int data[] = stack.pop();
                int r = data[0], p = data[1], phase = data[2];
                // phase 0 means entering the subtree
                if (phase == 0) { // Give phase values as entering the subtree and leaving the subtree
                    stack.push(new int[] { r, p, 1 });
                    nodeIn[r] = time++;
                    for (int child : tree.get(r)) {
                        if (child == p)
                            continue;
                        stack.push(new int[] { child, r, 0 });
                    }
                    // phase 1 means leaving the subtree
                } else if (phase != 0)
                    nodeOut[r] = time - 1;
            }
        }
    }

    public static class Fenwick {
        public long tree[];
        public int size;

        public Fenwick(int n) {
            this.size = n;
            this.tree = new long[size + 1];
        }

        public void update(int i, long delta) {
            while (i <= size) {
                tree[i] += delta;
                i += i & -i;
            }
        }

        public long query(int i) {
            long sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= i & -i;
            }
            return sum;
        }

        public long sumQuery(int l, int r) {
            return query(r) - query(l - 1);
        }
    }
}
