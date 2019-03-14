```
  1.ArrayList的大小是如何自动增加的?
  答:目的在于考扩容机制;
  private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
    Java会去检查arraylist，以确保已存在的数组中有足够的容量来存储这个新的对象。
    如果没有足够容量的话，那么就会新建一个长度更长的数组，旧的数组就会使用Arrays.copyOf方法被复制到新的数组中去，现有的数组引用指向了新的数组。
```
```
  2.什么情况下你会使用ArrayList？什么时候你会选择LinkedList？
  答:主要问两者的区别;
  多数情况下，当你遇到访问元素比插入或者是删除元素更加频繁的时候，你应该使用ArrayList。另外一方面，当你在某个特别的索引中，插入或者是删除元素更加频繁，或者你压根就不需要访问元素的时候，你会选择LinkedList;
  ArrayList中访问元素的时间复杂度为:O(1),根据下标直接命中;
  LinkedList中访问元素的时间复杂度为:O(n),需要一个一个找;
  在ArrayList中增加或者删除某个元素，通常会调用System.arraycopy方法，这是一种极为消耗资源的操作.
  因此，在频繁的插入或者是删除元素的情况下，LinkedList的性能会更加好一点。
```
```
  3.在索引中ArrayList的增加或者删除某个对象的运行过程？效率很低吗?解释一下为什么?
  答:原理就是2中提到的;
```
```
  4.ArrayList中大量使用了Arrays.copyof()和System.arraycopy()方法,两个的区别:
  答:前者的方法中调用了后者,后者是个本地方法,复制数组特别高效;
  首先来看Arrays.copyof()方法。它有很多个重载的方法，但实现思路都是一样的，我们来看泛型版本的源码：
  public static <T> T[] copyOf(T[] original, int newLength) {
    return (T[]) copyOf(original, newLength, original.getClass());
  }
  很明显调用了另一个copyof方法，该方法有三个参数，最后一个参数指明要转换的数据的类型，其源码如下：
  public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
    T[] copy = ((Object)newType == (Object)Object[].class)
        ? (T[]) new Object[newLength]
        : (T[]) Array.newInstance(newType.getComponentType(), newLength);
    System.arraycopy(original, 0, copy, 0,
                     Math.min(original.length, newLength));
    return copy;
   }
   这里可以很明显地看出，该方法实际上是在其内部又创建了一个长度为newlength的数组，调用System.arraycopy()方法，将 原来数组中的元素复制到了新的数组中;
   下面来看System.arraycopy()方法。该方法被标记了native，调用了系统的C/C++代码，在JDK中是看不到的，但在openJDK中可以看到其源码。
   该函数实际上最终调用了C语言的memmove()函数，因此它可以保证同一个数组内元素的正确复制和移动，比一般的复制方法的实现效率要高很多，很适合用来批量处理数组。
   Java强烈推荐在复制大量数组元素时用该方法，以取得更高的效率。
```