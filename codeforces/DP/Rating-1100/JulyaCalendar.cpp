#include <iostream>     // input output reader
#include <vector>       // vector operations (lists)
#include <limits>       // for setting up max and min values
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int helper(int num, vector<int>& dp) {
    if(num == 0)        // When number reduces to zero, base case
        return 0;
    if(dp[num] != numeric_limits<int>::max())       // When state is already computed
        return dp[num];
    int temp = num, ans = numeric_limits<int>::max();
    while(temp != 0) {
        int digit = temp % 10;      // Extracting digit
        if(digit > 0)
            // IMP- for each state, we find the minimum steps from all possible paths reaching that state
            ans = min(ans, helper(num-digit, dp)+1);        // Function call and adding cost 1 to the reached state
        temp /= 10;
    }
    return dp[num] = ans;       // Updating the dp[state] with minimum cost
}

int main() {
    fast;
    int num;
    cin >> num;
    vector<int> dp(num+1, numeric_limits<int>::max());      // Filling the dp with int max value
    cout << helper(num, dp) << "\n";
    return 0;
}
