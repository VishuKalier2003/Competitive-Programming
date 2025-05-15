#include <bits/stdc++.h>
using namespace std;

// Fenwick (Binary Indexed) Tree for point updates and prefix sums
struct Fenwick {
    int n;
    vector<int> f;
    Fenwick(int _n): n(_n), f(n+1, 0) {}

    // add delta at index i (1-based)
    void update(int i, int delta) {
        for (; i <= n; i += i & -i) {
            f[i] += delta;
        }
    }

    // sum of [1..i]
    int query(int i) const {
        int s = 0;
        for (; i > 0; i -= i & -i) {
            s += f[i];
        }
        return s;
    }

    // sum of [l..r]
    int rangeQuery(int l, int r) const {
        if (l > r) return 0;
        return query(r) - query(l - 1);
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, q;
    cin >> n >> q;

    vector<long long> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }

    struct Query { char type; long long x, y; };
    vector<Query> queries;
    queries.reserve(q);

    for (int i = 0; i < q; i++) {
        char t;
        cin >> t;
        if (t == '!') {
            long long k, x;
            cin >> k >> x;
            queries.push_back({t, k, x});
        } else {
            long long l, r;
            cin >> l >> r;
            queries.push_back({t, l, r});
        }
    }

    // Coordinate compression
    vector<long long> coords = a;
    coords.reserve(n + 2*q);
    for (auto &qr : queries) {
        if (qr.type == '!') {
            // update: new salary
            coords.push_back(qr.y);
        } else {
            // range query bounds
            coords.push_back(qr.x);
            coords.push_back(qr.y);
        }
    }
    sort(coords.begin(), coords.end());
    coords.erase(unique(coords.begin(), coords.end()), coords.end());

    auto getIndex = [&](long long v) {
        // 1-based index
        return int(lower_bound(coords.begin(), coords.end(), v) - coords.begin()) + 1;
    };

    Fenwick fenw((int)coords.size());

    // initialize frequencies
    for (int i = 0; i < n; i++) {
        int ci = getIndex(a[i]);
        fenw.update(ci, 1);
    }

    // process queries
    for (auto &qr : queries) {
        if (qr.type == '?') {
            int l = getIndex(qr.x);
            // upper_bound - 1 for right
            int r = int(upper_bound(coords.begin(), coords.end(), qr.y) - coords.begin());
            if (r < 1) {
                cout << 0 << '\n';
            } else {
                cout << fenw.rangeQuery(l, r) << '\n';
            }
        } else {
            // update ! k x
            int pos = int(qr.x) - 1;
            long long oldVal = a[pos];
            int oldCi = getIndex(oldVal);
            fenw.update(oldCi, -1);

            a[pos] = qr.y;
            int newCi = getIndex(qr.y);
            fenw.update(newCi, +1);
        }
    }

    return 0;
}
