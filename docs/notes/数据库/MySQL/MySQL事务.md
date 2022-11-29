<a name="Kyk8G"></a>
# 1.事务定义及特征
- 事务：一个最小的不可再分的工作单元；通常一个事务对应一个完整的业务(例如银行账户转账业务，该业务就是一个最小的工作单元)
- 一个完整的业务需要批量的DML(insert、update、delete)语句共同联合完成
- 事务只和DML语句有关，或者说DML语句才有事务。这个和业务逻辑有关，业务逻辑不同，DML语句的个数不同；
<a name="IDXH1"></a>
## 1.1 事务的四大特征

- 原子性（A）：事务的最小单位，不可再分。
- 一致性（C）：事务要求所有的DML语句操作的时候，必须保证同时成功或者同时失败。
- 隔离性（I） ：事务A和事务B之间具备隔离性。
- 持久性（D）：是事务的保证，事务终结的标致（内存中的数据持久到硬盘文件中）。

<a name="Hyh5J"></a>
# 2.事务引发的问题
<a name="bGA99"></a>
## 2.1 脏读（读到了未提交的数据）
指  一个事务读取了另外一个事务未提交的数据。此时如果回滚，前者两次读取同一个SQL获取的数据是不一样的。<br />【1】A事务首次查询到id=1 的数据为2，A事务未结束， B事务 将id=1的数据修改为3，B事务并未提交。<br />【2】A事务再次查询id=1的数据，发现数据发生了变化。
<a name="vmhDU"></a>
## 2.2 幻读（重点在于多了数据）
在一个事务的两次查询中数据不一致。例如 有一个事务查询了几列数据，而另一个事务却在此时**插入(insert)**了新的几列数据，先前的事务在接下来的查询中，**就会发现有几列数据是他之前所没有的。**
```java
在一个事务中使用相同的 SQL 两次读取，第二次读取到了其他事务新插入的行。
```
![](https://cdn.nlark.com/yuque/0/2021/png/177460/1617687962073-6fabba2f-e99a-4ed7-9522-e5da40ebe00b.png#crop=0&crop=0&crop=1&crop=1&height=493&id=rXnrS&originHeight=493&originWidth=555&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=555)
<a name="QJHHu"></a>
## 2.3 不重复读（重点在于读了别的已提交事务
<a name="kvKpa"></a>
## B修改的数据，事务A前后两次读不一致）
不可重复读是指对于数据库中的某个数据，一个事务范围内的多次查询却返回了不同的结果，这是由于在查询过程中，数据被另外一个事务修改并提交了。<br />指在一个事务内，多次读同一数据。在这个事务还没有结束时，另外一个事务也访问该同一数据。那么，在第一个事务中的两次读数据之间，由于第二个事务的**修改(update) 并事务提交**，那么第一个事务在同一个事务中两次读到的的数据可能是不一样的。这样就发生了在一个事务内两次读到的数据是不一样的，因此称为是不可重复读

![](https://cdn.nlark.com/yuque/0/2021/png/177460/1617687992613-4533a935-10e6-4faa-830d-49cbb9bb17b0.png#crop=0&crop=0&crop=1&crop=1&height=455&id=TCFBg&originHeight=455&originWidth=608&originalType=binary&ratio=1&rotation=0&showTitle=false&size=0&status=done&style=none&title=&width=608)


<a name="Rqdx1"></a>
# 3.事务的隔离级别
| 隔离级别 | 脏读 | 不可重复读 | 幻读 |
| --- | --- | --- | --- |
| 读未提交<br />READ-UNCOMMITTED | √ | √ | √ |
| 读已提交<br />READ-COMMITTED | × | √ | √ |
| 可重复读<br />REPEATABLE-READ | × | × | √ |
| 串行化<br />SERIALIZABLE | × | × | × |


| 读未提交（Read Uncommitted） | 最低的隔离级别，会读取到其他事务还未提交的内容，存在脏读。 |
| --- | --- |
| 读已提交（Read Committed） | 读取到的内容都是已经提交的，可以解决脏读，但是存在不可重复读。 |
| 可重复读（Repeatable Read） | 在一个事务中多次读取时看到相同的内容，可以解决不可重复读，但是存在幻读。但是在 InnoDB 中不存在幻读问题，对于快照读，InnoDB 使用 MVCC 解决幻读，对于当前读，InnoDB 通过 gap locks 或 next-key locks 解决幻读。 |
| 串行化（Serializable） | 最高的隔离级别，串行的执行事务，没有并发事务问题。 |


- 演示隔离级别 准备条件

创建一张金额表：
```sql
create table account
(
    id     bigint auto_increment
        primary key,
    amount bigint       null,
    name   varchar(128) null
);
```
预先写入一条数据;

| id | amount | name |
| --- | --- | --- |
| 1 | 1100 | A |

<a name="Np49V"></a>
## 3.1 读未提交 Read Uncommitted

<a name="idgDC"></a>
### 3.1.1 读未提交 造成的脏读
<a name="eTWNW"></a>
#### 步骤一：DataGrip 事务模式设置为 手动，，事务隔离级别设置为 读未提交；
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120323406-834bb700-0ac5-4af6-805b-5f1a0974b1b6.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=265&id=Qo85V&margin=%5Bobject%20Object%5D&name=image.png&originHeight=530&originWidth=751&originalType=binary&ratio=1&rotation=0&showTitle=false&size=33402&status=done&style=none&taskId=u5f8adb72-9e10-4245-8b6c-8eb52a173da&title=&width=375.5)
<a name="DTvR8"></a>
#### 步骤二：当前表 数据：
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120354494-d28e2fab-3b0e-4616-8e41-b69dd6402518.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=99&id=BefGW&margin=%5Bobject%20Object%5D&name=image.png&originHeight=197&originWidth=485&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11915&status=done&style=none&taskId=u5a7c34e8-d62d-4081-9b24-95222a7bc2f&title=&width=242.5)

<a name="hmVnx"></a>
#### 步骤三：console 查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120383960-ad60b345-af4a-49fa-8c85-d70e59b2db9a.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=366&id=oxnMy&margin=%5Bobject%20Object%5D&name=image.png&originHeight=732&originWidth=563&originalType=binary&ratio=1&rotation=0&showTitle=false&size=26888&status=done&style=none&taskId=ud1c7290a-cd83-4de2-8072-642f10115d7&title=&width=281.5)
<a name="pz6cy"></a>
#### 步骤四：console 1 操作更新
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120413072-34851281-af99-45b9-aea7-327e636bb628.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=252&id=BoRa8&margin=%5Bobject%20Object%5D&name=image.png&originHeight=503&originWidth=606&originalType=binary&ratio=1&rotation=0&showTitle=false&size=50493&status=done&style=none&taskId=u0c117f11-61b4-4af4-b328-40d639a9945&title=&width=303)
<a name="DgPny"></a>
#### 步骤五：console 再次查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120445672-635dfc1e-f625-44ef-81e8-61e6a0538830.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=171&id=vlKVC&margin=%5Bobject%20Object%5D&name=image.png&originHeight=342&originWidth=484&originalType=binary&ratio=1&rotation=0&showTitle=false&size=22269&status=done&style=none&taskId=ue4bdde2b-cec5-40c4-88a5-a57714c358a&title=&width=242)<br />读到了 未提交的变动
<a name="Ym64A"></a>
#### 步骤六：console 1: 事务回滚
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120491194-fd819916-bc04-4929-bd5d-ac532813ef17.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=329&id=hn8Bg&margin=%5Bobject%20Object%5D&name=image.png&originHeight=657&originWidth=907&originalType=binary&ratio=1&rotation=0&showTitle=false&size=68822&status=done&style=none&taskId=u7e9168a4-0198-4b1b-a9c3-a37695ba033&title=&width=453.5)
<a name="EQc3T"></a>
#### 步骤七：console 再次查询

![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628120548220-9001a434-07d8-4790-a2a2-9032658dc07b.png#clientId=u366d9fd9-6acc-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=224&id=laJCD&margin=%5Bobject%20Object%5D&name=image.png&originHeight=447&originWidth=487&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24087&status=done&style=none&taskId=u85792215-95fb-4c98-bd2d-afe25106082&title=&width=243.5)

<a name="SA1zC"></a>
### 3.1.2 读未提交 造成的幻读
上面同样的操作 做一个 insert 语句，读了别人 未提交的 数据，多读，别人一回滚 就发现又变少了。
<a name="5728Z"></a>
### 3.1.3 读未提交造成的不可重复读
很好理解，不可重复读 意思是前后 两次读到的不一致。肯定不一致呀。<br />都会造成脏读，更何况 不可重复读， console 1 只要修改了，console 就会查询到。怎么能做到重复读 ？
<a name="sHYZg"></a>
## 3.2 读已提交 Read Commited

<a name="ce5ca"></a>
### 3.2.1 读已提交造成的不可重复读
<a name="RnWEZ"></a>
#### 步骤一： 事务模式设置为 读已提交
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131060629-25edcbdb-962b-4447-8dcc-6e1f738ee885.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=178&id=u0ec62f88&margin=%5Bobject%20Object%5D&name=image.png&originHeight=355&originWidth=459&originalType=binary&ratio=1&rotation=0&showTitle=false&size=21384&status=done&style=none&taskId=uaa8198c3-c504-4256-a4fc-460b0be7bf9&title=&width=229.5)<br />Manual 事务手动提交

<a name="L5476"></a>
#### 步骤二：当前数据：
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131120451-38918483-26ea-4cbf-bf81-0dc33ccb27aa.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=53&id=uf24cae14&margin=%5Bobject%20Object%5D&name=image.png&originHeight=106&originWidth=384&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5721&status=done&style=none&taskId=u108cdae1-a50a-4d1d-9ccc-bad89e3ebd8&title=&width=192)
<a name="dia7u"></a>
#### 步骤三：console 查询
开启了一个事务，不要关闭<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131164688-e96a2eb8-f2d2-4d8f-a4ec-9c7f4c048af1.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=152&id=u69cb2aab&margin=%5Bobject%20Object%5D&name=image.png&originHeight=304&originWidth=415&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15946&status=done&style=none&taskId=u58aa2a5c-0629-49ea-9193-6c5bb9cb1e8&title=&width=207.5)
<a name="eAa0O"></a>
#### 步骤四：console 1 操作更新
在当前事务 更新并 查询 更新后的数据，发现age 变化了！<br />**但此时 事务还没有提交！**<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131277717-e6446cef-7921-418f-a202-01219bea1345.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=310&id=u445a4253&margin=%5Bobject%20Object%5D&name=image.png&originHeight=620&originWidth=415&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24130&status=done&style=none&taskId=uae276fc3-ba3c-422e-8b94-d9d53023cb1&title=&width=207.5)

<a name="DkO5P"></a>
#### 步骤五：console 再次查询
在console 无论重新开启多少次 事务查询，发现还是之前的数据。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131366816-ed72094c-cf9e-4cf6-b868-d5cb9a98d310.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=157&id=ua9216f58&margin=%5Bobject%20Object%5D&name=image.png&originHeight=313&originWidth=388&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15347&status=done&style=none&taskId=uf5884e1a-3c29-49f5-9a6e-0de6abc9aaf&title=&width=194)
<a name="fBEqT"></a>
#### 步骤六： 在console 1 中 提交事务

![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131403987-d156d289-a71d-4b88-a961-07d5d2bc343d.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=114&id=u12395c02&margin=%5Bobject%20Object%5D&name=image.png&originHeight=227&originWidth=640&originalType=binary&ratio=1&rotation=0&showTitle=false&size=19787&status=done&style=none&taskId=u0d70784e-bb04-4966-a8f0-7479ac58ecb&title=&width=320)
<a name="qBvBb"></a>
#### 步骤七： 在console中重新查询
发现 变化了，证明 在 事务一中读取了 事务2 已提交的更新。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628131448062-0b8101e9-f0c9-4ce9-9833-0f294028e58d.png#clientId=uc054d527-d12a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=155&id=u2dd89114&margin=%5Bobject%20Object%5D&name=image.png&originHeight=309&originWidth=388&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15393&status=done&style=none&taskId=ua951b5a3-3dec-49fd-a92c-2b2ba7491ec&title=&width=194)
<a name="ZXJLE"></a>
### 3.2.2 读已提交造成的幻读
根据 3.2.1 可以得知 ，console 在 一个事务中 可以 读到 console 1 中已提交的所有变动。<br />那么在console 中 第一次查询 id >1 的记录有2条，然后再 console 1中新插入一条后并提交事务<br />再次在console 中查询 id > 1 的记录 就有3条，因此 造成了 幻读。
<a name="Y1Tur"></a>
## 3.3 可重复读 Repeatable Read
<a name="EQgPk"></a>
### 3.3.1 解决不可不重复读
<a name="qKW0Z"></a>
#### 步骤一：设置事务隔离级别为 可重复读
手动提交<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628133052294-49e2544b-c323-4c9a-85c5-ea96382a91ff.png#clientId=ue1f1010d-3add-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=178&id=u6214bc02&margin=%5Bobject%20Object%5D&name=image.png&originHeight=356&originWidth=409&originalType=binary&ratio=1&rotation=0&showTitle=false&size=21313&status=done&style=none&taskId=ucb49516d-7df2-4931-bb38-6dd9651a073&title=&width=204.5)
<a name="pFI9A"></a>
#### 步骤二：当前数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628133119495-de709f76-6ea6-485b-94f0-f4d1a6b1a538.png#clientId=ue1f1010d-3add-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=167&id=u7fc7b58b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=334&originWidth=386&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15688&status=done&style=none&taskId=u5bd953b3-7a23-483f-863b-1b5f850aec6&title=&width=193)
<a name="H2hS7"></a>
#### 步骤三：console 查询
开启了查询事务，不要关闭<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628133119495-de709f76-6ea6-485b-94f0-f4d1a6b1a538.png#clientId=ue1f1010d-3add-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=167&id=tBMV5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=334&originWidth=386&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15688&status=done&style=none&taskId=u5bd953b3-7a23-483f-863b-1b5f850aec6&title=&width=193)

<a name="gr1dH"></a>
#### 步骤四：console 1 操作更新
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628140473490-86b72b11-5840-4d5a-a71b-225c341e0528.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=146&id=ua159aeaf&margin=%5Bobject%20Object%5D&name=image.png&originHeight=292&originWidth=527&originalType=binary&ratio=1&rotation=0&showTitle=false&size=19155&status=done&style=none&taskId=u45de35ea-6227-472b-af86-bb80fbc0c18&title=&width=263.5)<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628140488128-26bcd760-0e80-4c24-956f-354ebaf25a57.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=87&id=u1e9195ee&margin=%5Bobject%20Object%5D&name=image.png&originHeight=174&originWidth=383&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8312&status=done&style=none&taskId=u5eb8f2b2-a8a1-49bf-9c98-4d1970c79e7&title=&width=191.5)
<a name="TaHuV"></a>
#### 步骤五：console 再次查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628140981392-27e357be-8af1-4e6a-9340-454fc64a5527.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=164&id=u047543e1&margin=%5Bobject%20Object%5D&name=image.png&originHeight=328&originWidth=387&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15811&status=done&style=none&taskId=ub7ea26f5-7c28-4123-8a34-018b8f85805&title=&width=193.5)<br />发现没有变化，实现了可重复读;
<a name="zeSPc"></a>
#### 步骤六：console 事务提交后再次查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628141049355-89aa609a-63aa-4906-b0f5-929b2e32dee3.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=180&id=ueee17a8e&margin=%5Bobject%20Object%5D&name=image.png&originHeight=360&originWidth=382&originalType=binary&ratio=1&rotation=0&showTitle=false&size=16218&status=done&style=none&taskId=ue0ce19d6-9f3e-4150-8290-7ab810e051c&title=&width=191)
<a name="M97Q7"></a>
### 3.3.2 无法解决幻读
<a name="SHpx2"></a>
#### 3.3.2.1 正常避免幻读问题
<a name="Y9hsw"></a>
##### 步骤一：在console 开启一个查询事务
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628141161311-5974a86a-27a9-4828-baae-2c5ce2d5997e.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=193&id=u8621bb1e&margin=%5Bobject%20Object%5D&name=image.png&originHeight=385&originWidth=379&originalType=binary&ratio=1&rotation=0&showTitle=false&size=17142&status=done&style=none&taskId=ufbf729a4-6352-4457-8c6d-95fd75e8715&title=&width=189.5)

<a name="PKAxd"></a>
##### 步骤二：在console 1 中新增一条数据，查询更新后，并提交事务
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628141457874-2f7820ce-d5e2-4792-ac7a-2a4b839cc08f.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=256&id=u4e7f5294&margin=%5Bobject%20Object%5D&name=image.png&originHeight=512&originWidth=571&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30683&status=done&style=none&taskId=u6feaa018-7a25-4fdc-987a-ab7c7344553&title=&width=285.5)
<a name="wiuAb"></a>
##### 步骤三：在console 原事务中再次查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628141500459-3199a4d1-354f-4bdf-9402-3e64e19382e6.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=246&id=u31908af2&margin=%5Bobject%20Object%5D&name=image.png&originHeight=491&originWidth=442&originalType=binary&ratio=1&rotation=0&showTitle=false&size=20931&status=done&style=none&taskId=ud1bc9e5f-da47-4737-b8f9-82d811f1d92&title=&width=221)<br />发现数据没有变化，不会产生幻读（即多数据）
<a name="ubj90"></a>
##### 步骤四：在console 中将原事务提交后 重新查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628141559325-f8891f86-1dc1-4ad8-994a-a6a23b9ad7af.png#clientId=u5421fe0a-bd4d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=269&id=ude2ae0eb&margin=%5Bobject%20Object%5D&name=image.png&originHeight=538&originWidth=430&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24277&status=done&style=none&taskId=ud05106f4-8a49-41f2-ba83-f1d6202a8a1&title=&width=215)<br />发现了新增的数据
<a name="DOTfe"></a>
#### 3.3.2.2 无法避免幻读的问题
按照3.3.2.1 正常处理的流程，在 console 1 中新增一条数据后并提交
```cpp
insert student value (12345,'aaa',2);
```
在console 原事务中查询 是看不到新增数据的。<br />但是此时 做update 操作
```cpp
update student set age =10000 where id=12345
    id=12345，为新增数据
```
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1628150594858-6d0ad412-ddf3-42a9-b435-b986af291a3b.png#clientId=u964f052e-98c9-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=341&id=ue907c637&margin=%5Bobject%20Object%5D&name=image.png&originHeight=682&originWidth=534&originalType=binary&ratio=1&rotation=0&showTitle=false&size=38429&status=done&style=none&taskId=u1a2a8bca-c170-4eaa-8adc-7b802072117&title=&width=267)<br />然后再次执行查询就会发现 新增的数据 出现了，因此在同一个事务中可重复读 的事务隔离级别发生了幻读。<br />因为
<a name="jUc8S"></a>
##### 原因：
<a name="c7GuM"></a>
######  因为 MVCC 只能解决 快照读 下的幻读 ；
<a name="xxxJs"></a>
###### 当执行select操作是innodb默认会执行快照读.
而对于 update 、 insert、delete 都是采用当前读的模式。<br />在执行这几个操作时会读取最新的版本号记录，写操作后把版本号改为了当前事务的版本号，所以即使是别的事务提交的数据也可以查询到。
<a name="8noAH"></a>
## 3.4 串行化 Serializable

<a name="Fa81K"></a>
# 4.如何解决幻读问题？
1，Record Lock：单个行记录上的锁。<br />2，Gap Lock：间隙锁，锁定一个范围，但不包括记录本身。GAP锁的目的，是为了防止同一事务的两次当前读，出现幻读的情况。<br />3，Next-Key Lock：1+2，锁定一个范围，并且锁定记录本身。对于行的查询，都是采用该方法，主要目的是解决幻读的问题
<a name="p3Ng0"></a>
## 4.1 快照读情况下，使用MVCC 避免幻读

<a name="MV1TZ"></a>
## 4.2 当前读情况下，使用 next-key Lock 避免幻读
详见 MySQL 锁机制
<a name="wrmSJ"></a>
# 5.InnoDB 是如何实现事务的
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629945412528-7022a39c-c915-49d6-ae7f-b226013bc097.png#clientId=u1af99e30-1c41-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=u0dd98263&margin=%5Bobject%20Object%5D&name=image.png&originHeight=688&originWidth=788&originalType=url&ratio=1&rotation=0&showTitle=false&size=160018&status=done&style=none&taskId=u1da13f83-dd13-4806-9481-3c889d43dc1&title=)<br />Innodb 通过Buffer Pool ，Redo Log Buffer , Redo Log,Undo Log 来实现事务，以一个update 语句为例子：

- Innodb 在接收到一个update 语句的时候，会先根据条件找到数据所在的页，并将页缓存到Buffer Pool 中
- 执行update 语句的时候，修改Buffer Pool 中的数据，也就是内存中的数据。
- 针对update 语句在操作bufferpoll时，同时生产一个redo log 对象，并存入Redo Log buffer 中
- 针对update 语句生产undo log 日志，用于事务回滚。
- 如果事务提交之前，那么咋把Redolog 对象对象进行持久化，写入到redo 日志文件中（磁盘中）；
- 写binlog
- 定期将Buffer Pool中 脏页刷到磁盘中。（时机：【1】1s一次，可能丢事务【2】事务提交时【3】复合条件）
- 如果事务回滚，利用undo log 日志进行回滚。

[

](https://www.jianshu.com/p/8845ddca3b23)
<a name="PRGQ0"></a>
# 引用
[MVCC 快照读 引发 幻读原因](https://blog.csdn.net/weixin_36380516/article/details/115291399)<br />[https://www.jianshu.com/p/bdcf46cdef7c](https://www.jianshu.com/p/bdcf46cdef7c)<br />[https://zhuanlan.zhihu.com/p/48269420](https://zhuanlan.zhihu.com/p/48269420)<br />[https://www.jianshu.com/p/8845ddca3b23](https://www.jianshu.com/p/8845ddca3b23)

