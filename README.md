# April 29
IDEA快捷键
 - alt + enter 导入类
 - ctrl + Y 删除一行
 - ctrl + alt + v 抽取一个变量
 - ctrl + shift + n 输入名字打开文件
 - alt + enter 没有方法时创建方法

@SpringBootApplication添加在启动类当中相当于添加下面的注解
- @Configuration:把当前类作为bean定义源
- @EnableAutoConfiguration:
- @ComponentScan:扫描当前类下面的注解 

window命令
- dir 相当于 ls

github登录的理解

用户点击的登录按钮是跳到github的登录验证。并且附带了我注册OAuth信息的参数。
这个就可以表明一件事：当前点击登录用户想要认证我的这个网站。github收到这个信息之后
就让用户输入用户名密码。假设用户输入正确，github就会让用户的浏览器重定向注册OAuth
时填写的重定向地址，并且此时携带github验证成功后code。我的网站收到了这个code就可以
用这个code去向github获取用户github账号信息。

OKHttp用于我的网站与github交互。

配置变量放在application.properties

Cookies是存在自己的浏览器当中的。并且每个只属于一个网址。当我们浏览器中存在一个
网站的Cookies时，当我们请求这个网站时会自动把Cookies带上。我们请求的网站就可与
根据Cookies中的信息获取到登陆状态什么的。

H2数据库，能直接通过jar依赖来使用数据库。内嵌数据库。Spring Boot可以
自动配置嵌入式H2， HSQL和Derby数据库。您无需提供任何连接URL。您只需要包含要使用的嵌入式数据库的构建依赖项。
存储路径为：~/community.mv.db 具体的路径是14242文件下，~代表当前用户路径。

关于Maven的使用问题，我现在遇到的问题是在继承Flyway的时候,运行mvn flyway:migrate
发现mvn命令不能用。经过研究发现，我之前直接创建项目，使用的是IDEA自带的Maven插件。
这个Maven的目录为：
D:\JetBrains\IntelliJ IDEA 2020.1\plugins\maven\lib\maven3
仓库地址为：
C:\Users\14242\.m2\repository
因为这Maven是自带的，并未配置进系统环境变量。所以不能用mvn命令。解决办法如下：
(1)使用IDEA自带的Maven插件运行命令
(2)把插件配置进环境变量

Flyway:经过测试可知，flyway用法，当要修改表结构就在db文件下创建sql文件。然后mvn
当插入数据时不记录。可能时配置文件的原因。


