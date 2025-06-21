// https://codeforces.com/problemset/problem/1345/B

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Card {
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
        }, "Card-Construction", 1 << 26);
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
        while(t-- > 0)
            output.append(solve(fr.nextInt())).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    static List<Integer> dp;

    public static int solve(int n) {
        if(n < 2)
            return 0;
        dp = new ArrayList<>();
        for (int i = 1; cards(i) <= n; i++)
            dp.add(cards(i));
        int rem = n, count = 0, l = 0, r = dp.size()-1;
        while(true) {
            int idx = binarySearch(l, r, rem);
            if(idx != -1) {
                rem -= dp.get(idx);
                count++;
            } else
                return count;
        }
    }

    public static int binarySearch(int l, int r, int target) {
        int ans = -1;
        if(dp.get(0) > target)
            return ans;
        while(l <= r) {
            int mid = (l+r) >>> 1;
            if(dp.get(mid) == target)
                return mid;
            else if(dp.get(mid) < target) {
                ans = mid;
                l = mid+1;
            } else
                r = mid-1;
        }
        return ans;
    }

    public static int cards(int h) {
        return (h * (h + 1)) + ((h * (h - 1)) / 2);
    }
}
