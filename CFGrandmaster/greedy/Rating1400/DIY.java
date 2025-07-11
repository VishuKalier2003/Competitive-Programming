// https://codeforces.com/problemset/problem/2038/C

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class DIY {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() throws FileNotFoundException {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
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

    public static class FastWriter {
        public StringBuilder builder;
        public PrintWriter pr;

        public FastWriter() throws FileNotFoundException {
            this.builder = new StringBuilder();
            this.pr = new PrintWriter(new OutputStreamWriter(System.out));
        }

        public void add(String s) {
            this.builder.append(s);
        }

        public void flushMemory() {
            this.pr.write(builder.toString());
            this.pr.flush();
        }
    }

    public static void main(String[] args) {
        Thread greedy1400 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "DIY", 1 << 26);
        greedy1400.start();
        try {
            greedy1400.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        FastWriter fw = new FastWriter();
        int t = fr.nextInt();
        while(t-- > 0) {
            final SortedMap<Integer, Integer> mp = new TreeMap<>();
            final int n = fr.nextInt();
            for(int i = 0; i < n; i++) {
                int u = fr.nextInt();
                mp.put(u, mp.getOrDefault(u, 0) + 1);
            }
            fw.add(solve(mp));
        }
        fw.flushMemory();
    }

    public static String solve(final SortedMap<Integer, Integer> mp) {
        int c = 0;
        List<Integer> lst = new ArrayList<>();      // list to store the keys (coordinates)
        while(c < 2) {
            if(mp.isEmpty())        // If rectangle cannot be found
                return new StringBuilder().append("NO\n").toString();
            Map.Entry<Integer, Integer> e = mp.pollFirstEntry();    // poll from top
            if(e.getValue() >= 2) {
                lst.add(e.getKey());
                lst.add(e.getKey());
                c++;
                if(e.getValue() >= 2)       // putting back
                    mp.put(e.getKey(), e.getValue()-2);
            }
        }
        c = 0;
        while(c < 2) {
            if(mp.isEmpty())        // If rectangle cannot be found
                return new StringBuilder().append("NO\n").toString();
            Map.Entry<Integer, Integer> e = mp.pollLastEntry();     // poll from bottom
            if(e.getValue() >= 2) {
                lst.add(e.getKey());
                lst.add(e.getKey());
                c++;
                if(e.getValue() >= 2)       // putting back
                    mp.put(e.getKey(), e.getValue()-2);
            }
        }
        // Setting up the x and y coordinates for the four points
        int res[] = new int[8];
        res[0] = lst.get(0);
        res[2] = lst.get(0);
        res[1] = lst.get(2);
        res[5] = lst.get(2);
        res[3] = lst.get(4);
        res[7] = lst.get(4);
        res[6] = lst.get(6);
        res[4] = lst.get(6);
        final StringBuilder sb = new StringBuilder();
        sb.append("YES\n");
        for(int r : res)
            sb.append(r).append(" ");
        return sb.append("\n").toString();
    }
}
