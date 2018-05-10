<!-- GFM-TOC -->
* [Java集合工具包框架图](#java集合工具包框架图)
    * [1. Collection 简介](#1-collection-简介)
    * [2. List 简介](#2-list-简介)
    * [3. Set 简介](#3-set-简介)
    * [4. AbstractCollection](#4-abstractcollection)
    * [5. AbstractList](#5-abstractlist)
    * [6. AbstractSet](#6-abstractset)
    * [7. Iterator](#7-iterator)
<!-- GFM-TOC -->

# Java集合工具包框架图
![](http://wangkuiwu.github.io/media/pic/java/collection/collection01.jpg)
 - 说明: 
```
  Collection是一个接口，它主要的两个分支是：List 和 Set。
  Map独立于Collection存在;
List和Set都是接口，它们继承于Collection。List是有序的队列，List中可以有重复的元素；而Set是数学概念中的集合，Set中没有重复元素！
List和Set都有它们各自的实现类。

为了方便实现，集合中定义了AbstractCollection抽象类，它实现了Collection中的绝大部分函数；这样，在Collection的实现类中，我们就可以通过继承AbstractCollection省去重复编码。AbstractList和AbstractSet都继承于AbstractCollection，具体的List实现类继承于AbstractList，而Set的实现类则继承于AbstractSet。

  另外，Collection中有一个iterator()函数，它的作用是返回一个Iterator接口。通常，我们通过Iterator迭代器来遍历集合。ListIterator是List接口所特有的，在List接口中，通过ListIterator()返回一个ListIterator对象。
接下来，我们看看各个接口和抽象类的介绍；然后，再对实现类进行详细的了解。
```
## 1. Collection 简介
 - Collection定义如下:
```
  public interface Collection<E> extends Iterable<E> {}
```
```
 1.它是一个高度抽象的接口,它包含了集合的基本操作：添加,删除,清空,遍历(读取),是否为空,获取size();
 2. Collection的实现类中都有2个构造器,一个空构造器,一个参数为Collection<? extend E> c的构造器;
```
## 2. List 简介
 - List定义如下:
```
  public interface List<E> extends Collection<E>{}
```
```
 List是一个继承于Collection的接口，即List是集合中的一种。List是有序的队列，List中的每一个元素都有一个索引；第一个元素的索引值是0，往后的元素的索引值依次+1。和Set不同，List中允许有重复的元素。
```
 - 对指定位置元素的操作(index)
```
  由于List是有序队列，它也额外的有自己的API接口。主要有“添加、删除、获取、修改指定位置的元素”、“获取List中的子队列”等。
```
## 3. Set 简介
 - Set定义如下:
```
  public interface Set<E> extends Collection<E>{}
```
```
  Set中没有重复元素;
```
## 4. AbstractCollection
 - AbstractCollection定义如下:
```
   public abstract class AbstractCollection<E> implements Collection<E> {}
```
## 5. AbstractList
 - AbstractList定义如下:
 ```
   public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {}
 ```
 ```
   AbstractList是一个继承与AbstractCollection,并且实现List接口的抽象类.它实现了List中除size(),get(int index)之外的函数.
   AbstractList的主要作用: 它实现了List接口中的大部分函数,从而方便其他类继承List.
   另外,和AbstractCollection相比,AbstractList抽象类中,实现了Iterator()接口;
 ```
 ## 6. AbstractSet
  - AbstractSet
```
   public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {}
```
```
   AbstractSet是一个继承于AbstractCollection，并且实现Set接口的抽象类。由于Set接口和Collection接口中的API完全一样，Set也就没有自己单独的API。和AbstractCollection一样，它实现了List中除iterator()和size()之外的函数。
AbstractSet的主要作用：它实现了Set接口中的大部分函数。从而方便其它类实现Set接口。
```
 ## 7. Iterator
 - Iterator定义如下:
```
   public interface Iterator<E> {}
```
```
   Iterator是一个接口，它是集合的迭代器。集合可以通过Iterator去遍历集合中的元素。Iterator提供的API接口，包括：是否存在下一个元素、获取下一个元素、删除当前元素。
注意：Iterator遍历Collection时，是fail-fast机制的。即，当某一个线程A通过iterator去遍历某集合的过程中，若该集合的内容被其他线程所改变了；那么线程A访问集合时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。关于fail-fast的详细内容，我们会在fail-fast总结后面专门进行说明。
```
