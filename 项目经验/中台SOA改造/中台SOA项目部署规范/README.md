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
              * [1.2.2.4.2 配置信息](#12242-配置信息)
              * [1.2.2.4.3 文件描述](#12243-文件描述)
              * [1.2.2.4.4 实例Demo](#12244-实例demo)
            * [1.2.2.5 SpringBoot集成Apache Shiro](#1225-springboot集成apache-shiro)
            * [1.2.2.6 SpringBoot配置多数据源](#1226-springboot配置多数据源)
    * [2. 编码](#2-编码)
       * [2.1 IDE安装Alibaba P3C编码规范插件](#21-ide安装alibaba-p3c编码规范插件)
            * [2.1.1 IntelliJ IDEA安装教程](#211-intellij-idea安装教程)
            * [2.1.2 Eclipse安装教程](#212-eclipse安装教程)
            * [2.1.3 MyEclipse安装教程](#213-myeclipse安装教程)
       * [2.2 上品开发编码规范](#22-上品开发编码规范)
    * [3. 环境配置](#3-环境配置)
       * [3.1 版本兼容说明](#31-版本兼容说明)
       * [3.2 组件配置](#32-组件配置)
            * [3.2.1 数据库连接池配置与说明](#321-数据库连接池配置与说明)
              * [3.2.1.1 单数据源配置](#3211-单数据源配置)
              * [3.2.1.2 多数据源配置](#3212-多数据源配置)
            * [3.2.2 日志组件及配置](#322-日志组件及配置)
            * [3.2.3 缓存组件配置](#323-缓存组件配置)
            * [3.2.4 MQ组件配置](#324-mq组件配置)
            * [3.2.5 线程池配置](#325-线程池配置)
    * [4. 发布与维护](#4-发布与维护)
       * [4.1 快照版发布](#41-快照版发布)
       * [4.2 正式版发布](#42-正式版发布)
       * [4.3 Git仓库代码规约](#43-git仓库代码规约)
            * [4.3.1 代码上传规约](#431-代码上传规约)
              * [4.3.1.1 .gitignore文件的设置](#4311-.gitignore文件的设置)
            * [4.3.2 项目仓库规约](#432-项目仓库规约)
       * [4.4 Tomcat部署规约](#44-tomcat部署规约)
       * [4.5 上线部署规约](#45-上线部署规约)
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
补充说明:job任务单独会以war项目部署,因此要继承mpsi-parent聚合项目,实现版本控制及部署;

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
     这部分不需要在生产者的pom文件中写入,
     因为在mpsi-dependencies中已经定义了全部依赖的jar;
     
 <spring.boot.starter.dubbo.version>1.0.0</spring.boot.starter.dubbo.version 
 <dubbo.version>2.5.7</dubbo.version>  
 <zookeeper.version>3.4.10</zookeeper.version>  
 
 JAR依赖:
 
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <!--parent为mpsi-parent,因为继承其中的组件依赖-->
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
 - 服务消费者
```
  服务消费者依赖的关于
```
###### 1.2.2.4.2 配置信息

 - 服务生产者
```
   [1] application.properties
   	
   	//应用名称  
	spring.dubbo.application.name=inventory-query-provider  
	//注册中心地址 [单机或集群模式] 
	spring.dubbo.registry.address=zookeeper://172.16.103.145:2181  
	//传输协议  
	spring.dubbo.protocol.name=dubbo  
	//传输端口  
	spring.dubbo.protocol.port=20881 

	
   [2]	Service实现层注解改动
    @Service为Dubbo注解注入，不是Spring的!!!!!!
  	
  	@Service(version = "1.0.0", delay = -1)  
	public class SysRecordServiceImpl implements ISysRecordService {  
	  
	    @Autowired  
	    SysRecordMapper sysRecordMapper;  
	  
	    @Override  
	    public List<SysRecord> selectRecord() {  
	        return sysRecordMapper.selectRecord();  
	    }  
	}  
	
	[3].启动类添加@DubboComponentScan注解扫描

	@SpringBootApplication  
	@DubboComponentScan("net.shopin.service.impl")  
	public class InventoryQueryProviderApplication extends SpringBootServletInitializer {  
	  
	    @Override  
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
	        return application.sources(InventoryQueryProviderApplication.class);  
	    }  
	  
	    public static void main(String[] args) {  
	        SpringApplication.run(InventoryQueryProviderApplication.class, args);  
	    }  
	} 

```

 - 服务消费者
```
   [1]application.properties
   	# Springboot-dubbo 消费者配置信息  
	spring.dubbo.application.name=ssd-web  
	spring.dubbo.registry.address=zookeeper://172.16.103.145:2181  
	spring.dubbo.protocol.name=dubbo  
	spring.dubbo.protocol.port=20880  
   [2]Controller层注入DubboService(@Reference)
   	@RestController  
	public class SysRecordController {  
	  
	    @Reference(version = "1.0.0", check = false)  
	    ISysRecordService sysRecordService;  
	  
	    @RequestMapping(value = "selectRecord")  
	    public List<SysRecord> selectRecord() {  
	        return sysRecordService.selectRecord();  
	    }  
	} 
   [3]启动类添加@DubboComponentScan注解扫描
   扫描Controller包
   
   	@SpringBootApplication  
	@DubboComponentScan(basePackages = "net.shopin.controller")  
	public class ConsumerApplication extends SpringBootServletInitializer {  
	
	    @Override  
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
	        return application.sources(ConsumerApplication.class);  
	    }  
	  
	    public static void main(String[] args) {  
	        SpringApplication.run(ConsumerApplication.class, args);  
	    }  
	}  

```
###### 1.2.2.4.3 文件描述
###### 1.2.2.4.4 实例Demo
##### 1.2.2.5 SpringBoot集成Apache Shiro
```
   TODO
```
##### 1.2.2.6 SpringBoot配置多数据源
```
   TODO
```
## 2.编码

### 2.1 IDE安装Alibaba P3C编码规范插件
 - <font color="red">P3C插件</font>
```
   Alibaba 推出的可以集成到IDE上用于检测代码规范性的插件,检查内容就是《Alibaba Java开发规范》中规约内容;
```
#### 2.1.1 IntelliJ IDEA安装教程
[IntelliJ IDEA 安装P3C教程](https://jingyan.baidu.com/article/17bd8e524df1a185aa2bb87c.html)

#### 2.1.2 Eclipse安装教程
[Eclipse 安装P3C教程](https://jingyan.baidu.com/article/2d5afd6923e78b85a3e28e5e.html)

#### 2.1.3 MyEclipse安装教程

[MyEclipse 安装P3C教程](https://jingyan.baidu.com/article/72ee561a72cd74e16138df9e.html)

### 2.2 上品开发编码规范
```
《上品开发编码规范》结合《Alibaba Java开发规范》正式版以及上品具体业务撰写而成，详情可在《上品开发编码规范》中可见。
```
[上品开发编码规范](https://github.com/bjshopin/Shopin/tree/master/%E6%8A%80%E6%9C%AF%E5%BC%80%E5%8F%91/%E4%B8%8A%E5%93%81%E5%BC%80%E5%8F%91%E7%BC%96%E7%A0%81%E8%A7%84%E8%8C%83)

## 3. 环境配置
### 3.1 版本兼容说明

<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>容器</td>
          <td>容器版本</td>
     </tr>
</thead>

<tbody>
    <tr>
        <td>JDK</td>
        <td>JDK 1.8(8u144)</td>
    </tr>
    <tr>
        <td>Tomcat</td>
        <td>8.0.51</td>
    </tr>
    <tr>
        <td>SpringBoot</td>
        <td><font color="red">1.5.12.RELEASE</font></td>
    </tr>
    <tr>
        <td>Maven</td>
        <td>3.5.3</td>
    </tr>
    <tr>
        <td>数据库连接池</td>
        <td>Druid</td>
    </tr>
    <tr>
        <td>日志组件及配置</td>
        <td>Log4j2</td>
    </tr>
</tbody>
</table>

#### 3.2.1 数据库连接池配置与说明
##### 3.2.1.1 单数据源配置
 - 采用Druid数据源配置
```
########################################################
### 配置druid数据源
########################################################
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url=jdbc:mysql://xxx.xxx.xxx.xxx:3306/{project_name}
spring.datasource.username=shopin
spring.datasource.password=shopin
spring.datasource.driverClassName=com.mysql.jdbc.Driver

# 下面为连接池的补充设置，应用到上面所有数据源中
# 初始化大小，最小，最大
spring.datasource.initialSize=5
spring.datasource.minIdle=5
spring.datasource.maxActive=20
# 配置获取连接等待超时的时间
spring.datasource.maxWait=60000
# 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
spring.datasource.timeBetweenEvictionRunsMillis=60000
# 配置一个连接在池中最小生存的时间，单位是毫秒
spring.datasource.minEvictableIdleTimeMillis=300000
spring.datasource.validationQuery=SELECT 1 FROMDUAL
spring.datasource.testWhileIdle=true
spring.datasource.testOnBorrow=false
spring.datasource.testOnReturn=false
# 打开PSCache，并且指定每个连接上PSCache的大小
spring.datasource.poolPreparedStatements=true
spring.datasource.maxPoolPreparedStatementPerConnectionSize=20
# 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
spring.datasource.filters=stat,wall,log4j
# 通过connectProperties属性来打开mergeSql功能；慢SQL记录
spring.datasource.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
# 合并多个DruidDataSource的监控数据
#spring.datasource.useGlobalDataSourceStat=true

```
##### 3.2.1.2 多数据源配置
```
 TODO
```
#### 3.2.2 日志组件及配置
 - log4j2.xml配置
```
  所有项目采用同一套日志配置,差异点在于log的存放地点;[当然]
```
```
 <?xml version="1.0" encoding="UTF-8"?>
<!-- status :表示log4j自身日志的打印级别 -->
<!-- monitorInterval:含义是每隔300秒重新读取配置文件，可以不重启应用的情况下修改配置 -->
<Configuration status="warn" monitorInterval="300">

    <properties>
        <property name="INFO_FILE_NAME">info</property>
        <property name="ERROR_FILE_NAME">error</property>
    </properties>

    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <!-- 左对齐，最小宽度为4；长度>10也可正常显示，不足4用空格补齐 -->
            <PatternLayout pattern="%d{DEFAULT} [%t] %-4level - %l - %msg%n"/>
        </Console>
        <!-- fileName:日志位置以及文件名；filePattern:rolling时新建文件的位置以及命名规则。命名文件名称需要细到时分秒是注意不要这么写
            HH:mm:ss，文件名称不可以包含特殊字符：使用"-"代替 -->
        <RollingRandomAccessFile name="INFO_FILE"
                                 fileName="${sys:user.home}/logs/mpsi-inventory-query/${INFO_FILE_NAME}.log"
                                 filePattern="${sys:user.home}/logs/mpsi-inventory-query/base/$${date:yyyy-MM}/${INFO_FILE_NAME}-%d{yyyy-MM-dd}-%i.log.gz"
                                 append="true">

            <Filters>
                <ThresholdFilter level="WARN" onMatch="DENY" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="INFO" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout
                    pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %l - %msg%n"/>
            <Policies>
                <!-- 下面两个策略：满足一个，即会产生一个文件 -->
                <!-- 日志文件大于100M,就新建文件 -->
                <SizeBasedTriggeringPolicy size="100 MB"/>
                <!-- 结合filePattern:精确到dd(天)，所以表示每天产生一个日志文件 -->
                <TimeBasedTriggeringPolicy interval="1"/>
            </Policies>
            <!-- 作用于filePattern中的i，最大20个文件。 -->
            <DefaultRolloverStrategy max="20"/>
        </RollingRandomAccessFile>

        <RollingRandomAccessFile name="ERROR_FILE"
                                 fileName="${sys:user.home}/logs/mpsi-inventory-query/${ERROR_FILE_NAME}.log"
                                 filePattern="${sys:user.home}/logs/mpsi-inventory-query/error/$${date:yyyy-MM}/${ERROR_FILE_NAME}-%d{yyyy-MM}-%i.log.gz"
                                 append="true">
            <Filters>
                <ThresholdFilter level="fatal" onMatch="DENY" onMismatch="NEUTRAL"/>
                <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>
            <PatternLayout
                    pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %l - %msg%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="100 MB"/>
                <TimeBasedTriggeringPolicy interval="1"/>
            </Policies>
            <DefaultRolloverStrategy max="20"/>
        </RollingRandomAccessFile>

    </Appenders>
    <Loggers>
        <!-- additivity="false"表示在该logger中输出的日志不会再延伸到父层logger。这里如果改为true，则会延伸到Root
            Logger，遵循Root Logger的配置也输出一次。 -->

        <!-- logger的级别优先于appender的级别，logger为debug,appender为info,debug信息依然可以呈现。即以logger级别为主。 -->
        <logger name="cn.shopin.supplier.mapper" level="debug" additivity="false">
            <appender-ref ref="Console"/>
        </logger>

        <Root level="INFO">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="INFO_FILE"/>
            <AppenderRef ref="ERROR_FILE"/>
        </Root>
    </Loggers>
</Configuration>
```

#### 3.2.3 缓存组件配置

#### 3.2.4 MQ组件配置

#### 3.2.5 线程池配置

## 4 发布与维护
### 4.1 快照版发布
```
  快照版用于测试环境,快照版本均为X.Y.Z-SNAPSHOT 
  ,(字母Z后面为中横线-,不是. 否则上传私服会出现错误)X迭代即代表大版本升级或者框架升级,Z迭代即代表需求更新,功能添加,Z满十进一。
```
### 4.2 正式版发布
```
  正式版本用于生产环境, 正式版本均为X.Y.Z.RELEASE 
  ,X迭代即代表大版本升级或者框架升级,Z迭代即代表需求更新，功能添加，Z满十进一.
```
### 4.3 Git仓库代码规约
#### 4.3.1 代码上传规约
```
  Git仓库上传代码时只上传src源码文件夹 , 
  pom文件以及统一版本的.gitignore文件(用于过滤不需要的文件) , 严禁上传其它文件;
```
##### 4.3.1.1 .gitignore文件内容的设置
```
    统一.gitignore文件内容:
       排除内容包括: maven打包信息, IDE 生成信息(Eclipse,IDEA) 以及临时文件信息,内容如下:
```
```java
# Created by .ignore support plugin (hsz.mobi)
### Eclipse template

.metadata
bin/
tmp/
*.tmp
*.bak
*.swp
*~.nib
local.properties
.settings/
.loadpath
.recommenders

# External tool builders
.externalToolBuilders/

# Locally stored "Eclipse launch configurations"
*.launch

# PyDev specific (Python IDE for Eclipse)
*.pydevproject

# CDT-specific (C/C++ Development Tooling)
.cproject

# CDT- autotools
.autotools

# Java annotation processor (APT)
.factorypath

# PDT-specific (PHP Development Tools)
.buildpath

# sbteclipse plugin
.target

# Tern plugin
.tern-project

# TeXlipse plugin
.texlipse

# STS (Spring Tool Suite)
.springBeans

# Code Recommenders
.recommenders/

# Scala IDE specific (Scala & Java development for Eclipse)
.cache-main
.scala_dependencies
.worksheet
### JetBrains template
# Covers JetBrains IDEs: IntelliJ, RubyMine, PhpStorm, AppCode, PyCharm, CLion, Android Studio and WebStorm
# Reference: https://intellij-support.jetbrains.com/hc/en-us/articles/206544839

# User-specific stuff
.idea/**/workspace.xml
.idea/**/tasks.xml
.idea/**/dictionaries
.idea/**/shelf

# Sensitive or high-churn files
.idea/**/dataSources/
.idea/**/dataSources.ids
.idea/**/dataSources.local.xml
.idea/**/sqlDataSources.xml
.idea/**/dynamic.xml
.idea/**/uiDesigner.xml

# Gradle
.idea/**/gradle.xml
.idea/**/libraries

# CMake
cmake-build-debug/
cmake-build-release/

# Mongo Explorer plugin
.idea/**/mongoSettings.xml

# File-based project format
*.iws

# IntelliJ
out/

# mpeltonen/sbt-idea plugin
.idea_modules/

# JIRA plugin
atlassian-ide-plugin.xml

# Cursive Clojure plugin
.idea/replstate.xml

# Crashlytics plugin (for Android Studio and IntelliJ)
com_crashlytics_export_strings.xml
crashlytics.properties
crashlytics-build.properties
fabric.properties

# Editor-based Rest Client
.idea/httpRequests
### macOS template
# General
.DS_Store
.AppleDouble
.LSOverride

# Icon must end with two \r
Icon

# Thumbnails
._*

# Files that might appear in the root of a volume
.DocumentRevisions-V100
.fseventsd
.Spotlight-V100
.TemporaryItems
.Trashes
.VolumeIcon.icns
.com.apple.timemachine.donotpresent

# Directories potentially created on remote AFP share
.AppleDB
.AppleDesktop
Network Trash Folder
Temporary Items
.apdisk
### Example user template template
### Example user template

# IntelliJ project files
.idea
*.iml
out
gen### Java template
# Compiled class file
*.class

# Log file
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# virtual machine crash logs, see http://www.java.com/en/download/help/error_hotspot.xml
hs_err_pid*

```
#### 4.3.2 项目仓库规约
 - 概述
```
   每个项目按照1.2.1.3中 项目组成结构去拆分自己的项目:
   [1]各层次的impl
   [2]schedule
   [3]服务组件(project-parent聚合项目,包括bean,持久化层,service接口)

```
 - 划分实例
```
  以ssd-common项目为例,项目拆分之后,考虑到部署阶段使用JenKins自动化部署过程 , 需要将所有提供服务的项目(service-impl)部署为war包,其余项目分类之后依次打成jar包上传nexus私服;
```
<table frame="hsides" rules="groups" cellspacing=0 cellpadding=0>
<!-- 表头部分 -->
<thead align=center style="font-weight:bolder; background-color:#cccccc">
     <tr>
          <td>项目名称</td>
          <td>项目类型</td>
          <td>项目作用</td>
          <td>仓库地址</td>
     </tr>
</thead>

<tbody>
    <tr>
        <td>material-query-service-impl</td>
        <td>war </td>
        <td>主数据查询服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:materiel-query-service-impl.git</td>
    </tr>
    <tr>
        <td>material-update-service-impl</td>
        <td>war</td>
        <td>主数据更新服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:materiel-update-service-impl.git</td>
    </tr>
    <tr>
        <td>price-query-service-impl</td>
        <td>war </td>
        <td>价格查询服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:price-query-service-impl.git</td>
    </tr>
    <tr>
        <td>price-update-service-impl</td>
        <td>war</td>
        <td>价格更新服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:price-update-service-impl.git</td>
    </tr>
    <tr>
        <td>stock-query-service-impl</td>
        <td>war </td>
        <td>库存查询服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:stock-query-service-impl.git</td>
    </tr>
    <tr>
        <td>stock-update-service-impl</td>
        <td>war </td>
        <td>库存更新服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:stock-update-service-impl.git</td>
    </tr>
     <tr>
        <td>inventory-query-service-impl</td>
        <td>war</td>
        <td>盘点更新服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:inventory-query-service-impl.git</td>
    </tr>
     <tr>
        <td>inventory-update-service-impl</td>
        <td>war </td>
        <td>盘点更新服务提供方</td>
        <td>git@xxx.xxx.xxx.xxx:inventory-update-service-impl.git</td>
    </tr>
    <tr>
        <td>mpsi-dependencies</td>
        <td>pom</td>
        <td>控制项目所有的jar版本</td>
        <td>git@xxx.xxx.xxx.xxx:mpsi-dependencies.git</td>
    </tr>
    <tr>
        <td>mpsi-parent</td>
        <td>pom</td>
        <td>控制子模块的版本以及部署</td>
        <td>git@xxx.xxx.xxx.xxx:mpsi-parent.git</td>
    </tr>
    <tr>
        <td>mpsi-common-bean</td>
        <td>jar</td>
        <td>所有实体类</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>material-query-persistence</td>
        <td>jar</td>
        <td>主数据查询持久化层</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>material-query-service</td>
        <td>jar</td>
        <td>主数据查询服务接口</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>material-update-persistence</td>
        <td>jar</td>
        <td>主数据更新持久化层</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>material-update-service</td>
        <td>jar</td>
        <td>主数据更新服务接口</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>price-query-persistence</td>
        <td>jar</td>
        <td>价格查询持久化层</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>price-query-service</td>
        <td>jar</td>
        <td>价格查询服务接口</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>price-update-persistence</td>
        <td>jar</td>
        <td>价格更新持久化层</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>price-update-service</td>
        <td>jar</td>
        <td>价格更新服务接口</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>stock-query-persistence</td>
        <td>jar</td>
        <td>库存查询持久化层</td>
        <td>NULL</td>
    </tr>
     <tr>
        <td>stock-query-service</td>
        <td>jar</td>
        <td>库存查询服务接口</td>
        <td>NULL</td>
    </tr> 
    <tr>
        <td>stock-update-persistence</td>
        <td>jar</td>
        <td>库存更新持久化层</td>
        <td>NULL</td>
    </tr> 
    <tr>
        <td>stock-update-service</td>
        <td>jar</td>
        <td>库存更新持久化层</td>
        <td>NULL</td>
    </tr> 
    <tr>
        <td>inventory-query-persistence</td>
        <td>jar</td>
        <td>盘点查询持久化层</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>inventory-query-service</td>
        <td>jar</td>
        <td>盘点查询服务接口</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>inventory-update-persistence</td>
        <td>jar</td>
        <td>盘点更新持久化层</td>
        <td>NULL</td>
    </tr>
    <tr>
        <td>inventory-update-service</td>
        <td>jar</td>
        <td>盘点更新服务接口</td>
        <td>NULL</td>
    </tr>
</tbody>
</table>

### 4.4 Tomcat部署规约

 - Tomcat命名:
```
  部署的项目时要求命名为: apache-tomcat-8.0.33-[project]-[远程停服务端口]-[HTTP端口]-[AJP端口]
```
 - Tomcat配置(TODO):
```
  默认内存配置512M[需根据实际内存需求分配]
```

### 4.5 上线部署规约
  - impl服务实现
```
  服务提供方使用JenKins以war包形式部署
  
```
  - 后台组件
```
   测试阶段:SNAPSHOT版本上传私服
   正式阶段:RELEASE版本上传私服
```
 

 



  


    
