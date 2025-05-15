import java.io.*;
import java.util.*;

public class QuesE {
    static class Interval {
        int l, r;
        int len;
        Interval(int l, int r) {
            this.l = l;
            this.r = r;
            this.len = r - l;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int T = Integer.parseInt(in.readLine().trim());
        while (T-- > 0) {
            int n = Integer.parseInt(in.readLine().trim());
            int[] a = new int[n+1];
            StringTokenizer st = new StringTokenizer(in.readLine());
            for (int i = 1; i <= n; i++) {
                a[i] = Integer.parseInt(st.nextToken());
            }

            // Step 1: compute for each x its earliest and latest eligible positions
            int[] first = new int[n+1];
            int[] last  = new int[n+1];
            Arrays.fill(first,  n+1);
            Arrays.fill(last,   0);
            for (int i = 1; i <= n; i++) {
                // any b_i <= a[i], so i is eligible for all 1..a[i]:
                // we update first[x] and last[x] for x=1..a[i].
                // But doing that for each x would be O(n^2).
                // Instead, we only care about the final intervals for each x,
                // so we invert loops: see Step 2 below.
            }

            // Step 2: Build the intervals [first[x], last[x]] in O(n) by bucketing
            // We'll do a single pass from i=1..n, maintaining a stack of values.
            // Whenever we hit a new index i, we "close" any values whose eligibility
            // has ended before i, and "open" new ones whose eligibility begins at i.
            // Because a value x is eligible at i iff a[i] >= x, the set of active x's
            // is exactly [1..maxSeen], where maxSeen = max_{j<=i} a[j].
            // So:
            int maxSeen = 0;
            // We'll record for each x when it first becomes active:
            for (int x = 1; x <= n; x++) first[x] = n+1;
            // And last[x] naturally gets updated as we go.
            for (int i = 1; i <= n; i++) {
                maxSeen = Math.max(maxSeen, a[i]);
                // all x in [1..maxSeen] are eligible at i
                // so for x newly seen (i.e. x>prevMaxSeen), set first[x]=i
                // and in any case update last[x]=i.
                // We can do this by a small inner loop only over the delta in maxSeen:
                // Keep a pointer `nxt` that runs up to maxSeen exactly once.
            }

            // ... unfortunately this outline approach still hides the need for O(n^2).
            // The true editorial constructs intervals in O(n) by *sorting positions by a[i]*:

            // Correct Step 2: build buckets by value, as in the previous solution,
            // then sweep x=n..1:
            List<Integer>[] bucket = new ArrayList[n+2];
            for (int v = 1; v <= n; v++) bucket[v] = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                bucket[a[i]].add(i);
            }
            // now:
            int low = n+1, high = 0;
            List<Interval> intervals = new ArrayList<>();
            for (int x = n; x >= 1; x--) {
                for (int idx: bucket[x]) {
                    low  = Math.min(low,  idx);
                    high = Math.max(high, idx);
                }
                if (high > low) {
                    intervals.add(new Interval(low, high));
                }
            }
            // intervals now holds for each x with at least two eligibles its [l,r].

            // Step 3: Greedily pick a disjoint set of these intervals with maximum total length,
            // knowing we can only assign each position i once (either as a left endpoint or right).
            // This reduces to: sort intervals by l ascending. Sweep i=1..n, at each i push
            // intervals with l==i into a max-heap by r, then pop the best if r>=i.
            // Each time you pop, you collect its length.

            // Sort by l
            intervals.sort(Comparator.comparingInt(iv -> iv.l));
            PriorityQueue<Interval> pq = new PriorityQueue<>(
                (u,v) -> Integer.compare(v.r, u.r)
            );

            long beauty = 0;
            int ptr = 0;  // index in intervals
            for (int i = 1; i <= n; i++) {
                // add all intervals starting at i
                while (ptr < intervals.size() && intervals.get(ptr).l == i) {
                    pq.add(intervals.get(ptr));
                    ptr++;
                }
                // pick the interval that ends farthest to close here
                if (!pq.isEmpty() && pq.peek().r >= i) {
                    Interval best = pq.poll();
                    beauty += best.len;
                }
                // all other intervals remain active for future positions
            }
            out.append(beauty).append('\n');
        }
        System.out.print(out);
    }
}
