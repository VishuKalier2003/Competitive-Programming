#include <bits/stdc++.h>
using namespace std;
using ll = long long;

struct SegmentTree {
    int N;
    vector<ll> sum, maxPref;

    SegmentTree(const vector<ll>& a) {
        int n = a.size();
        N = 1;
        while (N < n) N <<= 1;
        sum.assign(2*N, 0);
        maxPref.assign(2*N, 0);
        // build leaves
        for (int i = 0; i < n; i++) {
            sum[N + i]     = a[i];
            maxPref[N + i] = max(0LL, a[i]);
        }
        // build internals
        for (int v = N-1; v > 0; v--) {
            ll ls = sum[2*v],   rs = sum[2*v+1];
            ll lp = maxPref[2*v], rp = maxPref[2*v+1];
            sum[v]     = ls + rs;
            maxPref[v] = max(lp, ls + rp);
        }
    }

    // point update: set position pos (1-based) to value val
    void update(int pos, ll val) {
        int v = N + pos - 1;
        sum[v]     = val;
        maxPref[v] = max(0LL, val);
        v >>= 1;
        while (v > 0) {
            ll ls = sum[2*v],   rs = sum[2*v+1];
            ll lp = maxPref[2*v], rp = maxPref[2*v+1];
            sum[v]     = ls + rs;
            maxPref[v] = max(lp, ls + rp);
            v >>= 1;
        }
    }

    // range query [l..r], returns maximum prefix sum
    ll query(int l, int r) {
        ll leftSum = 0, leftPref = 0;
        ll rightSum = 0, rightPref = 0;
        int L = N + l - 1, R = N + r - 1;
        while (L <= R) {
            if (L & 1) {
                ll sv = sum[L], pv = maxPref[L];
                leftPref = max(leftPref, leftSum + pv);
                leftSum  = leftSum + sv;
                L++;
            }
            if (!(R & 1)) {
                ll sv = sum[R], pv = maxPref[R];
                rightPref = max(pv, sv + rightPref);
                rightSum  = sv + rightSum;
                R--;
            }
            L >>= 1;
            R >>= 1;
        }
        return max(leftPref, leftSum + rightPref);
    }
};

int main(){
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    int n, q;
    cin >> n >> q;
    vector<ll> nums(n);
    for(int i = 0; i < n; i++){
        cin >> nums[i];
    }

    SegmentTree st(nums);

    ostringstream oss;
    while(q--){
        int type, x, y;
        cin >> type >> x >> y;
        if(type == 1){
            st.update(x, y);
        } else {
            ll ans = st.query(x, y);
            oss << ans << '\n';
        }
    }
    cout << oss.str();
    return 0;
}
