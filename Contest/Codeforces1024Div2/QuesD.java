import java.io.*;
import java.util.*;

public class QuesD {
    // Fenwick tree to compute inversion parity
    static class Fenwick {
        int n;
        int[] tree;
        Fenwick(int n) { this.n = n; tree = new int[n+1]; }
        void update(int i) { for (; i<=n; i += i&-i) tree[i]++; }
        int query(int i) { // sum[1..i]
            int s = 0;
            for (; i>0; i -= i&-i) s += tree[i];
            return s;
        }
    }

    static int inversionParity(List<Integer> vals, int k) {
        Fenwick fenw = new Fenwick(k);
        int parity = 0;
        // we want count of pairs i<j with vals[i] > vals[j]
        // process left→right, each time # seen > vals[i]
        for (int x : vals) {
            int seenLE = fenw.query(x);
            int seen   = fenw.query(k);
            // seen – seenLE = # seen > x
            parity ^= ((seen - seenLE) & 1);
            fenw.update(x);
        }
        return parity;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        int t = Integer.parseInt(in.readLine().trim());
        while (t-- > 0) {
            int n = Integer.parseInt(in.readLine().trim());
            StringTokenizer st = new StringTokenizer(in.readLine());
            // Split into the two parity‐blocks
            List<Integer> odds  = new ArrayList<>();
            List<Integer> evens = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                int x = Integer.parseInt(st.nextToken());
                if ((i & 1) == 1) odds.add(x);
                else             evens.add(x);
            }
            int pOdd  = inversionParity(odds,  n); // value range is 1..n
            int pEven = inversionParity(evens, n);
            Collections.sort(odds);
            Collections.sort(evens);
            if (pOdd != pEven) {
                if (n % 2 == 1) {
                    // odd‐length: last pos = n is odd => fix odds block
                    int m = odds.size();
                    Collections.swap(odds, m-1, m-2);
                } else {
                    // even‐length: last pos = n is even => fix evens block
                    int m = evens.size();
                    Collections.swap(evens, m-1, m-2);
                }
            }

            // Re‐interleave and print
            StringBuilder sb = new StringBuilder();
            int oi = 0, ei = 0;
            for (int i = 1; i <= n; i++) {
                if ((i & 1) == 1) sb.append(odds.get(oi++));
                else              sb.append(evens.get(ei++));
                if (i < n) sb.append(' ');
            }
            out.write(sb.toString());
            out.newLine();
        }
        out.flush();
    }
}
