import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Ascent {
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
        final int n = fast.nextInt();
        int minSeq[] = new int[n], maxSeq[] = new int[n];
        for(int i = 0; i < n; i++) {
            int l = fast.nextInt();
            for(int j = 0; j < l; j++) {
                int num = fast.nextInt();
                minSeq[i] = Math.min(minSeq[i], num);
                maxSeq[i] = Math.max(maxSeq[i], num);
            }
        }
        System.out.print(solve(n, minSeq, maxSeq));
    }

    public static int solve(final int n, final int minSeq[], int maxSeq[]) {
        Arrays.sort(maxSeq);
        int count = 0;
        for(int min : minSeq)
            count += binarySearch(n-1, maxSeq, min);
        return count;
    }

    public static int binarySearch(final int n, final int nums[], final int target) {
        int left = 0, right = n, ans = n;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            
        }
        return 0;
    }
}
