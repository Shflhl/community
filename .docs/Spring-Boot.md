[TOC]



# Spring Boot

## 1 Spring Boot中常用注解

### @SpringBootApplication

说明：添加在启动类当中相当于添加下面的注解

- @Configuration:把当前类作为bean定义源
- @EnableAutoConfiguration:
- @ComponentScan:扫描当前类下面的注解

### @Configuration

说明：添加此注解的类相当于配置Bean的配置文件，一个方法用@Bean表示生成一个Bean，这个配置类也会被注册为Bean。

~~~java
@Configuration
public class AppConfig {
    @Bean
    public MyBean myBean() {
        // instantiate, configure and return bean ...
    }
}
~~~

@Configuration类由AnnotationConfigApplicationContext或 其支持Web的变体AnnotationConfigWebApplicationContext进行引导。

~~~java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.register(AppConfig.class);
ctx.refresh();
MyBean myBean = ctx.getBean(MyBean.class);
// use myBean ...
~~~

### @Import

- 第一种使用方式：在配置类里使用导入其他类配置为Bean

```kotlin
// Test 类   这里不需要任何注解(@Component、@Service)这些都不需要
public class Test {}


// MyConfig 类
@Configuration
@Import({Test.class})
public class AppConfig {}
```

- 第二种使用方式：在配置类中导入其它配置类

~~~java
// AppConfigAux 类
@Configuration
public class AppConfigAux { // 假设这里内部由很多使用了@Bean注解的方法 }

// AppConfig 类
@Configuration
@Import({AppConfigAux.class})
public class AppConfig {}
~~~



## 2 Spring Boot 中的功能

### 2.1 Spring Boot 多模块

实战[https://segmentfault.com/a/1190000011367492]

### 2.2 Spring Boot Logging 

支持 Logback, Log4J2 , java util logging，默认是Logback，导入任何的starter都会引用Logging，默认输出到console。

**logging.level.\* :** *可以设置为包前缀，分别控制不同包下面日志输出级别。

~~~xml
logging.level.root= WARN 
logging.level.org.springframework.security= DEBUG
logging.level.org.springframework.web= ERROR
logging.level.org.hibernate= DEBUG
logging.level.org.apache.commons.dbcp2= DEBUG 
~~~

**logging.file :**配置日志输出的文件名，也可以配置文件名的绝对路径。
**logging.path :**配置日志的路径。如果没有配置**logging.file**属性或者**logging.file**配置为目录，Spring Boot 将默认使用spring.log作为文件名（默认在根路径下）。
**logging.pattern.console :**定义console中logging的样式。
**logging.pattern.file :**定义文件中日志的样式。
**logging.pattern.level :**定义渲染不同级别日志的格式。默认是%5p.
**logging.exception-conversion-word :**.定义当日志发生异常时的转换字
**PID :**定义当前进程的ID

- 创建一个SLF4J的例子，首先获得org.slf4j.Logger的实例。

  ~~~java
  package com.concretepage;
  import org.slf4j.Logger; //这里引用库的区别
  import org.slf4j.LoggerFactory;
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  @SpringBootApplication
  public class MyApplication {
      private static final Logger logger = LoggerFactory.getLogger(MyApplication.class);  
      public static void main(String[] args) {
          SpringApplication.run(MyApplication.class, args);
          logger.debug("--Application Started--");
          }       
  }  
  ~~~

- 在application.properties配置包com.concretepage日志记录级别

  ~~~java
  logging.level.root= WARN
  logging.level.org.springframework.web= ERROR
  logging.level.com.concretepage= DEBUG
  ~~~

- 默认的Logback，有两种使用方式：application.properties或者**src\main\resources\logback-spring.xml**

  ~~~xml
  <configuration>
      <include resource="org/springframework/boot/logging/logback/base.xml"/>
      <logger name="org.springframework.web" level="ERROR"/>
      <logger name="com.concretepage" level="DEBUG"/>
  </configuration>  
  ~~~

- 使用**Log4J2**要先去除Logback的依赖，并且引入spring-boot-starter-log4j2的依赖。类似的我们可以通过xml和application.properties配置

  ~~~xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <exclusions>
          <exclusion>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-logging</artifactId>
          </exclusion>
      </exclusions>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-log4j2</artifactId>
  </dependency>
  ~~~

### 2.3 Spring Boot 定时任务

1. 主程序入口 @EnableScheduling 开启定时任务

2. 定时方法上 @Scheduled 设置定时

   ~~~java
   @Component //定时任务类使用@Component注解
   public class ScheduledTask {
   
       @Scheduled(fixedRate = 3000) //定时方法使用@Scheduled注解
       public void scheduledTask() {
           System.out.println("任务执行时间：" + LocalDateTime.now());
       }
   }
   ~~~

3. @Scheduled配置规则
   - cron属性：按cron规则执行
     - [cron配置](https://blog.csdn.net/m0_37179470/article/details/81271607)
   - fixedRate 属性：以固定时间执行
   - fixedDelay 属性：上次执行完毕后延迟再执行
   - initialDelay 属性：第一次延时多久执行执行，需要配合fixedRate和fixedDelay使用

### 2.4 Spring Boot 取properties的方式

1. application.properties

~~~properties
spring.profiles.active=prod
url.lm=editMessage
url.orgCode=100120171116031838
url.ybd=http://www.test.com/sales/
url.PostUrl=/LmCpa/apply/applyInfo  
~~~

2. java 注入值

~~~java
@Component  
public class ManyProperties {  
   @Value("${url.lm}")  
   private String lmPage;  
   @Value("${url.ybd}")  
   private String sendYbdUrl;  
   @Value("${url.orgCode}")  
   private String orgCode;  
   @Value("${url.PostUrl}")  
   private String PostUrl;  
   // 省列getter setter 方法  
}  
~~~

