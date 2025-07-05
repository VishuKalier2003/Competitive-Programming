// https://codeforces.com/contest/35/problem/C

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BurnAgain {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
            this.buffer = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "BurnAgain", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static class FastWriter {
        public StringBuilder builder;
        public PrintWriter pr;

        public FastWriter() throws FileNotFoundException {
            this.builder = new StringBuilder();
            this.pr = new PrintWriter(new FileOutputStream("output.txt"));
        }

        public void add(String s) {
            this.builder.append(s);
        }

        public void flushMemory() {
            this.pr.write(builder.toString());
            this.pr.flush();
        }
    }

    public static int[][] grid;
    public static int x, y;

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        x = y = 0;
        List<int[]> trees = new ArrayList<>();
        int n = fr.nextInt(), m = fr.nextInt(), q = fr.nextInt();
        grid = new int[n][m];
        for(int i = 0; i < q; i++) {
            int u = fr.nextInt()-1, v = fr.nextInt()-1;
            trees.add(new int[]{u, v});
            grid[u][v] = 1;
        }
        solve(n, m, trees);
        FastWriter fw = new FastWriter();
        fw.add((x+1)+" "+(y+1));
        fw.flushMemory();
    }

    public static void solve(final int n, final int m, List<int[]> trees) {
        ArrayDeque<int[]> q = new ArrayDeque<>();
        for(int[] tree : trees) {
            q.add(tree);
        }
        while(!q.isEmpty()) {
            int s = q.size();
            for(int i = 0; i < s; i++) {
                int node[] = q.poll();
                int u = node[0], v = node[1];
                if(u > 0 && grid[u-1][v] == 0) {
                    grid[u-1][v] = 1;
                    q.add(new int[]{u-1, v});
                    x = u-1; y = v;
                }
                if(u < n-1 && grid[u+1][v] == 0) {
                    grid[u+1][v] = 1;
                    q.add(new int[]{u+1, v});
                    x = u+1; y = v;
                }
                if(v > 0 && grid[u][v-1] == 0) {
                    grid[u][v-1] = 1;
                    q.add(new int[]{u, v-1});
                    x = u; y = v-1;
                }
                if(v < m-1 && grid[u][v+1] == 0) {
                    grid[u][v+1] = 1;
                    q.add(new int[]{u, v+1});
                    x = u; y = v+1;
                }
            }
        }
    }
}
