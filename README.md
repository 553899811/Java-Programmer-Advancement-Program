# NewBie-Plan

|           Java基础             |             操作系统                   |             网络               |             算法                     |               数据库                   |          Java虚拟机            |               多线程                 |                分布式                  |              工具              |                  编码实践             |                 后记                  |
| :----------------------------: | :----------------------------------: | :----------------------------: |     :------------------------------: | :----------------------------------: | :----------------------------: |   :------------------------------: | :----------------------------------: | :----------------------------: | :------------------------------: | :----------------------------------: |
| [:coffee:](#coffee-Java基础)    | [:computer:](#computer-操作系统)      | [:cloud:](#cloud-计算机网络)     | [:pencil2:](#pencil2-算法)        | [:floppy_disk:](#floppy_disk-数据库)  |[:art:](#art-Java虚拟机)         | [:couple:](#coffee-Java多线程) | [:bulb:](#bulb-分布式) | [:hammer:](#hammer-工具) | [:speak_no_evil:](#speak_no_evil-框架及实战) | [:memo:](#memo-后记) |
## 概述
```
  不积跬步,无以至千里.
  人类真正的强大是自我改变.
```
## 目录
## :coffee: Java基础
#### 容器

  - Map
    - [HashMap原理](/docs/notes/Java基础/Java-容器/Map/HashMap.md)<br>
    - [LinkedHashMap原理](https://www.jianshu.com/p/8f4f58b4b8ab)
  - List  
    - [ArrayList原理](/docs/notes/Java基础/Java-容器/Collection集合/基础知识/ArrayList.md)<br>
    - [LinkedList原理](/docs/notes/Java基础/Java-容器/Collection集合/基础知识/LinkedList.md)<br>
  - Stack
    - [Stack原理](/docs/notes/Java基础/Java-容器/Collection集合/基础知识/Stack.md)
  - Queue
    - [PriorityQueue原理](https://blog.csdn.net/qq_35326718/article/details/72866180)

### 数据类型
 - String 
   - [String原理](/docs/notes/Java基础/Java-数据类型/引用数据类型/String.md)
### 设计模式
 - [23种设计模式详细说明](http://c.biancheng.net/design_pattern/)
    - [单例模式](/notes/设计模式/单例模式.md)
## :computer: 操作系统
 - [操作系统基础知识总结](https://blog.csdn.net/qq_35564813/article/details/80651259)
 - [操作系统阶段性学习](https://blog.csdn.net/qq_31278903/article/category/7954154)
 - [Linux入门教程](http://c.biancheng.net/linux_tutorial/)
## :cloud: 计算机网络
 - [计算机网络基础知识总结](https://blog.csdn.net/qq_34337272/article/details/81776275)
 - [计算机网络阶段性学习](https://blog.csdn.net/qq_35533401/article/category/7507100/)
## :pencil2: 算法
 - 基础算法
   - [自我总结-算法与数据结构](https://github.com/553899811/Algorithm-And-DataStructure)
   - [LeetCode刷题记录](https://github.com/553899811/LeetCode)
   - [剑指Offer](https://liweiwei1419.github.io/sword-for-offer/)
   - [算法与数据结构阶段性学习](https://www.geeksforgeeks.org/fundamentals-of-algorithms/)
 - 算法应用
   - [LRU缓存淘汰策略]()
## :floppy_disk: 数据库
 - MySQL
   - [MySQL阶段性学习](https://guobinhit.blog.csdn.net/column/info/16138/)
   - [MySQL索引原理](/docs/notes/数据库/MySQL/索引原理分析.md)
   - [MySQL索引失效及优化策略](/docs/notes/数据库/MySQL/索引失效及优化策略.md)
 - Redis
   - [Redis命令](http://redisdoc.com/)
   - [Redis分布式锁原理](/docs/notes/分布式/分布式锁/分布式锁的实现.md)
## :art: Java虚拟机
 - [Java内存结构](docs/notes/Java虚拟机/Java内存区域.md)
 - [HotSpot 虚拟机对象探秘]()
 - [垃圾收集策略与算法]()
 - [HotSpot 垃圾收集器]()
## :couple: Java多线程
 - [死磕Java并发](http://cmsblogs.com/?p=2611)
 - [Java并发性与多线程介绍目录](http://ifeve.com/java-concurrency-thread-directory/)
 - [Java并发之AQS详解](https://www.cnblogs.com/waterystone/p/4920797.html) 
 - 并发数据结构
   - Map
     - [深度剖析ConcurrentHashMap原理](http://www.importnew.com/28263.html)
   - Queue
     - [深度剖析ArrayBlockingQueue原理](https://blog.csdn.net/zzp_403184692/article/details/8021615)
     - [深度剖析LinkedBlockingQueue原理](https://blog.csdn.net/tonywu1992/article/details/83419448)
## :bulb: 分布式
 - [ZooKeeper专题](docs/notes/分布式/ZooKeeper/)
 - [分布式理论基础](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#%E5%88%86%E5%B8%83%E5%BC%8F%E4%B8%80%E8%87%B4)
 - [分布式一致性算法](https://www.cnblogs.com/bangerlee/tag/%E5%88%86%E5%B8%83%E5%BC%8F%E7%B3%BB%E7%BB%9F/)
   - [2PC与3PC及一致性概念](https://www.cnblogs.com/bangerlee/p/5268485.html)
   - [CAP](https://www.cnblogs.com/bangerlee/p/5328888.html)
   - [Paxos](https://www.cnblogs.com/bangerlee/p/5655754.html)
   - [Raft与Zab](https://www.cnblogs.com/bangerlee/p/5991417.html)
## :hammer: 工具
 - [Git](docs/notes/工具及组件/Git.md)
 - [Java-Xmind 思维脑图](https://github.com/553899811/Java-Xmind)
## :speak_no_evil: 框架及实战
 - 框架
   - [SpringBoot](https://spring.io/projects/spring-boot)
     - [SpringBoot学习教程](http://cmsblogs.com/?p=2919)
     - [SpringBoot常见面试题](/docs/notes/基础框架/Spring/SpringBoot常见面试题.md)
   - [SpringCloud](https://spring.io/projects/spring-cloud)
     - [SpringCloud学习目录](http://blog.didispace.com/spring-cloud-learning/)
     - [SpringCloud组件原理](/docs/notes/基础框架/Spring/SpringCloud组件原理.md)
   - [SpringMVC]
     - [SpringMVC原理](/docs/notes/基础框架/Spring/SpringMVC原理.md)
   - 其他应用
     - [自定义注解原理及实现](/docs/notes/基础框架/自定义注解原理及实现.md)
 - 项目实战
   - [LeYou商城](https://space.bilibili.com/248011590/video) 

## 鸣谢列表
### Code Contributors
<a href="https://github.com/553899811/NewBie-Plan/graphs/contributors"><img src="https://opencollective.com/NewBie-Plan/contributors.svg?width=890&button=false" /></a>

