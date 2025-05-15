#include <iostream>
#include <algorithm>
#include <vector>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int main() {
    fast;
    int n, x;
    cin >> n >> x;
    vector<int> nums(n), dp(x+1);
    for(int i = 0; i < n; i++)
        cin >> nums[i];
    dp[0] = 1;
    const int MOD = 1'000'000'007;
    sort(nums.begin(), nums.end());     // sort the numbers to reduce operations per i
    for(int i = 1; i <= x; i++)
        for(int j = 0; j < n; j++) {
            if(i-nums[j] < 0)
                break;
            dp[i] += dp[i-nums[j]];
            if(dp[i] >= MOD)
                dp[i] -= MOD;
        }
    cout << dp[x];
    return 0;
}
