import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CoprimeSplit {
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

    public static void main(String args[]) {
        FastReader fast = new FastReader();
        int t = fast.nextInt();
        final StringBuilder builder = new StringBuilder();      // String builder for input
        while(t-- > 0)      // for each test case
            builder.append(coprimeSplit(fast.nextInt(), fast.nextInt()));
        System.out.print(builder);
    }

    public static String coprimeSplit(int l, int r) {
        if(r <= 3)      // when r <= 3, then not at all possible
            return "-1\n";
        if(l == r) {
            for(int i = 2; i*i <= l; i++)       // IMP- checking if l is prime
                if(l % i == 0)      // If not prime, then we can simply create two numbers as l and l-i
                    return (l-i)+" "+i+"\n";
            return "-1\n";
        }
        int largestEven = r-(r%2);      // Finding largest even lesser than or equal to r
        return (largestEven/2)+" "+(largestEven/2)+"\n";        // Simply divide it by 2 to get the two even numbers
    }
}
