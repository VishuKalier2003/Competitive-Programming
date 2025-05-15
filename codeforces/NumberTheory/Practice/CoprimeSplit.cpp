#include <iostream>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

int main() {
    fast;
    int t;
    cin >> t;
    while(t--) {
        int l, r;
        cin >> l >> r;
        if(r <= 3)      // when r <= 3, then not at all possible
            cout << "-1\n";
        else {
            if(l == r) {
            bool flag = false;      //  Flag to ensure a factor is found
            for(int i = 2; i*i <= l; i++)       // IMP- checking if l is prime
                if(l % i == 0) {        // If not prime, then we can simply create two numbers as l and l-i
                    cout << (l-i) << " " << i <<"\n";
                    flag = true;
                    break;
                }
            if(!flag)
                cout << "-1\n";
            }
            else {
                int largestEven = r-(r%2);        // Finding largest even lesser than or equal to r
                cout << (largestEven/2) << " " << (largestEven/2) << "\n";      // Simply divide it by 2 to get the two even numbers
            }
        }
    }
    return 0;
}
