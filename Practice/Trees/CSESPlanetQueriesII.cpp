#include <bits/stdc++.h>
using namespace std;
using pii = pair<int,int>;

int main(){
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    int n, q;
    cin >> n >> q;
    vector<int> to(n+1);
    for(int i=1;i<=n;i++) cin>>to[i];
    vector<pair<int,int>> queries(q);
    int maxJump = 0;
    for(int i=0;i<q;i++){
        cin>>queries[i].first>>queries[i].second;
        maxJump = max(maxJump, queries[i].second);
    }
    
    // build reverse graph
    vector<vector<int>> rev(n+1);
    for(int i=1;i<=n;i++) rev[to[i]].push_back(i);

    // peel non-cycle nodes
    vector<int> indeg(n+1,0);
    deque<int> dq;
    for(int i=1;i<=n;i++) indeg[to[i]]++;
    for(int i=1;i<=n;i++) if(indeg[i]==0) dq.push_back(i);
    while(!dq.empty()){
        int u=dq.front(); dq.pop_front();
        int v = to[u];
        if(--indeg[v]==0) dq.push_back(v);
    }
    vector<int> cycle;
    for(int i=1;i<=n;i++) if(indeg[i]>0) cycle.push_back(i);

    // depth to cycle
    vector<int> depth(n+1,0);
    vector<char> seen(n+1,false);
    dq.clear();
    for(int u: cycle) { seen[u]=true; dq.push_back(u); }
    while(!dq.empty()){
        int v=dq.front(); dq.pop_front();
        for(int u: rev[v]){
            if(!seen[u]){
                seen[u]=true;
                depth[u] = depth[v]+1;
                dq.push_back(u);
            }
        }
    }

    // cycle components
    vector<int> comp(n+1), pos(n+1);
    vector<int> cycleLen;
    fill(seen.begin(), seen.end(), false);
    for(int u: cycle) if(!seen[u]){
        int cur=u, idx=0;
        while(!seen[cur]){
            seen[cur]=true;
            comp[cur] = (int)cycleLen.size();
            pos[cur] = idx++;
            cur = to[cur];
        }
        cycleLen.push_back(idx);
    }

    // binary lifting
    int LOG = 32 - __builtin_clz(maxJump);
    vector<vector<int>> up(LOG+1, vector<int>(n+1));
    for(int i=1;i<=n;i++) up[0][i]=to[i];
    for(int j=1;j<=LOG;j++){
        for(int i=1;i<=n;i++){
            up[j][i] = up[j-1][ up[j-1][i] ];
        }
    }

    auto liftNode = [&](int x, int d){
        for(int j=0;j<=LOG && x!=0;j++){
            if(d & (1<<j)) x = up[j][x];
        }
        return x;
    };

    // answer queries
    for(auto &pr: queries){
        int a = pr.first, b = pr.second;
        int ans;
        if(depth[b]>0){
            if(depth[a] < depth[b]) ans = -1;
            else{
                int diff = depth[a] - depth[b];
                int u = liftNode(a,diff);
                ans = (u==b? diff: -1);
            }
        } else {
            int da = depth[a];
            int u = liftNode(a, da);
            if(comp[u]!=comp[b]) ans = -1;
            else{
                int clen = cycleLen[ comp[b] ];
                int dp = (pos[b] - pos[u] + clen) % clen;
                ans = da + dp;
            }
        }
        cout<<ans<<"\n";
    }
    return 0;
}