// Ques - https://codeforces.com/problemset/problem/1325/C

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EhabandMEX {
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
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
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
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
                x = 10*x + (c-'0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {}
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(
            null, () -> {
                try {
                    callMain(args);
                } catch(IOException e) {
                    e.getLocalizedMessage();
                }
            },
            "journey",
            1 << 26
        );
        t1.start();
        try {
            t1.join();
        } catch(InterruptedException IE) {IE.getLocalizedMessage();}
    }

    private static List<List<Integer>> tree;
    private static final long C = 1_00_000L;
    private static Map<Long, Integer> resultIndex;
    private static int[] result;
    private static boolean[] used;

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        tree = new ArrayList<>();
        for(int i = 0; i <= n; i++)
            tree.add(new ArrayList<>());
        
        resultIndex = new HashMap<>();
        result = new int[n-1];
        used = new boolean[n-1];
        
        for(int i = 0; i < n-1; i++) {
            int u = fast.nextInt(), v = fast.nextInt();
            long hash1 = C*u + v;
            long hash2 = C*v + u;
            tree.get(u).add(v);
            tree.get(v).add(u);
            resultIndex.put(hash1, i);
            resultIndex.put(hash2, i);
        }
        
        solve(n);
        
        // Output results
        for(int i = 0; i < n-1; i++) {
            System.out.println(result[i]);
        }
    }

    public static void solve(final int n) {
        // Find a node with degree >= 3
        int highDegreeNode = -1;
        for(int i = 1; i <= n; i++) {
            if(tree.get(i).size() >= 3) {
                highDegreeNode = i;
                break;
            }
        }
        
        if(highDegreeNode != -1) {
            // Tree has a node with degree >= 3
            // Assign labels 0, 1, 2 to three incident edges
            List<Integer> neighbors = tree.get(highDegreeNode);
            for(int i = 0; i < 3; i++) {
                int neighbor = neighbors.get(i);
                long hash = C * highDegreeNode + neighbor;
                if(resultIndex.containsKey(hash)) {
                    int edgeIndex = resultIndex.get(hash);
                    result[edgeIndex] = i;
                    used[edgeIndex] = true;
                }
            }
            
            // Assign remaining labels arbitrarily
            int nextLabel = 3;
            for(int i = 0; i < n-1; i++) {
                if(!used[i]) {
                    result[i] = nextLabel++;
                }
            }
        } else {
            // Tree is a path (bamboo) - assign labels arbitrarily
            for(int i = 0; i < n-1; i++) {
                result[i] = i;
            }
        }
    }
}