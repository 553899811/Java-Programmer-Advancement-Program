<!-- GFM-TOC -->

* [HashMap介绍](#hashmap介绍)
    * [1. HashMap 简介](#1-hashmap-简介)
       * [1.1 实现细节](#11-实现细节)
         * [1.1.1 实现和继承关系](#111-实现和继承关系) 
         * [1.1.2 底层实现](#112-底层实现)
           * [1.1.2.1 哈希表原理](#1121-哈希表原理)
           * [1.1.2.2 底层实现](#1122-底层实现)
             * [1.1.2.2.1 基本面貌](#11221-基本面貌)
             * [1.1.2.2.2 基本组成成员](#11222-基本组成成员)
             * [1.1.2.2.3 构造器](#11223-构造器)
           * [1.1.2.3 基本操作](#1123-基本操作)
             * [1.1.2.3.1 put()方法](#11231-put方法)
             * [1.1.2.3.2 get()方法](#11232-get方法)
    * [2. HashMap 高级特性](#2-hashmap-高级特性)
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

   
### 1.1.2 底层实现
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
![](https://images2015.cnblogs.com/blog/1024555/201611/1024555-20161113235348670-746615111.png)
```
  简单来说，HashMap由数组+链表组成的，数组是HashMap的主体，链表则是主要为了解决哈希冲突而存在的，
  如果定位到的数组位置不含链表（当前entry的next指向null）,那么对于查找，添加等操作很快，仅需一次寻址即可；如果定位到的数组包含链表，
  对于添加操作，其时间复杂度依然为O(1)，因为最新的Entry会插入链表头部，因为最近被添加的元素被查找的可能性更高，
  而对于查找操作来讲，此时就需要遍历链表，然后通过key对象的equals方法逐一比对查找。
  所以，性能考虑，HashMap中的链表出现越少，性能才会越好；
```
##### 1.1.2.2.2 基本组成成员
 - Node
```
    //基本节点
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
 - 传入参数Map
```
  没使用过,所以不知道;
      /**
     * Constructs a new <tt>HashMap</tt> with the same mappings as the
     * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
     * default load factor (0.75) and an initial capacity sufficient to
     * hold the mappings in the specified <tt>Map</tt>.
     *
     * @param   m the map whose mappings are to be placed in this map
     * @throws  NullPointerException if the specified map is null
     */
    public HashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }
```
 - 传入参数: 指定数组长度
```
   /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
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
    在常规构造器中，没有为数组table分配内存空间(有一个入参为指定Map的构造器例外),
    而是在执行put操作的时候才真正构建table数组;
```
#### 1.1.2.3 基本操作
##### 1.1.2.3.1 put()方法
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
##### 1.1.2.3.2 get()方法
![](https://static.oschina.net/uploads/img/201612/28165110_Qgbu.png)
 - 过程分析:
   - 根据Key计算出hash值;
   - 桶位置index =hash & (len-1)
     - 如果要找的key就是上述数组index位置的元素,直接返回该元素的值;
     - 如果该数组index位置元素是TreeNode类型,则按照红黑树的查询方式来进行查询;
     - 如果该数组index位置元素非TreeNode类型,则按照链表的方式来进行遍历查询;
       
## 2. HashMap 高级特性
### 2.1 线程安全问题
