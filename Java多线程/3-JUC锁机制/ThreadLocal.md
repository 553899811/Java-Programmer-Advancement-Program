<!-- GFM-TOC -->
* [ThreadLocal概述](#ThreadLocal概述)
  * [1. ThreadLoca是什么](#1-threadlocal是什么) 
  * [2.实现原理](#2-实现原理)
    * [2.1 ThreadLocalMap数据结构](#21-threadlocalmap数据结构)
    * [2.2 ThreadLocalMap的key](#22-threadlocalmap的key)
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

 - Entry代码分析:
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
 - 在Thread类中存在的蛛丝马迹
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
### 2.2 ThreadLocalMap的key
```
        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    为什么要这么设计?
    [1]这样设计之后每个Map的Entry数量变小了：之前是Thread的数量,
    现在是ThreadLocal的数量，能提高性能;
    [2]当Thread销毁之后对应的ThreadLocalMap也就随之销毁了,能减少内存使用量。
```
## 3.1 ThreadLocal的弱引用
