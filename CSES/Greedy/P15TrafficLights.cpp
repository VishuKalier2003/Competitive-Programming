#include <iostream>
#include <set>
#include <map>
using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    int x, n;
    cin >> x >> n;
    
    set<int> lights;
    map<int, int> segments;

    lights.insert(0);
    lights.insert(x);
    segments[x] = 1;

    for (int i = 0; i < n; ++i) {
        int pos;
        cin >> pos;

        // Find the lights just before and after the new light
        auto rightIt = lights.lower_bound(pos);
        auto leftIt = prev(rightIt);

        int left = *leftIt;
        int right = *rightIt;

        int oldSegment = right - left;

        // Update segment frequency
        if (--segments[oldSegment] == 0) {
            segments.erase(oldSegment);
        }

        // Insert the new light
        lights.insert(pos);

        // Insert new segments formed by splitting
        segments[pos - left]++;
        segments[right - pos]++;

        // Output the largest segment
        cout << segments.rbegin()->first << " ";
    }

    cout << "\n";
    return 0;
}
