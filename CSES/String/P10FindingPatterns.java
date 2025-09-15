import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P10FindingPatterns {
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
        }, "Finding-Patterns-(https://cses.fi/problemset/task/2102)", 1 << 26);
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
        String text = fr.next();
        final int n = fr.nextInt();
        String words[] = new String[n];
        for(int i = 0; i < n; i++)
            words[i] = fr.next();
        solve(text, n, words);
        fw.attachOutput(output);
        fw.printOutput();
    }

    private static final StringBuilder output = new StringBuilder();

    public static void solve(final String text, final int n, final String words[]) {
        Trie corasick = new Trie();
        for(int i = 0; i < n; i++)
            corasick.addWord(words[i], i);
        corasick.buildFailureLinks();
        boolean res[] = corasick.findWords(text, words);
        for(boolean r : res)
            output.append(r ? "YES\n" : "NO\n");
    }

    private static class Node {
        public final Node child[];      // the child nodes
        // Fail node as the pointer storing the pointer to the longest suffix that is also a prefix in trie (in case of failure which node to fall back)
        public Node fail;
        public final List<Integer> pattern;     // list of word ids present

        public Node() {
            this.child = new Node[26];
            this.fail = null;
            this.pattern = new ArrayList<>();
        }
    }

    // Note: Aho-Corasick Algorithm
    private static class Trie {
        public final Node root = new Node();        // immutable root node

        public void addWord(String s, int pIndex) {
            Node node = root;
            int n = s.length();
            for(int i = 0; i < n; i++) {
                int idx = s.charAt(i)-'a';
                if(node.child[idx] == null)     // If not present, create the node
                    node.child[idx] = new Node();
                node = node.child[idx]; 
            }
            node.pattern.add(pIndex);       // add the word Index to the end node
        }

        public void buildFailureLinks() {
            Deque<Node> q = new ArrayDeque<>();
            // Each child of root points the fail link to the root itself
            for(int idx = 0; idx < 26; idx++) {
                if(root.child[idx] != null) {
                    root.child[idx].fail = root;
                    q.offer(root.child[idx]);
                } else
                    root.child[idx] = root;
            }
            while(!q.isEmpty()) {
                Node parent = q.poll();     // Polling the current node
                for(int idx = 0; idx < 26; idx++) {
                    Node node = parent.child[idx];
                    if(node != null) {      // If node is not null
                        // Info: Access its fail node (it will always be present for earlier nodes, since doing BFS)
                        Node f = parent.fail;
                        while(f != root && f.child[idx] == null)    // While the root node is not reached
                            f = f.fail;
                        if(f.child[idx] != null)        // Info: If current character node is present as a children for the fail node, mark it
                            node.fail = f.child[idx];
                        else
                            node.fail = root;       // fail to root (for default transition)
                        node.pattern.addAll(node.fail.pattern);         // add all necessary pattern ids
                        q.offer(node);      // offer to queue
                    } else {
                        // Note: Guarantees that the search is not null during traversal
                        parent.child[idx] = parent.fail.child[idx];     // If child for character c not present, mark default transition
                    }
                }
            }
        }

        public boolean[] findWords(String text, String words[]) {
            final boolean found[] = new boolean[words.length];
            int n = text.length();
            Node node = root;
            for(int i = 0; i < n; i++) {
                int idx = text.charAt(i)-'a';
                // Info: Since deterministic Automata, there is not any possibility of null pathways
                node = node.child[idx];
                for(int id : node.pattern)      // for each node mark the pattern ids found
                    found[id] = true;
            }
            return found;
        }
    }
}
