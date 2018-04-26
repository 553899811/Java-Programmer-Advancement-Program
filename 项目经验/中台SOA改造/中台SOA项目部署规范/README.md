<!-- GFM-TOC -->
* [中台SOA部署规范](#中台soa部署规范)
    * [1. 构建](#1-构建)
        * [1.1 项目拆分](#11-项目拆分)
          * [1.1.1 拆分逻辑](#111-拆分逻辑)
          * [1.1.2 项目命名](#112-项目命名)
            * [1.1.2.1 Group](#1121-group) 
            * [1.1.2.2 Artifact](#1122-artifact)
            * [1.1.2.3 Package](#1123-package)
          * [1.1.3 模块依赖](#113-模块依赖)
        * [1.2 项目搭建](#12-项目搭建)
          * [1.2.1 项目结构](#121-项目结构)
            * [1.2.1.1 mpsi-dependencies](#1211-mpsi-dependencies)
              * [1.2.1.1.1 作用](#12111-作用)
              * [1.2.1.1.2 Maven依赖](#12112-maven依赖)
            * [1.2.1.2 mpsi-parent](#1212-mpsi-parent)
              * [1.2.1.2.1 作用](#12121-作用)
              * [1.2.1.2.2 Maven依赖](#12122-maven依赖)
            * [1.2.1.3 项目组成结构](#1213-项目组成结构)
          * [1.2.2 SpringBoot集成](#122-springboot集成)
            * [1.2.2.1 SpringBoot集成RedisTemplate](#1221-springboot集成redistemplate)
              * [1.2.2.1.1 Maven依赖](#12211-maven依赖)
              * [1.2.2.1.2 配置信息](#12212-配置信息)
              * [1.2.2.1.3 文件描述](#12213-文件描述)
              * [1.2.2.1.4 实例Demo](#12214-实例demo)
            * [1.2.2.2 SpringBoot集成Schedule定时任务](#1222-springboot集成schedule定时任务)
              * [1.2.2.2.1 串行执行](#12221-串行执行)
              * [1.2.2.2.2 并行执行](#12222-并行执行)
              * [1.2.2.2.3 实例Demo](#12223-实例demo)
            * [1.2.2.3 SpringBoot集成Email](#1223-springboot集成email)
              * [1.2.2.3.1 Maven依赖](#12231-maven依赖)
              * [1.2.2.3.2 配置信息](#12232-配置信息)
              * [1.2.2.3.3 文件描述](#12233-文件描述)
              * [1.2.2.3.4 实例Demo](#12234-实例demo)
            * [1.2.2.4 SpringBoot集成Dubbo](#1224-springboot集成dubbo)
              * [1.2.2.4.1 Maven依赖](#12241-maven依赖) 
        * [1.2 包名规范](#12-包名规范)
        * [1.3 类名规范](#13-类名规范)
        
<!-- GFM-TOC -->
# 中台SOA部署规范
## 1. 构建
### 1.1 项目拆分
#### 1.1.1 拆分逻辑
```
 由于之前ssd的主要服务来源于ssd-common项目,其中主要包括四部分业务的处理: 
（1）主数据(materiel)
（2）价格(price)
（3）库存(stock)
（4）盘点(inventory)
```
```
  拆分逻辑就是: 主数据-> 价格 -> 库存 -> 盘点, 按照数据流向划分。
  以盘点为例(考虑读写分离) ,简单讲解下如何拆分:
```
<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>模块(项目)名称</td>
          <td>模块(项目)作用</td>
          <td>模块(项目)类型</td>
     </tr>
</thead>

<tbody>
    <tr>
        <td>mpsi-common-bean</td>
        <td>所有的实体类 </td>
        <td>jar</td>
    </tr>
    <tr>
        <td>inventory-query-persistence</td>
        <td>盘点查询内容的持久化层</td>
        <td>jar</td>
    </tr>
    <tr>
        <td>inventory-query-service</td>
        <td>盘点查询内容的Interface </td>
        <td>jar</td>
    </tr>
    <tr>
        <td>inventory-query-service-impl</td>
        <td>盘点查询内容的服务实现类</td>
        <td><font color="red">war</font></td>
    </tr>
    <tr>
        <td>inventory-update-persistence</td>
        <td>盘点更新操作的持久化层</td>
        <td>jar</td>
    </tr>
    <tr>
        <td>inventory-update-service</td>
        <td> 盘点更新操作的Interface</td>
        <td>jar</td>
    </tr>
     <tr>
        <td>inventory-update-service-impl</td>
        <td>盘点更新操作的服务实现类</td>
        <td><font color="red">war</font></td>
    </tr>
</tbody>
</table>
 
  - 解析
```
   在考虑了SOA架构,项目读写分离以及JenKins部署的前提下,我们将服务的提供方单独提取出来(共8个项目),
   其余组件(bean,mapper,service)构建为聚合工程来统一控制组件版本.经过整理之后,将所有的impl归为一类,
   将其他所有的组件(mpsi-parent)加入到聚合工程中,因为组件到时只是为了向私服部署,
   而impl是要结合Jenkins部署,因此impl要单独独立出来;
```
  - impl结构图
<div align=center><img src="https://github.com/bjshopin/Shopin/blob/master/%E6%8A%80%E6%9C%AF%E5%BC%80%E5%8F%91/%E4%B8%AD%E5%8F%B0SOA%E9%A1%B9%E7%9B%AE%E9%83%A8%E7%BD%B2%E8%A7%84%E8%8C%83/img/2.png"/>
</div>
 - 后台组件图(聚合项目)
<div align=center><img src="https://github.com/bjshopin/Shopin/blob/master/%E6%8A%80%E6%9C%AF%E5%BC%80%E5%8F%91/%E4%B8%AD%E5%8F%B0SOA%E9%A1%B9%E7%9B%AE%E9%83%A8%E7%BD%B2%E8%A7%84%E8%8C%83/img/1.png"/>
</div>

#### 1.1.2 项目命名
```
     统一使用: 项目名称 + (读/写) + 功能(persistence/service/serviceImpl/bean) 
```

##### 1.1.2.1 Group
```
 net.shopin.[project]
```
##### 1.1.2.2 Artifact
```
   [project]-bean
   [project]-[Query/Update]-persistence
   [project]-[Query/Update]-service
   [project]-[Query/Update]-service-impl
   [project]-schedule
```

##### 1.1.2.3 Package
```
 net.shopin.[artifact].[module]
```

#### 1.1.3 模块依赖

<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>项目ID</td>
          <td>项目名称</td>
          <td>项目作用</td>
          <td>项目类型</td>
          <td>项目依赖</td>
          <td>项目Parent</td>
     </tr>
</thead>

<tbody>
    <tr>
        <td>1</td>
        <td>bean </td>
        <td>实体类</td>
        <td>jar</td>
        <td>NULL</td>
        <td>mpsi-parent</td>
    </tr>
    <tr>
        <td>2</td>
        <td>persistence</td>
        <td>持久化Mapper</td>
        <td>jar</td>
        <td>1</td>
        <td>mpsi-parent</td>
    </tr>
    <tr>
        <td>3</td>
        <td>service </td>
        <td>暴露的service</td>
        <td>jar</td>
        <td>1</td>
        <td>mpsi-parent</td>
    </tr>
    <tr>
        <td>4</td>
        <td><font color="red">service-impl(非模块)</font></td>
        <td>服务实现</td>
        <td><font color="red">war</font></td>
        <td>2,3</td>
        <td>mpsi-parent</td>
    </tr>
    <tr>
        <td>5</td>
        <td>web</td>
        <td>项目View</td>
        <td><font color="red">war</font></td>
        <td>1,3</td>
        <td>mpsi-parent</td>
    </tr>
    <tr>
        <td>6</td>
        <td>schedule</td>
        <td>定时Job</td>
        <td><font color="red">war</font></td>
        <td>1,3</td>
        <td>mpsi-parent</td>
    </tr>
</tbody>
</table>

### 1.2 项目搭建
#### 1.2.1 项目结构
##### 1.2.1.1 mpsi-dependencies
###### 1.2.1.1.1 作用
```
  mpsi-dependencies的主要目的在于 : 
  用来指定SpringBoot体系中没有的第三方JAR版本,
  例如在此项目中需要的Zookeeper,Dubbo等等的版本号;
  
  其次一个目的就是 : 用于maven部署管理，一方面需要将自身deploy至Nexus,
  另一方面也使得继承与它的子项目可以打包deploy至私服;
```
###### 1.2.1.1.2 Maven依赖
```
   <?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- SpringBoot主版本控制 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.8.RELEASE</version>
        <!--
        默认值为../pom.xml
        查找顺序：relativePath元素中的地址–本地仓库–远程仓库
        -->
        <relativePath/>
    </parent>

    <groupId>net.shopin</groupId>
    <artifactId>mpsi-dependencies</artifactId>
    <version>1.0.0.SNAPSHOT</version>
    <packaging>pom</packaging>
    <modelVersion>4.0.0</modelVersion>

    <name>${project.artifactId}</name>
    <!-- shopin基础服务(主数据,价格,库存,盘点)中需要引入的依赖 -->
    <description>The parent project of mpsi</description>

    <!--开始时间-->
    <inceptionYear>2018</inceptionYear>

    <organization>
        <name>Shopin</name>
        <url>http://www.shopin.net</url>
    </organization>

    <properties>
        <junit.version>4.12</junit.version>
        <mybatis.spring.boot.starter.version>1.2.2</mybatis.spring.boot.starter.version>
        <spring.boot.starter.dubbo.version>1.0.0</spring.boot.starter.dubbo.version>
        <dubbo.version>2.5.7</dubbo.version>
        <zookeeper.version>3.4.10</zookeeper.version>
        <mysql.connector.java.version>5.1.41</mysql.connector.java.version>
        <lombok.version>1.16.18</lombok.version>
    </properties>

    <dependencyManagement>


        <dependencies>
            <!--引入mybatis与Springboot整合[持久化层引用]-->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.spring.boot.starter.version}</version>
            </dependency>
            <!--junit测试-->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
                <scope>test</scope>
            </dependency>

            <!--引入dubbo-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>dubbo</artifactId>
                <version>${dubbo.version}</version>
                <!-- 移除 dubbo中内嵌 Spring -->
                <exclusions>
                    <exclusion>
                        <groupId>org.springframework</groupId>
                        <artifactId>spring-context</artifactId>
                    </exclusion>
                    <exclusion>
                        <groupId>org.springframework</groupId>
                        <artifactId>spring-beans</artifactId>
                    </exclusion>
                    <exclusion>
                        <groupId>org.springframework</groupId>
                        <artifactId>spring-web</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <!--引入zookeeper-->
            <dependency>
                <groupId>org.apache.zookeeper</groupId>
                <artifactId>zookeeper</artifactId>
                <version>${zookeeper.version}</version>
                <type>pom</type>
            </dependency>
            <!--springboot整合Dubbo-->
            <dependency>
                <groupId>io.dubbo.springboot</groupId>
                <artifactId>spring-boot-starter-dubbo</artifactId>
                <version>${spring.boot.starter.dubbo.version}</version>
                <exclusions>
                    <exclusion>
                        <groupId>com.alibaba</groupId>
                        <artifactId>dubbo</artifactId>
                    </exclusion>
                    <exclusion>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <!--引入Mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.connector.java.version}</version>
                <scope>runtime</scope>
            </dependency>
            <!--引入junit测试类-->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>

            <!--引入lombok-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
                <scope>provided</scope>
            </dependency>

        </dependencies>
    </dependencyManagement>

    <!--私服地址配置-->
    <profiles>
        <profile>
            <id>ShopinProduceNexus</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <release.url>http://xxx.xxx.xxx.xxx:1081/nexus/content/repositories/releases/</release.url>
                <snapshot.url>http://xxx.xxx.xxx.xxx:1081/nexus/content/repositories/snapshots/</snapshot.url>
            </properties>
        </profile>
        <profile>
            <id>AliyunNexus</id>
            <properties>
                <release.url>http://xxx.xxx.xxx.xxx:1081/nexus/content/repositories/releases/</release.url>
                <snapshot.url>http://xxx.xxx.xxx.xxx:1081/nexus/content/repositories/snapshots/</snapshot.url>
            </properties>
        </profile>
    </profiles>
    <!-- 部署构件配置 begin -->
    <distributionManagement>
        <!--
            <server> <id>snapshots</id> <username>admin</username> <password>admin123</password>
            </server> <server> <id>releases</id> <username>admin</username> <password>admin123</password>
            </server>
        -->
        <repository>
            <id>releases</id><!-- 必须与setting.xml <server>id</server>认证信息一致 -->
            <name>releases</name>
            <url>${release.url}</url>
        </repository>
        <snapshotRepository>
            <id>snapshots</id>
            <name>Snapshots</name>
            <url>${snapshot.url}</url>
        </snapshotRepository>
    </distributionManagement>
    <!-- 部署构件配置 end -->
</project>

```

##### 1.2.1.2 mpsi-parent
###### 1.2.1.2.1 作用
```
  mpsi-parent的作用也有2方面;
【1】在于控制服务组件的版本，JDK版本，maven编译版本,编码格式等等;
【2】方便各个子模块的版本控制的同时,能够实现统一deploy至私服，方便部署.

```
###### 1.2.1.2.2 Maven依赖
```
  <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>net.shopin</groupId>
        <artifactId>mpsi-dependencies</artifactId>
        <version>1.0.0.SNAPSHOT</version>
    </parent>

    <!--各个子模块-->
    <modules>
        <module>inventory-query-service</module>
        <module>inventory-query-persistence</module>
        <module>inventory-update-persistence</module>
        <module>inventory-update-service</module>
        <module>materiel-query-persistence</module>
        <module>materiel-query-service</module>
        <module>materiel-update-persistence</module>
        <module>materiel-update-service</module>
        <module>mpsi-common-bean</module>
        <module>price-query-persistence</module>
        <module>price-query-service</module>
        <module>price-update-persistence</module>
        <module>price-update-service</module>
        <module>stock-query-persistence</module>
        <module>stock-query-service</module>
        <module>stock-update-persistence</module>
        <module>stock-update-service</module>
    </modules>

    <artifactId>mpsi-parent</artifactId>
    <!--mpsi版本控制所有module-->
    <version>1.0.0.SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>mpsi-parent</name>

    <properties>
        <!-- 文件拷贝时的编码 -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <!-- 编译时的编码 -->
        <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
        <!-- Java版本 -->
        <java.version>1.8</java.version>
        <!--组件版本控制-->
        <mpsi.backstage.section.version>1.0.0.SNAPSHOT</mpsi.backstage.section.version>
    </properties>

    <dependencyManagement>

        <!--将其余组件在root pom中引入,其余子模块可以在自己的pom中直接引入其它子模块-->
        <dependencies>
            <!--引入实体类-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>mpsi-common-bean</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
            <!--引入盘点查询接口服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>inventory-query-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入盘点查询持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>inventory-query-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入盘点更新接口服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>inventory-update-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入盘点更新持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>inventory-update-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
            <!--引入主数据查询服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>materiel-query-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
            <!--引入主数据查询持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>materiel-query-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
            <!--引入主数据更新服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>materiel-update-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
            <!--引入主数据更新持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>materiel-update-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入价格查询服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>price-query-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入价格查询持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>price-query-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入价格更新服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>price-update-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
            <!--引入价格更新持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>price-update-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入库存查询服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>stock-query-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入库存查询持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>stock-query-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入库存更新服务-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>stock-update-service</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>

            <!--引入库存更新持久化层-->
            <dependency>
                <groupId>net.shopin</groupId>
                <artifactId>stock-update-persistence</artifactId>
                <version>${mpsi.backstage.section.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>


```
##### 1.2.1.3 项目组成结构

<div align=center><img src="https://github.com/bjshopin/Shopin/blob/master/%E6%8A%80%E6%9C%AF%E5%BC%80%E5%8F%91/%E4%B8%AD%E5%8F%B0SOA%E9%A1%B9%E7%9B%AE%E9%83%A8%E7%BD%B2%E8%A7%84%E8%8C%83/img/3.jpg"/>
</div>

 - <font color="red">注意</font>
 
```
注意: mpsi-[module]-service-impl不纳入mpsi-parent的聚合项目中,每一个服务实现都是单独的war项目 ,
但是它需要依赖于mpsi-parent，原因在于mpsi-parent继承与mpsi-dependencies,
这样就可以将版本依赖和控制集成到项目中.
```
#### 1.2.2 SpringBoot集成
##### 1.2.2.1 SpringBoot集成RedisTemplate
###### 1.2.2.1.1 Maven依赖
```
  <dependency>  
         <groupId>org.springframework.boot</groupId>  
         <artifactId>spring-boot-starter-data-redis</artifactId>  
         <version>1.5.8.RELEASE</version>  
  </dependency>  
```
###### 1.2.2.1.2 配置信息
 - 在application.properties中配置:
```
 spring.redis.cluster.nodes=xxx.xxx.xxx.xxx:10001,xxx.xxx.xxx.xxx:10002,xxx.xxx.xxx:10003 
```
###### 1.2.2.1.3 文件描述
 - 自定义RedisTemplate类
```
在spring-data-redis中,RedisTemplate的基础上支持更多方法的复写:(如果方法不满足实际开发需求,请及时告知开发者使其完善功能)
```
代码地址: [JxRedisTemplate](https://paste.ubuntu.com/p/dPRhTB7cjn/)
###### 1.2.2.1.4 实例Demo
[SpringBoot整合Redis](https://github.com/553899811/NewBie-Plan/tree/master/SpringBoot/springboot-redis)

##### 1.2.2.2 SpringBoot集成Schedule定时任务
###### 1.2.2.2.1 串行执行
```
  SpringBoot中定时Job 使用@Schedule 注解启动Job方法,默认为串行依次执行Job任务,
  也就是说无论多少个Task,都是一个线程串行执行;
```
```
    串行Job添加类注解  
  @Component  
  @EnableScheduling  
  方法注解  
  @Scheduled(fixedRate = 3 * 1000)  
  或者 
  @Scheduled(cron = 表达式) 

```
###### 1.2.2.2.2 并行执行
```
 SpringBoot中Job支持并行执行(多个任务同时执行),需手动配置
```
```
  并行Job添加类注解  
  @Component  
  @EnableScheduling  
  @EnableAsync(mode = AdviceMode.PROXY, proxyTargetClass = false,  
          order = Ordered.HIGHEST_PRECEDENCE  
  )  
  方法注解  
  @Scheduled(fixedRate = 3 * 1000)  
  @Async  
  或者  
  @Scheduled(cron = 表达式)   
  @Async 
```
###### 1.2.2.2.3 实例Demo
[SpringBoot整合Schedule定时任务](https://github.com/553899811/NewBie-Plan/tree/master/SpringBoot/springboot-schedule)

##### 1.2.2.3 SpringBoot集成Email
###### 1.2.2.3.1 Maven依赖
###### 1.2.2.3.2 配置信息
###### 1.2.2.3.3 文件描述
###### 1.2.2.3.4 实例Demo

##### 1.2.2.4 SpringBoot集成Dubbo
###### 1.2.2.4.1 Maven依赖
  - 服务生产者
```
 版本配置:
 
 <spring.boot.starter.dubbo.version>1.0.0</spring.boot.starter.dubbo.version 
 <dubbo.version>2.5.7</dubbo.version>  
 <zookeeper.version>3.4.10</zookeeper.version>  
 
 JAR依赖:
 
 <?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>net.shopin</groupId>
        <artifactId>mpsi-parent</artifactId>
        <!--mpsi版本控制所有module-->
        <version>1.0.0.SNAPSHOT</version>
    </parent>

    <artifactId>inventory-query-service-impl</artifactId>
    <version>1.0.0.SNAPSHOT</version>
    <packaging>war</packaging>

    <dependencies>
        <!--引入自身模块的service组件-->
        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>inventory-query-service</artifactId>
        </dependency>

        <!--引入自身模块的mapper组件-->
        <dependency>
            <groupId>net.shopin</groupId>
            <artifactId>inventory-query-persistence</artifactId>
        </dependency>
        <!--引入spring-aop切面-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <!--引入springboot的自动化布置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        <!--引入-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 引入外部tomcat适配 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>

        <!-- 引入log4j2日志组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <!-- 移除 dubbo中内嵌 Spring -->
            <exclusions>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-context</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-beans</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-web</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <type>pom</type>
        </dependency>

        <dependency>
            <groupId>io.dubbo.springboot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>com.alibaba</groupId>
                    <artifactId>dubbo</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!--引入Mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- 开发组约定三套环境配置 begin -->
    <profiles>
        <profile>
            <id>local</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <runtime.env>src/main/resources/profile-active/local</runtime.env>
            </properties>
        </profile>
        <profile>
            <id>dev</id>
            <properties>
                <runtime.env>src/main/resources/profile-active/dev</runtime.env>
            </properties>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <runtime.env>src/main/resources/profile-active/prod</runtime.env>
            </properties>
        </profile>
    </profiles>

    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                    <include>**/*.tld</include>
                </includes>
                <filtering>false</filtering>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <excludes>
                    <exclude>profile-active/**</exclude>
                </excludes>
            </resource>
            <resource>
                <directory>${runtime.env}</directory>
            </resource>
        </resources>
        <!-- 开发组约定 配置文件 三套环境 end -->
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.7</source>
                    <target>1.7</target>
                </configuration>
            </plugin>

            <!--配置maven插件-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>2.1</version>
                <configuration>
                    <attach>true</attach>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!--maven-javadoc插件-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>2.10.3</version>
            </plugin>

            <!--忽略掉war中 web.xml -->
            <plugin>
                <artifactId>maven-war-plugin</artifactId>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <!--用于自动重启部署,如果没有该项配置，可能devtools不会起作用，即应用不会restart -->
                    <fork>true</fork>
                    <finalName>inventory-query-service-impl</finalName>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

```
    
