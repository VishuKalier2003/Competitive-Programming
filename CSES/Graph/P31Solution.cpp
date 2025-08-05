#include <bits/stdc++.h>
using namespace std;

static const int MOD = 1'000'000'007;

// Micro-optimisation: Fast I/O
struct FastIO {
    FastIO() {
        ios::sync_with_stdio(false);
        cin.tie(nullptr);
    }
} fastio;

int main() {
    int n, m;
    cin >> n >> m;

    // build list-of-lists temporarily
    vector<vector<int>> g(n+1);
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        g[u].push_back(v);
    }

    int fullMask = (1 << n) - 1;
    // dp[mask][u] = number of ways to reach u with visited-set = mask
    // Using vector of vectors; total size ≈ (2^n)*(n+1)
    vector<vector<int>> dp(fullMask + 1, vector<int>(n+1, 0));
    dp[1][1] = 1;    // base case: only city 1 visited, at city 1

    // Build primitive adjacency arrays for speed
    vector<int> deg(n+1);
    vector<vector<int>> adj(n+1);
    for (int u = 1; u <= n; u++) {
        deg[u] = g[u].size();
        adj[u].reserve(deg[u]);
        for (int v : g[u]) {
            adj[u].push_back(v);
        }
    }

    // Core bitmask-DP
    for (int mask = 1; mask <= fullMask; mask++) {
        // Micro-optimisation: ensure city 1 is in mask
        if ((mask & 1) == 0) continue;

        // iterate only set bits in mask
        int sub = mask;
        while (sub) {
            int lsb = sub & -sub;
            int node = __builtin_ctz(lsb) + 1;  // index of lowest set bit
            sub ^= lsb;                         // remove that bit
            int ways = dp[mask][node];
            // Micro-optimisation: skip unreachable states
            if (ways == 0) continue;

            // extend path from 'node' to its neighbors
            for (int v : adj[node]) {
                int bit = 1 << (v - 1);  // neighbor’s bit in the mask
                if ((mask & bit) == 0) {
                    int newMask = mask | bit;
                    int val = dp[newMask][v] + ways;
                    // fast modulus
                    if (val >= MOD) val -= MOD;
                    dp[newMask][v] = val;
                }
            }
        }
    }

    // Answer: ways to visit all cities (fullMask) ending at city n
    cout << dp[fullMask][n] << "\n";
    return 0;
}
