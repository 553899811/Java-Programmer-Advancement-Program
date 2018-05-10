<!-- GFM-TOC -->
* [Collections.synchronizedMap介绍](#collectionssynchronizedmap介绍)
    * [1. Collections.synchronizedMap简介](#1-collectionssynchronizedmap简介)
       * [1.1 实现细节](#11-实现细节)
         * [1.1.1 实现和继承关系](#111-实现和继承关系) 
         * [1.1.2 底层实现](#112-底层实现)
<!-- GFM-TOC -->
# Collections.synchronizedMap介绍
## 1. Collections.synchronizedMap简介
```
  一个加锁的HashMap
```
### 1.1 实现细节
```
    private static class SynchronizedMap<K,V>
        implements Map<K,V>, Serializable
```
#### 1.1.1 实现和继承关系
   -  **实现**  Map接口,Map是"key-value键值对"接口;
   -  **实现**  java.io.Serializable 接口,支持序列化,能通过序列化去传输;
#### 1.1.2 底层实现
```
  Collections.synchronizedMap和HashTable一样,实现了在调用Map所有方法时,对整个map进行同步;
  
```