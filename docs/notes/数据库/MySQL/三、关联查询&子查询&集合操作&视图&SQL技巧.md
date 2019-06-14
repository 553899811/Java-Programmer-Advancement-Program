[TOC]

参考：
- [ ] [易百MySQL教程](http://www.yiibai.com/mysql/)
- [x] [百度脑图](http://naotu.baidu.com/file/061cde937f8ac783ee33b33ad2016476)
- [x] ==空值表示未知的值(不被认为彼此相等或不相等)==



# 查询数据
> 1.where后面的多个and使用时，应该把可能性小的and放在前面，因为第一个条件为假时就不会去执行第二个条件了，这样查询速度会变快。（不过Oracle的基于开销的优化器是从右向左读的）

等价条件：

条件 | 等价于
---|---
NOT (NOT p) | p
not(p and q) | (not p) or (not q)
not(p or q) | (not p) and (not q)
p and (q or r) | (p and q) or (p and r)





---

> 表与表之间联结总结：
1. 确定主表与从表；
2. 在三表联结中，只有一个表可以作为另一个表和第三个表的桥梁。
3. 使用venn图进行分析问题。

有两个方式创建联结：==join==和==where==
```
select 
...
from MARA m,EINA a,EINE e,WRF_BRANDS_T b
where 
a.MATNR=m.MATNR and 
a.INFNR=e.INFNR and 
m.BRAND_ID = b.BRAND_ID AND
a.MANDT = e.MANDT and
a.MANDT = b.MANDT and
a.MANDT = m.MANDT and
m.MANDT = '800' 
and m.ATTYP !='01' 
and a.LOEKZ !='X' 
```

多表联结类型：

1. 交叉联结：(cross join)
> 笛卡尔积，基本没什么意义。

2. 自然联结(natural join)
3. ==使用外连接时需要注意on后面的筛选条件与where后的筛选条件的区别。==
```
select * from a natural join b 
//会把a,b表中相同名字的列联结到一起。
```


3. 内联结（inner join）
```
t1 inner join t2 on “筛选条件”

两表中至少有一行某种联结条件才返回行的内连接，但是对于外连接来说至少返回其中一个表的所有行（假设这些行满足后面的where或having条件）

```


4. 左外联结（left outer join）
> ==总之，是按on后面的条件进行连接，按where后面的条件进行筛选。==



```
//2条，先按t1.col = t2.col筛选，然后连接（与t1数量一致）
SELECT t1.col AS "col-1", t2.col AS "col-2" FROM t1 LEFT JOIN t2 
ON t1.col = t2.col

//返回一条记录
SELECT t1.col AS "col-1",t2.col AS "col-2" FROM t1 LEFT JOIN t2 
ON t1.col = t2.col WHERE t1.col = 1;
	
//5条记录（按t1.col = 1连接，比t1数量多）
SELECT t1.col AS "col-1", t2.col AS "col-2" FROM t1 LEFT JOIN t2 
ON t1.col = 1;

//按t1.col = t2.col AND t1.col = 1连接
SELECT t1.col "col-1", t2.col "col-2" FROM t1 LEFT JOIN t2 
ON t1.col = t2.col AND t1.col = 1;
```

5. 右外联结（right outer join）

6. 全外联结（full outer join）

> 相当于执行4,5，然后再进行union操作（会删除重复记录）

7. 自联结（self-join）
```
自联结常被改写成子查询

```


8. 等值联结（theta join）
使用比较操作符(>,<,=,<>，...)进行连接。    




## 子查询
包括简单(非相关)子查询、相关子查询；
前者独立于外部查询，在整个语句中只运行一次；
后者的内部查询依赖于外部查询。
```
//例：列出销量超过或等于所属类型平均销售的图书。
select a.* 
from books a
where 
sales >= (
    select avg(sales)
    from books b
    where b.type = a.type
);

```

==注意子查询中的空值==：
> 例：现有table1，table2,查询在table2中存在而在table1不存在的值。

//table1
```
CREATE TABLE `t1` (
  `col` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `t2` (
  `col` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

```

col | 
---|---
1 | 
2 | 
NULL |

//table2
col | 
---|---
1 | 
2 | 
3 |

```
1.如果我们按照下面的常规思路，将返回空表
select col from t2
where col not in
(select col from t1);

为了分析问题，我们可以进行下面的分析：

2.将not移至前面将不会改变1的含义。
select col from t2
where not 

col in (select col from t1);

3.改写not组合条件 
select col from t2
where not
(
(col = 1) OR
(col = 2) OR
(col = 3) OR
(col is NULL)
);

4.根据德·摩根定律，等价于：
select col from t2
where 
(col <> 1) AND
(col <> 2) AND
(col <> 3) AND
(col <> NULL);

5.最后的表达式col <>null 是未知的，即导致整个where搜索条件也变成未知的，并且总会被where条件拒绝。
select col from t2
where col not in
(select col from t1
WHERE col is not null);

```
# 集合操作
union（取并集）




intersect（取交集）



except(差操作)


# SQL技巧


1. 随机选取n条数据：
```
//1：每隔4条选取一个，使用mod(m,n)函数
SELECT * FROM `categories` where MOD(CategoryID,4)=0 limit 100;
//2.使用rand()函数
SELECT * FROM `territories` WHERE RAND() < 0.25;
//3.order by rand()
SELECT * FROM `territories` ORDER BY RAND() limit 10

```
2. 层次查询：

参考：
- [x] [高级sql-层次查询](http://blog.csdn.net/zq9017197/article/details/5940255)

方法一：

```
 TODO

```






# 视图
1. 当基本表数据更新以后，视图会自动更新。
2. 视图相关的sql语句能修改数据库对象和数据。
```
//创建视图
create view v1(col,col2) as //类似于表
(
select a.col,a.col2 from a;
);

//删除视图
drop view v1

```


# 行列转换

```
SELECT 
ss.product_detail_sid,
ss.supply_sid,
ss.shop_sid,
MAX(case ss.stock_type_sid when 1001 then pro_sum else 0 end) 'p1001',
MAX(case ss.stock_type_sid when 1002 then pro_sum else 0 end) 'p1002',
MAX(case ss.stock_type_sid when 1003 then pro_sum else 0 end) 'p1003',
MAX(case ss.stock_type_sid when 1004 then pro_sum else 0 end) 'p1004',
MAX(case ss.stock_type_sid when 1008 then pro_sum else 0 end) 'p1008'

from ssd_stock ss
where 
product_detail_sid = 3874049002
GROUP BY 
ss.product_detail_sid,//精确到行了
ss.supply_sid,
ss.shop_sid
```
# case when

简单Case函数：
```
CASE sex
WHEN '1' THEN '男'
WHEN '2' THEN '女'
ELSE '其他' END
```
升级版：

```
CASE WHEN col_1 IN ( 'a', 'b') THEN '第一类' 
WHEN col_1 IN ('a')  THEN '第二类' 
ELSE'其他' END
```




# 与、或、非运算：

```
select (1 and 1), (0 and 1), (3 and 1), (1 and null),(1 and ''); 
//1	 0	1	null    0
```





# mysql 获取昨天日期、今天日期、明天日期以及前一个小时和后一个小时的时间

1、当前日期

```
select DATE_SUB(curdate(),INTERVAL 0 DAY) ;
```

2、明天日期

```
select DATE_SUB(curdate(),INTERVAL -1 DAY) ;
```



3、昨天日期

```
select DATE_SUB(curdate(),INTERVAL 1 DAY) ;
```


4、前一个小时时间

```
select date_sub(now(), interval 1 hour);
```


5、后一个小时时间

```
select date_sub(now(), interval -1 hour);
```


6、前30分钟时间

```
select date_add(now(),interval -30 minute)
```


7、后30分钟时间

```
select date_add(now(),interval 30 minute)
```

# exists

https://dev.mysql.com/doc/refman/5.7/en/exists-and-not-exists-subqueries.html