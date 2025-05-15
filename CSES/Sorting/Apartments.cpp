#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m, k;
    cin >> n >> m >> k;

    vector<int> people(n), apartments(m);
    for (int i = 0; i < n; ++i) {
        cin >> people[i];
    }
    for (int j = 0; j < m; ++j) {
        cin >> apartments[j];
    }

    sort(people.begin(), people.end());
    sort(apartments.begin(), apartments.end());

    int i = 0, j = 0, matches = 0;
    while (i < n && j < m) {
        int desired = people[i];
        int actual  = apartments[j];
        if (actual < desired - k) {
            // apartment too small
            ++j;
        } else if (actual > desired + k) {
            // apartment too large for this applicant
            ++i;
        } else {
            // found a match
            ++matches;
            ++i;
            ++j;
        }
    }

    cout << matches << '\n';
    return 0;
}
