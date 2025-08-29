import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class BerserkMonster {
    // Fast input reader
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
    }

    // Fast output writer
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

    public static void main(String[] args) {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "berserk", 1 << 26);
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
        int t = fr.nextInt();
        while (t-- > 0) {
            final int n = fr.nextInt();
            int[] a = new int[n], d = new int[n];
            for (int i = 0; i < n; i++)
                a[i] = fr.nextInt();
            for (int j = 0; j < n; j++)
                d[j] = fr.nextInt();
            fw.attachOutput(solve(n, a, d));
        }
        fw.printOutput();
    }

    public static StringBuilder solve(final int n, final int[] a, final int[] d) {
        // Doubly-linked list using arrays
        int[] L = new int[n];
        int[] R = new int[n];
        for (int i = 0; i < n; i++) {
            L[i] = i - 1;
            R[i] = i + 1;
        }
        R[n - 1] = -1;

        boolean[] inQueue = new boolean[n];
        ArrayDeque<Integer> q = new ArrayDeque<>();

        // Initial check for monsters that will die in round 0
        for (int i = 0; i < n; i++) {
            long damage = (L[i] != -1 ? a[L[i]] : 0L) + (R[i] != -1 ? a[R[i]] : 0);
            if (damage > d[i]) {
                q.offer(i);
                inQueue[i] = true;
            }
        }

        StringBuilder ans = new StringBuilder();
        int rounds = 0;

        while (rounds < n) {
            if (q.isEmpty()) {
                ans.append("0 ");
                rounds++;
                continue;
            }

            List<Integer> dying = new ArrayList<>();
            while (!q.isEmpty()) {
                dying.add(q.poll());
            }

            ans.append(dying.size()).append(" ");
            rounds++;

            // Remove dying monsters
            for (int x : dying) {
                inQueue[x] = false;
                if (L[x] != -1)
                    R[L[x]] = R[x];
                if (R[x] != -1)
                    L[R[x]] = L[x];
            }

            // Check neighbors for next round
            for (int x : dying) {
                if (L[x] != -1 && !inQueue[L[x]]) {
                    long damage = (L[L[x]] != -1 ? a[L[L[x]]] : 0L) + (R[L[x]] != -1 ? a[R[L[x]]] : 0);
                    if (damage > d[L[x]]) {
                        q.offer(L[x]);
                        inQueue[L[x]] = true;
                    }
                }
                if (R[x] != -1 && !inQueue[R[x]]) {
                    long damage = (L[R[x]] != -1 ? a[L[R[x]]] : 0L) + (R[R[x]] != -1 ? a[R[R[x]]] : 0);
                    if (damage > d[R[x]]) {
                        q.offer(R[x]);
                        inQueue[R[x]] = true;
                    }
                }
            }
        }
        ans.append("\n");
        return ans;
    }
}
