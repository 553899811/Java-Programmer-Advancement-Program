<!-- GFM-TOC -->
* [ThreadLocal概述](#ThreadLocal概述)
  * [1. ThreadLoca是什么](#1-threadlocal是什么) 
  * [2.实现原理](#2-实现原理)
    * [2.1 ThreadLocalMap数据结构](#21-threadlocalmap数据结构)
      * [2.1.1 Entry代码分析](#211-entry代码分析)
      * [2.1.2 和Thread类的关系](#212-和thread类的关系)
      * [2.1.3 ThreadLocalMap的key设计原理](#213-threadlocalmap的key设计原理)
  * [3.高级特性](3-高级特性)
    * [3.1 ThreadLocal的弱引用](#31-threadlocal的弱引用)
<!-- GFM-TOC -->
# ThreadLocal概述
## 1.ThreadLocal是什么
 - JDK描述:
```
  This class provides thread-local variables.  These variables differ from
  their normal counterparts in that each thread that accesses one (via its
  {@code get} or {@code set} method) has its own, independently initialized
  copy of the variable.  {@code ThreadLocal} instances are typically private
  static fields in classes that wish to associate state with a thread (e.g.,
  a user ID or Transaction ID).
   
   简略翻译:
   ThreadLocal类用来提供线程内部的局部变量。
   这种变量在多线程环境下访问(通过get或set方法访问)时能保证各个线程里的变量相对独立于其他线程内的变量。
   ThreadLocal实例通常来说都是private static类型的，用于关联线程和线程的上下文(即线程可以共享它,进而在每个线程内部均创建自己的副本信息)。
```
``` 
   什么玩意?你说的是什么东西呀，我一点都不懂，没关系，看下面的例子先.
```
 - Demo
```
 public class Solution {

    private static final ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    static class MyThread implements Runnable {

        private int index;

        public MyThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            System.out.println("线程:" + index + "的初始值为:" + value.get());
            for (int i = 0; i < 10; i++) {
                value.set(value.get() + i);
            }
            System.out.println("线程" + index + "的累加值为:" + value.get());
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new MyThread(i)).start();
        }
    }
}
结果: 

线程:0的初始值value:0
线程:0的累加value:45
线程:3的初始值value:0
线程:3的累加value:45
线程:4的初始值value:0
线程:4的累加value:45
线程:2的初始值value:0
线程:2的累加value:45
线程:1的初始值value:0
线程:1的累加value:45
```
## 2.如何实现
### 2.1 ThreadLocalMap数据结构
```
  这是ThreadLocal的核心数据结构,特别像 HashMap,以Entry键值对存值;
```
![](https://upload-images.jianshu.io/upload_images/2184951-9611b7b31c9b2e20.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

#### 2.1.1 Entry代码分析:
```
        static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
        
    在ThreadLoalMap中，也是初始化一个大小16的Entry数组，Entry对象用来保存每一个key-value键值对，
    只不过这里的key永远都是ThreadLocal对象，是不是很神奇，通过ThreadLocal对象的set方法，
    结果把ThreadLocal对象自己当做key，放进了ThreadLoalMap中;
    
    这里需要注意的是，ThreadLoalMap的Entry是继承WeakReference，和HashMap很大的区别是,
    Entry中没有next字段，所以就不存在链表的情况了。
```
#### 2.1.2 和Thread类的关系
```
   /* 与线程有关的线程本地量 
    ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class. */
     
    ThreadLocal.ThreadLocalMap threadLocals = null;
    在使用ThreadLocal的时候将其定义为private static,使得所有线程可以共有;
    
    /*
     * InheritableThreadLocal values pertaining to this thread. This map is
     * maintained by the InheritableThreadLocal class.
     */
     
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
```
#### 2.1.3 ThreadLocalMap的key设计原理
```
    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
    在分析原理之前,我们先提出自己的几个问题?
```
  - [1]根据上一小结,我们发现多个线程共用一个thredlocal实例化对象,而且每个entry都拿该实例对象作为key,这怎么可能呢?
```
  set()方法:
    每个ThreadLocal对象都有一个hash值threadLocalHashCode，每初始化一个ThreadLocal对象，hash值就增加一个固定的大小0x61c88647。
    
    在插入过程中，根据ThreadLocal对象的hash值，定位到table中的位置i，过程如下：
    1、如果当前位置是空的，那么正好，就初始化一个Entry对象放在位置i上；
    2、不巧，位置i已经有Entry对象了，如果这个Entry对象的key正好是即将设置的key，那么重新设置Entry中的value；
    3、很不巧，位置i的Entry对象，和即将设置的key没关系，那么只能找下一个空位置；

    这样的话，在get的时候，也会根据ThreadLocal对象的hash值，定位到table中的位置，然后判断该位置Entry对象中的key是否和get的key一致，如果不一致，就判断下一个位置

    可以发现，set和get如果冲突严重的话，效率很低，因为ThreadLoalMap是Thread的一个属性，所以即使在自己的代码中控制了设置的元素个数，但还是不能控制其它代码的行为。
```
  - [2]为什么要这么设计?
    - 这样设计之后每个Map的Entry数量变小了：之前是Thread的数量,
    现在是ThreadLocal的数量，能提高性能;
    - 当Thread销毁之后对应的ThreadLocalMap也就随之销毁了,能减少内存使用量。

## 3.高级特性 
### 3.1 ThreadLocal的弱引用
```
  关于JVM的引用类型的讲解,在Java虚拟机模块中会有较为详细的讲解:
  
  
     弱引用与软引用的区别在于:
     只具有弱引用的对象拥有更短暂的生命周期。
     在垃圾回收器线程扫描它所管辖的内存区域的过程中，
     一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。
     不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象;
```
![](https://pic1.zhimg.com/80/9671b789e1da4f760483456c03e4f4b6_hd.jpg)



