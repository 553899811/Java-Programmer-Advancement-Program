[TOC]
# CentOS 7.4 使用手册

## 1.配置自定义静态IP
```
https://www.cnblogs.com/maowenqiang/articles/7727910.html
```
## 2.关闭防火墙
```
yum install iptables-services
systemctl stop firewalld.service
systemctl disable firewalld.service
https://www.cnblogs.com/maowenqiang/articles/7727910.html
```