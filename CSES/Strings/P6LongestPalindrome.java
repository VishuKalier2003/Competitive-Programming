import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class P6LongestPalindrome {
    public static class FastReader {        // Note: Fastest reading data
        private static final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;

        public int read() throws IOException {
            if (ptr >= len) {
                ptr = 0;
                len = System.in.read(buffer);
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++] & 0xff;
        }

        public int nextInt() throws IOException {
            int c;
            int x = 0;
            while ((c = read()) <= ' ')
                if (c < 0)
                    return -1;
            boolean neg = c == '-';
            if (neg)
                c = read();
            do {
                x = 10 * x + (c - '0');
            } while ((c = read()) <= '9' && c >= '0');
            return neg ? -x : x;
        }

        public String next() throws IOException {
            int c;
            while ((c = read()) != -1 && Character.isWhitespace(c)) {}
            if (c == -1)
                return null;
            StringBuilder sb = new StringBuilder();
            do {
                sb.append((char)c);
                c = read();
            } while (c != -1 && !Character.isWhitespace(c));
            return sb.toString();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) throws IOException {
        Thread t3 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "longest-palindrome", 1 << 26);
        t3.start();
        try {
            t3.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.next());
    }

    public static int left, right;

    public static void brute(final String s) {
        left = 0;
        right = 0;
        final int n = s.length();
        int max = 0;
        String maxP = "";
        for(int i = 0; i < n; i++) {
            pivotSpread(i, i, n, s);
            if(right-left > max) {
                max = right - left;
                maxP = s.substring(left, right);
            }
            pivotSpread(i, i+1, n, s);
            if(right-left > max) {
                max = right - left;
                maxP = s.substring(left, right);
            }
        }
        final StringBuilder output = new StringBuilder();
        output.append(maxP);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    public static void pivotSpread(int i, int j, final int n, final String s) {
        while(i >= 0 && j < n && s.charAt(i) == s.charAt(j)) {
            i--;
            j++;
        }
        left = i+1;
        right = j;
    }

    public static int p[]; 

    public static void solve(final String s) {
        manacher(s, s.length());
        int bestCenter = 0, max = 0;
        for(int i = 0; i < p.length; i++)
            if(p[i]-1 > max) {
                max = p[i]-1;
                bestCenter = i;
            }
        int start = (bestCenter - max)/2;
        final StringBuilder output = new StringBuilder();
        output.append(s.substring(start, start+max));
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }

    // Hack: Used to find Longest palindrome in O(n), and we can also check whether a give range [l,r) is a palindrome or not in O(1) time, hence solving queries as well
    public static void manacher(final String s, final int n) {
        char[] seq = new char[2*n+1];
        int len = seq.length;      // Note: From here len will be used instead of n, we are invested into length of seq not s
        for(int i = 0; i < len; i += 2)
            seq[i] = '#';
        for(int i = 1, j = 0; j < n; i += 2, j++)
            seq[i] = s.charAt(j);
        p = new int[len];
        Arrays.fill(p, 1);
        // Info: Since l equal to 0 is # which is already a palindrome
        int l = 1, r = 1;
        for(int i = 1; i < len; i++) {
            // Info: If i falls inside currenr right boundary, then it is a mirror position
            if(i < r) {
                int mirror = l+r-i; // Hack: Since p[mirror] is already computed, we use this propery of palindromes to shift our r
                p[i] = Math.max(0, Math.min(r-i, p[mirror]));
            }
            while(i+p[i] < len && i-p[i] >= 0 && seq[i-p[i]] == seq[i+p[i]]) {
                p[i]++;     // Increasing radius of p[i]
            }
            if(i + p[i] > r) {      // Note: When our palindrome extends beyond
                l = i - p[i];
                r = i + p[i];
            }
        } 
    }

    // Note: Extracting the longest palindrom from the center in the manacher sequence
    public static int palindromeLength(int center, boolean odd) {
        int pos = 2 *center+1+(odd ? 0 : 1); 
        return p[pos]-1;
    }

    // Note: Checking if there is a palindrome under the range (l,r)
    public static boolean checkPalindrome(int l, int r) {
        return (r-l+1) <= palindromeLength((l+r) >>> 1, l % 2 == r % 2);
    }
}
