# NewBie-Plan

|           Java基础             |             操作系统                   |             网络               |             算法                     |               数据库                   |          Java虚拟机            |               多线程                 |                分布式                  |              工具              |                  编码实践             |                 后记                  |
| :----------------------------: | :----------------------------------: | :----------------------------: |     :------------------------------: | :----------------------------------: | :----------------------------: |   :------------------------------: | :----------------------------------: | :----------------------------: | :------------------------------: | :----------------------------------: |
| [:coffee:](#coffee-Java基础)    | [:computer:](#computer-操作系统)      | [:cloud:](#cloud-计算机网络)     | [:pencil2:](#pencil2-算法)        | [:floppy_disk:](#floppy_disk-数据库)  |[:art:](#art-Java虚拟机)         | [:couple:](#coffee-Java多线程) | [:bulb:](#bulb-分布式) | [:hammer:](#hammer-工具) | [:speak_no_evil:](#speak_no_evil-框架及实战) | [:memo:](#memo-后记) |
## 概述
```
  主要针对于现阶段Java开发工程师开启的学习推荐和辅助,主要会从Java基础(IO,Collection集合,反射,多态),MySQL,多线程,JVM,操作系统,计算机网络及部分高级应用等方面做较为详细的学习探究.
  
  不积跬步,无以至千里.学习,是一个拨乱反正的过程,逐步调整学习心态,逐渐发现最合适自己的学习方法和节奏是最关键的;
  不要想一口吃成一个胖子!
  各位,不要让自己发出 “书到用时方恨少的感叹” , 愿每一个有梦想的Coder都能成为引以为豪的人.
  希望有兴趣的同学能够加入我们,学习总结,一起成长!
```
## 目录
## :coffee: Java基础
#### 容器

  - Map
    - [深度剖析HashMap原理](/docs/notes/Java基础/Java-容器/Map/HashMap.md)<br>
    - [深度剖析LinkedHashMap原理](https://www.jianshu.com/p/8f4f58b4b8ab)
  - List  
    - [深度剖析ArrayList原理](/docs/notes/Java基础/Java-容器/Collection集合/基础知识/ArrayList.md)<br>
    - [深度剖析LinkedList原理](/docs/notes/Java基础/Java-容器/Collection集合/基础知识/LinkedList.md)<br>
  - Stack
    - [深度剖析Stack原理](/docs/notes/Java基础/Java-容器/Collection集合/基础知识/Stack.md)
  - Queue
    - [深度剖析PriorityQueue原理](https://blog.csdn.net/qq_35326718/article/details/72866180)

### 数据类型
 - String 
   - [深度剖析String原理](/docs/notes/Java基础/Java-数据类型/引用数据类型/String.md)
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
 - [剑指Offer](http://www.zhenganwen.top/posts/72fb66ce/)
 - [LeetCode刷题记录](https://github.com/553899811/LeetCode)
 - [算法与数据结构阶段性学习](https://www.geeksforgeeks.org/fundamentals-of-algorithms/)
## :floppy_disk: 数据库
 - MySQL
   - [MySQL基础教程](http://www.runoob.com/mysql/mysql-tutorial.html)
   - [MySQL阶段性学习](https://guobinhit.blog.csdn.net/column/info/16138/)
   - [MySQL优化](http://www.zhenganwen.top/posts/62645e84/)
   - [MySQL索引原理](https://blog.csdn.net/u013967628/article/details/84305511)
 - Redis
   - [Redis从入门到高可用](https://www.bilibili.com/video/av48611210)
## :art: Java虚拟机
 - [Java虚拟机（JVM）你只要看这一篇就够了！](https://blog.csdn.net/qq_41701956/article/details/81664921)
 - [Java虚拟机原理图解](https://blog.csdn.net/u010349169/column/info/jvm-principle)
 - [Java虚拟机阶段性学习](http://www.ityouknow.com/java.html)
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
 - [分布式理论基础](https://github.com/xingshaocheng/architect-awesome/blob/master/README.md#%E5%88%86%E5%B8%83%E5%BC%8F%E4%B8%80%E8%87%B4)
 - [分布式一致性算法](https://www.cnblogs.com/bangerlee/tag/%E5%88%86%E5%B8%83%E5%BC%8F%E7%B3%BB%E7%BB%9F/)
   - [2PC与3PC及一致性概念](https://www.cnblogs.com/bangerlee/p/5268485.html)
   - [CAP](https://www.cnblogs.com/bangerlee/p/5328888.html)
   - [Paxos](https://www.cnblogs.com/bangerlee/p/5655754.html)
   - [Raft与Zab](https://www.cnblogs.com/bangerlee/p/5991417.html)
## :hammer: 工具
 - [Git](/notes/工具及组件/Git.md)
 - [Java-Xmind 思维脑图](https://github.com/553899811/Java-Xmind)
## :speak_no_evil: 框架及实战
 - 框架
   - [SpringBoot](https://spring.io/projects/spring-boot)
     - [SpringBoot1.x学习目录](http://blog.didispace.com/spring-boot-learning-1x/)
     - [SpringBoot2.x学习目录](http://blog.didispace.com/spring-boot-learning-2x/)
     - [SpringBoot非官方教程](https://blog.csdn.net/forezp/column/info/15397)
   - [SpringCloud](https://spring.io/projects/spring-cloud)
     - [SpringCloud学习目录](http://blog.didispace.com/spring-cloud-learning/)
   - [Maven](http://maven.apache.org/)
     - [Maven](https://www.cnblogs.com/xdp-gacl/tag/Maven%E5%AD%A6%E4%B9%A0%E6%80%BB%E7%BB%93/)
 - 项目
   - [LeYou商城](https://space.bilibili.com/248011590/video) 
   - [Java秒杀系统方案优化](https://pan.baidu.com/s/1W-cMJT0Q7BDiwrywI9VucQ)
## :memo: 后记
### 贡献
 The repo is maintained by

 - [pengp](https://github.com/pengp)</br>
 - [songsir01](https://github.com/songsir01)</br>
 - [liulukuan](https://github.com/liulukuan)</br>
 - [mySohGit](https://github.com/mySohGit)
    
### 公众号
```
  记录个人成长感悟：
```
<center>
<img src="about/conghuajidan.jpg" width="20%" height="20%"/>
</center>
