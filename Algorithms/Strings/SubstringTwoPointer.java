package Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class SubstringTwoPointer {
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
        }, "substring-window-two-pointer", 1 << 26);
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

    public static String solve(final String s, final int n, final String t, final int m) {
        String sub = "";
        if (m > n || m == 0)
            return sub;
        int l = 0, r = 0;
        int minLen = n + 1;
        int map[] = new int[26], mapWindow[] = new int[26];
        for (int i = 0; i < m; i++) {
            map[t.charAt(i) - 'a']++;
        }
        while (r < n) {     // Run till r reaches n
            // Exapnd from right
            mapWindow[s.charAt(r)-'a']++;
            // While the window is valid and l never exceeds r, check for minimum length
            while (l <= r && isSubstring(map, mapWindow)) {
                if (minLen > r - l + 1) {       // check for min length
                    minLen = r - l + 1;
                    sub = s.substring(l, r + 1);
                }
                // shrink from left, if this causes condition to false, we move out of the loop
                mapWindow[s.charAt(l)-'a']--;
                l++;
            }
            r++;    // update r
        }
        return sub;
    }

    // Main condition check logic
    public static boolean isSubstring(final int map[], final int mapWindow[]) {
        for(int i = 0; i < 26; i++)
            if(mapWindow[i] < map[i])
                return false;
        return true;
    }

}
