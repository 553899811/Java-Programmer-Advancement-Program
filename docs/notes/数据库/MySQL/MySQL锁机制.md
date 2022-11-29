<a name="LZ5gk"></a>
# 0.MySQL并发事务访问相同记录
<a name="EikeX"></a>
## 0.1 情景分类 
| 情景 | 造成问题 | 如何处理 |
| --- | --- | --- |
| 读-读 | 无 | 允许并发事务读 |
| 写-写 | 脏写 | 锁，排队执行 |
| 读-写 or 写-读 | 脏读，幻读，不可重复读 | MVCC |

<a name="Jz1yl"></a>
### 0.1.1 写-写
在这种情况下会发生 脏写 的问题，任何一种隔离级别都不允许这种问题的发生。所以在多个未提交事务 相继对一条记录做改动时，需要让它们 排队执行 ，这个排队的过程其实是通过 锁 来实现的。这个所谓 的锁其实是一个 内存中的结构 ，在事务执行前本来是没有锁的，也就是说一开始是没有 锁结构 和记录进 行关联的，如图所示：<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1652159298603-90615389-82fc-413e-9c3d-d8435e72a203.png#clientId=uab76eb42-7d8d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=64&id=ua0ab4b3f&margin=%5Bobject%20Object%5D&name=image.png&originHeight=128&originWidth=506&originalType=binary&ratio=1&rotation=0&showTitle=false&size=7996&status=done&style=none&taskId=u305900bd-adc4-428d-9aee-bd42b8e7eec&title=&width=253)

当一个事务想对这条记录做改动时，首先会看看内存中有没有与这条记录关联的 锁结构 ，当没有的时候 就会在内存中生成一个 锁结构 与之关联。比如，事务 T1 要对这条记录做改动，就需要生成一个 锁结构 与之关联：<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1652159307288-e49d6f17-4e3f-47d5-9b3d-5b981487e306.png#clientId=uab76eb42-7d8d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=203&id=uedfb5c73&margin=%5Bobject%20Object%5D&name=image.png&originHeight=406&originWidth=1306&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40689&status=done&style=none&taskId=ube7180f7-f9aa-42c6-a47d-c5ad0bb6e12&title=&width=653)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1652159312152-cf09e98b-9551-4ec3-9f44-a3e6a9224abc.png#clientId=uab76eb42-7d8d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=279&id=ue4525778&margin=%5Bobject%20Object%5D&name=image.png&originHeight=558&originWidth=841&originalType=binary&ratio=1&rotation=0&showTitle=false&size=56777&status=done&style=none&taskId=u4fe7a5fc-1d43-475e-9b92-04840d7c5ce&title=&width=420.5)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1652159321148-dc5e343f-ec36-4f41-8156-6c2b606700fa.png#clientId=uab76eb42-7d8d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=118&id=ub8f30058&margin=%5Bobject%20Object%5D&name=image.png&originHeight=235&originWidth=676&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15333&status=done&style=none&taskId=udc492268-178f-4f4e-954b-5c22706c037&title=&width=338)

- 不加锁 意思就是不需要在内存中生成对应的 锁结构 ，可以直接执行操作。 
- 获取锁成功，或者加锁成功 意思就是在内存中生成了对应的 锁结构 ，而且锁结构的 is_waiting 属性为 false ，也就是事务 可以继续执行操作。 
- 获取锁失败，或者加锁失败，或者没有获取到锁 意思就是在内存中生成了对应的 锁结构 ，不过锁结构的 is_waiting 属性为 true ，也就是事务 需要等待，不可以继续执行操作。
<a name="klSLa"></a>
## 0.2 并发问题的解决方案
<a name="UpV3K"></a>
### 0.2.1 MVCC
读操作利用多版本并发控制（ MVCC ），写操作进行 加锁

普通的SELECT语句在READ COMMITTED(读已提交)和REPEATABLE READ（可重复读）隔离级别下会使用到MVCC读取记录。

- 在 READ COMMITTED 隔离级别下，一个事务在执行过程中每次执行SELECT操作时都会生成一 个ReadView，ReadView的存在本身就保证了事务不可以读取到未提交的事务所做的更改 ，也就 是避免了脏读现象；
- 在 REPEATABLE READ 隔离级别下，一个事务在执行过程中只有第一次执行select操作才会生成一个ReadView,之后的 select 操作都是复用这个ReadView，这样也就避免了不可重复读和幻读的问题。
<a name="EL0X3"></a>
### 0.2.2 读写加锁
![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1652172996710-6e8ef33b-ede3-4be8-9aab-3d92b5e35d32.png#clientId=u52872ca8-4abf-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=282&id=u8ffb45d8&margin=%5Bobject%20Object%5D&name=image.png&originHeight=564&originWidth=1932&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1182443&status=done&style=none&taskId=ue986a8b0-2ad3-4bee-b255-095eb86527b&title=&width=966)
<a name="rGn9e"></a>
# 1.锁的分类
![](https://cdn.nlark.com/yuque/0/2022/jpeg/177460/1650875380005-d2e34662-dbd7-44a1-a8a7-f10258ee191d.jpeg)<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629035828669-f8c28cc1-55bb-49b7-9ed3-718a690f1b28.png#clientId=ufc40fbd1-b37e-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=uf829e427&margin=%5Bobject%20Object%5D&name=image.png&originHeight=237&originWidth=602&originalType=url&ratio=1&rotation=0&showTitle=false&size=71500&status=done&style=none&taskId=uf9d5dbef-776c-4152-a203-4c2f3ced79a&title=)
<a name="NYM8y"></a>
#### <br />

<a name="cPnME"></a>
## 1.1 按照锁的粒度
<a name="pHqE9"></a>
### 1.1.1 表锁
<a name="yhrFM"></a>
#### 1.1.1.1 表级别的S/X锁

<a name="Q8eB3"></a>
#### 1.1.1.2 意向锁
```cpp
InnoDB支持多粒度锁，它允许行级锁与表级锁共存，而意向锁就是其中的一种表锁。

1、意向锁的存在是为了协调行锁和表锁的关系，支持多粒度（表锁和行锁）的锁共存。
2、允许行级锁与表级锁共存，而意向锁就是其中的一种表锁。
3、表明“某个事务正在某些行持有了锁 或 该事务准备去持有锁”。
```
<a name="LATqF"></a>
###### 
<a name="Kcf5a"></a>
##### 意向共享锁（Intention Shared Lock）
意向共享锁（IS）：表示事务准备（有意向）给数据行加入共享锁（S锁），也就是说一个数据行加共享锁前必须先取得该表的IS锁
```java
-- 事务要获取某些行的S锁，必须先获的表的IS锁
select * from table ... LOCK IN SHARE MODE;
```
<a name="JfYun"></a>
##### 意向排他锁（Exclusive Lock）
意向排他锁（IX）：类似上面，表示事务准备（有意向）给数据行加入排他锁（X锁），说明事务在一个数据行加排他锁前必须先取得该表的IX锁。
```java
-- 事务要获取某些行的X锁，必须先活得表的IX锁
select * from table ...FOR UPDATE;
```
InnoDB支持多种锁，特定情境下，行锁可以与表级锁共存。<br />意向锁是由存储引擎自己维护的，用户无法手动操作意向锁，在为数据行添加共享/排他锁之前，InooDB会先获取该数据行 **所在数据表的对应意向锁**。

<a name="fQyXk"></a>
##### 意向锁兼容互斥性
<a name="jKcv2"></a>
###### 1. 意向锁与意向锁的兼容互斥性
|  | **意向共享锁(IS）** | **意向互斥锁（IX)** |
| --- | --- | --- |
| **意向共享锁（IS)** | 兼容 | 兼容 |
| **意向互斥锁（IX)** | 兼容 | 兼容 |


<a name="QtaSZ"></a>
###### 2.意向锁与排他锁/共享锁的兼容互斥性
注意这里的排他/共享锁指的是表锁，意向锁不会与行级别的共享/排他锁互斥。

|  | **意向共享锁(IS）** | **意向互斥锁（IX)** |
| --- | --- | --- |
| **共享锁（S)** | 兼容 | 互斥 |
| **互斥锁（X)** | 互斥 | 互斥 |



<a name="cPniw"></a>
##### 意向锁目的

- 意向锁的存在是为了协调行锁和表锁的关系，支持多粒度的锁并存。
- 意向锁是一种不与行级锁冲突的表级锁。
- 表明“某个事务正在某行持有了锁或该事务准备去持有锁”。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1650301228225-917a16af-8c40-4d08-9d9c-ccedc0d5bd3b.png#clientId=u97847986-e30f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=269&id=ub9819934&margin=%5Bobject%20Object%5D&name=image.png&originHeight=358&originWidth=962&originalType=binary&ratio=1&rotation=0&showTitle=false&size=344307&status=done&style=none&taskId=u0aa0ff96-39ce-4c1b-bc4a-6d26ec8e7a1&title=&width=722)
<a name="PQ9ly"></a>
#### 
<a name="CkOrP"></a>
#### 1.1.1.3 自增锁（AUTO_INCREMENT）
AUTO_INC 锁是当向

<a name="H8j0f"></a>
#### 1.1.1.4 元数据锁
Meta Data Lock ,MDL 属于表锁范畴。MDL 的作用是 保证读写的正确性。<br />比如一个查询正在遍历一个表中的数据，而执行期间另外一个线程对这个表结构做变更，增加了一列，那么查询线程拿到的结果跟表结构对不上，肯定不行。

因此，当对一个表做增删改查的时候，加MDL 读锁；当要对表做结构变更操作的时候，加MDL写锁。
<a name="tIAWu"></a>
### 1.1.2 行锁
<a name="MRB5D"></a>
#### 1.1.2.1 记录锁（Record Lock）
记录锁是仅仅把一条记录锁上。<br />如图所示，仅仅是锁住了id为8 的记录，对周围的数据没有影响。<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/177460/1651752590579-f0108e9b-9527-4dcf-8222-0649c688d04f.png#clientId=u79b9e341-179e-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=181&id=u866268b5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=362&originWidth=1034&originalType=binary&ratio=1&rotation=0&showTitle=false&size=58490&status=done&style=none&taskId=u3181a335-1877-4c70-a38f-70dd8d7f82d&title=&width=517)<br />记录锁是有S锁和X锁之分的，称之为S型记录锁和X型记录锁。

- 当一个事务获取了一条记录的S记录锁后，其他事务也可以继续获取该记录的S记录锁，但是不可以获取X锁。
- 当一个事务获取了一条记录的X锁后，其他事务既不可以获取S锁，也不可以继续获取X锁。

<a name="ZqwdU"></a>
#### 1.1.2.2 间隙锁（Gap Lock）

<a name="fhtKY"></a>
#### 1.1.2.3 临键锁（Next-Key Locks）

<a name="iXT2M"></a>
#### 1.1.2.4 插入意向锁（Insert Intention Locks）

<a name="gsynD"></a>
### 1.1.3 页锁


<a name="VX8gF"></a>
# 2.间隙锁实践
<a name="hGrBW"></a>
## 2.1 主键索引-间隙锁
<a name="sEIcN"></a>
### 1.数据准备
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629717516505-645aa580-7a7a-4a8b-bd58-c0e7ed969a3a.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=359&id=u23b95d32&margin=%5Bobject%20Object%5D&name=image.png&originHeight=718&originWidth=1319&originalType=binary&ratio=1&rotation=0&showTitle=false&size=52565&status=done&style=none&taskId=u861eb033-a9a6-423e-afbf-43e54ac5853&title=&width=659.5)
<a name="HvYZY"></a>
### 2.事务A中开启区间排他锁
for update 开启排他锁（X锁）。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629717669349-bf6b565a-cb86-4bf2-b6ef-ea61f2f62017.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=158&id=uca0865dc&margin=%5Bobject%20Object%5D&name=image.png&originHeight=316&originWidth=1219&originalType=binary&ratio=1&rotation=0&showTitle=false&size=33614&status=done&style=none&taskId=u6f3a127d-7ae5-4b2c-ba7f-7fd53d7cf71&title=&width=609.5)
<a name="TcrFL"></a>
### 3.事务B中插入区间内数据
很明显被阻塞了。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629717712986-59a998ef-f580-468f-9767-0ea6eeb81bfe.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=114&id=u4be9944c&margin=%5Bobject%20Object%5D&name=image.png&originHeight=228&originWidth=965&originalType=binary&ratio=1&rotation=0&showTitle=false&size=17067&status=done&style=none&taskId=ue899fc42-d4b0-432c-b8ce-b1f46ad7b6b&title=&width=482.5)
<a name="l2qx2"></a>
## 2.2 主键索引-死锁
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735600655-bbb7c5fe-d28a-4f62-9881-c8ae23b0d226.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=81&id=u12129965&margin=%5Bobject%20Object%5D&name=image.png&originHeight=162&originWidth=354&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8783&status=done&style=none&taskId=u03a9ae6e-da02-4032-a35b-b575ab72725&title=&width=177)

<a name="bAPds"></a>
### 2.2.1 准备数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629734945473-af212b78-38d0-4a71-8f08-202979947ee2.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=166&id=ubcab78b5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=332&originWidth=426&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12653&status=done&style=none&taskId=u121d7e30-612c-4e32-ad9c-26c4f7030de&title=&width=213)

<a name="LSm06"></a>
### 2.2.2 事务A删除一个不存在的值 获取间隙锁
```sql
delete from student where id=4;
获取间隙锁(1,5]
```
<a name="JeeLP"></a>
### 2.2.3 事务B删除一个不存在的值 获取间隙锁
```sql
delete
from student
where id = 3;

获取间隙锁(1,5]
```
<a name="U7OE6"></a>
### 2.2.4 事务A 要往间隙内插入数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735174315-82e5fe20-4adf-4348-ba71-1fc4f6b244af.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=146&id=ub1b8d03b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=291&originWidth=937&originalType=binary&ratio=1&rotation=0&showTitle=false&size=17934&status=done&style=none&taskId=ua2de6be3-5580-488b-ad22-cb958a52149&title=&width=468.5)<br />由于事务B已获取到间隙锁，此时事务A插入阻塞

<a name="KxgRd"></a>
### 2.2.5 事务B 要往间隙内插入数据
出现死锁<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735318018-e49ea7d1-3779-48b1-aa60-211cbe5aa677.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=281&id=uf064c575&margin=%5Bobject%20Object%5D&name=image.png&originHeight=562&originWidth=1138&originalType=binary&ratio=1&rotation=0&showTitle=false&size=60261&status=done&style=none&taskId=ufd4e2094-b103-4e16-9413-3c1540a9ddc&title=&width=569)
<a name="fSQw9"></a>
## 2.3 普通索引-间隙锁
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735600655-bbb7c5fe-d28a-4f62-9881-c8ae23b0d226.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=81&id=zUXxz&margin=%5Bobject%20Object%5D&name=image.png&originHeight=162&originWidth=354&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8783&status=done&style=none&taskId=u03a9ae6e-da02-4032-a35b-b575ab72725&title=&width=177)
<a name="JOZFb"></a>
### 2.3.1 准备数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735842877-e1567134-f71e-4817-a593-b280167630f1.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=123&id=u3aaf07d6&margin=%5Bobject%20Object%5D&name=image.png&originHeight=246&originWidth=400&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8675&status=done&style=none&taskId=ua110dc0a-c9dc-4fdf-b104-85712681591&title=&width=200)
<a name="cXTwk"></a>
### 2.3.2  事务A删除一个不存在的值获取到间隙锁
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735930706-c5e5aa1d-46d4-41d1-8a0e-80d80286cb8e.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=52&id=u791f2137&margin=%5Bobject%20Object%5D&name=image.png&originHeight=103&originWidth=508&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5114&status=done&style=none&taskId=u69e1db88-4f53-4688-95f4-b77733caefd&title=&width=254)<br />不存在的值就会获取到向上及向下最近的间隙锁。<br /> 获取到（12，25] 范围的间隙锁

<a name="kw1bD"></a>
### 2.3.3 事务B要往间隙锁内插入数据
说明被阻塞<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629735996670-c2d16808-6774-4338-81fe-bf78499d6f58.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=56&id=u8f874cbf&margin=%5Bobject%20Object%5D&name=image.png&originHeight=111&originWidth=622&originalType=binary&ratio=1&rotation=0&showTitle=false&size=7234&status=done&style=none&taskId=u9bd5dcc6-2a71-441b-baf1-49cc74a1fe4&title=&width=311)

<a name="MDcN0"></a>
## 2.4 普通索引-死锁

- **在普通索引列上，不管是何种查询，只要加锁，都会产生间隙锁，这跟唯一索引不一样；**
- **在普通索引跟唯一索引中，数据间隙的分析，数据行是优先根据普通普通索引排序，再根据唯一索引排序。**
- **普通索引如果删除不存在的值key，则会在最左 num[i]<key 和 最右num[i+1] > key 充当间隙锁。**
- **普通索引如果删除索引所在的值num[i]，则会在[num[i-1],nums[i+1]]之间建立间隙锁，很容易两个事务之间出现死锁情况。**
<a name="MHgVz"></a>
### 2.4.1 准备数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629736176481-e202e142-c797-464b-92a8-e8177990693b.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=139&id=u19e51773&margin=%5Bobject%20Object%5D&name=image.png&originHeight=278&originWidth=423&originalType=binary&ratio=1&rotation=0&showTitle=false&size=10151&status=done&style=none&taskId=u92281647-bf2d-4393-820c-4f1cc53e0fd&title=&width=211.5)<br />id 主键索引<br />age 普通索引
<a name="bsDlS"></a>
### 2.4.2 事务A删除存在的一个普通索引所对应数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629736498383-d1ed4f6a-db2d-4925-a9f4-90661449601b.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=44&id=u2f7eaaea&margin=%5Bobject%20Object%5D&name=image.png&originHeight=88&originWidth=543&originalType=binary&ratio=1&rotation=0&showTitle=false&size=4976&status=done&style=none&taskId=u8e9e1dfd-d546-4595-be74-f437961f9b9&title=&width=271.5)

产生间隙锁(8,25]

<a name="EP16m"></a>
### 2.4.3 事务B删除存在的一个普通索引所对应数据
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629736559141-e9f43f65-1268-45db-91fb-6f597e636545.png#clientId=u5ed35ee3-4e6d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=67&id=ua30e4cc5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=134&originWidth=288&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5434&status=done&style=none&taskId=u4fbe7718-6b99-4076-b476-d65685ff76a&title=&width=144)<br />产生间隙锁（3，12]

<a name="hp149"></a>
### 2.4.4 事务A创建普通索引在交际区间内数据
```sql
insert into student value(12,10);
此时被阻塞。
```
<a name="AsKAP"></a>
### 2.4.5 事务B 创建普通索引在交际区间内数据
```sql
insert into student value (6, 9);
直接爆死锁。
```
<a name="mr4HM"></a>
# 3.加锁规则
<a name="KfzrG"></a>
## 3.1 唯一索引等值查询

- 当索引项存在时，next-key lock 退化为 record lock；
- 当索引项不存在时，默认 next-key lock，访问到不满足条件的第一个值后next-key lock退化成gap lock

<a name="UOnky"></a>
## 3.2 唯一索引范围查询
默认 next-key lock，(特殊’<=’ 范围查询直到访问不满足条件的第一个值为止)

<a name="fI2FI"></a>
# 引用
[https://www.cnblogs.com/shoshana-kong/p/14109826.html](https://www.cnblogs.com/shoshana-kong/p/14109826.html)
