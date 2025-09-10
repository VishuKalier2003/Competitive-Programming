import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P1WordCombination {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        // Creates a 1MB buffer such that 1MB of data is stored
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException { // reading byte
            if (ptr >= len) {
                ptr = 0;
                // len marks the length of the last unchecked index in the buffer
                len = System.in.read(buffer); // Stores the entire buffer data in read
                if (len <= 0)
                    return -1;
            }
            // Extract buffer and move pointer to next without calling System.in.read()
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException { // reading int
            int x = 0, c;
            while ((c = read()) <= ' ') // While whitespace is not provided
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

        public long nextLong() throws IOException { // reading long
            long x = 0l, c;
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

        public String next() throws IOException { // reading string (whitespace exclusive)
            int c;
            while ((c = read()) <= ' ') // Read until whitespace
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        public String nextLine() throws IOException { // reading string (whitespace inclusive)
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (c < 0)
                return null;
            while (c != '\n' && c >= 0) {
                if (c != '\r')
                    sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }
    }

    // Micro-optimisation: FastWriter class to reduce flushes in each write
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

    // Micro-optimisation: creating new thread, not hitting MLE
    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Word-Combinations-(https://cses.fi/problemset/task/1731)", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    // Micro-optimisation: defining final variables stored in cache reducing time
    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        final String s = fr.next();
        final int n = fr.nextInt();
        for(int i = 0; i < n; i++)
            trie.insert(fr.next());
        solve(s.length(), s);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final Trie trie = new Trie();
    private static final StringBuilder output = new StringBuilder();
    private static final int MOD = 1_000_000_007;

    // Note: Prefix Trie
    public static void solve(final int len, final String s) {
        long dp[] = new long[len+1];
        dp[0] = 1L;
        for(int i = 0; i < len; i++) {
            if(dp[i] == 0)
                continue;
            TrieNode node = trie.root;
            // For each index from i to length of the string
            for(int j = i; j < len; j++) {
                // Traverse the trie
                int idx = s.charAt(j) - 'a';
                if(node.letters[idx] == null)
                    break;
                node = node.letters[idx];       
                if(node.isEnd)      // Info: If the end node is reached, then it means that the segment (i, j) is a substring
                    dp[j+1] = (dp[j+1] + dp[i]) % MOD;
            }
        }
        output.append(dp[len]);
    }

    public static final class TrieNode {
        public TrieNode[] letters;
        public boolean isEnd;

        public TrieNode() {
            this.letters = new TrieNode[26];        // 26 lower case letters
            this.isEnd = false;
        }
    }

    public static final class Trie {
        private final TrieNode root = new TrieNode();       // Note: root never gets tampered

        public void insert(String s) {      // Note: Insert function for Trie
            TrieNode node = root;   // temp variable
            int n = s.length();
            for(int i = 0; i < n; i++) {
                int idx = s.charAt(i) - 'a';
                if(node.letters[idx] == null)   // If null assign node
                    node.letters[idx] = new TrieNode();
                node = node.letters[idx];
            }
            node.isEnd = true;      // mark the end as true
        }
    }
}