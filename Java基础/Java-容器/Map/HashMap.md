<!-- GFM-TOC -->

* [HashMap介绍](#hashmap介绍)
    * [1. HashMap 简介](#1-hashmap-简介)
       * [1.1 实现细节](#11-实现细节)
         * [1.1.1 实现和继承关系](#111-实现和继承关系) 
         * [1.1.2 HashMap引入](#112-hashmap引入)
           * [1.1.2.1 哈希表原理](#1121-哈希表原理)
           * [1.1.2.2 底层实现](#1122-底层实现)
             * [1.1.2.2.1 基本面貌](#11221-基本面貌)
             * [1.1.2.2.2 基本组成成员](#11222-基本组成成员)
             * [1.1.2.2.3 构造器](#11223-构造器)
           * [1.1.2.3 基本操作](#1123-基本操作)
             * [1.1.2.3.1 如何获取Hash值](#11231-如何获取hash值)
             * [1.1.2.3.2 获取桶索引位置](#11232-获取桶索引位置)
             * [1.1.2.3.3 put()方法](#11233-put方法)
             * [1.1.2.3.4 get()方法](#11234-get方法)
    * [2. HashMap 高级特性](#2-hashmap-高级特性)
      * [2.1 扩容机制](#21-扩容机制) 
<!-- GFM-TOC -->

# HashMap介绍
## 1. HashMap 简介
## 1.1 实现细节
```
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable 
```
### 1.1.1 实现和继承关系
   - **继承** 抽象类**AbstractMap**,提供了 Map 的基本实现，使得我们以后要实现一个 Map 不用从头开始，只需要继承 AbstractMap, 然后按需求实现/重写对应方法即可;
   - **实现** Map接口,Map是"key-value键值对"接口;
   - **实现** Cloneable接口,即覆盖了clone()方法,能被克隆;
   - **实现** java.io.Serializable 接口,支持序列化,能通过序列化去传输;

   
### 1.1.2 HashMap引入
#### 1.1.2.1 哈希表原理
 - **哈希表**
```
  [1]我们知道在数组中根据下标查找某个元素,一次定位就可以达到,哈希表利用了这个特性,哈希表的主干就是数组;
  [2]比如我们要新增或查找某个元素,我们通过把当前元素的关键字通过某个函数映射到数组的某一个位置,通过数组下标一次定位就可完成操作;
```
    存储位置 = f(关键字)
    其中这个函数f()一般就称之为【哈希函数】,这个函数的设计好坏会直接影响到哈希表的优劣.举个栗子,比如我们要在哈希表中执行插入操作:
![](https://images2015.cnblogs.com/blog/1024555/201611/1024555-20161113180447499-1953916974.png)
    查找操作:同理,先通过哈希函数计算 出实际存储地址,然后从数组中对应位置取出即可;
 - **哈希冲突**
```
  如果两个不同的元素，通过哈希函数得出的实际存储地址相同怎么办？
  也就是说，当我们对某个元素进行哈希运算，得到一个存储地址，然后要进行插入的时候，发现已经被其他元素占用了，
  其实这就是所谓的哈希冲突，也叫哈希碰撞。
  
  解决hash冲突的方法:
  [1]开放地址法(发生冲突，继续寻找下一块未被占用的存储地址)
  [2]再散列函数法
  [3]链地址法(HashMap采用的就是这种方法)
```
#### 1.1.2.2 底层实现
##### 1.1.2.2.1 基本面貌
```
  HashMap采用链地址法,大概长下面的样子
```
 - 横着看
![](https://github.com/CyC2018/Interview-Notebook/raw/3f1469ab746c2d7a48fce074dc09cf2ecade3152/pics/b9a39d2a-618c-468b-86db-2e851f1a0057.jpg)

 - 竖着看
![](http://tech.meituan.com/img/java-hashmap/hashMap%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84%E5%9B%BE.png)
```
  简单来说，HashMap由数组+链表组成的，数组是HashMap的主体，链表则是主要为了解决哈希冲突而存在的，
  如果定位到的数组位置不含链表（当前entry的next指向null）,那么对于查找，添加等操作很快，仅需一次寻址即可；如果定位到的数组包含链表，
  对于添加操作，其时间复杂度依然为O(1)，因为最新的Entry会插入链表头部，因为最近被添加的元素被查找的可能性更高，
  而对于查找操作来讲，此时就需要遍历链表，然后通过key对象的equals方法逐一比对查找。
  所以，性能考虑，HashMap中的链表出现越少，性能才会越好；
```
##### 1.1.2.2.2 基本组成成员
 - table
```
  // 我们称之为 桶(数组),初始化为16个;
  transient Node<K,V>[] table;
```
 - Node
```
    //基本节点(构成链表的基本元素)
    // hash 当前节点 hash值;
    // key 当前节点的 key;
    // value 当前节点 value;
    // next 当前节点的下一个指向;
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
    }
```
 - 重要属性:
```
 // 初始化HashMap数组的长度
 static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
 // 最大数组的长度
 static final int MAXIMUM_CAPACITY = 1 << 30;
 // 默认负载因子为0.75,代表了table的填充度有多少;
 static final float DEFAULT_LOAD_FACTOR = 0.75f;
 //用于快速失败，由于HashMap非线程安全，在对HashMap进行迭代时，如果期间其他线程的参与导致HashMap的结构发生变化了（比如put，remove等操作），需要抛出异常ConcurrentModificationException
 transient int modCount;
 //实际存储的key-value键值对的个数
 transient int size;
 //当add一个元素到某一个桶位置时,若链表长度达到8就会将链表转化为红黑树;
  static final int TREEIFY_THRESHOLD = 8;
  //当某一个桶位置上的链表长度退减为6时,由红黑树转化为链表;
  static final int UNTREEIFY_THRESHOLD = 6;
 //阈值，当table == {}时，该值为初始容量（初始容量默认为16）；当table被填充了，也就是为table分配内存空间后，threshold一般为 capacity*loadFactory。HashMap在进行扩容时需要参考threshold，后面会详细谈到
  int threshold;

```
##### 1.1.2.2.3 构造器
 - 没有传入参数
```
    // 默认构造器,负载因子为0.75;
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
```
 - 传入参数: 指定数组长度,指定负载因子
```
  /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and load factor.
     *
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                             initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }
    
```
   <font color="red">**tableSizeFor方法**</font>
```
    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    功能: tableSizeFor的功能（不考虑大于最大容量的情况）
    是返回大于输入参数且最近的2的整数次幂的数;
    
    为什么上述的位运算能得到大于参数且最近的2的整数次幂的数呢?
    
```

  <font color="red">对于这个构造函数,主要讲两点:</font>

    [1]虽然说是自定义initialCapacity,但是实际上并不是你所想的那样的任意数组长度;
    经过tableSizeFor方法的处理之后,会得到一个大于输入参数且最近的2的整数次幂的数;
    比如你输入一个10,得到的是初始化数组长度为16的HashMap;
    
    [2]在常规构造器中，没有为数组table分配内存空间(有一个入参为指定Map的构造器例外),
    而是在执行put操作的时候才真正构建table数组;

#### 1.1.2.3 基本操作
##### 1.1.2.3.1 如何获取Hash值
 - 扰动函数
```
  static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }
```
  <font color="red">**扰动函数解析:**</font>
```
  上述代码中key.hashCode()函数调用的是超类Object类中的哈希函数,JVM进而调用本地方法,返回一个int值;
  理论上是一个int类型的值,范围为-2147483648-2147483647,
  前后加起来大概有40亿的映射空间,的确这么大的空间,映射会很均匀
  但是这么大的数组,内存中是放不下的。HashMap的初始化数组只有16
  ,所以这个散列值是无法直接拿来用的;
  
  扰动函数的结果是hashCode中高位和地位的复合产物;
```
##### 1.1.2.3.2 获取桶索引位置
```
  JDK 1.8中是直接通过p = tab[i = (n - 1) & hash 这个方法来确定桶索引位置的;
  [1]hash为扰动函数的产物;
  [2]n-1位当前hashmap的数组个数.
  
  此时,我们会有诸多疑问,比如:
  【1】为什么是 n-1 呢? 
  
   答: 源头就是HashMap的数组长度为什么要取2的整数幂? 因为这样(n-1)
   相当于一个"低位掩码"; 
   这样能结合下面的&运算使散列更为均匀(相对其他数字来说,比如10的话)
    
    和15做    和10做      和15做    和10做
   00100101  00100101    00110001  00110001
   00001111  00001010    00001111  00001010
   &-------  --------    --------  --------
   00000101  00000000    00000001  00000000
   
   我们发现两个不同的数00100101 和0011001 分别和15以及10做与运算,
   和15做&运算会得到两个不同的结果,而和10做与运算得到的是相同的结果,这就会出现冲突!
  
  【2】为什么要做&运算呢?
   答: 与操作的结果就是高位全部归零,只保留低位值,用来做数组下标访问。举一个例子说明,初始化长度为16,16-1=15 . 
   2进制(4字节,32位)表示就是 00000000 00000000 00000000 00001111 
   和 某散列值做与操作结果就是
   00000000 10100101 11000100 00100101
   00000000 00000000 00000000 00001111
 & -------------------------------------
   00000000 00000000 00000000 00000101 //高位全部归零,只保留尾部四位
   
   
  【3】为什么扰动函数是那样子的呢?
  
```
##### 1.1.2.3.3 put()方法
 - put()方法
```
    [1]调用put方法放入key-value键值对;
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
```
```
   [2] putVal()过程
```
![](https://static.oschina.net/uploads/img/201612/28154542_9bV6.png)
  - 分析put过程:
    - 根据Key值计算Hash值;
    - 然后计算在数组中的索引位置,index=(n-1) & hash 
      - 如果该index位置的Node元素不存在,则直接创建一个新的Node; 
      - 如果该index位置的Node元素是TreeNode类型即是红黑树类型了,则直接按照红黑树的插入方式进行插入;
      - 如果该index位置的Node元素是非TreeNode类型,则按照链表的形式进行插入操作.链表插入操作完成后,判断是否超过阈值TREEIFY_THRESHOLD(默认是8),超过则要么数组扩容,要么链表转化为红黑树结构;
    - 判断当前总容量是否超过阈值,如果超过则执行扩容;
```
   [1]这里重点说明下,在JDK1.7中index = hash % (len-1) 做与运算 ,在JDK 1.8中 变为了 i =(n-1) & hash,两者的作用是相同的;
   [2]Put时如果key为null，存储位置为table[0]或table[0]的冲突链上(table为HashMap中存的数组),
   如果该对应数据已存在，执行覆盖操作。用新value替换旧value，并返回旧value，如果对应数据不存在,则添加到链表的头上(保证插入O(1));
    put：首先判断key是否为null，若为null，则直接调用putForNullKey方法。若不为空则先计算key的hash值，
    然后根据hash值搜索在table数组中的索引位置，如果table数组在该位置处有元素，循环遍历链表，比较是否存在相同的key，
    若存在则覆盖原来key的value，否则将该元素保存在链头（最先保存的元素放在链尾）。若table在该处没有元素，则直接保存。
```
##### 1.1.2.3.4 get()方法
![](https://static.oschina.net/uploads/img/201612/28165110_Qgbu.png)
 - 过程分析:
   - 根据Key计算出hash值;
   - 桶位置index =hash & (len-1)
     - 如果要找的key就是上述数组index位置的元素,直接返回该元素的值;
     - 如果该数组index位置元素是TreeNode类型,则按照红黑树的查询方式来进行查询;
     - 如果该数组index位置元素非TreeNode类型,则按照链表的方式来进行遍历查询;
       
## 2. HashMap 高级特性
### 2.1 扩容机制
