# 1001 A+B Format

<a name="eeWV1"></a>
## [1001 A+B Format](https://pintia.cn/problem-sets/994805342720868352/problems/994805528788582400)
```cpp
#include<bits/stdc++.h>

using namespace std;
#define FIO ios_base::sync_with_stdio(0);cin.tie(0);cout.tie(0);

class Solution {
public:
    //1,009,999
    void slove(int &a, int &b) {
        int res = a + b;
        if (res < 0) {
            cout << "-";
            res = 0 - res;
        }
        string str = to_string(res);
        int len = str.size();
        for (int i = 0; i < len; ++i) {
            cout << str[i];
            if ((i + 1) % 3 == len % 3 && i != len - 1) {
                cout << ",";
            }
        }
    }
};

int main() {
    FIO;
    int a, b;
    Solution sl;
    cin >> a >> b;
    sl.slove(a, b);
}

```
