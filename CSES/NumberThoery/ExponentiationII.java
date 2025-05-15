// https://cses.fi/problemset/task/1712/

// Imp- Good Question on Fernet's Theorem
import java.io.*;
import java.util.StringTokenizer;

public class ExponentiationII {
    public static final int MOD = 1_000_000_007;

    public static void main(String[] args) {
        FastReader in = new FastReader();
        int n = in.nextInt();
        StringBuilder out = new StringBuilder();
        while (n-- > 0) {
            long a = in.nextLong(), b = in.nextLong(), c = in.nextLong();
            out.append(solve(a,b,c)).append('\n');
        }
        System.out.print(out);
    }

    private static long solve(long a, long b, long c) {
        long A = mod(a, MOD);

        // Special-case base ≡0 mod M:
        if (A == 0) {
            // exponentActual = b^c:
            //   =0 exactly when b==0&&c>0; =1 when b==c==0; >0 otherwise
            if (b == 0 && c > 0) {
                // 0^0 → 1
                return 1;
            } else {
                // 0^positive → 0
                return 0;
            }
        }

        // Compute exponent E = b^c mod (M–1), with 0^0 → 1
        long E = powMod(b, c, MOD - 1);

        // Now compute A^E mod M
        return powMod(A, E, MOD);
    }

    /** returns (x^y) mod m, with the convention 0^0 → 1 */
    private static long powMod(long x, long y, long m) {
        x = mod(x, m);
        long result = 1;
        if (x == 0 && y == 0) {
            return 1;       // enforce 0^0 = 1
        }
        while (y > 0) {
            if ((y & 1) == 1) {
                result = (result * x) % m;
            }
            x = (x * x) % m;
            y >>= 1;
        }
        return result;
    }

    private static long mod(long x, long m) {
        long r = x % m;
        return r < 0 ? r + m : r;
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        FastReader() { br = new BufferedReader(new InputStreamReader(System.in)); }
        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try { st = new StringTokenizer(br.readLine()); }
                catch (IOException e) { throw new RuntimeException(e); }
            }
            return st.nextToken();
        }
        int nextInt()    { return Integer.parseInt(next()); }
        long nextLong()  { return Long.parseLong(next()); }
    }
}
