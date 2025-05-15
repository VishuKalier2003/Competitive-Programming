import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class GroupIncrease {
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
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();
        while(t-- > 0) {
            final int n = fast.nextInt();
            int nums[] = new int[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextInt();
            builder.append(solve(n, nums)).append("\n");
        }
        System.out.print(builder);
    }

    public static StringBuilder solve(final int n, final int nums[]) {
        int last1 = Integer.MAX_VALUE, last2 = Integer.MAX_VALUE, penalty = 0;
        for(int num : nums) {
            boolean pass1 = last1 >= num, pass2 = last2 >= num;
            if(pass1 && !pass2)
                last1 = num;
            else if(!pass1 && pass2)
                last2 = num;
            else {
                if(pass1 && pass2 && last1 == last2)
                    last1 = num;
                else if(pass1 && pass2) {
                    if(last1 < last2)
                        last1 = num;
                    else last2 = num;
                }
                else {
                    if(last1 < last2)
                        last1 = num;
                    else last2 = num;
                    penalty++;
                }
            }
        }
        return new StringBuilder().append(penalty);
    }
}
