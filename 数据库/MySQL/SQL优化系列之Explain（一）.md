[TOC]

参考：

- [x] [Explain的解释](http://www.jb51.net/article/33736.htm)
- [x] [MySQL 索引及查询优化总结](https://cloud.tencent.com/community/article/382852)
- [x] [当谈 SQL 优化时谈些什么？](https://cloud.tencent.com/community/article/302356)
- [x] [EXPLAIN Syntax——mysql5.6](https://dev.mysql.com/doc/refman/5.6/en/explain.html)
- [x] 《高性能MySQL第三版》

> 基于mysql 5.7.18


# explain的使用以及各个参数的含义

- 它解释出来的内容只是近似结果。
- 不仅可以解释select语句还可以解释update，delete，insert语句



例：
- 包含了查询类型、关联类型等12个列。
```
mysql> explain select (select sid from zh_xiache limit 1) from zh_xiache_detail;
+----+--------------------+------------------+------------+-------+---------------+----------+---------+------+------+----------+-------------+
| id | select_type        | table            | partitions | type  | possible_keys | key      | key_len | ref  | rows | filtered | Extra       |
+----+--------------------+------------------+------------+-------+---------------+----------+---------+------+------+----------+-------------+
|  1 | PRIMARY            | zh_xiache_detail | NULL       | index | NULL          | PRIMARY  | 4       | NULL | 5486 |   100.00 | Using index |
|  2 | DEPENDENT SUBQUERY | zh_xiache        | NULL       | index | NULL          | idx_date | 4       | NULL | 1295 |   100.00 | Using index |
+----+--------------------+------------------+------------+-------+---------------+----------+---------+------+------+----------+-------------+
2 rows in set (0.01 sec)
```


## id——select语句的序号

用于标识==select所属的行==，如果只有简单查询则只会显示一行，如果含有复杂查询（简单子查询、派生表、union查询）则会有多行。

## select_type——查询类型

查询分为简单查询和复杂查询两类，则select_type的值也分别对应着这些查询类型：

- 简单查询——SIMPLE

表示不包含子查询。如果有任何复杂部分则最外层查询会标记成PRIMARY。


```
mysql> explain select * from zh_xiache;
+----+-------------+-----------+------------+------+---------------+------+---------+------+------+----------+-------+
| id | select_type | table     | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra |
+----+-------------+-----------+------------+------+---------------+------+---------+------+------+----------+-------+
|  1 | SIMPLE      | zh_xiache | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 1295 |   100.00 | NULL  |
+----+-------------+-----------+------------+------+---------------+------+---------+------+------+----------+-------+
1 row in set (0.01 sec)
```


- 简单子查询——SUBQUERY

在select后的子查询，例如上例。



- 派生表——DERIVED

在from后的子查询。

```
mysql> explain select * from (select id from zh_xiache limit 1) as a;
+----+-------------+------------+------------+--------+---------------+----------+---------+------+------+----------+-------------+
| id | select_type | table      | partitions | type   | possible_keys | key      | key_len | ref  | rows | filtered | Extra       |
+----+-------------+------------+------------+--------+---------------+----------+---------+------+------+----------+-------------+
|  1 | PRIMARY     | <derived2> | NULL       | system | NULL          | NULL     | NULL    | NULL |    1 |   100.00 | NULL        |
|  2 | DERIVED     | zh_xiache  | NULL       | index  | NULL          | idx_date | 4       | NULL | 1295 |   100.00 | Using index |
+----+-------------+------------+------------+--------+---------------+----------+---------+------+------+----------+-------------+
2 rows in set (0.02 sec)
```


- union查询——UNION


```
mysql> explain select 1 union all select 1;
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+----------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra          |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+----------------+
|  1 | PRIMARY     | NULL  | NULL       | NULL | NULL          | NULL | NULL    | NULL | NULL | NULL     | No tables used |
|  2 | UNION       | NULL  | NULL       | NULL | NULL          | NULL | NULL    | NULL | NULL | NULL     | No tables used |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+----------------+
2 rows in set (0.02 sec)
```

- UNION RESULT

用来从union的匿名临时表检索结果的select被标记成union result

- dependent union  union中的第二个或后面的select语句，取决于外面的查询

- dependent subquery  子查询中的第一个select，取决于外面的查询
## table——读取的表的名字

表明了对应的该行在访问哪个表


## type——关联类型（即表示MysQL如何去查询表中的行）

可能有all、index等7种值。

### ALL

全表扫描。即需要扫描整张表去取数据。



### index

同全表扫描，只不过是按索引次序进行，而不是行。避免了排序，但是需要承担按索引次序读取整张表的开销。

### range

有范围的索引扫描。

### ref

索引访问，又称索引查找。

它返回所有匹配某个单个值的行，只有访问非唯一性索引或者唯一性索引的非唯一性前缀时才发生。

ref即表示需要参考某个值。

ref_or_null是ref的一个变体：表示在初次查找的结果里进行第二次查找以找出null条目。


### eq_ref

最多只返回一条符合条件的结果。在使用主键或者唯一性索引查找时可以看到。

### const，system

当MySQL能对查询的某部分进行优化并将其转换成一个常量时，会使用这种访问类型。



### NULL

这种访问方式意味着MySQL==能在优化阶段分解查询语句，在执行阶段甚至用不着再访问表或者索引==。



## possible_keys——查询时可以选用的索引

这是基于查询访问的列和使用的比较操作符来判断的。



## key——显示了MySQL决定采用哪个来优化对该表的访问

- 如果该索引没有出现在possible_keys列中，它可能选择了一个覆盖索引，或出于别的原因。



## key_len——索引里实际使用的字节数

- 显示了索引字段中可能的最大长度，而非表中索引列实际字节数

## ref

显示了之前的表在key列记录索引中查找值所用的列或常量。

## rows——为了查询符合条件的数据，可能读取的行数

- 把所有explain输出的每行的rows列的数据相乘，即可估计执行该查询需要读取的行数。


## filtered——针对某个条件（where子句或联结条件）的记录数所占的百分比

- rows*filtered基本相当于它将和查询计划里前一个表关联的行数。


## extra——用于显示在其他列不适合显示的信息


常见的值如下：

- using index

表示将使用覆盖索引，以避免访问表。

- using where

表示将在存储引擎检索行后再进行过滤。

- using temporary

表示在对结果排序时将使用临时表。

- using filesort

表示会对结果使用一个外部索引排序。



# SQL优化目标——阿里

【推荐】 SQL性能优化的目标：至少要达到 range 级别，要求是ref级别，如果可以是consts最好。 

说明： 
- consts 单表中最多只有一个匹配行（主键或者唯一索引），在优化阶段即可读取到数据。 
- ref 指的是使用普通的索引（normal index）。 
- range 对索引进行范围检索。 

反例：explain表的结果，type=index，索引物理文件全扫描，速度非常慢，这个index级别比较range还低，与全表扫描是小巫见大巫。