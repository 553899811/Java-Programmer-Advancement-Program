# Java虚拟机常见面试问题
 - 常用参数
```
  -server 
  -Xmx2g 
  -Xms2g 
  -Xmn256m 
  -XX:PermSize=128m 
  -Xss256k 
  -XX:+DisableExplicitGC 
  -XX:+UseConcMarkSweepGC 
  -XX:+CMSParallelRemarkEnabled 
  -XX:+UseCMSCompactAtFullCollection 
  -XX:LargePageSizeInBytes=128m 
  -XX:+UseFastAccessorMethods 
  -XX:+UseCMSInitiatingOccupancyOnly 
  -XX:CMSInitiatingOccupancyFraction=70
```
 - 1.堆内存常见的分配策略?
   - https://blog.csdn.net/wangdongli_1993/article/details/81188055
   - https://blog.csdn.net/codejas/article/details/78752825
 ```
    [1]优先使用Eden区域
    [2]大对象直接放入老年代
       虚拟机提供了一个 -XX:PretenureSizeThreshold 参数，令大于这个设置值的对象直接在老年代进行分配内存。
    [3]长期存活的对象将进入老年代
      15岁 
    [4]动态判断对象年龄
       虚拟机还规定如果在 Survivor 中相同年龄的所有对象的大小的总和大于 Survivor 空间的一半，年龄大于或这等于该对象年龄的对象就可以直接进入到老年代。
    [5]空间分配担保
 ```
 - 2.如何判断对象是否死亡?
```
  
```