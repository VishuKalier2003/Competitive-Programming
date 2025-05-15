#include <iostream>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int main() {
    int t;
    cin >> t;
    while(t--) {
        int x;
        cin >> x;
        if(x % 2 == 0)
            cout << "No\n";
        else
            cout << "Yes\n";
    }
    return 0;
}
