import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Eugeny {
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
        final StringBuilder builder = new StringBuilder();
        final int n = fast.nextInt(), notes = fast.nextInt();
        long nums[] = new long[n+1];
        for(int i = 0; i < n; i++) {
            long noteLength = fast.nextLong(), notePlays = fast.nextLong();
            nums[i+1] = nums[i] + (noteLength*notePlays);
        }
        for(int i = 0; i < notes; i++)
            builder.append(keyNoteSearch(n+1, nums, fast.nextLong())).append("\n");
        System.out.print(builder);
    }

    public static int keyNoteSearch(final int n, final long notes[], final long key) {
        int left = 0, right = n, ans = 0;
        while(left <= right) {
            int mid = left + ((right - left) >> 1);
            if(notes[mid] >= key) {
                ans = mid;
                right = mid-1;
            }   else    left = mid+1;
        }
        return ans;
    }
}
