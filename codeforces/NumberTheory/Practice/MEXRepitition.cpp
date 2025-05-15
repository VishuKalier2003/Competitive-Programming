#include <iostream>
#include <vector>
#include <set>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
#define ll long long

string mexRepeated(int n, ll k, const vector<int>& nums, int MEX) {
    int len = n + 1;
    // Build state array: first n elements from nums, then append MEX
    vector<int> state(len);
    for (int i = 0; i < n; i++)
        state[i] = nums[i];
    state[n] = MEX; // appended MEX
    // Calculate the effective right shift
    int r = k % len;
    // Build the result after cyclic right shift by r positions.
    // New array at index i is the old array at index (i - r + len) % len.
    string result = "";
    for (int i = 0; i < len - 1; i++) {
        int index = (i - r + len) % len;
        result += to_string(state[index]) + " ";
    }
    // Remove the trailing space if needed
    if (!result.empty() && result.back() == ' ')
        result.pop_back();
    return result;
}

int main() {
    fast;
    int t;
    cin >> t;
    while(t--) {
        int n;
        ll k;
        cin >> n >> k;
        vector<int> nums(n);
        // Use set to maintain sorted order so that the smallest missing number is first.
        set<int> mexSet;
        for (int i = 0; i <= n; i++)
            mexSet.insert(i);
        for (int i = 0; i < n; i++) {
            cin >> nums[i];
            mexSet.erase(nums[i]); // remove numbers that appear
        }
        // Get the MEX (smallest missing number)
        int MEX = *mexSet.begin();
        // Get the result after performing MEX repetition transformation
        cout << mexRepeated(n, k, nums, MEX) << "\n";
    }
    return 0;
}
