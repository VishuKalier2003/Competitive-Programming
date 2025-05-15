#include <iostream>
#include <vector>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int maxHealthPoints(const vector<int>& nums, const vector<int>& again, const int n, const int k) {
    // Variables to store maxHealth, max of again till i index and sum of nums till i index
    int maxHealth = 0, maxAgain = 0, prefixSum = 0;
    for(int i = 0; i < min(n, k); i++) {
        prefixSum += nums[i];   // Updating the sum
        // IMP- It is best to work on quests which have max again values for the left over k
        maxAgain = max(maxAgain, again[i]);     // Updating the max of again
        // IMP- For every index i, we will have left over k as k-i-1 and then we greedily find the max health
        maxHealth = max(maxHealth, prefixSum+(k-i-1)*maxAgain);
    }
    return maxHealth;
}

int main() {
    fast;
    int t;
    cin >> t;
    while(t--) {        // for each test case
        int n, k;
        cin >> n >> k;
        vector<int> nums(n), again(n);
        for(int i = 0; i < n; i++) {        // nums filling
            int x;
            cin >> x;
            nums[i] = x;
        }
        for(int j = 0; j < n; j++) {        // again filling
            int x;
            cin >> x;
            again[j] = x;
        }
        cout << maxHealthPoints(nums, again, n, k) << "\n";
    }
    return 0;
}
