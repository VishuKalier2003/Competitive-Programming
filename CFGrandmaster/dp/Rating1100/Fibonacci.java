// https://codeforces.com/problemset/problem/2111/B

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Fibonacci {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {
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

    public static void main(String[] args) {
        Thread constructive1300 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }, "Fibonacci-cubes", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader(); // reading input
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fr.nextInt();
        while (t-- > 0) {
            final int x = fr.nextInt(), n = fr.nextInt();
            List<int[]> boxes = new ArrayList<>();
            for (int i = 0; i < n; i++)
                boxes.add(new int[] { fr.nextInt(), fr.nextInt(), fr.nextInt() });
            output.append(solve(x, n, boxes)).append("\n");
        }
        wr.write(output.toString());
        wr.flush();
    }

    public static int dp[];

    public static StringBuilder solve(final int x, final int n, final List<int[]> boxes) {
        dp = new int[x+1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= x; i++)
            dp[i] = dp[i - 1] + dp[i - 2];
        final StringBuilder s = new StringBuilder();
        // System.out.println(Arrays.toString(dp));
        for (int box[] : boxes)
            s.append((canFit(box, dp[x], dp[x-1])) ? "1" : "0");
        return s;
    }

    public static boolean canFit(int box[], int cube1, int cube2) {
        // System.out.println("cube1 : "+cube1+" cube2 : "+cube2);
        if (box[0] >= cube1 && box[1] >= cube1 && box[2] >= cube1) {
            int x = box[0] - cube1, y = box[1] - cube1, z = box[2] - cube1;
            if (x >= cube2 || y >= cube2 || z >= cube2)
                return true;
        }
        return false;
    }
}
