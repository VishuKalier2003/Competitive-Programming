import java.io.*;
import java.util.StringTokenizer;

public class Typewriter {
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
        int nextInt() { return Integer.parseInt(next()); }
    }

    public static void main(String[] args) {
        FastReader in = new FastReader();
        StringBuilder out = new StringBuilder();

        int t = in.nextInt();
        while (t-- > 0) {
            int n = in.nextInt();
            String s = in.next();

            // 1) count runs
            int runs = 1;
            for (int i = 1; i < n; i++) {
                if (s.charAt(i) != s.charAt(i - 1)) runs++;
            }

            // 2) base cost without any reversal
            //    = presses (n) + moves (runs)
            //    but if s[0]=='0' you save one initial move
            int baseCost = n + runs - (s.charAt(0) == '0' ? 1 : 0);

            // 3) how many boundaries can we merge by an internal reversal?
            int internalGain;
            if (runs <= 2)        internalGain = 0;
            else if (runs == 3)   internalGain = 1;
            else                  internalGain = 2;

            // 4) what can a prefix-reversal do?
            //    - flip first char from '1'→'0' if there's at least one '0' (i.e. runs>=2)
            //    - merge one run-boundary if runs>=3
            int prefixGain = 0;
            if (runs >= 2 && s.charAt(0) == '1') prefixGain++;  // flip front
            if (runs >= 3)                        prefixGain++;  // merge one

            // 5) we choose the single reversal that gives the bigger gain
            int bestGain = Math.max(internalGain, prefixGain);

            // 6) answer is baseCost minus that gain
            out.append(baseCost - bestGain).append('\n');
        }

        System.out.print(out);
    }
}
