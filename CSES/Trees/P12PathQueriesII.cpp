#include <bits/stdc++.h>
using namespace std;

static const int MAXN = 200000;
int n, q;
vector<int> adj[MAXN+1];
int vals[MAXN+1];

// HLD data
int parent_[MAXN+1], depth_[MAXN+1], sz[MAXN+1], heavy[MAXN+1];
int head[MAXN+1], pos[MAXN+1], curPos;

// LCA up‐table
static const int LOG = 18;
int up[MAXN+1][LOG];

// segment tree (bottom‐up)
struct SegTree {
    int N;
    vector<int> t;
    SegTree(int n): N(1) {
        while (N < n) N <<= 1;
        t.assign(2*N, 0);
    }
    void build(const vector<int>& a) {
        int n = a.size();
        for (int i = 0; i < n; i++) t[N + i] = a[i];
        for (int i = N-1; i >= 1; i--)
            t[i] = max(t[2*i], t[2*i+1]);
    }
    // set a[i] = v
    void update(int i, int v) {
        i += N;
        t[i] = v;
        for (i >>= 1; i; i >>= 1)
            t[i] = max(t[2*i], t[2*i+1]);
    }
    // query max on [l..r]
    int query(int l, int r) {
        int res = 0;
        for (l += N, r += N; l <= r; l >>= 1, r >>= 1) {
            if (l&1) res = max(res, t[l++]);
            if (!(r&1)) res = max(res, t[r--]);
        }
        return res;
    }
};

// 1) compute parent, depth, subtree sizes, and heavy child (recursive but O(n) depth)
int dfs_sz(int u, int p) {
    parent_[u] = p;
    depth_[u]  = (p? depth_[p]+1 : 0);
    sz[u] = 1;
    int maxSub = 0;
    for (int w : adj[u]) if (w != p) {
        int sub = dfs_sz(w, u);
        if (sub > maxSub) {
            maxSub = sub;
            heavy[u] = w;
        }
        sz[u] += sub;
    }
    return sz[u];
}

// 2) decompose into heavy paths
void dfs_hld(int u, int h) {
    head[u] = h;
    pos[u]  = curPos++;
    if (heavy[u]) dfs_hld(heavy[u], h);
    for (int w : adj[u]) if (w != parent_[u] && w != heavy[u]) {
        dfs_hld(w, w);
    }
}

// 3) build binary-lifting "up" table
void build_lca() {
    for (int v = 1; v <= n; v++)
        up[v][0] = parent_[v];
    for (int j = 1; j < LOG; j++) {
        for (int v = 1; v <= n; v++) {
            up[v][j] = up[ up[v][j-1] ][j-1];
        }
    }
}

// iterative LCA
int lca(int a, int b) {
    if (depth_[a] < depth_[b]) swap(a,b);
    int diff = depth_[a] - depth_[b];
    for (int j = 0; j < LOG; j++)
        if (diff & (1<<j))
            a = up[a][j];
    if (a == b) return a;
    for (int j = LOG-1; j >= 0; j--) {
        if (up[a][j] != up[b][j]) {
            a = up[a][j];
            b = up[b][j];
        }
    }
    return parent_[a];
}

// query max on path u→anc (exclusive)
int query_up(SegTree &st, int u, int anc) {
    int res = 0;
    while (head[u] != head[anc]) {
        res = max(res,
            st.query(pos[ head[u] ], pos[u]));
        u = parent_[ head[u] ];
    }
    if (u != anc) {
        res = max(res,
            st.query(pos[anc]+1, pos[u]));
    }
    return res;
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> q;
    for (int i = 1; i <= n; i++) {
        cin >> vals[i];
        adj[i].clear();
        heavy[i] = 0;
    }
    for (int i = 1, u, v; i < n; i++){
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    // 1) HLD preprocess
    dfs_sz(1, 0);
    build_lca();
    curPos = 0;
    dfs_hld(1, 1);

    // 2) build base array in HLD order
    vector<int> base(n);
    for (int v = 1; v <= n; v++)
        base[ pos[v] ] = vals[v];

    // 3) build segment tree
    SegTree st(n);
    st.build(base);

    // 4) answer queries
    vector<int> ans;
    ans.reserve(q);

    while (q--) {
        int t; cin >> t;
        if (t == 1) {
            int s, x; cin >> s >> x;
            st.update(pos[s], x);
        } else {
            int a,b; cin >> a >> b;
            int c = lca(a,b);
            int res = max( query_up(st, a, c),
                           query_up(st, b, c) );
            res = max(res, st.query(pos[c], pos[c]));
            ans.push_back(res);
        }
    }

    // 5) flush all at once
    ostringstream out;
    for (int v : ans) out << v << ' ';
    cout << out.str() << "\n";
    return 0;
}
