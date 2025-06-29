package Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class SubstringWindow {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreElements()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) throws IOException {
        Thread t = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "substring-window", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final String s = fr.next(), t = fr.next();
        final PrintWriter pr = new PrintWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        output.append(solve(s, s.length(), t, t.length()));
        pr.write(output.toString());
        pr.flush();
    }

    static String out = "";
    static int fMap[];

    public static String solve(String s, int n, String t, int m) {
        if (m > n)
            return "";
        fMap = new int[26];
        for (int i = 0; i < m; i++)
            fMap[t.charAt(i) - 'a']++;
        int l = m, r = n;
        while (l <= r) {
            int mid = (l + r) >>> 1;
            if (found(mid, s, n))
                r = mid - 1;
            else
                l = mid + 1;
        }
        return out;
    }

    public static boolean found(final int window, final String s, final int n) {
        int map[] = new int[26];
        for (int i = 0; i < window; i++) {
            int idx = s.charAt(i) - 'a';
            map[idx]++;
        }
        if (isSubstring(map)) {
            out = s.substring(0, window);
            return true;
        }
        for (int i = window; i < n; i++) {
            int idxL = s.charAt(i - window) - 'a', idxR = s.charAt(i) - 'a';
            map[idxL]--;
            map[idxR]++;
            if (isSubstring(map)) {
                out = s.substring(i - window + 1, i + 1);
                return true;
            }
        }
        return false;
    }

    public static boolean isSubstring(final int map[]) {
        for (int i = 0; i < 26; i++)
            if (map[i] < fMap[i])
                return false;
        return true;
    }
}