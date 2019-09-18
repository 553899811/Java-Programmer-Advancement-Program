# SpringBoot 常见面试题
## 1.SpringBoot启动原理
[SpringBoot启动原理](http://tengj.top/2017/03/09/springboot3/)
```
 @SpringBootApplication
  |
 @EnableAutoConfiguration
  | 
 @Import(EnableAutoConfigurationImportSelector.class)
  |
 SpringFactoriesLoader
 
```
