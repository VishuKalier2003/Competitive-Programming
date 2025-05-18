#include <bits/stdc++.h>
using namespace std;
using ll = long long;
static const int MAXN = 200000;

int n, k1, k2;
vector<int> adj[MAXN+1];
bool dead[MAXN+1];
int subtree[MAXN+1];

// Fenwick tree on [1..k2+1]
int fenw[MAXN+2];
inline void fenw_update(int i, int v) {
    for (; i <= k2+1; i += i & -i) fenw[i] += v;
}
inline int fenw_query(int i) {
    int s = 0;
    for (; i > 0; i -= i & -i) s += fenw[i];
    return s;
}

ll answer = 0;

// Scratch stacks and buffers
int stk1[2*MAXN][3], top1;
int stk2[MAXN][3], top2;
int distBuf[MAXN], distLen;
int usedBuf[MAXN], usedLen;

void computeSizes(int root) {
    top1 = 0;
    stk1[top1][0] = root; stk1[top1][1] = -1; stk1[top1][2] = 0; 
    ++top1;
    while (top1) {
        auto &e = stk1[--top1];
        int u = e[0], p = e[1], phase = e[2];
        if (phase == 0) {
            subtree[u] = 1;
            stk1[top1][0] = u; stk1[top1][1] = p; stk1[top1][2] = 1; 
            ++top1;
            for (int v: adj[u]) if (v!=p && !dead[v]) {
                stk1[top1][0] = v; stk1[top1][1] = u; stk1[top1][2] = 0;
                ++top1;
            }
        } else {
            if (p != -1) subtree[p] += subtree[u];
        }
    }
}

int findCentroid(int root, int compSize) {
    int u = root, p = -1;
    bool moved;
    do {
        moved = false;
        for (int v: adj[u]) {
            if (v!=p && !dead[v] && subtree[v] > compSize/2) {
                p = u; u = v; moved = true; break;
            }
        }
    } while (moved);
    return u;
}

void collectDists(int start, int parent) {
    distLen = 0;
    top2 = 0;
    stk2[top2][0] = start; stk2[top2][1] = parent; stk2[top2][2] = 1;
    ++top2;
    while (top2) {
        auto &e = stk2[--top2];
        int u = e[0], p = e[1], d = e[2];
        if (d > k2) continue;
        distBuf[distLen++] = d;
        for (int v: adj[u]) if (v!=p && !dead[v]) {
            stk2[top2][0] = v; stk2[top2][1] = u; stk2[top2][2] = d+1;
            ++top2;
        }
    }
}

void countPaths(int c) {
    // seed centroid at distance 0 => fenw idx = 1
    fenw_update(1, +1);
    usedLen = 0;
    usedBuf[usedLen++] = 1;

    for (int v: adj[c]) {
        if (dead[v]) continue;
        collectDists(v, c);
        // MATCH
        for (int i = 0; i < distLen; ++i) {
            int d = distBuf[i];
            int lo = k1 - d, hi = k2 - d;
            if (hi < 0) continue;
            if (lo < 0) lo = 0;
            int li = lo+1, hii = hi+1;
            if (li>k2+1) continue;
            if (hii>k2+1) hii = k2+1;
            answer += fenw_query(hii) - fenw_query(li-1);
        }
        // MERGE
        for (int i = 0; i < distLen; ++i) {
            int idx = distBuf[i] + 1;
            if (idx > k2+1) continue;
            // record first touch
            int before = fenw_query(idx) - fenw_query(idx-1);
            if (!before) usedBuf[usedLen++] = idx;
            fenw_update(idx, +1);
        }
    }
    // CLEANUP
    for (int i = 0; i < usedLen; ++i) {
        int idx = usedBuf[i];
        int cnt = fenw_query(idx) - fenw_query(idx-1);
        if (cnt) fenw_update(idx, -cnt);
    }
}

void decompose(int root) {
    computeSizes(root);
    int csize = subtree[root];
    int c = findCentroid(root, csize);
    countPaths(c);
    dead[c] = true;
    for (int v: adj[c]) if (!dead[v])
        decompose(v);
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> k1 >> k2;
    for (int i = 1; i < n; i++){
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    decompose(1);
    cout << answer << "\n";
    return 0;
}
