import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P2MaxXORsubarray {
    public static class FastReader {        // Note: FastReader for taking inputs
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

        public int nextInt() throws IOException {       // Integer input
            int c;
            int x = 0;
            while((c = read()) <= ' ')
                if(c < 0)
                    return -1;
            boolean neg = c == '-';
            if(neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while((c = read()) <= '9' && c >= '0');
            return x;
        }

        public long nextLong() throws IOException {     // Long input
            int c;
            long x = 0l;
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
    }

    public static void main(String args[]) {
        // Info: Threading for faster memory allocation and provision of larger memory 1 << 26 bytes
        Thread t1 = new Thread(null,
        () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, 
        "max-xor-subarray",
        1 << 26);
        t1.start();
        try {
            t1.join();
        } catch(InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fast = new FastReader();
        final int n = fast.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++)
            nums[i] = fast.nextInt();
        solve(n, nums);
    }

    public static void solve(final int n, final int nums[])  {
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        Trie trie = new Trie();
        trie.insert(0);
        int prefix = 0, maxXor = 0;
        for(int i = 0; i < n; i++) {
            prefix ^= nums[i];      // Compute the prefix
            maxXor = Math.max(maxXor, trie.query(prefix));      // Info: Check for current prefix i, is there any j, j < i, that can maximize trie
            trie.insert(prefix);        // We add later, since we do not want the current prefix to be included in checking the maxXor in current iteration
        }
        output.append(maxXor);
        writer.write(output.toString());
        writer.flush();
    }

    public static class TrieNode {
        TrieNode child[];

        public TrieNode() {
            this.child = new TrieNode[2];       // Info: Binary prefix trie, since we will have only two choices per depth, either 1 or 0
        }
    }

    // Hack: Use a Trie (prefix) to store all prefix (xor's) uptil i and then find the best xor, since we can greedily reach the best xor in O(h) here O(log n)
    public static final class Trie {
        TrieNode root = new TrieNode();

        public void insert(int num) {
            TrieNode node = root;       // Info: We always start from root node
            // Note: Building from 31 to 0, since we want max value and to first take max value we start from 31 down to 0
            for(int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if(node.child[bit] == null)     // Here depth is represented by the power of bit (2^31 at level 1, 2^30 at level 2)
                    node.child[bit] = new TrieNode();
                node = node.child[bit];
            }
        }

        public int query(int num) {     // Querying the current num to get max xor
            TrieNode node = root;
            int maxXor = 0;
            for(int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;       // Info: If we want the current bit, to maximize the xor we must flip the current bit
                int desired = 1 - bit;
                if(node.child[desired] != null) {       // Note: If the desired flipped bit is present, then we can xor with the i-th bit
                    maxXor |= 1 << i;
                    node = node.child[desired];
                } else {
                    node = node.child[bit];
                }
            }
            return maxXor;
        }
    }
}
