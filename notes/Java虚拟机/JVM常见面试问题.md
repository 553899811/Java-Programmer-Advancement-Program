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
 
     - 1.优先使用Eden区域
      ```
       -XX:NewRatio 新生代和老年代的比值
       -XX:SurvivorRatio 设置两个Survivor区和Eden区的比值
      ```
     - 2.大对象直接放入老年代
      ```
       虚拟机提供了一个 -XX:PretenureSizeThreshold 参数，令大于这个设置值的对象直接在老年代进行分配内存。
      ```
     - 3.长期存活的对象将进入老年代
      ```
          每个对象有一个对象年龄计数器，与前面的对象的存储布局中的GC分代年龄对应。对象出生在Eden区、经过一次Minor GC后仍然存活，并能够被Survivor容纳，设置年龄为1，对象在Survivor区每次经过一次Minor GC，年龄就加1，当年龄达到一定程度（默认15），就晋升到老年代，虚拟机提供了-XX:MaxTenuringThreshold来进行设置。
      ``` 
     - 4.动态判断对象年龄
      ```
       虚拟机还规定如果在 Survivor 中相同年龄的所有对象的大小的总和大于 Survivor 空间的一半，年龄大于或这等于该对象年龄的对象就可以直接进入到老年代。
      ```
     - 5.空间分配担保

 - 2.如何判断对象是否死亡?
   - 是否可达 
     - 引用计数
     - 可达性分析
   - 2次筛选
     - finalize方法
 - 3.出发Full GC的情况
   - 1.调用System.gc();
   - 2.老年代空间不足
   - 3.GC担保失败
     ```
      在发生Minor GC之前，JVM会检查老年代最大可用的连续空间是否大于新生代所有对象总空间。如果条件成立，那么Minor GC是安全的。反之，如果不成立，那么要仍然要看HandlePromotionFailure值，是否允许担保失败。如果允许担保失败，那么会继续检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小。如果大于，则冒险尝试一次Minor GC，如果小于或者不允许担保失败，则要进行一次Full GC。
     ```  