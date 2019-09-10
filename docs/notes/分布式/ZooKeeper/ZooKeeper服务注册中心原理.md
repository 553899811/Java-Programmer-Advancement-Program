# ZooKeeper实现服务注册的原理
## 1.ZooKeeper中的节点
  ```
    ZooKeeper是一个树形结构的目录服务，支持变更推送，因此适合当Dubbo服务的注册中心。
 ```
   - 节点分类（根据维度来划分）:
     - 构成集群的机器，我们称之为机器节点。
     - 数据模型中的数据单元，称之为数据节点ZNode。
```
  ZooKeeper将所有数据存储在内存中，数据模型是一棵树(ZNode Tree)，由斜杠（/）进行分割的路径，就是一个ZNode，例如/foo/path1。每个ZNode上都会保存自己的数据内容，同时还会保存一系列属性信息。
```
```
  在ZooKeeper中，数据节点Znode可分为持久节点和临时节点两类，所谓持久节点是指一旦这个ZNode被创建了，除非主动进行ZNode的移除操作，否则这个ZNode将一直保存在ZooKeeper上。而临时节点就不一样了，它的生命周期和客户端会话绑定，一旦客户端会话失效，那么这个客户端创建的所有临时节点都会被移除。
```
![](/about/media/pic/zookeeper-node.jpg)
 - 上图为 ：基于ZooKeeper实现的注册中心节点结构示意
   - /dubbo:这是dubbo在ZooKeeper上创建的根节点；
   - /dubbo/com.foo.BarService:这是服务节点，代表了Dubbo的一个服务；
   - /dubbo/com.foo.BarService/providers:这是服务提供者的根节点，其子节点代表了每一个服务真正的提供者；
   - /dubbo/com.foo.BarService/consumers:这是服务消费者的根节点，其子节点代表每一个服务真正的消费者；

## 2.注册中心的工作流程
```
  接下来以上述的BarService为例，说明注册中心的工作流程。
```
### 2.1 服务提供方启动

服务提供者在启动的时候，会在ZooKeeper上注册服务。<font color='red'>所谓注册服务，其实就是在ZooKeeper的/dubbo/com.foo.BarService/providers节点下创建一个子节点，并写入自己的URL地址，</font> 这就代表了com.foo.BarService这个服务的一个提供者。

### 2.2 服务消费方启动

服务消费者在启动的时候，会向ZooKeeper注册中心订阅自己的服务。<font color='red'>其实，就是读取并订阅ZooKeeper上/dubbo/com.foo.BarService/providers节点下的所有子节点，并解析出所有提供者的URL地址来作为该服务地址列表。</font>
同时，**服务消费者还会在ZooKeeper的/dubbo/com.foo.BarService/consumers节点下创建一个临时节点，并写入自己的URL地址，这就代表了com.foo.BarService这个服务的一个消费者。**

### 2.3 消费者远程调用提供者
```
   服务消费者，从提供者地址列表中，基于软负载均衡算法，选一个提供者进行调用，如果调用失败，再选另一个提供者调用。
```

### 2.4 增加服务提供者
```
   增加提供者，也就是在providers下面新建子节点。一旦服务提供方有变动，zookeeper就会把最新的服务列表推送给消费者
```

### 2.5 减少服务提供者
  <font color='red'>**所有提供者在ZooKeeper上创建的节点都是临时节点，利用的是临时节点的生命周期和客户端会话相关的特性，因此一旦提供者所在的机器出现故障导致该提供者无法对外提供服务时，该临时节点就会自动从ZooKeeper上删除，同样，zookeeper会把最新的服务列表推送给消费者。** </font>

### 2.6 ZooKeeper宕机以后

消费者每次调用服务提供方是不经过ZooKeeper的，消费者只是从zookeeper那里获取服务提供方地址列表。所以当zookeeper宕机之后，不会影响消费者调用服务提供者，影响的是zookeeper宕机之后如果提供者有变动，增加或者减少，无法把最新的服务提供者地址列表推送给消费者，所以消费者感知不到。
