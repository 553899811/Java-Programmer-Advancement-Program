# 50道SQL练习
## URL

  [https://www.jianshu.com/p/476b52ee4f1b](https://www.jianshu.com/p/476b52ee4f1b)

## 表结构
 - 学生表Student
 ```
 Student(SId,Sname,Sage,Ssex)
--SId 学生编号,Sname 学生姓名,Sage 出生年月,Ssex 学生性别
 ```
 ```
create table Student(SId varchar(10),Sname varchar(10),Sage datetime,Ssex varchar(10));
insert into Student values('01' , '赵雷' , '1990-01-01' , '男');
insert into Student values('02' , '钱电' , '1990-12-21' , '男');
insert into Student values('03' , '孙风' , '1990-12-20' , '男');
insert into Student values('04' , '李云' , '1990-12-06' , '男');
insert into Student values('05' , '周梅' , '1991-12-01' , '女');
insert into Student values('06' , '吴兰' , '1992-01-01' , '女');
insert into Student values('07' , '郑竹' , '1989-01-01' , '女');
insert into Student values('09' , '张三' , '2017-12-20' , '女');
insert into Student values('10' , '李四' , '2017-12-25' , '女');
insert into Student values('11' , '李四' , '2012-06-06' , '女');
insert into Student values('12' , '赵六' , '2013-06-13' , '女');
insert into Student values('13' , '孙七' , '2014-06-01' , '女');
 ```
  - 科目表Course
```
 Course(CId,Cname,TId)
--CId 课程编号,Cname 课程名称,TId 教师编号
```
```
create table Course(CId varchar(10),Cname nvarchar(10),TId varchar(10));
insert into Course values('01' , '语文' , '02');
insert into Course values('02' , '数学' , '01');
insert into Course values('03' , '英语' , '03');
```
 - 教师表Teacher
 ```
  Teacher(TId,Tname)
--TId 教师编号,Tname 教师姓名
 ```
 ```
create table Teacher(TId varchar(10),Tname varchar(10));
insert into Teacher values('01' , '张三');
insert into Teacher values('02' , '李四');
insert into Teacher values('03' , '王五');
 ```
  - 成绩表
```
   SC(SId,CId,score)
--SId 学生编号,CId 课程编号,score 分数
```
```
  create table SC(SId varchar(10),CId varchar(10),score decimal(18,1));
insert into SC values('01' , '01' , 80);
insert into SC values('01' , '02' , 90);
insert into SC values('01' , '03' , 99);
insert into SC values('02' , '01' , 70);
insert into SC values('02' , '02' , 60);
insert into SC values('02' , '03' , 80);
insert into SC values('03' , '01' , 80);
insert into SC values('03' , '02' , 80);
insert into SC values('03' , '03' , 80);
insert into SC values('04' , '01' , 50);
insert into SC values('04' , '02' , 30);
insert into SC values('04' , '03' , 20);
insert into SC values('05' , '01' , 76);
insert into SC values('05' , '02' , 87);
insert into SC values('06' , '01' , 31);
insert into SC values('06' , '03' , 34);
insert into SC values('07' , '02' , 89);
insert into SC values('07' , '03' , 98);
```

## 题目
1.查询" 01 "课程比" 02 "课程成绩高的学生的信息及课程分数
```
  select * from student stu
RIGHT JOIN
(select t1.sid,sc1_score,sc2_score
FROM
 (select sc1.SId,sc1.score sc1_score from sc sc1 where sc1.CId='01') as t1,
 (select sc2.sid,sc2.score sc2_score from sc sc2 where sc2.cid='02') as t2
where t1.SId=t2.sid AND sc1_score > sc2_score) result
ON stu.sid=result.sid;
```
2.查询同时存在" 01 "课程和" 02 "课程的情况
```
  
```