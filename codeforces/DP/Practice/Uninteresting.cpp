#include <iostream>
#include <string>
#include <algorithm> // for std::min

using namespace std;

string isInteresting(const string &num) {
    long long sum = 0;
    int count2 = 0, count3 = 0;
    for (char digit : num) {
        int d = digit - '0';
        sum += d;
        if (digit == '2')
            count2++;
        else if (digit == '3')
            count3++;
    }

    // If the current sum is already divisible by 9
    if (sum % 9 == 0)
        return "Yes\n";

    // We only need to check up to 8 transformations for each digit type.
    for (int i = 0; i <= min(count2, 8); i++) {
        for (int j = 0; j <= min(count3, 8); j++) {
            // Upgrading a 2 increases sum by 2 (4 - 2)
            // Upgrading a 3 increases sum by 6 (9 - 3)
            if ((sum + i * 2 + j * 6) % 9 == 0)
                return "Yes\n";
        }
    }
    return "No\n";
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int t;
    cin >> t;
    string num;
    string result;
    while(t--) {
        cin >> num;
        result += isInteresting(num);
    }
    cout << result;
    return 0;
}
