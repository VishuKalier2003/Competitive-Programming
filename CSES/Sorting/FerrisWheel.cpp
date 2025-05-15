#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int main() {
    fast;
    int n, x;
    cin >> n >> x;
    vector<int> nums(n);
    for(int i = 0; i < n; i++)
        cin >> nums[i];
    sort(nums.begin(), nums.end());
    int left = 0, right = n-1, result = 0;
    while(left <= right) {
        if(left <= right && nums[left]+nums[right] <= x) {
            left++;
            right--;
        } else
            right--;
        result++;
    }
    cout << result;
    return 0;
}
