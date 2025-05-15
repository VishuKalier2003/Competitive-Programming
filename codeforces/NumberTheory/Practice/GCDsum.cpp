#include <iostream>
#include <vector>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
#define ll long long

ll gcd(ll a, ll b) {        // GCD function
    if(b == 0)
        return a;
    return gcd(b, a % b);
}

ll findMaxGCD(const int n, const vector<ll>& nums, const ll sum) {
    // When we want to maximize GCD it is always optimal to split the array into exactly 2 segments and not more than 2
    ll prefix = nums[0], maxGCD = gcd(prefix, sum-prefix);
    for(int i = 1; i < n-1; i++) {      // Need not to take boundaries, skip 0 and n-1
        prefix += nums[i];
        // IMP- Finding max GCD of the increasing segment and the leftover sum iteratively
        maxGCD = max(maxGCD, gcd(prefix, sum-prefix));
    }
    return maxGCD;
}

int main() {
    fast;
    int t;
    cin >> t;
    while(t--) {
        int n;
        ll sum = 0;
        cin >> n;
        vector<ll> nums(n);         // vector
        for(int i = 0; i < n; i++) {
            ll num;
            cin >> num;
            nums[i] = num;
            sum += num;
        }
        cout << findMaxGCD(n, nums, sum) << "\n";
    }
    return 0;
}
