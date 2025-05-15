#include <iostream>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int main() {
    fast;
    int t;
    cin >> t;
    while(t--) {
        int n;
        string s, t;
        cin >> n;
        cin >> s >> t;
        bool flag = true;
        for(int i = 0; i < s.size() && s[i] == '0'; ++i)
            if(t[i] != '0') {
                cout << "No\n";
                flag = false;
                break;
            }
        if(flag)
            cout << "Yes\n";
    }
    return 0;
}
