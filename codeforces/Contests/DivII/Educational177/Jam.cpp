#include <iostream>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
#define ll long long

int main() {
    fast;
    int t;
    cin >> t;
    while(t--) {
        ll n;
        cin >> n;
        cout << (2*n) << "\n";
    }
    return 0;
}

/*
7
5 3 10
3 4 2 1 5
15 97623 1300111
105 95 108 111 118 101 95 118 97 108 111 114 97 110 116
1 100000 1234567891011
1
1 1 1
1
1 1 1
2
2 1 2
1 1
2 1 5
2 1
 */
