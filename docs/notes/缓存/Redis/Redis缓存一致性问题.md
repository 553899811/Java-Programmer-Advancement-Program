<a name="SIgxk"></a>
# 1.数据更新场景
```cpp
失效：应用程序先从cache取数据，没有得到，则从数据库中取数据，成功后，放到缓存中;
命中：应用程序从cache中取数据，取到后返回;
更新：先把数据存到数据库中，成功后，再让缓存失效;
```
<a name="hHTiQ"></a>
## 1.1 为啥对缓存的操作是失效 而不是更新 ？
多线程写缓存容易 造成脏数据，谁知道 那个线程快 哪个慢呢？
<a name="zrQ1A"></a>
## 1.2 为啥不同时对DB和cache 操作呢 ？
事务一旦失败就会造成数据不一致的问题。
<a name="TXuAn"></a>
# 2.数据不一致的原因
<a name="p5Mat"></a>
## 2.1 逻辑失败造成的数据不一致
在并发的情况下，无论是先删除缓存还是更新数据库，还是更新数据库再失效缓存，都会出现数据不一致的情况。<br />主要是因为 异步读写请求在并发情况下的操作时序导致的数据不一致，称之为“逻辑失败”。<br />解决这种因为并发导致的问题，核心的解决思路是将异步操作进行串行化。
<a name="nHxtJ"></a>
## 2.2 物理失败造成的数据不一致
在Cache Aside Pattern 中先更新数据库再删除缓存以及异步双闪策略等，如果删除缓存失败时都出现数据不一致的情况。<br />但是数据库更新以及缓存操作是没办法放到一个事务中，一般来说，使用缓存是分布式缓存如果缓存服务很耗时，那么将更新数据库以及失效缓存放到一个事务中，就会造成大量的数据库链接挂起，严重的降低系统性能，甚至会因为数据库链接数过多，导致系统奔溃。像这种因为缓存操作失效，导致的数据不一致称之为“物理失效”。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628219872133-fea33dfc-1a12-453c-9eb4-66f2264ee665.png#clientId=ud536f1b9-ff7c-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=228&id=ud2c681d5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=455&originWidth=886&originalType=binary&ratio=1&rotation=0&showTitle=false&size=97125&status=done&style=none&taskId=u611c04af-016b-43d1-b9bc-39e123ecb4f&title=&width=443)
<a name="udhuz"></a>
# 3. 缓存策略
| Cache Aside 更新策略 | 先更新数据库，然后再删除缓存； |
| --- | --- |
| Read/Write Through 更新策略 | 先更新缓存，缓存负责同步更新数据库； |
| Write Behind Caching 更新策略 | 先更新缓存，缓存定时异步更新数据库； |

<a name="U6ohI"></a>
## 3.1 Cache Aside 更新策略
```sql
失效：应用程序先从cache取数据，没有得到，则从数据库中取数据，成功后，放到缓存中。
命中：应用程序从cache中取数据，取到后返回;
更新：先把数据存到数据库中，成功后，再让缓存失效
```
<a name="STGas"></a>
### 3.1.1 先更新数据库，再更新缓存
<a name="bJg3z"></a>
#### 原因一：线程安全问题

同时有请求A和请求B进行更新操作，那么会出现<br />（1）线程A更新了数据库<br />（2）线程B更新了数据库<br />（3）线程B更新了缓存<br />（4）线程A更新了缓存<br />这就出现请求A更新缓存应该比请求B更新缓存早才对，但是因为网络等原因，B却比A更早更新了缓存。这就导致了脏数据，因此不考虑。

<a name="leB4O"></a>
### 3.1.2 先删除缓存，再更新数据库
有一个请求A进行更新操作，另一个请求B进行查询操作。（A的写事务 耗费的时间 远大于 B的查询事务）<br />（1）请求A进行写操作，删除缓存<br />（2）请求B查询发现缓存不存在<br />（3）请求B去数据库查询得到旧值<br />（4）请求B将旧值写入缓存<br />（5）请求A将新值写入数据库<br />此时缓存依旧是旧值，还是有脏数据。<br />**write:**
```sql
public void write(String key,Object data){
		redis.delKey(key);
	    db.updateData(data);
	    Thread.sleep(1000);
	    redis.delKey(key);
}
```
**read:**
```sql
public void read(String key){
		 if(redis.get(key)==null){
         return db.get(key);
     }else {
         return redis.get(key);
     }
}
```
<a name="MdjSM"></a>
#### 解决方案：延时双删策略<br />（1）先淘汰缓存<br />（2）再写数据库（这两步和原来一样）<br />（3）休眠1秒，再次淘汰缓存

双删在保证数据一致性上代价比较大。
<a name="fdVOk"></a>
#### 疑问一：延时时间如何确定？
```sql
确保读请求结束，写请求可以删除读请求造成的缓存脏数据。
几百ms 足矣;
```
<a name="H4WAj"></a>
#### 疑问二：采用同步淘汰策略，吞吐量降低怎么办？
```sql
将第二次删除改为异步。起一个线程异步删除，写请求就不用沉睡
```
<a name="XjHkx"></a>
#### 疑问三：第二次删除失败了，怎么办？
具体解决方案，且看第(3)种更新策略的解析。

<a name="UPMv8"></a>
### 3.1.3 先更新数据库，再删除缓存
<a name="YDZNR"></a>
#### 3.1.3.2 存在的问题
```sql
假设这会有两个请求，一个请求A做查询操作，一个请求B做更新操作，那么会有如下情形产生
（1）缓存刚好失效
（2）请求A查询数据库，得一个旧值;
（3）请求B将新值写入数据库
（4）请求B删除缓存
（5）请求A将查到的旧值写入缓存
ok，如果发生上述情况，确实是会发生脏数据。


```
这种情况 出现的概率很低。因为先天性 读 要比写快很多。 （2）要比（3）先执行完后写入缓存，（3）执行完后将缓存删掉。
<a name="C9THb"></a>
#### 如何解决如此抬杠的问题？
```sql
采用3.2 中的延时双删策略。
```

<a name="tcmGt"></a>
### 3.1.4 对于第二次删缓存失败了，怎么办 ？
提供一个保障的重试机制即可，能保证最后把缓存删掉是最后的目的。<br />这里给出两套方案。

<a name="xl8cB"></a>
#### 3.1.4.1  业务中使用RocketMQ异步删缓存
ESC系统就是这么搞得，一个缓存一个TOPIC，很是麻烦。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629006541381-08d855d6-f28f-463c-830e-8670eb2dd559.png#clientId=ue72a421f-5b92-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=ubc6cf61f&margin=%5Bobject%20Object%5D&name=image.png&originHeight=426&originWidth=656&originalType=url&ratio=1&rotation=0&showTitle=false&size=36358&status=done&style=none&taskId=u24f8a5e8-4c8b-40bf-b36a-80710ad365d&title=)<br />流程如下所示<br />（1）更新数据库数据；<br />（2）缓存因为种种问题删除失败<br />（3）将需要删除的key发送至消息队列<br />（4）自己消费消息，获得需要删除的key<br />（5）继续重试删除操作，直到成功
<a name="E7HTb"></a>
##### 缺点
```sql
对业务代码侵入比较严重
```
<a name="pHv1G"></a>
#### 3.1.4.2 binlog + RocketMQ异步删缓存
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629006610784-bf09b7b2-05a6-48e3-81d6-25a2aa6897af.png#clientId=ue72a421f-5b92-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=u825b2a37&margin=%5Bobject%20Object%5D&name=image.png&originHeight=505&originWidth=743&originalType=url&ratio=1&rotation=0&showTitle=false&size=61753&status=done&style=none&taskId=u94b7ed3b-c991-40a9-a0c7-1d94444f864&title=)<br />流程如下图所示：<br />（1）更新数据库数据<br />（2）数据库会将操作信息写入binlog日志当中<br />（3）订阅程序提取出所需要的数据以及key<br />（4）另起一段非业务代码，获得该信息<br />（5）尝试删除缓存操作，发现删除失败<br />（6）将这些信息发送至消息队列<br />（7）重新从消息队列中获得该数据，重试操作。

<a name="UCdVV"></a>
##### 优点
```sql
没有代码侵入。只需要监听数据库更新，之后使用单独的消息处理流程重试至成功。
```
<a name="bVRPw"></a>
## 3.2 Read/Write Through 更新策略
<a name="t5g43"></a>
### 3.2.1 Read Through 
不同点在于程序不需要再去管理从哪去读数据（缓存还是数据库）。相反它会直接从缓存中读数据，该场景下是缓存去决定从哪查询数据。当我们比较两者的时候这是一个优势因为它会让程序代码变得更简洁。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629438602058-6f5a8050-407f-44cb-a4a5-953e9e491156.png#clientId=u814dbc7c-a19d-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=u818592b9&margin=%5Bobject%20Object%5D&name=image.png&originHeight=792&originWidth=624&originalType=url&ratio=1&rotation=0&showTitle=false&size=127988&status=done&style=none&taskId=ub5525c25-8aa3-43f0-b40c-ff82a30b5c0&title=)
<a name="nGJw4"></a>
### 3.2.2 Write-Through
Write-Through下所有的写操作都经过缓存，每次我们向缓存中写数据的时候，缓存会把数据持久化到对应的数据库中去，且这两个操作都在一个事务中完成。因此，只有两次都写成功了才是最终写成功了。这的确带来了一些写延迟但是它保证了数据一致性。<br />同时，因为程序只和缓存交互，编码会变得更加简单和整洁，当你需要在多处复用相同逻辑的时候这点变的格外明显。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629438648568-b6a51a7f-9eb4-4249-acca-f87be0b859d1.png#clientId=u814dbc7c-a19d-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=u7be0f59f&margin=%5Bobject%20Object%5D&name=image.png&originHeight=562&originWidth=293&originalType=url&ratio=1&rotation=0&showTitle=false&size=54755&status=done&style=none&taskId=ue720d72c-b9e2-4317-bdb8-1062575be96&title=)<br />当使用Write-Through的时候一般都配合使用Read-Through。<br />Write-Through适用情况有：

- 需要频繁读取相同数据
- 不能忍受数据丢失（相对Write-Behind而言）和数据不一致

**Write-Through的潜在使用例子是银行系统。**
<a name="nmap4"></a>
## 3.3 Write Behind Caching 更新策略
Write-Behind和Write-Through在“程序只和缓存交互且只能通过缓存写数据”这一点上很相似。**不同点在于Write-Through会把数据立即写入数据库中，而Write-Behind会在一段时间之后（或是被其他方式触发）把数据一起写入数据库，这个异步写操作是Write-Behind的最大特点**。<br />数据库写操作可以用不同的方式完成，其中一个方式就是收集所有的写操作并在某一时间点（比如数据库负载低的时候）批量写入。另一种方式就是合并几个写操作成为一个小批次操作，接着缓存收集写操作（比如5个）一起批量写入。

异步写操作极大的降低了请求延迟并减轻了数据库的负担。**同时也放大了数据不一致的。**比如有人此时直接从数据库中查询数据，但是更新的数据还未被写入数据库，此时查询到的数据就不是最新的数据。

<a name="jDpkT"></a>
### 3.3.1 例子
使用例子，某一个页面的数据 查询的QPS达到了3000+， 这么高的QPS，如果同时修改 数据库 直接宕机，先操作Redis ，然后使用MQ异步修改 数据库。
<a name="TJxQQ"></a>
# 引用
[https://www.cnblogs.com/rjzheng/p/9096228.html](https://www.cnblogs.com/rjzheng/p/9096228.html)<br />[https://blog.csdn.net/cywosp/article/details/23397179/](https://blog.csdn.net/cywosp/article/details/23397179/)
