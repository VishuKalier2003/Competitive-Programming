import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LengthDigits {
    public static class FastReader {        // fast reader class
        public StringTokenizer tokenizer;
        public BufferedReader buffer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}       // Input reader

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}     // reading int
        public long nextLong() {return Long.parseLong(next());}     // reading long
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        System.out.print(solve(fast.nextInt(), fast.nextInt()));
    }

    public static String solve(final int m, final int s) {
        return getMin(m, s)+" "+getMax(m, s);
    }

    public static String getMin(int m, int s) {
        if (s == 0) return (m == 1) ? "0" : "-1";
        StringBuilder res = new StringBuilder();
        int sum = s;
        for (int i = 0; i < m; ++i) {
            for (int d = (i == 0 ? 1 : 0); d <= 9; ++d) {
                if (sum - d <= 9 * (m - i - 1)) {
                    res.append(d);
                    sum -= d;
                    break;
                }
            }
        }
        return (res.length() == m && sum == 0) ? res.toString() : "-1";
    }

    public static String getMax(int m, int s) {
        if (s == 0) return (m == 1) ? "0" : "-1";
        StringBuilder res = new StringBuilder();
        int sum = s;
        for (int i = 0; i < m; ++i) {
            for (int d = 9; d >= (i == 0 ? 1 : 0); --d) {
                if (sum - d >= 0 && sum - d <= 9 * (m - i - 1)) {
                    res.append(d);
                    sum -= d;
                    break;
                }
            }
        }
        return (res.length() == m && sum == 0) ? res.toString() : "-1";
    }

}
