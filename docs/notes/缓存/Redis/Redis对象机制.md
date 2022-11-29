![](https://cdn.nlark.com/yuque/0/2021/png/177460/1621091432663-a311f78e-addc-418f-92c2-c3056d31abfc.png#averageHue=%23f2ede3&crop=0&crop=0&crop=1&crop=1&height=1128&id=afZCE&originHeight=1128&originWidth=1938&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=1938)
<a name="ZXdAe"></a>
# 1.为什么Redis会设计redisObject对象？
[<br />](https://www.pdai.tech/md/db/nosql-redis/db-redis-x-redis-object.html)<br />在redis的命令中，用于对键进行处理的命令占了很大一部分，而对于键所保存的值的类型（键的类型），键能执行的命令又各不相同。如： LPUSH 和 LLEN 只能用于列表键, 而 SADD 和 SRANDMEMBER 只能用于集合键, 等等; 另外一些命令, 比如 DEL、 TTL 和 TYPE, 可以用于任何类型的键；但是要正确实现这些命令, 必须为不同类型的键设置不同的处理方式: 比如说, 删除一个列表键和删除一个字符串键的操作过程就不太一样。<br />以上的描述说明, **Redis 必须让每个键都带有类型信息, 使得程序可以检查键的类型, 并为它选择合适的处理方式**.<br />比如说， 集合类型就可以由字典和整数集合两种不同的数据结构实现， 但是， 当用户执行 ZADD 命令时， 他/她应该不必关心集合使用的是什么编码， 只要 Redis 能按照 ZADD 命令的指示， 将新元素添加到集合就可以了。<br />这说明, **操作数据类型的命令除了要对键的类型进行检查之外, 还需要根据数据类型的不同编码进行多态处理**.

<a name="safVW"></a>
# 2.redisObjest数据结构
```objectivec
/*
 * Redis 对象
 */
typedef struct redisObject {

    // 类型
    unsigned type:4;

    // 编码方式
    unsigned encoding:4;

    // LRU - 24位, 记录最末一次访问时间（相对于lru_clock）; 或者 LFU（最少使用的数据：8位频率，16位访问时间）
    unsigned lru:LRU_BITS; // LRU_BITS: 24

    // 引用计数
    int refcount;

    // 指向底层数据结构实例
    void *ptr;

} robj;

```
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1632913925951-e6927e54-1ee9-483f-905d-48f32dfff707.png#averageHue=%23fbfae5&clientId=u3e45a780-e1e1-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=uc52d352b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=337&originWidth=680&originalType=url&ratio=1&rotation=0&showTitle=false&size=12253&status=done&style=none&taskId=u6998b860-33f2-4cc7-9a6c-8c5345111f0&title=)
<a name="hCPSC"></a>
## 2.1 type
**type记录了对象所保存的值的类型**
```objectivec
/*
* 对象类型
*/
#define OBJ_STRING 0 // 字符串
#define OBJ_LIST 1 // 列表
#define OBJ_SET 2 // 集合
#define OBJ_ZSET 3 // 有序集
#define OBJ_HASH 4 // 哈希表

```
<a name="oGseE"></a>
## 2.2 encoding
**记录了对象所保存的值的编码**
```objectivec
/*
* 对象编码
*/
#define OBJ_ENCODING_RAW 0     /* Raw representation */
#define OBJ_ENCODING_INT 1     /* Encoded as integer */
#define OBJ_ENCODING_HT 2      /* Encoded as hash table */
#define OBJ_ENCODING_ZIPMAP 3  /* 注意：版本2.6后不再使用. */
#define OBJ_ENCODING_LINKEDLIST 4 /* 注意：不再使用了，旧版本2.x中String的底层之一. */
#define OBJ_ENCODING_ZIPLIST 5 /* Encoded as ziplist */
#define OBJ_ENCODING_INTSET 6  /* Encoded as intset */
#define OBJ_ENCODING_SKIPLIST 7  /* Encoded as skiplist */
#define OBJ_ENCODING_EMBSTR 8  /* Embedded sds string encoding */
#define OBJ_ENCODING_QUICKLIST 9 /* Encoded as linked list of ziplists */
#define OBJ_ENCODING_STREAM 10 /* Encoded as a radix tree of listpacks */

```
<a name="vww6a"></a>
## 2.3 ptr 指针
[<br />](https://www.pdai.tech/md/db/nosql-redis/db-redis-x-redis-object.html)<br />**ptr是一个指针，指向实际保存值的数据结构**，这个数据结构由type和encoding属性决定。举个例子， 如果一个redisObject 的type 属性为OBJ_LIST ， encoding 属性为OBJ_ENCODING_QUICKLIST ，那么这个对象就是一个Redis 列表（List)，它的值保存在一个QuickList的数据结构内，而ptr 指针就指向quicklist的对象；
