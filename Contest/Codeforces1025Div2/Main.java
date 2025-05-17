import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder out = new StringBuilder();

        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            int l = Integer.parseInt(st.nextToken());

            int[] A = new int[l];
            long totalSum = 0;
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < l; i++) {
                A[i] = Integer.parseInt(st.nextToken());
                totalSum += A[i];
            }

            // --- 1) build graph and BFS from 1 to get dist[] and 2‑coloring ---
            List<Integer>[] graph = new ArrayList[n+1];
            for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                graph[u].add(v);
                graph[v].add(u);
            }

            int[] dist = new int[n+1], color = new int[n+1];
            Arrays.fill(dist, -1);
            Deque<Integer> dq = new ArrayDeque<>();
            dist[1] = 0;
            dq.add(1);
            boolean isBip = true;

            while (!dq.isEmpty()) {
                int u = dq.poll();
                for (int v : graph[u]) {
                    if (dist[v] == -1) {
                        dist[v] = dist[u] + 1;
                        color[v] = color[u] ^ 1;
                        dq.add(v);
                    } else if (color[v] == color[u]) {
                        isBip = false;
                    }
                }
            }

            // --- 2) subset‑sum DP of all Ai ≤ M, using a long‑array bitset ---
            int M = n - 1;
            int W = (M + 64) >>> 6;
            long[] dp = new long[W];
            dp[0] = 1L;  // only sum 0 reachable initially

            for (int k : A) {
                if (k > M) continue;
                int wordShift = k >>> 6;
                int bitShift  = k & 63;
                long[] tmp = new long[W];

                if (bitShift == 0) {
                    for (int w = 0; w + wordShift < W; w++) {
                        tmp[w + wordShift] = dp[w];
                    }
                } else {
                    for (int w = 0; w + wordShift < W; w++) {
                        long val = dp[w];
                        if (val == 0) continue;
                        tmp[w + wordShift]     |= val << bitShift;
                        if (w + wordShift + 1 < W) {
                            tmp[w + wordShift + 1] |= val >>> (64 - bitShift);
                        }
                    }
                }
                for (int w = 0; w < W; w++) {
                    dp[w] |= tmp[w];
                }
            }

            // extract max‑even and max‑odd sums ≤ M
            int maxEvenSmall = -1, maxOddSmall = -1;
            for (int k = M; k >= 0; k--) {
                int wi = k >>> 6, bi = k & 63;
                if (((dp[wi] >>> bi) & 1L) == 1L) {
                    if ((k & 1) == 0 && maxEvenSmall < k) maxEvenSmall = k;
                    if ((k & 1) == 1 && maxOddSmall  < k) maxOddSmall  = k;
                }
                if (maxEvenSmall >= 0 && maxOddSmall >= 0) break;
            }
            boolean hasSmallOdd = (maxOddSmall >= 1);

            // --- 3) compute overflow (sums > M) parity availability ---
            boolean hasLargeEvenElem = false;
            boolean hasLargeOddElem  = false;
            int oddLargeCount = 0;

            for (int k : A) {
                if (k > M) {
                    if ((k & 1) == 0) hasLargeEvenElem = true;
                    else {
                        hasLargeOddElem = true;
                        oddLargeCount++;
                    }
                }
            }

            boolean hasLargeEven = false, hasLargeOdd = false;
            if (totalSum > M) {
                // full‑sum contributes
                if ((totalSum & 1) == 0) hasLargeEven = true;
                else                    hasLargeOdd  = true;

                // any single large Ai
                if (hasLargeEvenElem) hasLargeEven = true;
                if (hasLargeOddElem)  hasLargeOdd  = true;

                // two odds → even
                if (oddLargeCount >= 2) hasLargeEven = true;

                // odd small + even large → odd
                if (hasSmallOdd && hasLargeEvenElem) hasLargeOdd  = true;
                // odd small + odd large → even
                if (hasSmallOdd && hasLargeOddElem ) hasLargeEven = true;
            }

            // --- 4) per‑vertex reachability check ---
            StringBuilder sb = new StringBuilder(n);
            for (int i = 1; i <= n; i++) {
                if (dist[i] < 0) {
                    sb.append('0');
                } else if (!isBip) {
                    // non‑bipartite: any parity is fine
                    sb.append(dist[i] <= totalSum ? '1' : '0');
                } else {
                    // bipartite: must match parity
                    if ((dist[i] & 1) == 0) {
                        // even target distance
                        sb.append((maxEvenSmall >= dist[i] || hasLargeEven) ? '1' : '0');
                    } else {
                        // odd target distance
                        sb.append((maxOddSmall  >= dist[i] || hasLargeOdd ) ? '1' : '0');
                    }
                }
            }

            out.append(sb).append('\n');
        }

        System.out.print(out);
    }
}
