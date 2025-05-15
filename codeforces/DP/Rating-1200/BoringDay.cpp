#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int helper(int i, int n, const vector<long long>& nums, long long sum, long l, long r,
           vector<unordered_map<long long, int>>& dp) {
    if (i == n)
        return 0;
    if (dp[i].count(sum)) return dp[i][sum];

    int noTake = helper(i + 1, n, nums, 0, l, r, dp);
    int take = 0;
    long long newSum = sum + nums[i];
    if (newSum < l)
        take = helper(i + 1, n, nums, newSum, l, r, dp);
    else if (newSum <= r)
        take = 1 + helper(i + 1, n, nums, 0, l, r, dp);
    int result = max(take, noTake);
    dp[i][sum] = result;
    return result;
}

int bestGameSet(int n, const vector<long long>& nums, long l, long r) {
    vector<unordered_map<long long, int>> dp(n + 1);
    return helper(0, n, nums, 0, l, r, dp);
}

int main(){
    fast;
    int t;
    cin >> t;
    while(t--){
        int n, l, r;
        cin >> n >> l >> r;
        vector<long long> nums(n);
        for (int i = 0; i < n; i++) {
            cin >> nums[i];
        }
        cout << bestGameSet(n, nums, l, r) << "\n";
    }
    return 0;
}
