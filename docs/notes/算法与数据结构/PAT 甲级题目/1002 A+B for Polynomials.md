# 1002 A+B for Polynomials

<a name="JQChy"></a>
## [1002 A+B for Polynomials](https://pintia.cn/problem-sets/994805342720868352/problems/994805526272000000)
<br />
```cpp
#include<bits/stdc++.h>

using namespace std;
#define FIO ios_base::sync_with_stdio(0);cin.tie(0);cout.tie(0);
const int N = 1005;
double a[N];

class Solution {
public:
    //1,009,999
    void slove() {
        int m, x;
        double y;
        cin >> m;
        for (int i = 0; i < m; ++i) {
            cin >> x;
            cin >> y;
            a[x] += y;
        }
        cin >> m;
        for (int j = 0; j < m; ++j) {
            cin >> x;
            cin >> y;
            a[x] += y;
        }
        int count = 0;
        for (int k = 0; k < 1001; ++k) {
            if (a[k] != 0) {
                count += 1;
            }
        }
        cout << count;
        for (int l = 1001; l >= 0; l--) {
            if (a[l] != 0) {
                //cout << " " << l << " " << a[l];
                 printf(" %d %.1f", l, a[l]);
            }
        }
    }
};

int main() {
    FIO;
    Solution sl;
    sl.slove();
}

```
