// https://codeforces.com/problemset/problem/2094/D

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class TungTung {
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
        }, "tung-tung-sahar", 1 << 26);
        t.start();
        try {
            t.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final PrintWriter pr = new PrintWriter(new OutputStreamWriter(System.out));
        final StringBuilder output = new StringBuilder();
        int t = fr.nextInt();
        while(t-- > 0) 
            output.append(solve(fr.next(), fr.next())).append("\n");
        pr.write(output.toString());
        pr.flush();
    }

    public static StringBuilder solve(final String s, final String p) {
        int n = s.length(), m = p.length();
        int i = 0, j = 0;
        final StringBuilder sb = new StringBuilder();
        while(i < n) {
            char ch = s.charAt(i);
            if(j < m-1 && p.charAt(j) == ch && p.charAt(j+1) == ch)
                j += 2;
            else if(j < m && p.charAt(j) == ch)
                j++;
            else
                return sb.append("No");
            i++;
        }
        if(j >= m)
            return sb.append("Yes");
        return sb.append("No");
    }
}
