// Ques- https://codeforces.com/problemset/problem/1363/A

// Note- Think reverse as the conditions in which the requirements will not be fulfilled
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Problem1363A {
    public static class FastReader {
        public BufferedReader buffer;
        public StringTokenizer tokenizer;

        public FastReader() {this.buffer = new BufferedReader(new InputStreamReader(System.in));}

        public String next() {
            while(tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {tokenizer = new StringTokenizer(buffer.readLine());}
                catch(IOException e) {e.printStackTrace();}
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {return Integer.parseInt(next());}
        public long nextLong() {return Long.parseLong(next());}
    }

    public static void main(String[] args) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder output = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt(), k = fast.nextInt();
            int nums[] = new int[n];
            int odd = 0, even = 0, sum = 0;
            for(int i = 0; i < n; i++) {
                nums[i] = fast.nextInt();
                sum += nums[i];
                if((nums[i] & 1) == 1)
                    odd++;
                else    even++;
            }
            output.append(solve(n, k, odd, even, sum)).append("\n");
        }
        System.out.print(output);
    }

    public static StringBuilder solve(final int n, final int k, final int odd, final int even, final int sum) {
        if(n == k)
            return new StringBuilder().append((sum & 1) == 1 ? "Yes" : "No");
        if(odd == n && (k & 1) == 0)
            return new StringBuilder().append("No");
        if(even == n)
            return new StringBuilder().append("No");
        return new StringBuilder().append("Yes");
    }
}
