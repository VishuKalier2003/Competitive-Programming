#include <bits/stdc++.h>
using namespace std;

using ll = long long;

vector<ll> subsetSums(const vector<ll>& nums) {
    int n = nums.size();
    int total = 1 << n;
    vector<ll> sums(total);
    for (int mask = 0; mask < total; mask++) {
        ll sum = 0;
        for (int i = 0; i < n; i++) {
            if (mask & (1 << i)) sum += nums[i];
        }
        sums[mask] = sum;
    }
    return sums;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    ll x;
    cin >> n >> x;

    vector<ll> nums(n);
    for (int i = 0; i < n; i++) cin >> nums[i];

    int mid = n / 2;
    vector<ll> nums1(nums.begin(), nums.begin() + mid);
    vector<ll> nums2(nums.begin() + mid, nums.end());

    vector<ll> mp1 = subsetSums(nums1);
    vector<ll> mp2 = subsetSums(nums2);

    sort(mp2.begin(), mp2.end());

    ll ways = 0;
    for (ll s1 : mp1) {
        ll target = x - s1;

        auto lb = lower_bound(mp2.begin(), mp2.end(), target);
        auto ub = upper_bound(mp2.begin(), mp2.end(), target);
        ways += (ub - lb);
    }

    cout << ways << "\n";
    return 0;
}
