<!-- GFM-TOC -->
* [HashTable](#HashTable)
    * [1. HashTable 简介](#1-HashTable-简介)
       * [1.1 实现细节](#11-实现细节)
         * [1.1.1 实现和继承关系](#111-实现和继承关系) 
         * [1.1.2 底层实现](#112-底层实现)
    * [2. HashTable 高级特性](#2-HashTable-高级特性)
<!-- GFM-TOC -->

# HashTable
## 1. HashTable 简介
## 1.1 实现细节
```
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable
```

### 1.1.1 实现和继承关系
   - **继承** 抽象类**Dictionary**,它的所有方法都是抽象的,这些性质导致它更像一个接口。但是这个类出现时还不存在接口的概念;
   - **实现** Map接口,Map是"key-value键值对"接口;
   - **实现** Cloneable接口,即覆盖了clone()方法,能被克隆;
   - **实现** java.io.Serializable 接口,支持序列化,能通过序列化去传输;

### 1.1.2 底层实现
``` 
  HashTable继承Dictionary,利用哈希算法来在字典中通过关键字查找对应的内容.查找的复杂度是常数O(N),具体与Dictionary的元素的冲突来决定的;
```
#### 1.1.2.1 HashTable函数列表

<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>函数名称</td>
          <td>函数描述</td>
     </tr>
</thead>

<tbody>
    <tr>
        <td>HashTable()</td>
        <td>构造函数,初始化容量为11,负载因子: 0.75</td>
    </tr>
    <tr>
        <td>clear()</td>
        <td>清空hashTable中所有的元素</td>
    </tr>
    <tr>
        <td>clone()</td>
        <td>获取一个hashTable的拷贝</td>
    </tr>
    <tr>
        <td>contains()</td>
        <td>检查某个对象是否作为值存在于dictionary中</td>
    </tr>
    <tr>
        <td>containsValue()</td>
        <td>同上</td>
    </tr>
    <tr>
        <td>containsKey()</td>
        <td>检查某个对象是否作为关键存于dictionary中</td>
    </tr>
    <tr>
        <td>entrySet()</td>
        <td>返回hashTable里的所有(key,value)</td>
    </tr>
    <tr>
        <td>equals()</td>
        <td>判断当前对象和参数是否相等</td>
    </tr>
    <tr>
        <td>hashCode()</td>
        <td>获得一个hashTable的哈希码</td>
    </tr>
    <tr>
        <td>putAll()</td>
        <td>将一个集合添加到dictionary当中</td>
    </tr>
    <tr>
        <td>rehash()</td>
        <td>增长hashTable的内部的空间</td>
    </tr>
    
</tbody>
</table>