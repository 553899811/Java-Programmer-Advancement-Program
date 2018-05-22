<!-- GFM-TOC -->
* [JVM 学习进阶](#jvm-学习进阶)
    * [1.图文Class文件结构](#1图文class文件结构)
    * [2.堆内存布局](#2堆内存布局)
    * [3.关于字符串常量池的相关问题](#3关于字符串常量池的相关问题)
    * [4.垃圾回收](#4垃圾回收)
    * [5.类加载过程](#5类加载过程)
<!-- GFM-TOC -->


# JVM 学习进阶
## 1.图文Class文件结构
 - 核心: 经典 Java虚拟机原理图解</br>
  [https://blog.csdn.net/column/details/jvm-principle.html](https://blog.csdn.net/column/details/jvm-principle.html ) 
 - 配合问题解决:
   - 符号引用和直接引用
   [https://blog.csdn.net/u014656992/article/details/51107127](https://blog.csdn.net/u014656992/article/details/51107127 )
   - JAVA String对象和字符串常量的关系解析
   [https://blog.csdn.net/sureyonder/article/details/5569366](https://blog.csdn.net/sureyonder/article/details/5569366)
   - 变量、字面量、常量的区别
   [https://blog.csdn.net/chelen_jak/article/details/49814353](https://blog.csdn.net/chelen_jak/article/details/49814353)
   - CONSTANT_Fieldref_info和字段表中的field_info的区别?
   [https://www.zhihu.com/question/58697825](https://www.zhihu.com/question/58697825)
   - 此篇从最基础的helloword讲解Class结构 从字节码层面看helloword
   [https://blog.csdn.net/wangtaomtk/article/details/52263989](https://blog.csdn.net/wangtaomtk/article/details/52263989)
## 2.堆内存布局
  - jvm内存堆布局图解分析
  [http://www.cnblogs.com/WJ5888/p/4374791.html](http://www.cnblogs.com/WJ5888/p/4374791.html)
  - 从jvm角度看对象创建过程</br>
  [https://yq.aliyun.com/articles/388902?utm_content=m_40612](https://yq.aliyun.com/articles/388902?utm_content=m_40612)
## 3.关于字符串常量池的相关问题
 - 对于JVM中方法区，永久代，元空间以及字符串常量池的迁移和string.intern方法</br>
 [http://www.cnblogs.com/hadoop-dev/p/7169252.html](http://www.cnblogs.com/hadoop-dev/p/7169252.html)
 - Java字符串池（String Pool）深度解析
 [https://www.cnblogs.com/fangfuhai/p/5500065.html](https://www.cnblogs.com/fangfuhai/p/5500065.html)
 - Java中几种常量池的区分</br>
 [https://www.cnblogs.com/holos/p/6603379.html](https://www.cnblogs.com/holos/p/6603379.html)
 - JAVA String对象和字符串常量的关系解析
 [https://blog.csdn.net/sureyonder/article/details/5569366](https://blog.csdn.net/sureyonder/article/details/5569366)
 - Java内存模型</br>
 [https://www.cnblogs.com/ITtangtang/p/3976820.html](https://www.cnblogs.com/ITtangtang/p/3976820.html)
 - Java 中new String("字面量") 中 "字面量" 是何时进入字符串常量池的?
 [https://www.zhihu.com/question/55994121/answer/147296098](https://www.zhihu.com/question/55994121/answer/147296098)
## 4.垃圾回收
 - JVM初探：内存分配、GC原理与垃圾收集器
 [http://www.importnew.com/23035.html](http://www.importnew.com/23035.html)
 - JAVA四种引用类型
 [https://www.cnblogs.com/fengbs/p/7019687.html](https://www.cnblogs.com/fengbs/p/7019687.html)
 - JVM类加载以及执行的实战
 [http://www.cnblogs.com/cz123/p/6937212.html](http://www.cnblogs.com/cz123/p/6937212.html)
 - JVM内存堆布局图解分析
 [https://www.cnblogs.com/SaraMoring/p/5713732.html](https://www.cnblogs.com/SaraMoring/p/5713732.html)
 - 栈和堆的特点
 [https://blog.csdn.net/qw_xingzhe/article/details/35569841](https://blog.csdn.net/qw_xingzhe/article/details/35569841)
```
  分析了 字符串以及基础类型的内存分配问题
```
## 5.类加载过程
 - 类加载过程</br>
 [https://yq.aliyun.com/articles/377198](https://yq.aliyun.com/articles/377198)
 [https://blog.csdn.net/ithomer/article/details/6252552](https://blog.csdn.net/ithomer/article/details/6252552)
