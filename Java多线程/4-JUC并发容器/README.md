# JUC并发容器
## JUC集合类框架
### 1.List和Set
```
  JUC集合包中List和Set实现类包括: CopyOnWriteArrayList,  CopyOnWriteArraySet 和ConcurrentSkipListSet
```
 - CopyOnWriteArrayList:
```
 CopyOnWriteArrayList相当于线程安全的ArrayList,它实现了List接口.CopyOnWriteArrayList支持高并发;
```
 - CopyOnWriteArraySet
```java
   CopyOnWriteArraySet相当于线程安全的HashSet,它实现了AbstractSet类.CopyOnWriteArraySet内部包含了一个CopyOnWriteArrayList对象,它是通过CopyOnWriteArrayList实现的;
```
### 2.Map
```java
   JUC集合包中Map的实现类包括:ConcurrentHashMap和ConcurrentSkipListMap
```
 - ConcurrentHashMap
```java
    
```