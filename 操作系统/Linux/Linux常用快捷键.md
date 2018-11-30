[TOC]
# 一、vi编辑器相关

 - 光标移动到句首：^
 - 光标移动到句尾：$
 - 显示文本行号：:set nu
 - 光标移动到指定n行：nG

# 二、日志查看技巧

- 根据关键字查询tomcat日志并显示关键字在日志中的行号：cat -n catalina.out | grep "关键字"
- 根据关键字动态展示tomcat日志并显示关键字在日志中的行号：tail -f catalina.out | grep "关键字"
- 查询展示关键字上下n行日志并显示关键字在日志中的行号：cat -n catalina.out | grep -n "关键字"
- 查询到关键字所在行号后，编辑日志（vi catalina.out）
- 显示日志行号：：set nu
- 定位到关键字所在行号：nG
- 向上翻页：ctrl + b
- 向下翻页：ctrl + f

**然后就可以愉快的查看关键字附近的各种日志了。**

# 三、tomcat操作

## 1、查看文件列表

 - ll 相当于 ls -l
```
[root@kong songsir]# ll
total 8
drwxrwxrwx 9 root root 4096 Aug 22 15:05 apache-tomcat-8.0.33-25081-25080-25082
drwxrwxrwx 9 root root 4096 Jul 16 12:20 my-tomcat8-7175-7180-7179
```

## 2、查看带有某个关键字的tomcat的进程情况


```
[root@kong songsir]# ps axu | grep 25080
root      2326  2.8  4.3 2131008 88752 pts/0   Sl   16:56   0:05 /home/jdk1.8.0_73/bin/java -Djava.util.logging.config.file=/songsir/apache-tomcat-8.0.33-25081-25080-25082/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -server -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=512m -Djava.endorsed.dirs=/songsir/apache-tomcat-8.0.33-25081-25080-25082/endorsed -classpath /songsir/apache-tomcat-8.0.33-25081-25080-25082/bin/bootstrap.jar:/songsir/apache-tomcat-8.0.33-25081-25080-25082/bin/tomcat-juli.jar -Dcatalina.base=/songsir/apache-tomcat-8.0.33-25081-25080-25082 -Dcatalina.home=/songsir/apache-tomcat-8.0.33-25081-25080-25082 -Djava.io.tmpdir=/songsir/apache-tomcat-8.0.33-25081-25080-25082/temp org.apache.catalina.startup.Bootstrap start
root      2356  0.0  0.0  61172   784 pts/0    S+   16:59   0:00 grep 25080
```

