大部分以 C++实现，绝大部分都是模板题目，汇总 **高频研发面试**专题。<br />每个专题中的题目都具备关联性，一点一点刷。<br />每道题目给出在LC的链接，题解看LC提供的，找自己能理解的才是最好的 Solution , 体会下题目的变种与联系。<br />


| 深度优先搜索 |
| --- |
| 广度优先搜索 |
| 链表 |
| 二叉树 |
| 排序 |
| 字符串 |
| 双指针 |
| 哈希 |
| 位运算 |
| 数组 |
| DP |
| 栈与队列 |
| 图论 |
| 并查集 |
| 线段树 |

<a name="CkcBm"></a>
# C++ 刷题模板
```cpp
#include <bits/stdc++.h>
#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/trie_policy.hpp>
using namespace std;
using ll = long long;
using db = long double;
using gragh = vector<vector<ll>>;
#define PI acos(-1)

struct Point {
  int x, y;
  Point(int _x, int _y) : x(_x), y(_y) {}
};
struct ListNode {
  /* data */
  int val;
  ListNode *next;
  ListNode() : val(0), next(nullptr) {}
  ListNode(int val) : val(val), next(nullptr) {}
  ListNode(int val, ListNode *next) : val(val), next(next) {}
};
struct TreeNode {
  int val;
  TreeNode *left;
  TreeNode *right;
  TreeNode(int val) : val(val), left(nullptr), right(nullptr) {}
};
struct DLinkedListNode {
  DLinkedListNode *next, *prev;
  int key, val;
  DLinkedListNode() : key(0), val(0), next(nullptr), prev(nullptr) {}
  DLinkedListNode(int key, int val)
      : key(key), val(val), next(nullptr), prev(nullptr) {}
};

template <typename T> inline void read(T &t) {
  int f = 0, c = getchar();
  t = 0;
  while (!isdigit(c))
    f |= c == '-', c = getchar();
  while (isdigit(c))
    t = t * 10 + c - 48, c = getchar();
  if (f)
    t = -t;
}

template <typename T> void print(T x) {
  if (x < 0)
    x = -x, putchar('-');
  if (x > 9)
    print(x / 10);
  putchar(x % 10 + 48);
}

class Solution {
public:
  void solve() {
    ll N;
    cin >> N;
    ll sum = (N + 1) * N;
    if (sum % 4 != 0) {
      cout << "NO" << endl;
    } else {
      cout << "YES" << endl;
      vector<ll> left, right;
      if ((N & 1) == 0) { // even
        cout << N / 2 << endl;
        // 8
        // YES
        // 4
        // 1 4 5 8
        // 4
        // 2 3 6 7

        int begin = 1;
        while (begin <= N) {
          if (begin == N) {
            cout << begin << endl;
            break;
          }
          cout << begin << " ";
          if (begin % 2 == 0) {
            begin += 1;
            cout << begin << " ";
          }
          if (begin % 2 != 0) {
            begin += 3;
          }
        }

        cout << N / 2 << endl;
        int start = 2;
        while (start <= N - 1) {
          if (start == N - 1) {
            cout << start << "\n";
            break;
          }
          cout << start << " ";
          if (start % 2 == 0) {
            start += 1;
            cout << start << " ";
          }
          if (start % 2 != 0) {
            start += 3;
          }
        }

      } else { 
        // odd
               // 7
               // YES
               // 4
               // 2 3 4 5
               // 3
               // 1 6 7
      }
    }
  }
};

int main() {
  ios_base::sync_with_stdio(false);
  cin.tie(NULL);

  Solution slu;
  slu.solve();
  return 0;
}
```
<a name="bJkYB"></a>
# 建议
目前面算法途径，如果是线上大部分是在牛客，建议大部分题目能做到在牛客编辑器中 流畅写出来，自己多训练几遍，避免面试过程中因忘记语法，紧张导致失误。
<a name="v8TCu"></a>
# 引用
| STL教程 | [http://c.biancheng.net/stl/](http://c.biancheng.net/stl/) |
| --- | --- |


<br />
<br />

