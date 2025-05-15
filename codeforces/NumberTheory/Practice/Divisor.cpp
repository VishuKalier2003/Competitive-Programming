#include <iostream>
#include <vector>
#include <unordered_set>
#include <algorithm>
#include <climits>
using namespace std;

#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int findSecondNumber(int n, vector<int>& nums, int firstNum) {
    unordered_set<int> firstDivisors;
    for(int i = 0; i < n; i++) {
        // IMP- If nums[i] divides firstNum and hasn't already been added (element not present)
        if(firstNum % nums[i] == 0 && firstDivisors.find(nums[i]) == firstDivisors.end()) {
            firstDivisors.insert(nums[i]);
            nums[i] = -1; // mark it
        }
    }
    // IMP- Find the maximum among unmarked values
    int secondNum = INT_MIN;
    for(int i = 0; i < n; i++) {
        if(nums[i] != -1)
            secondNum = max(secondNum, nums[i]);
    }
    return secondNum;
}

int main() {
    fast;
    int n;
    cin >> n;
    vector<int> nums(n);
    int firstNum = -1;
    for(int i = 0; i < n; i++) {
        cin >> nums[i];
        firstNum = max(firstNum, nums[i]);
    }
    int secondNum = findSecondNumber(n, nums, firstNum);
    cout << secondNum << " " << firstNum << "\n";

    return 0;
}
