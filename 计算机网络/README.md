<!-- GFM-TOC -->
   
<!-- GFM-TOC -->
## 1.网络层次划分
### 1.1 OSI/RM模型
```
  它将计算机网络体系结构的通信协议划分为七层;
  自下而上依次为：
  物理层（Physics Layer）、
  数据链路层（Data Link Layer）、
  网络层（Network Layer）、
  传输层（Transport Layer）、
  会话层（Session Layer）、
  表示层（Presentation Layer）、
  应用层（Application Layer）。
  其中第四层完成数据传送服务，上面三层面向用户。
```
```
   除了标准的OSI七层模型以外,常见的网络层次划分还有TCP/IP四层协议以及TCP/IP五层协议.
```
![](img/network-01.jpg)

## 2.OSI七层网络模型
![](img/network-02.gif)
## 2.1 物理层(Physical Layer)
```
   激活、维持、关闭通信端点之间的机械特性、电气特性、功能特性以及过程特性。
   该层为上层协议提供了一个传输数据的可靠的物理媒体。
   简单的说，物理层确保原始的数据可在各种物理媒体上传输。
   物理层记住两个重要的设备名称，中继器（Repeater，也叫放大器）和集线器。
```
## 2.2 数据链路层(Data Link Layer)
### 2.2.1 作用
```
   数据链路层在物理层提供的服务的基础上向网络层提供服务，其最基本的服务是将源自网络层来的数据可靠地传输到相邻节点的目标机网络层。
```