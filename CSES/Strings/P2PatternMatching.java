import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class P2PatternMatching {
    public static class FastReader {
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
        Thread t1 = new Thread(null, () -> {
            try {
                callMain(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "pattern-matching", 1 << 26);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException iE) {
            iE.printStackTrace();
        }
    }

    public static void callMain(String[] args) throws IOException {
        FastReader fast = new FastReader();
        solve(fast.next(), fast.next());
    }

    public static final int BASE = 26;
    public static final int MOD = 1_000_000_007;

    public static void brute(final String text, final String pattern) {
        int t = text.length(), p = pattern.length();
        if (p > t) {
            System.out.println(0);
            return;
        }
        long highestPower = 1L;
        for(int i = 0; i < p-1; i++)
            highestPower = (highestPower * BASE) % MOD;
        int count = 0;
        long hashP = 0, hashT = 0;
        // Compute initial hashes for pattern and first window of text
        for (int i = 0; i < p; i++) {
            hashP = (hashP * BASE + pattern.charAt(i) - 'a') % MOD;
            hashT = (hashT * BASE + text.charAt(i) - 'a') % MOD;
        }
        // Check first window
        if (hashP == hashT && check(text, pattern, 0) == 1)
            count++;
        // Slide window and update hash
        for (int i = 1; i <= t - p; i++) {
            // Remove leading character
            hashT = (hashT - (text.charAt(i - 1) - 'a') * highestPower % MOD + MOD) % MOD;
            // Shift left and add trailing character
            hashT = (hashT * BASE + (text.charAt(i + p - 1) - 'a')) % MOD;
            if (hashP == hashT && check(text, pattern, i) == 1)
                count++;
        }
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(count);
        writer.write(output.toString());
        writer.flush();
    }

    public static int check(String text, String pattern, int tIdx) {
        for (int i = 0; i < pattern.length(); i++) {
            if (text.charAt(tIdx + i) != pattern.charAt(i))
                return 0;
        }
        return 1;
    }

    // Hack: Use two hashes to reduce collision, Rabin Karp is better performed with two hashes (no need to use check function then)
    public static final long BASE1 = 51L, BASE2 = 57L;
    public static final long MOD1 = (long)1e9 + 7, MOD2 = (long)1e9 + 9;

    public static void solve(final String text, final String pattern) {
        int t = text.length(), p = pattern.length(), count = 0;
        if(p > t) {     // If pattern is greater than length of text
            System.out.println(0);
            return;
        }
        // Info: arrays to store the powers of base (powers of d)
        long power1[] = new long[t], power2[] = new long[t];
        power1[0] = 1L;
        power2[0] = 1L;
        for(int i = 1; i < t; i++) {        // Prefix sums for computation
            power1[i] = (power1[i-1] * BASE1) % MOD1;
            power2[i] = (power2[i-1] * BASE2) % MOD2;
        }
        // Note: Evaluating hashes of the pattern only once
        long patternHash1 = computeHash(pattern, power1, p, MOD1), patternHash2 = computeHash(pattern, power2, p, MOD2);
        long hashPrefix1[] = new long[t], hashPrefix2[] = new long[t];
        // Hack: Using prefix technique here to compute hashes of all indices, as powers of d^i from 0 to n-1, a unique approach and then we can use prefix sums technique of prefix[r] - prefix[l-1] and since the difference has powers of l common, we divide then by d^l, since the actual hash is computed from d^0 to d^m and not from d^j to d^(j+m)
        hashPrefix1[0] = (long)(text.charAt(0) - 'a' + 1);
        hashPrefix2[0] = (long)(text.charAt(0) - 'a' + 1);
        for(int i = 1; i < t; i++) {        // Prefix sums for computation
            long data = text.charAt(i) - 'a' + 1;
            hashPrefix1[i] = (hashPrefix1[i-1] + (data * power1[i]) % MOD1) % MOD1;
            hashPrefix2[i] = (hashPrefix2[i-1] + (data * power2[i]) % MOD2) % MOD2;
        }
        // Hack: Fernet's theorem is used to evaluate inv(p^l) as a^p = a^(p-2) whew a or p are prime
        long invPower1 = fernetTheorem(BASE1, MOD1, MOD1), invPower2 = fernetTheorem(BASE2, MOD2, MOD2);
        long divisionPowers1[] = new long[t], divisionPowers2[] = new long[t];
        divisionPowers1[0] = 1L;
        divisionPowers2[0] = 1L;
        for(int i = 1; i < t; i++) {    // Prefix sums for computation
            divisionPowers1[i] = (divisionPowers1[i-1] * invPower1) % MOD1;
            divisionPowers2[i] = (divisionPowers2[i-1] * invPower2) % MOD2;
        }
        for(int l = 0; l+p-1 < t; l++) {
            int r = l+p-1;      // Setting l and r range
            long prefixDiff1 = hashPrefix1[r], prefixDiff2 = hashPrefix2[r];
            if(l > 0) {     // Info: When l is greater than 0, then we can evaluate l-1, for 0 case we can directly check the hashes since hashPrefix[r] is already storing the required data
                prefixDiff1 = (prefixDiff1 - hashPrefix1[l-1] + MOD1) % MOD1;       // Converting data under range [0, MOD1-1]
                prefixDiff2 = (prefixDiff2 - hashPrefix2[l-1] + MOD2) % MOD2;       // Converting data under range [0, MOD2-1]
            }
            // Info: Evaluating windows hashes by multiplying with the inv(p^l)
            long windowhash1 = (prefixDiff1 * divisionPowers1[l] % MOD1), windowHash2 = (prefixDiff2 * divisionPowers2[l]) % MOD2;
            if(windowhash1 == patternHash1 && windowHash2 == patternHash2)
                count++;
        }
        final StringBuilder output = new StringBuilder();
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        output.append(count);
        writer.write(output.toString());
        writer.flush();
    }

    // Note: One time use function for computing hashes
    public static long computeHash(String pattern, long power[], int len, final long MOD) {
        long hash = 0l;
        for(int i = 0; i < len; i++) {
            long data = pattern.charAt(i) - 'a' + 1;
            hash = (hash + (data * power[i])) % MOD;
        }
        return hash;
    }

    // Note: Modular exponentiation
    public static long exp(long a, long b, final long MOD) {
        long result = 1L;
        while(b > 0) {
            if((b & 1) == 1)
                result = (result * a) % MOD;
            a = (a * a) % MOD;
            b >>= 1;
        }
        return result;
    }

    public static long fernetTheorem(long a, long p, final long MOD) {
        return exp(a, p-2, MOD);
    }
}