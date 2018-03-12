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