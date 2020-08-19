[TOC]

# IDEA

 - alt + enter 导入类
 - ctrl + Y 删除一行
 - ctrl + alt + v 抽取一个变量
 - ctrl + shift + n 输入名字打开文件
 - alt + enter 没有方法时创建方法

# Spring Boot

@SpringBootApplication添加在启动类当中相当于添加下面的注解
- @Configuration:把当前类作为bean定义源
- @EnableAutoConfiguration:
- @ComponentScan:扫描当前类下面的注解 

分页功能：跳到首页和跳到尾页可以通过

SpringBoot：拦截器功能



window命令
- dir 相当于 ls

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
   ​    前提条件：创建好数据库以及数据表。
   ​    使用方法：
   ​        （1）写好配置文件generatorConfig.xml
   ​        （2）mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
   ​        （3）运行完上面的就可以自动生成与数据库表对应的model、以及操做这个模型的类modelExample、以及具体对应的xml（sql）

   

