// Topological sorting - https://codeforces.com/problemset/problem/316/B1

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EKG {
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
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public long nexLong() throws IOException { // reading long
            long x = 0l, c;
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
            while (c != '\n' && c >= 0)
                if (c != '\r')
                    sb.append((char) c);
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
        }, "EKG(316B1)",
                1 << 26);
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
        final int n = fr.nextInt(), beaver = fr.nextInt();
        int g[] = new int[n + 1], next[] = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            g[i] = fr.nextInt();
            if (g[i] != 0)
                next[g[i]] = i;
        }
        fw.attachOutput(solve(n, beaver, next));
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int beaver, final int next[]) {
        List<Integer> otherChains = new ArrayList<>();
        int offIndex = -1, offLength = 0;
        boolean vis[] = new boolean[n + 1];

        for (int i = 1; i <= n; i++) {
            if (!vis[i]) {
                // find head of this chain
                int head = i;
                while (true) {
                    boolean foundPrev = false;
                    for (int j = 1; j <= n; j++) {
                        if (next[j] == head) {
                            head = j;
                            foundPrev = true;
                            break;
                        }
                    }
                    if (!foundPrev)
                        break;
                }

                // now traverse forward from head
                int curr = head, count = 0;
                boolean flag = false;
                while (curr != 0 && !vis[curr]) {
                    vis[curr] = true;
                    count++;
                    if (curr == beaver) {
                        offIndex = count;
                        flag = true;
                    }
                    curr = next[curr];
                }

                if (flag) {
                    offLength = count;
                } else {
                    otherChains.add(count);
                }
            }
        }

        // subset sum dp
        final boolean dp[] = new boolean[n + 1];
        dp[0] = true;
        for (int chain : otherChains) {
            for (int subset = n - chain; subset >= 0; subset--) {
                if (dp[subset])
                    dp[subset + chain] = true;
            }
        }

        final StringBuilder output = new StringBuilder();
        for (int i = 0; i <= n - offLength; i++) {
            if (dp[i])
                output.append(i + offIndex).append("\n");
        }
        return output;
    }
}
