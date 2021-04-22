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

using namespace std;
using ll = long long;
using db = long double;

#define PI acos(-1)

struct ListNode {
    int val;
    ListNode *next;

    ListNode() : val(0), next(nullptr) {}

    ListNode(int val) : val(val), next(nullptr) {}

    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

struct TreeNode {
    int val;
    struct TreeNode *left;
    struct TreeNode *right;

    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

struct DLinkedListNode {
    DLinkedListNode *next, *prev;
    int key, val;

    DLinkedListNode() : key(0), val(0), next(nullptr), prev(nullptr) {}

    DLinkedListNode(int key, int val) : key(key), val(val), next(nullptr), prev(nullptr) {}
};

class Solution {
public:
    ListNode* reverseKGroup(ListNode* head, int k) {

    }
};

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(0);
    Solution slu;
    string str = "helloworld";
    string str2 = str;
    str[0] = 'q';
    str2[0] = 'w';
    cout << str << endl;
    cout << str2 << endl;
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

