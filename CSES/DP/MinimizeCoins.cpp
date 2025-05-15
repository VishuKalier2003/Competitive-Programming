#include <iostream>
#include <vector>
#include <climits>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int helper(int money, int n, vector<int>& nums, vector<int>& dp) {
    if(money == 0)
        return 0;
    if(money < 0)
        return 1'000'000;
    if(dp[money] != -1)
        return dp[money];
    int minCoins = INT_MAX;
    for(int i = 0; i < n; i++)
        minCoins = min(minCoins, 1 + helper(money - nums[i], n, nums, dp));
    return dp[money] = minCoins;
}

int main() {
    fast;
    int x, money;
    cin >> x >> money;
    vector<int> nums(x);
    vector<int> dp(money+1, -1);
    for(int i = 0; i < x; i++) {
        int a;
        cin >> a;
        nums[i] = a;
    }
    int ways = helper(money, x, nums, dp);
    if(ways > 1'000'000)        // The range of numbers is 1'000'000 so we need to take not possible answer as greater than this value
        cout << -1;
    else
        cout << ways;
    return 0;
}
