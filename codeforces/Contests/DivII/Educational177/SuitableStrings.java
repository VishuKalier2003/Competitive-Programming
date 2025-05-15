import java.io.*;
import java.util.*;

public class SuitableStrings {
    static final int MOD = 998244353;
    static long[] fact, invFact;

    // Precompute factorials and inverse factorials modulo MOD up to maxN.
    static void precomputeFactorials(int maxN) {
        fact = new long[maxN+1];
        invFact = new long[maxN+1];
        fact[0] = 1;
        for (int i = 1; i <= maxN; i++) {
            fact[i] = fact[i-1] * i % MOD;
        }
        invFact[maxN] = modInverse(fact[maxN], MOD);
        for (int i = maxN-1; i >= 0; i--) {
            invFact[i] = invFact[i+1] * (i+1) % MOD;
        }
    }

    // Compute modular inverse using Fermat's little theorem.
    static long modInverse(long a, int mod) {
        return modPow(a, mod-2, mod);
    }

    static long modPow(long a, int p, int mod) {
        long result = 1;
        a %= mod;
        while (p > 0) {
            if ((p & 1) == 1) result = result * a % mod;
            a = a * a % mod;
            p >>= 1;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder output = new StringBuilder();

        int t = Integer.parseInt(br.readLine().trim());
        // Maximum possible sum of c[i] is 5e5.
        precomputeFactorials(500000);

        for (int tc = 0; tc < t; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int[] c = new int[26];
            long total = 0;
            // Read counts for 26 letters.
            for (int i = 0; i < 26; i++) {
                c[i] = Integer.parseInt(st.nextToken());
                total += c[i];
            }

            // Total length L and counts of odd and even positions.
            int L = (int) total;
            int O = (L + 1) / 2;  // odd positions count (ceiling)
            int E = L / 2;        // even positions count

            // DP for counting valid partitions – consider only letters with positive count.
            // dp[j] = number of ways to get total = j using decisions for letters with c[i] > 0.
            long[] dp = new long[O+1];
            dp[0] = 1;

            for (int i = 0; i < 26; i++) {
                if(c[i] == 0) continue; // Skip letters with zero occurrences.
                int cnt = c[i];
                long[] newDP = new long[O+1];
                for (int j = 0; j <= O; j++) {
                    // Not choosing letter i (i.e. assign letter i to even positions)
                    newDP[j] = (newDP[j] + dp[j]) % MOD;
                    // Choosing letter i (assign all c[i] occurrences to odd positions)
                    if(j + cnt <= O) {
                        newDP[j + cnt] = (newDP[j + cnt] + dp[j]) % MOD;
                    }
                }
                dp = newDP;
            }

            long validPartitions = dp[O]; // number of valid ways to assign letters with c[i]>0.
            if (validPartitions == 0) {
                output.append("0\n");
                continue;
            }

            // The number of arrangements once the partition is fixed:
            // Odd positions can be arranged in O! / (∏_{i in odd} c[i]!) and even positions in E! / (∏_{i in even} c[i]!).
            // Multiplying gives:
            // ways = (O! * E!) / (∏_{i=1}^{26} c[i]!)    (0! = 1 for letters with c[i] = 0).
            long arrangements = fact[O] * fact[E] % MOD;
            for (int i = 0; i < 26; i++) {
                arrangements = arrangements * invFact[c[i]] % MOD;
            }

            long ans = validPartitions % MOD * arrangements % MOD;
            output.append(ans).append("\n");
        }

        System.out.print(output);
    }
}
