<!-- GFM-TOC -->

* [HashMap](#hashmap)
    * [1. HashMap 简介](#1-hashmap-简介)
       * [1.1 类定义](#11-类定义)
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
        * [2.1.1 扩容后位置变化](#211-扩容后位置变化)
        * [2.1.2 HashMap扩容为啥是2倍扩展](#212-hashmap扩容为啥是2倍扩展) 
      * [2.2 负载因子为什么是0.75](#22-负载因子为什么是0.75)  
      * [2.3 线程安全问题](#23-线程安全问题)
        * [2.3.1 线程不安全的表现](#231-线程不安全的表现)
        * [2.3.2 源码分析](#232-源码分析)
          * [2.3.2.1 扩容后节点顺序](#2321-扩容后节点顺序)
          * [2.3.2.2 JDK1.7死环现象分析](#2322-jdk1.7死环现象分析)
            * [2.3.2.2.1 扩容后节点顺序反向存储](#23221-扩容后节点顺序反向存储)
          * [2.3.2.3 JDK1.8扩容过程分析及数据丢失](#2323-jdk1.8扩容过程分析及数据丢失)
      * [2.4 树状化存储结构](#24-树状化存储结构)
<!-- GFM-TOC -->






# HashMap



<a name="cf4deb37"></a>
## 1. 什么是Hash
<br />
<a name="vTRnw"></a>
## 2. HashMap 简介


<a name="fa7b4e14"></a>
### 2.1 类定义


```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable
```


<a name="bfc6520d"></a>
### 2.1.1 实现和继承关系


- **继承**  抽象类**AbstractMap**,提供了 Map 的基本实现，使得我们以后要实现一个 Map 不用从头开始，只需要继承 AbstractMap, 然后按需求实现/重写对应方法即可;
- **实现**  Map接口,Map是"key-value键值对"接口;
- **实现**  Cloneable接口,即覆盖了clone()方法,能被克隆;
- **实现**  java.io.Serializable 接口,支持序列化,能通过序列化去传输;



<a name="jDfpu"></a>
### 1.1.2 HashMap引入


<a name="ac862159"></a>
#### 1.1.2.1 哈希表原理


- **哈希表**



> [1]我们知道在数组中根据下标查找某个元素,一次定位就可以达到,哈希表利用了这个特性,哈希表的主干就是数组;
> 

> [2]比如我们要新增或查找某个元素,我们通过把当前元素的关键字通过某个函数映射到数组的某一个位置,通过数组下标一次定位就可完成操作;



> 存储位置 = f(关键字)
> 其中这个函数f()一般就称之为【哈希函数】,这个函数的设计好坏会直接影响到哈希表的优劣.举个栗子,比如我们要在哈希表中执行插入操作:
> ![hash.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002469233-182b06a6-70da-4def-96dc-71685778392d.png#align=left&display=inline&height=864&margin=%5Bobject%20Object%5D&name=hash.png&originHeight=864&originWidth=1362&size=81064&status=done&style=none&width=1362)
> 查找操作:同理,先通过哈希函数计算 出实际存储地址,然后从数组中对应位置取出即可;

```
-  **哈希冲突**
```


> 如果两个不同的元素，通过哈希函数得出的实际存储地址相同怎么办？
> 也就是说，当我们对某个元素进行哈希运算，得到一个存储地址，然后要进行插入的时候，发现已经被其他元素占用了，
> 其实这就是所谓的哈希冲突，也叫哈希碰撞。



> 解决hash冲突的方法:
> 

> [1]开放地址法(发生冲突，继续寻找下一块未被占用的存储地址)
> 

> [2]再散列函数法
> 

> [3]链地址法(HashMap采用的就是这种方法)



<a name="ef9b5aa8"></a>
#### 1.1.2.2 底层实现


<a name="b0a8aee2"></a>
##### 1.1.2.2.1 基本面貌


```
  HashMap采用链地址法,大概长下面的样子
```


- 横着看


<br />![hashmap-横着看.jpg](https://cdn.nlark.com/yuque/0/2020/jpeg/177460/1581002719429-d3decf0c-22d3-459c-bb0b-aa65e791f973.jpeg#align=left&display=inline&height=517&margin=%5Bobject%20Object%5D&name=hashmap-%E6%A8%AA%E7%9D%80%E7%9C%8B.jpg&originHeight=517&originWidth=1080&size=115795&status=done&style=none&width=1080)<br />

- 竖着看


<br />![hashMap-竖着看.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002726434-e8662c6a-2e5e-4045-85e1-f11a7e0a45d0.png#align=left&display=inline&height=734&margin=%5Bobject%20Object%5D&name=hashMap-%E7%AB%96%E7%9D%80%E7%9C%8B.png&originHeight=734&originWidth=970&size=74114&status=done&style=none&width=970)<br />

> 简单来说，HashMap由数组+链表组成的，数组是HashMap的主体，链表则是主要为了解决哈希冲突而存在的，
> 如果定位到的数组位置不含链表（当前entry的next指向null）,那么对于查找，添加等操作很快，仅需一次寻址即可；如果定位到的数组包含链表，
> 对于添加操作，其时间复杂度依然为O(1)，因为最新的Entry会插入链表头部，因为最近被添加的元素被查找的可能性更高，
> 而对于查找操作来讲，此时就需要遍历链表，然后通过key对象的equals方法逐一比对查找。
> 所以，性能考虑，HashMap中的链表出现越少，性能才会越好；



<a name="9630ff71"></a>
##### 1.1.2.2.2 基本组成成员


- table



```java
  // 我们称之为 桶(数组),初始化为16个;
  transient Node<K,V>[] table;
```


- Node



```java
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



```java
 // 初始化HashMap数组的长度
 static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
 // 最大数组的长度
 static final int MAXIMUM_CAPACITY = 1 << 30;
 // 默认负载因子为0.75,代表了table的填充度有多少;
 static final float DEFAULT_LOAD_FACTOR = 0.75f;
 //用于快速失败，由于HashMap非线程安全，在对HashMap进行迭代时，
 //如果期间其他线程的参与导致HashMap的结构发生变化了（比如put，remove等操作），
 //需要抛出异常ConcurrentModificationException
 transient int modCount;
 //实际存储的key-value键值对的个数
 transient int size;
 //当add一个元素到某一个桶位置时,若链表长度达到8就会将链表转化为红黑树;
  static final int TREEIFY_THRESHOLD = 8;
  //当某一个桶位置上的链表长度退减为6时,由红黑树转化为链表;
  static final int UNTREEIFY_THRESHOLD = 6;
 //阈值，当table == {}时，该值为初始容量（初始容量默认为16）；
 //当table被填充了，也就是为table分配内存空间后，threshold一般为 capacity*loadFactory。
 //HashMap在进行扩容时需要参考threshold，后面会详细谈到
  int threshold;
```
| 变量名 | 变量初始值 | 变量值含义 |
| --- | --- | --- |
| DEFAULT_INITIAL_CAPACITY | 2^4 | HashMap默认初始化桶(数组)数量 |
| MAXIMUM_CAPACITY | 2^30 | HashMap最大桶(数组)数量 |
| DEFAULT_LOAD_FACTOR | 0.75 | 负载因子(影响扩容机制) |
| size | 无初始化值 | 实际存储的key-value键值对的个数 |
| TREEIFY_THRESHOLD | 8 | 链表长度>8时变化数据结构为红黑树 |
| UNTREEIFY_THRESHOLD | 6 | 当桶位置上元素个数
     |



<a name="326bbeeb"></a>
##### 1.1.2.2.3 构造器


- 没有传入参数



```java
    // 默认构造器,负载因子为0.75;
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
```


- 传入参数 :  指定数组长度,指定负载因子



```java
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

<br />**tableSizeFor方法**<br />

```java
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
    答:| 为或操作符, >>>y为无符号右移y位 ,空位补0,
       n |= n >>> y会导致它的高n+1位全为1,
       最终n将为111111..1 加1,即为100000..00,即2的整次幂;
```

<br />对于这个构造函数,主要讲两点:<br />

```
[1]虽然说是自定义initialCapacity,但是实际上并不是你所想的那样的任意数组长度;
经过tableSizeFor方法的处理之后,会得到一个大于输入参数且最近的2的整数次幂的数;
比如你输入一个10,得到的是初始化数组长度为16的HashMap;

[2]在常规构造器中，没有为数组table分配内存空间(有一个入参为指定Map的构造器例外),
而是在执行put操作的时候才真正构建table数组;
```


<a name="7b48a4e2"></a>
#### 1.1.2.3 基本操作


<a name="d057dcbd"></a>
##### 1.1.2.3.1 如何获取Hash值


- 扰动函数


<br />**[1]获取hashCode值**<br />

```java
   public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
```

<br />**[2]获取key的hash值**<br />

```java
  static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }
```

<br />**扰动函数解析:**<br />

```
  上述代码中key.hashCode()函数调用的是超类Object类中的哈希函数,JVM进而调用本地方法,返回一个int值;
  理论上是一个int类型的值,范围为-2147483648-2147483647,
  前后加起来大概有40亿的映射空间,的确这么大的空间,映射会很均匀
  但是这么大的数组,内存中是放不下的。HashMap的初始化数组只有16
  ,所以这个散列值是无法直接拿来用的,所以要对hashCode做一些处理
  ,具体在下面小节中解答;
  
  扰动函数的结果: key的hash是hashCode中高位和低位的复合产物;
```


<a name="8367bd4a"></a>
##### 1.1.2.3.2 获取桶索引位置


```
  JDK 1.8中是直接通过p = tab[i = (n - 1) & hash这个方法来确定桶索引位置的;
  
  [1]hash为扰动函数的产物;
  [2]n-1为当前hashmap的数组个数len-1;
  
  此时,我们会有诸多疑问,比如:
  【1】为什么要做&运算呢?
    
   答: 与操作的结果就是高位全部归零,只保留低位值,用来做数组下标访问。
   举一个例子说明,初始化长度为16,16-1=15 . 
   2进制(4字节,32位)表示就是 00000000 00000000 00000000 00001111 
   和 某散列值做与操作结果就是
   00000000 10100101 11000100 00100101
   00000000 00000000 00000000 00001111
 & -------------------------------------
   00000000 00000000 00000000 00000101 //高位全部归零,只保留尾部四位
   
  【2】为什么是 n-1 呢? 
   答: 源头就是HashMap的数组长度为什么要取2的整数幂?
   因为这样(n-1)
   相当于一个"低位掩码"; 
   这样能结合下面的&运算使散列更为均匀,减少冲突
   (相对其他数字来说,比如10的话,下面我们以00100101和00110001这两个数做一个例子说明)
    
    和15&      和10&       和15&    和10&
   00100101  00100101    00110001  00110001
   00001111  00001010    00001111  00001010
   &-------  --------    --------  --------
   00000101  00000000    00000001  00000000
   
   我们发现两个不同的数00100101 和0011001 分别和15以及10做与运算,
   和15做&运算会得到两个不同的结果(即会被散列到不同的桶位置上),而和10做与运算得到的是相同的结果(散列到同一个位置上),
   这就会出现冲突!!!!!
   
  【3】hash&(len-1) 和 hash%len 的效果是相同的;
   
   例如 x=1<<4,即2^4=16;
   x:   00010000
   x-1: 00001111
   我们假设一个数M=01011011 (十进制为:Integer.valueOf("01011011",2) = 91)
   M :  01011011
   x-1: 00001111
   &-------------
        00001011(十进制数为:11)
   
   91 % 16 = 11
   证明两者效果相同,位运算的性能更高在操作系统级别来分析;
  【4】为什么扰动函数是那样子的呢?
   如下图所示,h>>>16 之后和h 做异或运算得到的hash前半部分是h的高8位,
   后半部分是hash的高16位和低16位的复合产物;
```

<br />![扰动函数.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002756561-6c2e3e08-b027-4f49-84ae-031d7fa96cab.png#align=left&display=inline&height=684&margin=%5Bobject%20Object%5D&name=%E6%89%B0%E5%8A%A8%E5%87%BD%E6%95%B0.png&originHeight=684&originWidth=1198&size=106217&status=done&style=none&width=1198)<br />

<a name="c8e0a65c"></a>
##### 1.1.2.3.3 put()方法


- put()方法



```java
    [1]调用put方法放入key-value键值对;
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
```


```
   [2] putVal()过程
```

<br />![putVal.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002768568-3f384e20-b984-4563-96d6-6e3ca26c801e.png#align=left&display=inline&height=969&margin=%5Bobject%20Object%5D&name=putVal.png&originHeight=969&originWidth=922&size=56049&status=done&style=none&width=922)<br />

- 分析put过程:
  - [1]首先判断table是否为空或者为null,如果是,则初始化数组table;
  - [2]根据键值key计算hash值 并得到桶索引位置((n-1)& hash),如果table[i]=null,直接新建节点添加,转向[6],如果table[i]不为空,则转向[3];
  - [3]判断table的首个元素是否和key相同,如果相同的话直接覆盖value,否则直接转向[4],这里的相同指的是hashCode和equals
  - [4]判断table[i]是否为treeNode,即table[i]是否为红黑树,如果是红黑树,则直接在树中插入键值对,否则转向[5];
  - [5]遍历table[i](%E7%AC%ACi+1%E4%B8%AA%E6%A1%B6),判断链表长度是否大于8,大于8的话将链表转换为红黑树,在红黑树中执行插入操作,否则进行链表的插入操作;遍历过程中若发现key已经存在直接覆盖value即可;
  - [6]插入成功,判断实际存在的键值对size是否超过了最大容量(阈值),如果超过,进行扩容;


<br />
<br />[1]这里重点说明下,在JDK1.6中index = hash % len 做与运算 ,在JDK 1.7/1.8中 变为了 i =(len-1) & hash,两者的作用是相同的;<br />
<br />[2]Put时如果key为null，存储位置为table[0]或table[0]的冲突链上(table为HashMap中存的数组),<br />
<br />如果该对应数据已存在，执行覆盖操作。用新value替换旧value，并返回旧value，如果对应数据不存在,则添加到链表的头上(保证插入O(1));<br />put：首先判断key是否为null，若为null，则直接调用putForNullKey方法。若不为空则先计算key的hash值，<br />然后根据hash值搜索在table数组中的索引位置，如果table数组在该位置处有元素，循环遍历链表，比较是否存在相同的key，<br />若存在则覆盖原来key的value，否则将该元素保存在链头（最先保存的元素放在链尾）。若table在该处没有元素，则直接保存。<br />

- 拉链法的工作原理



```java
    HashMap<String, String> map = new HashMap<>();
    map.put("K1","V1");
    map.put("K2","V2");
    map.put("K3","V3");
    
    [1]新建一个HashMap,默认大小为1<<4(16);
    [2]插入Node<K1,V1> ,先计算K1的hash值为115,使用和(len-1)做&运算得到所在的桶下标为115&15=3;
    [3]插入Node<K2,V2>,先计算K2的hash值为118,使用和(len-1)做&运算得到所在的桶下标为118&15=6;
    [4]插入Node<K3,V3>,先计算K3的hash值为118,使用和(len-1)做&运算得到所在的桶下标为118&15=6,插在<K2,V2>后面.
```

<br />![hashmap-put.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002789297-a335160a-2561-4c57-9c5c-f2a5eb18eb5a.png#align=left&display=inline&height=439&margin=%5Bobject%20Object%5D&name=hashmap-put.png&originHeight=439&originWidth=770&size=19456&status=done&style=none&width=770)<br />

<a name="bcf7a08d"></a>
##### 1.1.2.3.4 get()方法

<br />![get.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002802913-8af16756-5bbd-4b07-b28c-06d0a7131f02.png#align=left&display=inline&height=467&margin=%5Bobject%20Object%5D&name=get.png&originHeight=467&originWidth=914&size=33330&status=done&style=none&width=914)<br />

- 过程分析:
  - 根据Key计算出hash值;
  - 桶位置index =hash & (len-1)
    - 如果要找的key就是上述数组index位置的元素,直接返回该元素的值;
    - 如果该数组index位置元素是TreeNode类型,则按照红黑树的查询方式来进行查询;
    - 如果该数组index位置元素非TreeNode类型,则按照链表的方式来进行遍历查询;



<a name="ec0b6ddb"></a>
## 2. HashMap 高级特性


<a name="5f395b11"></a>
### 2.1 扩容机制


```
 resize() 方法用于初始化数组(初始化数组过程由于涉及到类加载过程,将会放到JVM模块中进一步解释)或数组扩容，每次扩容后，容量为原来的 2 倍,并进行数据迁移。
```


<a name="0afc3a8e"></a>
#### 2.1.1 扩容后位置变化


```
  每次扩容，新容量为旧容量的2倍;

  扩容之后元素的位置
  [1]要么在原来位置上
  
  [2]要么在原位置再移动2^n的位置上;
```


- **解释过程**



```java
hashmap的原来桶数量为16,即n-1=15(二进制表示:1111)
[1]扩容前
n-1 :  0000 0000 0000 0000 0000 0000 0000 1111
hash1: 1111 1111 1111 1111 0000 1111 0000 0101
    &----------------------------------------
      0000 0000 0000 0000 0000 0000 0000 0101 (桶位置为5)
      
      
n-1 :  0000 0000 0000 0000 0000 0000 0000 1111       
hash2: 1111 1111 1111 1111 0000 1111 0001 0101
    &----------------------------------------
      0000 0000 0000 0000 0000 0000 0000 0101 (桶位置也是5)
两个hash值不同的对象,和n-1做&运算之后,得到相同的结果;
[2]扩容后
hashmap此时桶的数量变为32,即n-1=31(二进制表示:11111)
n-1:   0000 0000 0000 0000 0000 0000 0001 1111
hash1: 1111 1111 1111 1111 0000 1111 0000 0101
     &---------------------------------------
      0000 0000 0000 0000 0000 0000 0000 0101 (扩容后,桶位置依旧是5)
      
n-1:   0000 0000 0000 0000 0000 0000 0001 1111
hash2: 1111 1111 1111 1111 0000 1111 0001 0101
     &---------------------------------------
      0000 0000 0000 0000 0000 0000 0001 0101 (扩容后,桶位置变为之前5+2^4=21)
```


```
  元素在hashmap扩容之后,会重新计算桶下标,从上面的例子中可以看出来,hash1的桶位置在扩容前后没有发生变化,
  hash2的桶位置在扩容前后发生了变化;
  
上述扩容过程中&运算的关键点就在于
扩容之后新的长度(n-1)转化为2进制之后新增的bit为1,
而key的hash 值所对应位置的bit是1 还是0,
如果是0的话那么桶位置就不会变化,依旧为index;
是1 的话桶位置就会变成index+oldCap;
通过if((e.hash&oldCap)==0)来判断hash的新增判断bit是1还是0;
```


> 
> 



<a name="0b491328"></a>
#### 2.1.2 HashMap扩容为啥是2倍扩展


```
 综上所述，根本原因为2进制数字;
```


<a name="5a748573"></a>
### 2.2 负载因子为什么是0.75


```
  结论: 通俗地说默认负载因子(0.75)在时间和空间成本上提供了很好的折中;
```


```
     * Because TreeNodes are about twice the size of regular nodes, we
     * use them only when bins contain enough nodes to warrant use
     * (see TREEIFY_THRESHOLD). And when they become too small (due to
     * removal or resizing) they are converted back to plain bins.  In
     * usages with well-distributed user hashCodes, tree bins are
     * rarely used.  Ideally, under random hashCodes, the frequency of
     * nodes in bins follows a Poisson distribution
     * (http://en.wikipedia.org/wiki/Poisson_distribution) with a
     * parameter of about 0.5 on average for the default resizing
     * threshold of 0.75, although with a large variance because of
     * resizing granularity. Ignoring variance, the expected
     * occurrences of list size k are (exp(-0.5) * pow(0.5, k) /
     * factorial(k)). The first values are:
     *
     * 0:    0.60653066
     * 1:    0.30326533
     * 2:    0.07581633
     * 3:    0.01263606
     * 4:    0.00157952
     * 5:    0.00015795
     * 6:    0.00001316
     * 7:    0.00000094
     * 8:    0.00000006
     * more: less than 1 in ten million
```

<br />
<br />资料见引用<br />

```
   理想状态下,在随机哈希值的情况，对于loadfactor = 0.75 ,
  虽然由于粒度调整会产生较大的方差,桶中的Node的分布频率服从参数为0.5的泊松分布;
   而对于一个bucket是空或非空的概率为0.5，通过牛顿二项式等数学计算，得到这个loadfactor的值为log（2），约等于0.693.
  同回答者所说，可能小于0.75 大于等于log（2）的factor都能提供更好的性能，0.75这个数说不定是 pulled out of a hat
```


<a name="1ded16b3"></a>
### 2.3 线程安全问题


<a name="938ef9d0"></a>
#### 2.3.1 线程不安全的表现


- JDK 1.7: **并发死环，丢数据**
- JDK 1.8: **并发丢数据**



<a name="eb8c7aae"></a>
#### 2.3.2 源码分析


<a name="d5189e36"></a>
##### 2.3.2.1 扩容后节点顺序

- JDK 1.7
  - **头插法** : 新增节点插入链表头部
- JDK 1.8
  - **尾插法** : 新增节点插入链表尾部
- **源码分析与参考**:
  - [HashMap到底是插入链表头部还是尾部](https://blog.csdn.net/qq_33256688/article/details/79938886)



<a name="c94e1dbb"></a>
##### 2.3.2.2 JDK1.7死环源码分析
```
   说到根本就是扩容之后,链表元素顺序发生变化导致的;
   JDK 1.7 中扩容之后 链表顺序会倒置（采用头插法）;
```


```java
    JDK 1.7 扩容中重新安排Entry操作:

    void transfer(Entry[] newTable) {
     Entry[] src = table;                   //src引用了旧的Entry数组
     int newCapacity = newTable.length;
     for (int j = 0; j < src.length; j++) { //遍历旧的Entry数组
         Entry<K,V> e = src[j];             //取得旧Entry数组的每个元素
         if (e != null) {
             src[j] = null;//释放旧Entry数组的对象引用（for循环后，旧的Entry数组不再引用任何对象）
             do {
                 Entry<K,V> next = e.next;
                 int i = indexFor(e.hash, newCapacity); //！！重新计算每个元素在数组中的位置
                 e.next = newTable[i]; //标记[1]
                 newTable[i] = e;      //将元素放在数组上
                 e = next;             //访问下一个Entry链上的元素
             } while (e != null);
         }
     }
 }
实际就是一个链表的反转，迭代方法; newTable[i] 的作用就是配合操作，保存值;
```


<a name="8c881bff"></a>
###### 2.3.2.2.1 扩容后节点顺序反向存储 （重要）
实际就是一个链表的反转，迭代方法;<br />

```
   在分析HashMap的线程不安全之前,我们先看一下上面扩容重组过程中的一段扩容后重新分配bucket的代码
   ,并结合一小段自定义代码来理解这个过程。
```


```java
   我最开始看上面那段代码的时候，死活看不懂,最后自己模拟了这个过程才明白。咱们先看下 我的模拟代码:
    
    链表反转
```


- Entry :



```java
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Entry<T> {
    int val;
    Entry next;

    public Entry(int val) {
        this.next = null;
        this.val = val;
    }
}

 public static void main(String[] args) {
        Entry e = new Entry(1);
        e.next = new Entry(2);
        e.next.next = new Entry(3);
        e.next.next.next = new Entry(4);
        Entry[] newTable = new Entry[6];
        while (e != null) {
            // [1]
            Entry next = e.next;
            // [2]
            e.next = newTable[5];
            // [3]
            newTable[5] = e;
            // [4]
            e = next;
        }
    }
```


<a name="bbbdefe4"></a>
###### 2.3.2.2.2 模拟过程
![hashmap-1.7-resize-01.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002906063-439c7fe1-73aa-42d7-9c21-80ef3019cd75.png#align=left&display=inline&height=288&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-01.png&originHeight=288&originWidth=778&size=26711&status=done&style=none&width=778)![hashmap-1.7-resize-02.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002907371-1a43ba1f-e8d7-4f54-9f17-7198f6f737c8.png#align=left&display=inline&height=363&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-02.png&originHeight=363&originWidth=802&size=35280&status=done&style=none&width=802)![hashmap-1.7-resize-03.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002907822-c7e3de41-8976-4ace-90bb-ee7af691a481.png#align=left&display=inline&height=246&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-03.png&originHeight=246&originWidth=740&size=22801&status=done&style=none&width=740)![hashmap-1.7-resize-04.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002908580-13786e72-5614-4921-8d10-06db4cd5275d.png#align=left&display=inline&height=251&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-04.png&originHeight=251&originWidth=745&size=23602&status=done&style=none&width=745)![hashmap-1.7-resize-05.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002909625-05590895-6f02-4766-8ba8-12803c562a14.png#align=left&display=inline&height=266&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-05.png&originHeight=266&originWidth=739&size=23467&status=done&style=none&width=739)![hashmap-1.7-resize-06.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002910175-f08e900b-9b55-40b8-b246-f2f9d54ede57.png#align=left&display=inline&height=232&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-06.png&originHeight=232&originWidth=752&size=21634&status=done&style=none&width=752)![hashmap-1.7-resize-07.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002910855-efe6911c-8923-4348-8e98-3a1e1490d553.png#align=left&display=inline&height=219&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-07.png&originHeight=219&originWidth=774&size=21662&status=done&style=none&width=774)![hashmap-1.7-resize-08.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002911378-348b8844-5d7b-4239-9a14-e688de66ede2.png#align=left&display=inline&height=203&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-08.png&originHeight=203&originWidth=761&size=19339&status=done&style=none&width=761)![hashmap-1.7-resize-09.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1581002911952-18050ba2-98dc-4d5a-bd26-334e02b3a208.png#align=left&display=inline&height=213&margin=%5Bobject%20Object%5D&name=hashmap-1.7-resize-09.png&originHeight=213&originWidth=738&size=19683&status=done&style=none&width=738)<br />

<a name="9dff0002"></a>
##### 
上面讲完了链表反转的基本操作，我们举一个例子来看下 如何在多线程环境下形成死环的。（该例子 来自于CSDN ，博客地址见引用）

![](https://cdn.nlark.com/yuque/0/2020/png/177460/1590828008633-a7aa4d35-2b43-424e-847d-b75467a83e09.png#align=left&display=inline&height=886&margin=%5Bobject%20Object%5D&originHeight=886&originWidth=1670&size=0&status=done&style=none&width=1670)<br />假设HashMap 初始化容量为2，阈值为1，即塞入元素3的时候 就要发生扩容。<br />
<br />![](https://cdn.nlark.com/yuque/0/2020/png/177460/1590828103790-e5c16b47-347d-4139-b484-11397190a5d0.png#align=left&display=inline&height=1020&margin=%5Bobject%20Object%5D&originHeight=1020&originWidth=1618&size=0&status=done&style=none&width=1618)<br />
<br />之前我们分析了transfer 方法就是让 原链表顺序发生逆转。<br />现在假设有2个线程A,B<br />A在扩容获取到元素3时，执行完 Entry<K,V> next = e.next，之后发生了时间片切换，此时next 指向 元素 7 .<br />B上场开始扩容，B扩容完毕之后，元素7 的next指针指向 元素3 ，而CPU切回到线程A之后，对于A继续执行，但是此时 ，元素3和元素7 之间互相指向。如图所示，<br />
<br />![image.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1590828638927-a1c6329e-95d2-460e-b820-ea418594c86c.png#align=left&display=inline&height=174&margin=%5Bobject%20Object%5D&name=image.png&originHeight=180&originWidth=394&size=3643&status=done&style=none&width=380)<br />A线程就一直会陷在do while循环中找 e.next!=null之中。
<a name="5oYpO"></a>
##### 2.3.2.3 JDK1.8扩容过程分析及数据丢失

- 扩容后保证顺序不变化



```
  JDK 1.8扩容时保持链表顺序不变,避免了死环现象的发生;
```

- 数据丢失问题分析

![image.png](https://cdn.nlark.com/yuque/0/2020/png/177460/1590829307157-e37c0395-03c4-465c-b343-425f371a1a2d.png#align=left&display=inline&height=244&margin=%5Bobject%20Object%5D&name=image.png&originHeight=488&originWidth=1396&size=58370&status=done&style=none&width=698)
```java
HashMap 数据丢失的问题 比较好理解，我们看下JDK 8中 上图这段代码，
704行和705行。
假设线程A在704行判断 oldTab[j]!=null,然后在705行 将其oldTab[j]设置为null,此时时间片切换，
线程B在704行判断oldTab[j]为空，直接跳过这个索引下的旧数据，然后往下走执行完整个扩容过程，
会直接导致这个桶位置上的所有元素丢失。
```


<a name="913fac29"></a>
### 2.4 树状化存储结构

<br />知识铺垫：<br />需要了解下面三种树的优点及缺点,才能了解到HashMap为何选择 红黑树作为树状化存储结构的原因。<br />
<br />二叉搜索树（BST: BinarySearch Tree） ->  平衡二叉树（AVL Tree）->  红黑树（2-3树）<br />
<br />
<br />

<a name="YgBzs"></a>
# 引用
[Stackoverflow-老外的牛顿二项式](https://stackoverflow.com/questions/10901752/what-is-the-significance-of-load-factor-in-hashmap)<br />[泊松分布](http://www.ruanyifeng.com/blog/2015/06/poisson-distribution.html#comment-356111)<br />[HashMap线程安全问题](https://blog.csdn.net/qq_36879870/article/details/90108711)<br />[美团技术团队-Java8 系列之重新认识HashMap](https://tech.meituan.com/2016/06/24/java-hashmap.html)<br />
