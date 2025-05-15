import java.io.*;
import java.util.*;

public class ConstructByLayers {
    static int[] a, p;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine().trim());
        StringBuilder out = new StringBuilder();

        while (t-- > 0) {
            int n = Integer.parseInt(in.readLine().trim());
            a = new int[n];
            StringTokenizer st = new StringTokenizer(in.readLine());
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(st.nextToken());
            }

            p = new int[n];
            // initial full index list [0,1,...,n-1]
            List<Integer> idx = new ArrayList<>(n);
            for (int i = 0; i < n; i++) idx.add(i);

            // maximum number of iterations is ceil(log2(n)), but
            // our recursion will naturally stop once survivors.size()==1
            build(idx, 1, 1, n);

            // output 1‐based permutation
            for (int v : p) out.append(v).append(' ');
            out.append('\n');
        }
        System.out.print(out);
    }

    /**
     * @param idx  the current positions we're assigning (in original order)
     * @param iter which iteration (1-based).  Odd=local-minima pass, Even=local-maxima.
     * @param lo   lowest value we may assign
     * @param hi   highest value we may assign
     */
    static void build(List<Integer> idx, int iter, int lo, int hi) {
        if (idx.size() == 0) return;
        if (idx.size() == 1) {
            // only one left: give it whatever is left in [lo..hi]
            p[idx.get(0)] = lo;
            return;
        }
        // split into "removed this round" vs "survive to next"
        List<Integer> survivors = new ArrayList<>(), removed = new ArrayList<>();
        for (int i : idx) {
            if (a[i] == iter)
                removed.add(i);
            else
                survivors.add(i);
        }
        int cntS = survivors.size(), cntR = removed.size();

        // assign values so that
        //  - if odd iter: survivors are minima => give them the smallest cntS values in [lo..hi]
        //  - if even iter: survivors are maxima => give them the largest cntS values
        int cur = lo;
        if ((iter & 1) == 1) {
            // odd: survivors low, removed high
            for (int i : survivors) p[i] = cur++;
            for (int i : removed)   p[i] = cur++;
            // next recursion uses the block [lo .. lo+cntS-1]
            build(survivors, iter+1, lo, lo+cntS-1);
        } else {
            // even: removed low, survivors high
            for (int i : removed)   p[i] = cur++;
            for (int i : survivors) p[i] = cur++;
            // next recursion uses the block [hi-cntS+1 .. hi]
            build(survivors, iter+1, hi-cntS+1, hi);
        }
    }
}
