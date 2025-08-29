#include <bits/stdc++.h>
using namespace std;

struct BIT {
    int n; vector<int> f;
    BIT(int n): n(n), f(n+1,0) {}
    void add(int i,int v){ for(; i<=n; i+=i&-i) f[i]+=v; }
    int sum(int i){ int s=0; for(; i>0; i-=i&-i) s+=f[i]; return s; }
    int rangeSum(int l,int r){ return sum(r)-sum(l-1); }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n,q; 
    if(!(cin>>n>>q)) return 0;
    vector<int> h(n+1);
    for(int i=1;i<=n;i++) cin>>h[i];

    // prev_ge[i] = nearest j<i with h[j] >= h[i]
    vector<int> prev_ge(n+1,0);
    stack<int> st;
    for(int i=1;i<=n;i++){
        while(!st.empty() && h[st.top()] < h[i]) st.pop(); // <  => keeps equal to block visibility
        prev_ge[i] = st.empty()? 0 : st.top();
        st.push(i);
    }

    struct Query { int a,b,idx; };
    vector<Query> qs(q);
    for(int i=0;i<q;i++){
        cin>>qs[i].a>>qs[i].b;
        qs[i].idx = i;
    }
    // Process a in increasing order so the predicate prev_ge[i] < a grows monotonically
    sort(qs.begin(), qs.end(), [](const Query& x, const Query& y){ return x.a < y.a; });

    // Buildings ordered by prev_ge ascending
    vector<int> ord(n); iota(ord.begin(), ord.end(), 1);
    sort(ord.begin(), ord.end(), [&](int i,int j){ return prev_ge[i] < prev_ge[j]; });

    BIT bit(n);
    vector<int> ans(q);
    int ptr = 0;
    for(const auto& qu : qs){
        int a = qu.a, b = qu.b;
        // Activate all buildings with prev_ge < a
        while(ptr < n && prev_ge[ord[ptr]] < a){
            bit.add(ord[ptr], 1);
            ++ptr;
        }
        ans[qu.idx] = bit.rangeSum(a, b);
    }

    for(int i=0;i<q;i++) cout << ans[i] << '\n';
    return 0;
}
