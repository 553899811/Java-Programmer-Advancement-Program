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
   ConcurrentHashMap是线程安全的哈希表(相当于线程安全的HashMap)；它继承于AbstractMap类，并且实现ConcurrentMap接口。ConcurrentHashMap是通过“锁分段”来实现的，它支持并发。
```
 - ConcurrentSkipListMap
```
   ConcurrentSkipListMap是线程安全的有序的哈希表(相当于线程安全的TreeMap); 它继承于AbstractMap类，并且实现ConcurrentNavigableMap接口。ConcurrentSkipListMap是通过“跳表”来实现的，它支持并发。
```
 - ConcurrentSkipListSet
```
   ConcurrentSkipListSet是线程安全的有序的集合(相当于线程安全的TreeSet)；它继承于AbstractSet，并实现了NavigableSet接口。ConcurrentSkipListSet是通过ConcurrentSkipListMap实现的，它也支持并发。
```
### 3.Queue
```
  JUC集合包中Queue的实现类包括: ArrayBlockingQueue, LinkedBlockingQueue, LinkedBlockingDeque, ConcurrentLinkedQueue和ConcurrentLinkedDeque。
```
 - ArrayBlockingQueue
 ```
   ArrayBlockingQueue是数组实现的线程安全的有界的阻塞队列。
 ```
 - LinkedBlockingQueue
 ```
   LinkedBlockingQueue是单向链表实现的(指定大小)阻塞队列，该队列按 FIFO（先进先出）排序元素。
 ```
 - LinkedBlockingDeque
 ```
   LinkedBlockingDeque是双向链表实现的(指定大小)双向并发阻塞队列,该阻塞队列同时支持FIFO和FILO两种操作方式。
 ```
 - ConcurrentLinkedQueue
 ```
   ConcurrentLinkedQueue是单向链表实现的无界队列，该队列按 FIFO（先进先出）排序元素
 ```
 - ConcurrentLinkedDeque
```
   ConcurrentLinkedDeque是双向链表实现的无界队列，该队列同时支持FIFO和FILO两种操作方式。
```