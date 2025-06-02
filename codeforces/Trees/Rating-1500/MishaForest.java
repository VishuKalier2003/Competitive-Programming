import java.io.*;
import java.util.*;

public class MishaForest {
    public static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c, x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nextLong() throws IOException {
            int c;
            long x = 0L;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "MishaForest", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void run() throws IOException {
        FastReader in = new FastReader();
        int n = in.nextInt();

        // Read degree[i] and sx[i] for each vertex i = 0..n-1
        int[] degree = new int[n];
        int[] sx = new int[n];
        for (int i = 0; i < n; i++) {
            degree[i] = in.nextInt();
            sx[i] = in.nextInt();
        }

        // Prepare a queue of all vertices of degree == 1
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                q.addLast(i);
            }
        }

        // We'll collect the edges in an ArrayList of pairs
        ArrayList<int[]> edges = new ArrayList<>();

        // Peel leaves until the queue is empty
        while (!q.isEmpty()) {
            int u = q.removeFirst();
            // If u is no longer a leaf (deg != 1), skip it
            if (degree[u] != 1) {
                continue;
            }
            // Its unique neighbor is sx[u]
            int v = sx[u];
            // Record the edge (u,v)
            edges.add(new int[] { u, v });

            // "Remove" u from the forest by updating v's info
            degree[u]--; // becomes 0
            degree[v]--; // loses one neighbor

            // Remove u from v's XOR‐sum:
            sx[v] ^= u;

            // If v has just become a leaf, add it to the queue
            if (degree[v] == 1) {
                q.addLast(v);
            }
        }

        // At this point, 'edges' contains exactly all m edges of the forest
        // Print the result
        System.out.println(edges.size());
        for (int[] e : edges) {
            System.out.println(e[0] + " " + e[1]);
        }
    }
}
