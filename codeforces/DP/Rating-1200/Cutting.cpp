#include <iostream>
#include <vector>
#include <queue>
#include <cmath>

using namespace std;

int maxCuts(const int n, const int totalOdd, const int totalEven, const vector<int>& nums, int bitcoin) {
    priority_queue<int, vector<int>, greater<int>> cutCostHeap;  // Min-heap
    int segmentOdd = 0, segmentEven = 0, cuts = 0;
    for (int i = 0; i < n; i++) {
        // IMP- When a valid cut is found we will take the ith element into consideration for the next segment (the next segment starts at i)
        if (segmentEven == segmentOdd && segmentOdd > 0) {
            cutCostHeap.push(abs(nums[i] - nums[i - 1]));
            segmentEven = segmentOdd = 0;
        }
        // We want to take i-th element into consideration
        if (nums[i] % 2 == 0)
            segmentEven++;
        else
            segmentOdd++;
    }

    // IMP- Find the top smallest cuts, which when summed are less than or equal to bitcoin
    while (!cutCostHeap.empty()) {
        if (bitcoin - cutCostHeap.top() < 0)
            break;  // When exceeds break
        bitcoin -= cutCostHeap.top();
        cutCostHeap.pop();
        cuts++;
    }
    return cuts;
}

int main() {
    int n, bitcoin;
    cin >> n >> bitcoin;
    vector<int> nums(n);
    for (int i = 0; i < n; ++i)
        cin >> nums[i];

    // Total even and odd cuts, will be n/2 of the array size
    cout << maxCuts(n, n / 2, n / 2, nums, bitcoin) << endl;

    return 0;
}
