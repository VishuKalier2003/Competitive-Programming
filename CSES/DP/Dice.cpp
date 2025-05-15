#include <iostream>
#include <vector>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

const int MOD = 1'000'000'007;

int helper(int num, vector<int>& dp) {
    if(num == 0)
        return 1;
    if(num < 0)
        return 0;
    if(dp[num] != -1)
        return dp[num];
    int count = 0;
    for(int diceFace = 1; diceFace <= 6; diceFace++)
        count = (count + helper(num - diceFace, dp)) % MOD;
    return dp[num] = count;
}

int main() {
    fast;
    int n;
    cin >> n;
    vector<int> dp(n+1, -1);
    cout << helper(n, dp);
    return 0;
}
