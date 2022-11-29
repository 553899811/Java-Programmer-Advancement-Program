<a name="4b0bf318"></a>
# 1. 索引原理分析
<a name="Vb3Dl"></a>
## 1.1 索引分类

- 索引类型:
   - BTree索引
   - B+Tree索引
   - 哈希索引
   - 全文索引
- 索引分类
   - 普通索引（INDEX）: 仅加速查询
   - 唯一索引（UNIQUE INDEX）：加速查询 + 列值唯一（可以有null）
   - 主键索引：加速查询 + 列值唯一（不可以有null）+ 表中只有一个
   - 组合索引：多列值组成一个索引，专门用于组合搜索，其效率大于索引合并
   - 全文索引：对文本的内容进行分词，进行搜索

<a name="UV4ux"></a>
### 1.1.1 哈希索引

只有memory（内存）存储引擎支持哈希索引，哈希索引用索引列的值计算该值的hashCode，然后在hashCode相应的位置存执该值所在行数据的物理位置，因为使用散列算法，因此访问速度非常快，但是一个值只能对应一个hashCode，而且是散列的分布方式，因此哈希索引**不支持范围查找和排序的功能。**

<a name="W4HLn"></a>
### 1.1.2 全文索引

FULLTEXT（全文）索引，仅可用于MyISAM和InnoDB，针对较大的数据，生成全文索引非常的消耗时间和空间。对于文本的大对象，或者较大的CHAR类型的数据，如果使用普通索引，那么匹配文本前几个字符还是可行的，但是想要匹配文本中间的几个单词，那么就要使用LIKE %word%来匹配，这样需要很长的时间来处理，响应时间会大大增加，这种情况，就可使用时FULLTEXT索引了，在生成FULLTEXT索引时，会为文本生成一份单词的清单，在索引时及根据这个单词的清单来索引。FULLTEXT可以在创建表的时候创建，也可以在需要的时候用ALTER或者CREATE INDEX来添加：

```
  //创建表的时候添加FULLTEXT索引
CTREATE TABLE my_table(
    id INT(10) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    my_text TEXT,
    FULLTEXT(my_text)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;
```

- 全文索引的查询也有自己特殊的语法，而不能使用LIKE %查询字符串%的模糊查询语法

```
  SELECT * FROM table_name MATCH(ft_index) AGAINST('查询字符串');
```

<a name="c11d9f4f"></a>
## 1.2 B 树 和B+ 树的区别
| **特性** | **B树** | **B+树** |
| --- | --- | --- |
|  非叶子节点存值 | 非叶子节点存data | 非叶子节点存索引，叶子节点存Data |
| 区间查询 | 因非叶子节点上也有值，需要中序遍历 | 叶子节点存有所有值，直接遍历即可。 |

<a name="olcnN"></a>
## 1.3 聚簇索引和非聚簇索引的区别
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629684710779-2892bfe1-7756-42e5-bf25-af8d8fa0823b.png#clientId=u4a89a6cf-6a10-4&crop=0&crop=0&crop=1&crop=1&from=paste&id=u89d516e3&margin=%5Bobject%20Object%5D&name=image.png&originHeight=452&originWidth=583&originalType=url&ratio=1&rotation=0&showTitle=false&size=127619&status=done&style=none&taskId=uabacfd33-6756-433f-80c4-b9041d2d0f7&title=)

| **特性** | **聚簇索引** | **聚簇索引（InnoDB：辅助索引）** | **非聚簇索引（MyISAM：主键索引&&辅助索引）** |
| --- | --- | --- | --- |
| 叶子节点存值 | 叶子节点存数据节点 | 叶子节点存主键 | 叶子节点存行地址 |
| 区间查询 | 叶子节点存有所有值，直接遍历即可。 | 叶子节点有所有的值，直接遍历查（回表） | 叶子节点有所有的值，直接遍历查（无需回表） |
| 树种 | B+ | B+ | B+ |


<a name="h5rkd"></a>
## 1.4 B+Tree 索引 是如何支撑千万级表的快速查询

[1] 查看mysql文件页的大小

```
   SHOW GLOBAL status LIKE 'Innodb_page_size';
   16384 B/1024 = 16KB
```

[2] 索引结构的节点大小被设计为一个页的大小 16kb;

每次新建节点时，直接申请一个页的空间，这样就保证一个节点物理上也存储在一个页里，加之计算机存储分配都是按页(Linux 默认一页4kb)对齐的，就实现了一个node只需一次I/O;

再看 非叶子节点，假设主键ID （比如15）为BIGINT类型，那么长度就是8B，指针大小在InnoDB源码中为 6B，那么一共就是14B，那么一个非叶子节点就可以存储16K/14=16*1024/14=1170 个（主键+指针）

目的: 让非叶子节点  可以存储更多的元素，也就有更多的分叉，树的高度就会相对低，查询一个数据就会更快，IO次数也就更少。

[3] 假设B+Tree的高度为3;

```
  - 第一层也就是root节点 ，可以存储 1170个元素;

  - 第二层可以存储 1170*1170 个元素

  - 第三层因为是叶子节点，叶子节点上存储着data（1：索引所在行的磁盘文件指针 2：索引所在行的整个数据）；
  我们假设data+主键索引 一共的大小为1K,那么一个叶子节点就可以存储16K/1=16个节点;

  对于高度h为3的一棵B+Tree来讲，一共可以存储1170*1170*16=21902400 个元素（千万级表的存储高度只需要3!）
```

[4] 由以上3步骤我们可以看出，B+Tree的低高度高容量的特性;

B-Tree中一次检索最多需要h-1次I/O（根节点常驻内存），渐进复杂度为。一般实际应用中，出度d是非常大的数字，通常超过100，因此h非常小（通常不超过3）。

<a name="0fb6fbc6"></a>
# 2.MySQL索引实现

| **特性** | **MyISAM** | **InnoDB** |
| --- | --- | --- |
| 叶子节点存储 | 主键索引和辅助索引 存数据记录的地址 | 主键索引存数据；  辅助索引存 主键值 |

<a name="7d1ee822"></a>
## MyISAM索引实现
<a name="2fba8855"></a>
### 1.MyISAM索引的原理图

MyISAM引擎使用B+Tree作为索引结构，叶节点的data域存放的是**数据记录的地址**。下图是MyISAM索引的原理图:

![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1622443306610-8058db3a-aef4-48fe-b8f9-7a6de53ff059.png#crop=0&crop=0&crop=1&crop=1&height=269&id=LQbOr&margin=%5Bobject%20Object%5D&name=image.png&originHeight=537&originWidth=667&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36675&status=done&style=none&title=&width=333.5)

<a name="0fe03d25"></a>
### 2.MyISAM辅助索引

在MyISAM中，主索引和辅助索引（Secondary key）在结构上没有任何区别，只是主索引要求key是唯一的，**而辅助索引的key可以重复**。如果我们在Col2上建立一个辅助索引，则此索引的结构如下图所示：<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1622443345322-8769103f-eec3-406a-ad36-a64a173fb269.png#crop=0&crop=0&crop=1&crop=1&height=268&id=a8dAe&margin=%5Bobject%20Object%5D&name=image.png&originHeight=535&originWidth=667&originalType=binary&ratio=1&rotation=0&showTitle=false&size=37003&status=done&style=none&title=&width=333.5)

<a name="f521d754"></a>
### 3.MyISAM索引总结

```
  同样也是一颗B+Tree，data域保存数据记录的地址。因此，MyISAM中索引检索的算法为首先按照B+Tree搜索算法搜索索引，如果指定的Key存在，则取出其data域的值，然后以data域的值为地址，读取相应数据记录。

   MyISAM的索引方式也叫做“非聚集”(非聚簇)的，之所以这么称呼是为了与InnoDB的聚集索引区分。
```

<a name="d20ad46f"></a>
## InnoDB索引实现

<a name="7f7f7fff"></a>
### 1.InnoDB索引原理图

InnoDB的数据文件本身就是索引文件。从上文知道，MyISAM索引文件和数据文件是分离的，索引文件仅保存数据记录的地址。而在InnoDB中，表数据文件本身就是按B+Tree组织的一个索引结构，这棵树的叶节点data域保存了完整的数据记录。这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1622443357546-065a8472-43a3-48a2-b9ff-3d74bca9fdad.png#crop=0&crop=0&crop=1&crop=1&height=122&id=X0G04&margin=%5Bobject%20Object%5D&name=image.png&originHeight=243&originWidth=545&originalType=binary&ratio=1&rotation=0&showTitle=false&size=17114&status=done&style=none&title=&width=272.5)

叶节点包含了完整的数据记录。这种索引叫做**聚集索引(聚簇索引)**)。因为InnoDB的数据文件本身要按主键聚集，所以InnoDB要求表必须有主键（MyISAM可以没有），如果没有显式指定，则MySQL系统会自动选择一个可以唯一标识数据记录的列作为主键，如果不存在这种列，则MySQL自动为InnoDB表生成一个隐含字段作为主键，这个字段长度为6个字节，类型为长整形。

<a name="d408e0c9"></a>
### 2.InnoDB辅助索引原理图

与MyISAM索引的不同是InnoDB的辅助索引data域存储相应记录**主键的值**而不是地址。换句话说，InnoDB的所有辅助索引都引用主键作为data域。例如，定义在Col3上的一个辅助索引：<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1622443372796-f072c1e1-a6f1-4838-9938-3b302bdfad81.png#crop=0&crop=0&crop=1&crop=1&height=112&id=L0y1S&margin=%5Bobject%20Object%5D&name=image.png&originHeight=224&originWidth=545&originalType=binary&ratio=1&rotation=0&showTitle=false&size=16224&status=done&style=none&title=&width=272.5)

- 聚集索引这种实现方式使得按主键的搜索十分高效，但是辅助索引搜索需要检索两遍索引：首先检索辅助索引获得主键，然后用主键到主索引中检索获得记录。
- 了解不同存储引擎的索引实现方式对于正确使用和优化索引都非常有帮助，例如知道了InnoDB的索引实现后，就很容易明白为什么不建议使用过长的字段作为主键，因为所有辅助索引都引用主索引，过长的主索引会令辅助索引变得过大。再例如，用非单调的字段作为主键在InnoDB中不是个好主意，因为InnoDB数据文件本身是一颗B+Tree，非单调的主键会造成在插入新记录时数据文件为了维持B+Tree的特性而频繁的分裂调整，十分低效，而使用自增字段作为主键则是一个很好的选择。

- 需要回表查询

<a name="kk5NT"></a>
# 3.MySQL索引对读写的影响
<a name="R6ArD"></a>
## 3.1 降低了写入的速度
进行增删改的时候，B+树的弊端就出现了。每进行一次增删改的操作时，都要改变一次B+树的结构（当然也可能不变，但是检查一次是否平衡是肯定要的），以保证B+树是有序、平衡的。<br />数据量小的时候可能看不出来，但是数据量一旦大了，就会非常的影响性能。<br />因此，是不可以无限制的增加索引的，需要根据具体的情况来设计索引。
<a name="c3UNf"></a>
# 4.索引的使用原则
| <br />1. 全值匹配<br /> |  |
| --- | --- |
| <br />2. 最左前缀匹配<br /> | 如果索引了多列，要遵守最左前缀法则。指的是查询从索引的最左前列开始并且不跳过索引中的列。 |
| <br />3. 不在索引列上做任何操作，会导致索引失效而转向全表扫描<br /> |  |
| <br />4. **存储引擎不能使用索引中范围条件右边的列**<br /> | a>10 and b=10 and c=2; (b,c 联合索引都用不到) |
| <br />5. 尽量使用覆盖索引（只访问索引的查询（索引列和查询列一致）），减少 select *<br /> | 索引覆盖了就不用再回表查了。 |
| <br />6. 负向条件索引不会使用索引，建议用in。负向条件有：!=、<>、not in、not exists、not like 等；<br /> |  |
| <br />7. 前导模糊查询不会使用索引，例如 like %李<br /> |  |
| <br />8. 字符串不加单引号'abcd' 会造成索引失效<br /> |  |
| <br />9. 少用or 查询。<br /> |  |


<a name="TuSXA"></a>
# 5.大数据分页查询
![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629637505867-d12767d2-228e-4d64-93f4-e67335c7fde5.png#clientId=u653c6ecc-090e-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=419&id=u3daf55d5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=837&originWidth=1311&originalType=binary&ratio=1&rotation=0&showTitle=false&size=94075&status=done&style=none&taskId=ub17f8e4b-aa68-4523-be8d-475815b5cee&title=&width=655.5)<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629640921273-c60f6a28-effb-42a1-b38a-e3de76e07c3c.png#clientId=u653c6ecc-090e-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=408&id=u95373fac&margin=%5Bobject%20Object%5D&name=image.png&originHeight=816&originWidth=1324&originalType=binary&ratio=1&rotation=0&showTitle=false&size=109709&status=done&style=none&taskId=uea92c87e-c6cf-45ac-bc18-63d7ff533cb&title=&width=662)
<a name="gOBhx"></a>
### 方法一：延迟关联
```sql
select * from orders limit 2000000,5 
耗时 3618 ms
SELECT * from orders a1 INNER JOIN ( SELECT id from orders WHERE 1=1 limit 2000000,5) b1 on a1.id = b1.id;
耗时 376ms
```
<a name="Xjgl1"></a>
### 方法二：使用书签的方式
【1】根据分页信息获取到(curr-1)*pageSize== startIndex, <br />【2】根据limit startIndex,1 获取到数据的第一个id<br />【3】根据 id>[id] limit pageSize 获取新数据。

<a name="Q6obF"></a>
# 6.慢SQL 如何定位与处理
<a name="teH5c"></a>
## 6.1处理流程
<a name="AXuFA"></a>
### 6.1.1根据日志定位查询慢的sql
show variables like '%query%'; 查询慢日志相关信息<br />1）、重点关注项：<br />slow_query_log 默认是off关闭的，使用时，需要改为on 打开　　　　　　<br />slow_query_log_file 记录的是慢日志的记录文件<br />long_query_time 默认是10S，每次执行的sql达到这个时长，就会被记录<br />2）、SHOW STATUS LIKE '%slow_queries%' 查看慢查询状态<br />Slow_queries 记录的是慢查询数量 当有一条sql执行一次比较慢时，这个value就是1（记录的是本次<br />会话的慢sql条数）<br />3）、注意：<br />a: 如何打开慢查询 ： SET GLOBAL slow_query_log = ON;<br />b: 将默认时间改为1S： SET GLOBAL long_query_time = 1;<br />（设置完需要重新连接数据库，PS：仅在这里改的话，当再次重启数据库服务时，所有设置又会自动恢复成默<br />认值，永久改变需去my.ini中改）
<a name="nQSuk"></a>
### 6.1.2 使用explain等工具分析sql
1）在要执行的sql前加上explainexplain SELECT vin FROM yx_vehicle_info where vin = 'SXY30020180827104';<br />2）重点关注字段：<br />type: 如果type 栏显示的是 index 或者 all 说明sql需要优化。<br />extra：extra 栏
<a name="BvvES"></a>
### 6.1.3 修改SQL或者尽量让SQL走索引

<a name="gQyEq"></a>
## 6.2 案例分析
<a name="M8pPw"></a>
### 6.2.1 因关联的数据很多导致的慢SQL
<a name="veKKE"></a>
#### 背景
运营使用的列表，查看商家资质审核信息，除了审核信息外， 关联到商家公司表（公司信息），商家门店表（商家门店），商家账户表（入驻的账户信息） ；
<a name="xg4Kb"></a>
#### 错误的做法
left join 关联表，直接分页去查。导致慢SQL
<a name="CGitF"></a>
#### 正确的做法
分页查审核信息，其余字段 批量查表，可以同步查，也可以 并行查 补充字段。

<a name="sbsrX"></a>
### 6.2.2 因索引问题导致的慢SQL

<a name="CnqOB"></a>
#### 案例一
```sql
explain select
    os.id,
    os.order_id,
    os.super_order_id,
    os.payment_trade_num,
    os.vender_id,
    os.pop_status,
    os.pay_way,
    os.order_time,
    os.pay_time,
    os.delivery_time,
    os.complete_time,
    os.finish_time,
    os.cancel_time,
    os.pay_time,
    os.modified,
    os.item_total_price,
    os.coupon_price,
    os.promotion_price,
    os.freight_fee,
    os.order_price,
    os.order_pay_price,
    os.is_lock
    from orders os
    where os.yn = 1
      and os.vender_id= 1062
      and os.pop_status = 32
      and os.is_lock = 0
      and os.order_type = 1
    order by os.id desc
    limit 15
```

![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1629523522917-3d89c58b-f146-4bbe-b8e5-643ba56e4c37.png#clientId=ua6e777bd-c782-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=201&id=ub73a0f41&margin=%5Bobject%20Object%5D&name=image.png&originHeight=401&originWidth=415&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36606&status=done&style=none&taskId=ue4c6fe18-45a6-416d-9fbf-0ea98ed4886&title=&width=207.5)
<a name="LG34O"></a>
##### 执行计划
| type | possible_keys | key | key_len | rows | extra |
| --- | --- | --- | --- | --- | --- |
| index | idx_vendorid_ordertime,idx_pop_status | PRIMARY | 8 | 3169 | Using Where |

<a name="GAvLj"></a>
##### 问题
没有使用vendor_id 前缀索引，pop_status 索引；<br />直接扫描了全部索引树。

<a name="UQu3m"></a>
#### 改进后
```sql
explain select
    os.id,
    os.order_id,
    os.super_order_id,
    os.payment_trade_num,
    os.vender_id,
    os.pop_status,
    os.pay_way,
    os.order_time,
    os.pay_time,
    os.delivery_time,
    os.complete_time,
    os.finish_time,
    os.cancel_time,
    os.pay_time,
    os.modified,
    os.item_total_price,
    os.coupon_price,
    os.promotion_price,
    os.freight_fee,
    os.order_price,
    os.order_pay_price,
    os.is_lock
    from orders os
    where  os.vender_id= 1062
	    and os.yn = 1
      and os.pop_status = 32
      and os.is_lock = 0
      and os.order_type = 1
	  and `order_time` >'2021-08-01'
   order by os.id desc
    limit 15
```
加上 下单时间 后，执行计划

| type | possible_keys | key | key_len | rows | extra |
| --- | --- | --- | --- | --- | --- |
| range | idx_vendorid_ordertime,idx_pop_status | idx_vendorid_ordertime | 8 | 293 | Using index condition; Using where; Using filesort |



此时优化到 扫描部分索引 ，而不是整个索引树。

<a name="rPxSj"></a>
# 常见问题
