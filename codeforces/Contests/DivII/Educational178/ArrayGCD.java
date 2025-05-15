import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ArrayGCD {
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
            final int n = fast.nextInt();
            long nums[] = new long[n];
            for(int i = 0; i < n; i++)
                nums[i] = fast.nextLong();
            output.append(solve(n, nums)).append("\n");
        }
        System.out.print(output);
    }

    public static boolean sieve[];

    public static void sieveOfErasthosthenes(int N) {
        sieve = new boolean[N*50];
        Arrays.fill(sieve, true);
        sieve[0] = sieve[1] = false;
        for(int num = 2; num * num <= N*50; num++) {
            if(sieve[num])
                for(int multiple = num*2; multiple < N*50; multiple += num)
                    sieve[multiple] = false;
        }
    }

    public static StringBuilder solve(final int n, long nums[]) {
        sieveOfErasthosthenes(n);
        Arrays.sort(nums);
        int j = 2, count = 0;
        long cost = 0;
        for(int i = n-1; i >= 0; i--, j++) {
            if(!sieve[j])
                while(!sieve[j])
                    j++;
            long update = nums[i] - j;
            if(cost + update < 0)
                count++;
            else
                cost += update;
        }
        return new StringBuilder().append(count);
    }
}
