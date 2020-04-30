# April 29
IDEA快捷键
 - alt + enter 导入类
 - ctrl + Y 删除一行

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

OKHttp

用于我的网站与github交互。