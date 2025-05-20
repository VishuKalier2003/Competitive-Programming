import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class WordCombinations {
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

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "word-combinations", 1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        String s = fast.next();
        final int n = fast.nextInt();
        String[] words = new String[n];
        for (int i = 0; i < n; i++)
            words[i] = fast.next();
        solve(s.length(), s, words);
    }

    public static final int MOD = 1_000_000_007;
    public static int[] dp;

    public static void solve(final int n, final String s, final String[] words) {
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        dp = new int[n + 1];
        Trie trie = new Trie();
        dp[0] = 1; // Base case: empty string has 1 way
        for (String word : words)
            trie.insert(new StringBuilder().append(word).reverse().toString());
        for (int i = 1; i <= n; i++) {
            TrieNode node = trie.root;
            for (int j = i; j >= 1 && node != null; j--) { // Check suffixes ending at i
                int index = s.charAt(j - 1) - 'a';
                node = node.children[index];
                if (node != null && node.end == 1) {
                    dp[i] = (dp[i] + dp[j - 1]) % MOD;
                }
            }
        }
        output.append(dp[n]);
        writer.write(output.toString());
        writer.flush();
    }

    public static class TrieNode {
        private final TrieNode[] children;
        private int end;

        public TrieNode() {
            this.children = new TrieNode[26];
            this.end = 0;
        }

        public void setEnd() {
            this.end = 1;
        }
    }

    public static final class Trie {
        private final TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (node.children[index] == null)
                    node.children[index] = new TrieNode();
                node = node.children[index];
            }
            node.setEnd();
        }
    }
}