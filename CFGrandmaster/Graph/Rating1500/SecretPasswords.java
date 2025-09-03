import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SecretPasswords {
    // Micro-optimisation: FastReader defined for fast input reading via byte buffer
    public static class FastReader {
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int x = 0, c;
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

        public String next() throws IOException {
            int c;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
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

    // Union-Find (DSU) implementation
    private static final int[] parent = new int[26];

    private static int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a != b)
            parent[b] = a;
    }

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "https://codeforces.com/problemset/problem/1263/D", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();

        final int n = fr.nextInt();
        String words[] = new String[n];
        for (int i = 0; i < 26; i++) parent[i] = i; // DSU init
        boolean[] used = new boolean[26];           // marks letters that appear

        for (int i = 0; i < n; i++) {
            words[i] = fr.next();
            int first = words[i].charAt(0) - 'a';
            used[first] = true;
            for (int j = 1; j < words[i].length(); j++) {
                int c = words[i].charAt(j) - 'a';
                used[c] = true;
                union(first, c);
            }
        }

        // Count distinct DSU parents for letters that appeared
        boolean[] seen = new boolean[26];
        int components = 0;
        for (int i = 0; i < 26; i++) {
            if (used[i]) {
                int p = find(i);
                if (!seen[p]) {
                    seen[p] = true;
                    components++;
                }
            }
        }

        fw.attachOutput(new StringBuilder().append(components));
        fw.printOutput();
    }
}
