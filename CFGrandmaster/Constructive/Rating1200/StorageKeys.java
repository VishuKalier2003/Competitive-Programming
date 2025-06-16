// https://codeforces.com/problemset/problem/2072/C

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class StorageKeys {
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
        }, "Creating-Storage-Keys", 1 << 26);
        constructive1300.start();
        try {
            constructive1300.join();
        } catch (InterruptedException iE) {
            iE.getLocalizedMessage();
        }
    }

    public static void callMain(String args[]) throws IOException {
        FastReader fr = new FastReader();
        final StringBuilder output = new StringBuilder();
        final PrintWriter wr = new PrintWriter(new OutputStreamWriter(System.out));
        int t = fr.nextInt();
        while(t-- > 0)
            output.append(solve(fr.nextInt(), fr.nextInt())).append("\n");
        wr.write(output.toString());
        wr.flush();
    }

    public static StringBuilder solve(int n, int x) {
        final StringBuilder out = new StringBuilder();
        int i;
        for(i = 0; i <= x && n > 1; i++, n--) {
            // Checking if doing or does not set incorrect bit
            if(((x ^ -1) & i) > 0)  // xor with -1 (all 1s) and then doing & with i tell if there is any incorrect bit set to 1
                break;
            out.append(i).append(" ");
        }
        // While the MSB are same, we can maintain the MEX property
        while(Integer.numberOfLeadingZeros(i) == Integer.numberOfLeadingZeros(x) && n-- > 0) {
            out.append(i).append(" ");
            i++;
        }
        while(n-- > 0)      // Fill all with same value of x as the last resort
            out.append(x).append(" ");
        return out;
    }
}
