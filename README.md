

# IDEA 

 - alt + enter 导入类
 - ctrl + Y 删除一行
 - ctrl + alt + v 抽取一个变量
 - ctrl + shift + n 输入名字打开文件
 - alt + enter 没有方法时创建方法

## 机器环境

1. T490
   - Maven
   - Maven home dir:C:/Users/duansihong/.m2/wrapper/dists/apache-maven-3.6.3-bin/1iopthnavndlasol9gbrbg6bf2/apache-maven-3.6.3
   - User settting:C:/Users/duansihong/.m2
   - 仓库位置：C:/Users/duansihong/.m2

# Spring Boot

@SpringBootApplication添加在启动类当中相当于添加下面的注解
- @Configuration:把当前类作为bean定义源
- @EnableAutoConfiguration:
- @ComponentScan:扫描当前类下面的注解 

分页功能：跳到首页和跳到尾页可以通过

SpringBoot：拦截器功能

1. Spring Boot Logging 

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

2. Spring Boot 定时任务

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
      - fixedRate 属性：以固定时间执行
      - fixedDelay 属性：上次执行完毕后延迟再执行
      - initialDelay 属性：第一次延时多久执行执行，需要配合fixedRate和fixedDelay使用

3. 





# github登录

用户点击的登录按钮是跳到github的登录验证。并且附带了我注册OAuth信息的参数。这个就可以表明一件事：当前点击登录用户想要认证我的这个网站。github收到这个信息之后就让用户输入用户名密码。假设用户输入正确，github就会让用户的浏览器重定向注册OAuth时填写的重定向地址，并且此时携带github验证成功后code。我的网站收到了这个code就可以用这个code去向github获取用户github账号信息。

OKHttp用于我的网站与github交互。

配置变量放在application.properties

Cookies是存在自己的浏览器当中的。并且每个只属于一个网址。当我们浏览器中存在一个网站的Cookies时，当我们请求这个网站时会自动把Cookies带上。我们请求的网站就可与根据Cookies中的信息获取到登陆状态。论坛这个系统保存登录信息的方法

- 第一次登录论坛时，并且验证成功，我们就可以生成个UUID作为登录标识存入数据库，并且写回用户浏览器。
- 第二再登录的时候用户浏览器会自动带上上次写回的Cookies，我们可以取得Cookies并且与数据库中的做对比。

# H2数据库

​	能直接通过jar依赖来使用数据库。内嵌数据库。Spring Boot可以自动配置嵌入式H2， HSQL和Derby数据库。您无需提供任何连接URL。您只需要包含要使用的嵌入式数据库的构建依赖项。
存储路径为：~/community.mv.db 具体的路径是14242文件下，~代表当前用户路径。

# Maven

关于Maven的使用问题，我现在遇到的问题是在继承Flyway的时候,运行mvn flyway:migrate发现mvn命令不能用。经过研究发现，我之前直接创建项目，使用的是IDEA自带的Maven插件。
这个Maven的目录为：D:\JetBrains\IntelliJ IDEA 2020.1\plugins\maven\lib\maven3
仓库地址为：C:\Users\14242\.m2\repository
因为这Maven是自带的，并未配置进系统环境变量。所以不能用mvn命令。解决办法如下：

1. 使用IDEA自带的Maven插件运行命令
2. 把插件配置进环境变量

# Flyway

​	Flyway 是一款开源的数据库版本管理工具，它更倾向于规约优于配置的方式。Flyway 可以独立于应用实现管理并跟踪数据库变更，支持数据库版本自动升级，并且有一套默认的规约，不需要复杂的配置，Migrations 可以写成 SQL 脚本，也可以写在 Java 代码中，不仅支持 Command Line 和 Java API，还支持 Build 构建工具和 Spring Boot 等，同时在分布式环境下能够安全可靠地升级数据库，同时也支持失败恢复等。

https://juejin.im/entry/6844903802215071758

​	经过测试可知，flyway用法，当要修改表结构就在db文件下创建sql文件。然后mvn当插入数据时不记录。可能时配置文件的原因。

1. 在pom.xml 中加入下面插件

~~~xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>6.4.1</version>
    <configuration>
        <url>jdbc:h2:~/community</url>
        <user>sa</user>
        <password>123</password>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.200</version>
        </dependency>
    </dependencies>
</plugin>
~~~

2. 使用flyway

   1. 在 src/main/resources 下创建 db/migration

   2. 后面我们需要对数据库做一次修改，我们都需要先在migration中创建一个sql文件，在sql文件中添加修改的sql语句，或是插入数据的sql语句。

   3. 执行下面的命令，这会自动执行db/migration中未被执行的sql文件，以及把执行的记录存在flyway的表中去。

      ```java
      mvn flyway:migrate
      ```

# Mybatis

（第 17 节）

1. 添加jar包或者是通过Maven添加依赖，依赖代码如下

   ~~~java
   <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
       <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>2.1.3</version>
   </dependency>
   ~~~

2. 在spring中使用Mybatis需要SqlSessionFactory，这个类需要SqlSessionFactoryBean来生成。所以spring boot与Mybatis的整合框架里会自动做以下的事。

   1. 自动识别一个存在数据源（DataSource）

      - Spring boot 会内置一个数据源（引用了spring-boot-starter-jdbc或spring-boot-starter-data-jpa）还必须在配置文件中配置

        ~~~java
        spring.datasource.url=jdbc:h2:~/community
        spring.datasource.username=sa
        spring.datasource.password=123
        spring.datasource.driver-class-name=org.h2.Driver
        ~~~

   2. 通过这个数据源创建、注册SqlSessionFactoryBean继而生成SqlSessionFactory

   3. 通过SqlSessionFactory创建、注册SqlSessionTemlate

   4. 扫描我的Mapper并链接到SqlSessionTemlate，并且在Spring上下文中注册

3. 后面就可以使用了，在下面的例子里面我们假设已经在数据库中创建好了User表。

   ~~~java
   // User
   public class User{
       
   }
   ~~~

   ~~~java
   // UserMapper 需要添加
   @Mapper
   public interface UserMapper {
     // #{} 这个里面直接写java类中的字段
     @Select("SELECT * FROM user WHERE account_id = #{accountId}")
     User findByAccountId(User user);
   
   }
   ~~~

   ~~~java
   @Autowired
   private UserMapper userMapper;
     //使用userMapper访问数据库
   ~~~

   ## Mybatis-Generator 插件
   ​	详细文章链接（https://juejin.im/post/6844903982582743048）    
   
   ​	前提条件：创建好数据库以及数据表。
   
   


