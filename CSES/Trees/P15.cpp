#include <bits/stdc++.h>
using namespace std;
using ll = long long;

static const int MAXN = 200000;

int n, k;
vector<vector<int>> tree;
vector<bool> dead;
vector<int> subtree;
ll answer = 0;

// frequency buffer for depths [0..k], plus list of which indices were used
vector<int> freq;
vector<int> usedDepths;

// 1) Iterative subtree‐size DFS
void computeSubtreeSizes(int root) {
    // stack entries: (node, parent, phase)
    // phase 0 = pre‐visit, 1 = post‐visit
    static array<int,3> st[MAXN * 2];
    int top = 0;
    st[top++] = {root, -1, 0};

    while (top) {
        auto [u, p, phase] = st[--top];
        if (phase == 0) {
            subtree[u] = 1;
            // schedule post
            st[top++] = {u, p, 1};
            // push children
            for (int v : tree[u]) {
                if (v != p && !dead[v]) {
                    st[top++] = {v, u, 0};
                }
            }
        } else {
            // accumulate into parent
            if (p != -1) {
                subtree[p] += subtree[u];
            }
        }
    }
}

// 2) Iterative centroid finder
int findCentroid(int start, int compSize) {
    int u = start, p = -1;
    bool moved;
    do {
        moved = false;
        for (int v : tree[u]) {
            if (v != p && !dead[v] && subtree[v] > compSize/2) {
                p = u;
                u = v;
                moved = true;
                break;
            }
        }
    } while (moved);
    return u;
}

// 3) Iterative depth‐collection up to depth k
void collectDepths(int start, int parent, vector<int>& dists) {
    // stack entries: (node, parent, depth)
    static array<int,3> st[MAXN];
    int top = 0;
    st[top++] = {start, parent, 1};

    while (top) {
        auto [u, p, depth] = st[--top];
        if (depth > k) continue;
        dists.push_back(depth);
        for (int v : tree[u]) {
            if (v != p && !dead[v]) {
                st[top++] = {v, u, depth+1};
            }
        }
    }
}

// 4) Count all paths of length exactly k passing through this centroid
void countPathsAt(int centroid) {
    // seed: path of length 0 at centroid
    freq[0] = 1;
    usedDepths.push_back(0);

    for (int v : tree[centroid]) {
        if (dead[v]) continue;

        // gather all depths from centroid→v
        vector<int> dists;
        dists.reserve(subtree[v]);
        collectDepths(v, centroid, dists);

        // match: for each d in this subtree, add freq[k-d]
        for (int d : dists) {
            if (k - d >= 0 && k - d <= k)
                answer += freq[k - d];
        }

        // merge: make these depths available for future subtrees
        for (int d : dists) {
            if (freq[d] == 0) 
                usedDepths.push_back(d);
            freq[d]++;
        }
    }

    // clear freq for this centroid
    for (int d : usedDepths) {
        freq[d] = 0;
    }
    usedDepths.clear();
}

// 5) Recursive decomposition (depth O(log n))
void decompose(int root) {
    computeSubtreeSizes(root);
    int compSize = subtree[root];
    int centroid = findCentroid(root, compSize);

    countPathsAt(centroid);
    dead[centroid] = true;

    for (int v : tree[centroid]) {
        if (!dead[v]) {
            decompose(v);
        }
    }
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> k;
    tree.assign(n+1, {});
    dead.assign(n+1, false);
    subtree.assign(n+1, 0);
    freq.assign(k+1, 0);

    for (int i = 0, u, v; i < n-1; i++) {
        cin >> u >> v;
        tree[u].push_back(v);
        tree[v].push_back(u);
    }

    decompose(1);
    cout << answer << "\n";
    return 0;
}
