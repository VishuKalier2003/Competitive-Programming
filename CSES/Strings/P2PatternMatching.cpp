#include <iostream>
#include <string>
#include <vector>
using namespace std;
#define fast ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);

// Compute KMP prefix function (also known as failure function or LPS array)
vector<int> computeKMPPrefix(const string& pattern) {
    int m = pattern.length();
    vector<int> prefix(m, 0);
    
    for (int i = 1, k = 0; i < m; i++) {
        while (k > 0 && pattern[k] != pattern[i]) {
            k = prefix[k - 1];
        }
        
        if (pattern[k] == pattern[i]) {
            k++;
        }
        
        prefix[i] = k;
    }
    
    return prefix;
}

void solve(const string& text, const string& pattern) {
    int t = text.length(), p = pattern.length();
    if (p > t) {
        cout << 0 << endl;
        return;
    }

    // Compute KMP prefix function for pattern
    vector<int> prefix = computeKMPPrefix(pattern);
    
    int count = 0;
    int j = 0;  // Number of characters matched in pattern
    
    // Traverse the text string
    for (int i = 0; i < t; i++) {
        // If there's a mismatch, use the prefix function to find the next matching position
        while (j > 0 && text[i] != pattern[j]) {
            j = prefix[j - 1];
        }
        
        // If current characters match, advance j
        if (text[i] == pattern[j]) {
            j++;
        }
        
        // If we've matched the entire pattern, we've found an occurrence
        if (j == p) {
            count++;
            j = prefix[j - 1];  // Look for the next match
        }
    }
    
    cout << count;
}

int main() {
    fast;
    string s1, s2;
    cin >> s1 >> s2;
    solve(s1, s2);
}