#include <iostream>
#include <vector>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
#define ll long long

class SegmentTree {
public:
    vector<ll> tree, lazy;
    int size;

    SegmentTree(int n, const vector<ll>& nums) {
        size = n;
        tree.assign(4 * n + 50, 0);
        lazy.assign(4 * n + 50, 0);
        build(1, 1, n, nums);
    }

    void build(int index, int left, int right, const vector<ll>& nums) {
        if (left == right) {
            tree[index] = nums[left - 1];  // 0-based input, 1-based tree
            return;
        }
        int mid = (left + right) >> 1;
        build(index * 2, left, mid, nums);
        build(index * 2 + 1, mid + 1, right, nums);
    }

    void update(int index, int l, int r, int ql, int qr, ll val) {
        lazyPropagate(index, l, r);
        if (qr < l || ql > r) return;  // no overlap
        if (ql <= l && r <= qr) {  // total overlap
            tree[index] += val;
            if (l != r) {
                lazy[index * 2] += val;
                lazy[index * 2 + 1] += val;
            }
            return;
        }
        int mid = (l + r) >> 1;
        update(index * 2, l, mid, ql, qr, val);
        update(index * 2 + 1, mid + 1, r, ql, qr, val);
    }

    ll pointQuery(int index, int l, int r, int pos) {
        lazyPropagate(index, l, r);
        if (l == r) return tree[index];  // leaf node
        int mid = (l + r) >> 1;
        if (pos <= mid)
            return pointQuery(index * 2, l, mid, pos);
        else
            return pointQuery(index * 2 + 1, mid + 1, r, pos);
    }

    void lazyPropagate(int index, int l, int r) {
        if (lazy[index] != 0) {
            tree[index] += lazy[index];
            if (l != r) {
                lazy[index * 2] += lazy[index];
                lazy[index * 2 + 1] += lazy[index];
            }
            lazy[index] = 0;
        }
    }
};

int main() {
    fast;
    int n, k;
    cin >> n >> k;
    vector<ll> nums(n);
    for (int i = 0; i < n; ++i) cin >> nums[i];

    SegmentTree segTree(n, nums);

    for (int i = 0; i < k; ++i) {
        int type;
        cin >> type;
        if (type == 1) {
            int l, r;
            ll val;
            cin >> l >> r >> val;
            segTree.update(1, 1, n, l, r, val);
        } else {
            int idx;
            cin >> idx;
            cout << segTree.pointQuery(1, 1, n, idx) << "\n";
        }
    }
    return 0;
}
