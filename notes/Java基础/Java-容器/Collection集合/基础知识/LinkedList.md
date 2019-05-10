<!-- GFM-TOC -->
* [LinkedList介绍](#linkedlist介绍)
    * [1. LinkedList 简介](#1-linkedlist-简介)
       * [1.1 实现细节](#11-实现细节)
         * [1.1.1 实现和继承关系](#111-实现和继承关系) 
         * [1.1.2 底层实现](#112-底层实现)
           * [1.1.2.1 主要成员](#1121-主要成员)
           * [1.1.2.2 方法分类](#1122-方法分类)
       
    * [2. LinkedList 高级特性](#2-linkedlist-高级特性)
    
       <!-- GFM-TOC -->

# LinkedList介绍
## 1. LinkedList 简介
```
  LinkedList是双向链表,大致长下面的样子
```
![](https://www.geeksforgeeks.org/wp-content/uploads/gq/2014/03/DLL1.png)
### 1.1 实现细节
```
  基于JDK 1.8版本;
  public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
```
#### 1.1.1 实现和继承关系
   - **继承** 抽象类**AbstractSequentialList**,它也可以被当作堆栈、队列或双端队列进行操作;
   - **实现** List 接口，能对它进行队列操作。
   - **实现** Cloneable接口,即覆盖了clone()方法,能被克隆;
   - **实现** java.io.Serializable 接口,支持序列化,能通过序列化去传输;
   - **实现** Deque 接口，即能将LinkedList当作双端队列使用

```
 [继承关系]
 java.lang.Object
   ↳     java.util.AbstractCollection<E>
         ↳     java.util.AbstractList<E>
               ↳     java.util.AbstractSequentialList<E>
                     ↳     java.util.LinkedList<E>
```
#### 1.1.2 底层实现
##### 1.1.2.1 主要成员
 - 节点Node
```
  private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
  //链表由递归构成,双向节点存在一个prev的指向,指向上一个节点;
  //以及一个next的指向,指向下一个节点;
  //E item 说明保存的元素类型为泛型类型;
```
 - 头结点[即链表第一个节点]
```
   /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     */
    transient Node<E> first;
```
 - 尾节点[即链表最后一个节点]
```
    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    transient Node<E> last;
```
##### 1.1.2.2 方法分类
```
  LinkedList实现了List和Deque接口,源代码中具备了实现List和Queue的特性;
  重点来看下LinkedList是如何实现栈(Stack)和Queue(队列)的功能的,主要体现在它的方法分类上:
```
 - LinkedList中等效 Queue方法
<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>Queue队列方法</td>
          <td>LinkedList等效方法</td>
          <td>作用</td>
          <td>返回值</td>
          <td>非空判断</td>
     </tr>
</thead>

<tbody>
    <tr>
        <td>add(e)</td>
        <td>addLast(e)</td>
        <td>队列尾部添加元素</td>
        <td>void</td>
        <td>false</td>
    </tr>
    <tr>
        <td>offer(e)</td>
        <td>offerLast(e)</td>
        <td>队列尾部添加元素</td>
        <td>boolean</td>
        <td>false</td>
    </tr>
    <tr>
        <td>remove()</td>
        <td>removeFirst()</td>
        <td>移除队列头部元素</td>
        <td>泛型E</td>
        <td><font color="red">true</font></td>
    </tr>
    <tr>
        <td>poll()</td>
        <td>pollFirst()</td>
        <td>移除队列头部元素</td>
        <td>泛型E</td>
        <td>false</td>
    </tr>
    <tr>
        <td>element()</td>
        <td>getFirst()</td>
        <td>获取队列头部元素</td>
        <td>泛型E</td>
        <td><font color="red">true</font></td>
    </tr>
    <tr>
        <td>peek()</td>
        <td>peekFirst()</td>
        <td>获取队列头部元素</td>
        <td>泛型E</td>
        <td>false</td>
    </tr>
</tbody>
</table>

 - LinkedList中等效Stack的方法

<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>Stack栈方法</td>
          <td>LinkedList等效方法</td>
          <td>实质调用</td>
          <td>作用</td>
          <td>返回值</td>
          <td>非空判断</td>
     </tr>
</thead>
<tbody>
    <tr>
        <td>push(e)</td>
        <td>push(e)</td>
        <td>addFirst</td>
        <td>栈中压入元素</td>
        <td>void</td>
        <td>false</td>
    </tr>
     <tr>
        <td>pop(e)</td>
        <td>pop(e)</td>
        <td>removeFirst</td>
        <td>栈中弹出元素</td>
        <td>void</td>
        <td>false</td>
    </tr>
    <tr>
        <td>peek(e)</td>
        <td>peek(e)</td>
        <td>peek(e)</td>
        <td>获取队列头部元素</td>
        <td>void</td>
        <td>false</td>
    </tr>
</tbody>
</table>

# 2. LinkedList 高级特性
 - LinkedList线程不安全性
 - LinkedList模拟队列,栈实现数据结构操作(**TODO** :之后遇到会调重点的举例说明)
 - LinkedList 双向链表从数据结构的特征出发的特性(**Done**:重点是一些基本操作,比如翻转链表,判断链表是否存在环),这些在 [Programming](https://github.com/553899811/Programming/tree/master/Data%20Structures/List/LinkedList/java)项目中有具体说明;