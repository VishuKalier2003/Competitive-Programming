#include <bits/stdc++.h>
using namespace std;

class SegmentTree {
private:
    vector<int> tree;
    int n;

public:
    SegmentTree(int size) {
        n = size;
        tree.assign(4 * n, INT_MAX);
    }

    void build(int index, int left, int right, const vector<int>& nums) {
        if (left == right) {
            tree[index] = nums[left];
            return;
        }
        int mid = left + (right - left) / 2;
        build(2 * index, left, mid, nums);
        build(2 * index + 1, mid + 1, right, nums);
        tree[index] = min(tree[2 * index], tree[2 * index + 1]);
    }

    void update(int index, int left, int right, int pos, int value) {
        if (left == right) {
            tree[index] = value;
            return;
        }
        int mid = left + (right - left) / 2;
        if (pos <= mid)
            update(2 * index, left, mid, pos, value);
        else
            update(2 * index + 1, mid + 1, right, pos, value);
        tree[index] = min(tree[2 * index], tree[2 * index + 1]);
    }

    int rangeMinQuery(int index, int left, int right, int queryLeft, int queryRight) {
        if (queryRight < left || queryLeft > right)
            return INT_MAX;
        if (queryLeft <= left && right <= queryRight)
            return tree[index];
        int mid = left + (right - left) / 2;
        return min(
            rangeMinQuery(2 * index, left, mid, queryLeft, queryRight),
            rangeMinQuery(2 * index + 1, mid + 1, right, queryLeft, queryRight)
        );
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, k;
    cin >> n >> k;

    vector<int> nums(n);
    for (int i = 0; i < n; ++i)
        cin >> nums[i];

    SegmentTree segmentTree(n);
    segmentTree.build(1, 0, n - 1, nums);

    while (k--) {
        int type, a, b;
        cin >> type >> a >> b;
        if (type == 1) {
            // Point update: set nums[a-1] = b
            segmentTree.update(1, 0, n - 1, a - 1, b);
        } else {
            // Range min query from a-1 to b-1
            cout << segmentTree.rangeMinQuery(1, 0, n - 1, a - 1, b - 1) << '\n';
        }
    }

    return 0;
}
