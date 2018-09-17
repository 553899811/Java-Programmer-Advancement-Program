<!-- GFM-TOC -->
* [1. HTTP1.1新特性](#1-http11新特性)
* [2. HTTP和HTTPS相比优缺点](#2-http和https相比优缺点)
<!-- GFM-TOC -->

![](https://github.com/553899811/NewBie-Plan/raw/master/计算机网络/img/面试题-1.jpg)
![](https://github.com/553899811/NewBie-Plan/raw/master/计算机网络/img/面试题-2.jpg)
![](https://github.com/553899811/NewBie-Plan/raw/master/计算机网络/img/面试题-3.jpg)
[一文读懂一个URL请求的过程是怎样的](https://juejin.im/post/5b83b0bfe51d4538c63131a8)

# 1. HTTP1.1新特性

   - 默认持久连接节省通信量，只要客户端服务端任意一端没有明确提出断开TCP连接，就一直保持连接，可以发送多次HTTP请求。

   - 管线化，客户端可以同时发出多个HTTP请求，而不用一个个等待响应。

   - 断点续传原理。

# 2. HTTP和HTTPS相比优缺点

   - 通信使用明文不加密，内容可能被窃听。

   - 不验证通信方身份，可能遭到伪装。

   - 无法验证报文完整性，可能被篡改。

   - HTTPS不完全普及的原因：由于多了几层加密验证，造成访问速度降低。
