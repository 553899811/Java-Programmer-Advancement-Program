<!-- GFM-TOC -->
* [1. HTTP1.1新特性](#1-http1.1新特性)
<!-- GFM-TOC -->
![](https://github.com/553899811/NewBie-Plan/raw/master/计算机网络/img/面试题-1.jpg)
![](https://github.com/553899811/NewBie-Plan/raw/master/计算机网络/img/面试题-2.jpg)
![](https://github.com/553899811/NewBie-Plan/raw/master/计算机网络/img/面试题-3.jpg)

# 1. HTTP1.1新特性

   - 默认持久连接节省通信量，只要客户端服务端任意一端没有明确提出断开TCP连接，就一直保持连接，可以发送多次HTTP请求。

   - 管线化，客户端可以同时发出多个HTTP请求，而不用一个个等待响应。

   - 断点续传原理。
