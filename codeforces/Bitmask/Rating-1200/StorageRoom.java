import java.io.*;
import java.util.*;

public class StorageRoom {

    public static class FastReader {
        BufferedReader buffer;
        StringTokenizer tokenizer;

        public FastReader() {
            this.buffer = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(buffer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while (t-- > 0) {
            int n = fast.nextInt();
            int x = fast.nextInt();
            builder.append(solve(n, x)).append("\n");
        }
        System.out.print(builder);
    }

    public static StringBuilder solve(int n, int x) {
        final StringBuilder result = new StringBuilder();
        if(n == 1) {
            result.append(x);
            return result;
        }
        int v = 0, count = 0;  // Variable to store bitwise OR result
        for (int m = 0; m < Math.min(n, x + 1); m++) {
            if ((v | m) == (v | m & x)) { // Ensuring that OR does not introduce extra bits beyond x
                result.append(m+" ");
                v |= m;  // Update OR result
                count++;        // Update size
            } else
                break;
        }
        if (v == x && count == n)
            return result;
        if(count++ < n)
            result.append(x+" ");
        while(count++ < n)
            result.append("0 ");
        return result;
    }
}
