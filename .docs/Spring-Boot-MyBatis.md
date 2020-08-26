[Toc]



# 1 Mybatis - Spring Boot

## 1 前言

​    在Spring Boot中使用MyBatis本质就是在Spring框架中集成MyBatis，并没有其他任何高级的东西。只不过在Spring Boot中使用时因为插件封装的关系使得相关的配置可以更简洁一些。

## 2 准备工作

### 2.1 添加数据库驱动<连接池>依赖

~~~xml
<!-- 添加MySQL数据库驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<!-- 添加数据库连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>${version.druid}</version>
</dependency>
~~~

### 2.2 配置数据源<连接池>

在Spring Boot项目中，如果在application.properties中配置了driver-class-name、url、username、password时会自动配置一个数据源，相当于在容器中注册了一个数据源。

~~~yaml
spring: 
    datasource: 
        name: testDatasource
        driver-class-name: org.h2.Driver
        url: jdbc:h2:~/community
        username: sa
        password: 123
        # 使用druid连接池
        type: com.alibaba.druid.pool.DruidDataSource
        filters: stat
        maxActive: 20
        initialSize: 1
        maxWait: 60000
        minIdle: 1
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: select 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxOpenPreparedStatements: 20
~~~

## 3 原生集成Mybatis 集成到Spring Boot

​    此集成方式本质上是Spring集成Mybatis，在非Spring Boot框架下也能用。下面添加的依赖中就不是Spring Boot Mybatis依赖。

### 3.1 添加依赖

~~~xml
<!-- mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>${version.mybatis}</version>
</dependency>
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper</artifactId>
    <version>${version.mybatis.mapper}</version>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>${version.pagehelper}</version>
</dependency>

<!-- mybatis-spring -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>${version.mybatis.spring}</version>
</dependency>

<!-- spring事务 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
</dependency>

<!-- spring jdbc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
</dependency>
~~~

### 3.2 注册MyBatis核心组件

#### 3.2.1 在Spring中注册MyBatis核心组件（注册xml映射器）

​	SqlSessionFactory、SqlSession、Spring事务管理器，下面代码是Java形式配置，还可以xml形式配置这些组件。放在Spring Bean配置文件中。处理注册上面核心组件外，还需要在下面的配置类中注册xml映射器。

~~~java
@Configuration
@EnableTransactionManagement
public class MyBatisSpringConfig implements TransactionManagementConfigurer {
    @Autowired
    private DataSource dataSource;
    
    // 在Spring中注册SqlSessionFactory，在这里可以设置一下参数：
    // 1.设置分页参数
    // 2.配置MyBatis运行时参数
    // 3.注册xml映射器
    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 设置映射POJO对象包名
        // sqlSessionFactoryBean.setTypeAliasesPackage("org.chench.test.springboot.model");
        
        // 分页插件
        /*PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);*/
        //添加插件
        //sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
        
        // 配置mybatis运行时参数
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 自动将数据库中的下划线转换为驼峰格式
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultFetchSize(100);
        configuration.setDefaultStatementTimeout(30);
        
        sqlSessionFactoryBean.setConfiguration(configuration);
        
        // 在构建SqlSessionFactory时注册xml映射器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 注入sqlSession对象
     * @param sqlSessionFactory
     * @return
     */
    @Bean(value = "sqlSession")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // Spring事务管理器
    @Bean(value = "transactionManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
~~~

#### 3.2.2 注册MyBatis接口映射器

​    假设箱项目除了使用xml映射器，还是用了接口映射器。这个时候就必须加入下面的配置类。上面配置文件已经注册了xml映射器。**无论是使用接口调用xml映射器还是使用接口映射器都必须配置下面的类**。**

~~~java
@Configuration
@AutoConfigureAfter(MyBatisSpringConfig.class) //注意，由于MapperScannerConfigurer执行的比较早，所以必须有该注解
public class MyBatisMapperScannerConfig {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 设置sqlSessionFactory名
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        // 设置接口映射器基础包名
        mapperScannerConfigurer.setBasePackage("org.chench.test.springboot.mapper");
        Properties properties = new Properties();
        //properties.setProperty("mappers", "org.chench.test.springboot.mapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
~~~

## 3.4 定义并使用映射器（接口/xml）

#### 3.4.1 接口映射器

- 定义接口映射器

~~~java
@Repository
public interface AccountMapper {
    @Select("select * from account where id = #{id}")
    public Account getAccountById(@Param("id") long id);
}
~~~

- 使用接口映射器

~~~java
@Service
public class AccountService {
    // 直接注入接口映射器Bean进行使用
    @Autowired
    private AccountMapper accountMapper;
    
    public Account getAccountById(long id) {
        return accountMapper.getAccountById(id);
    }
}
~~~

#### 3.4.2 xml映射器

- 定义xml映射器

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.chench.test.mybatis.mapper">
	<select id="selectOneTest" resultType="org.chench.test.mybatis.model.Test">
		select * from test where id = #{id}
	</select>
</mapper>
~~~

- 使用xml映射器

~~~java
// 因为这里集成了Spring Boot，并没有使用Spring Boot MyBatis。所以如果注入SqlSession之
// 后，通过SqlSeesion.<Mapper id>调用映射器。
~~~

## 4 通过MyBatis-Spring-Boot-Starer集成

### 4.1 添加MyBatis-Spring-Boot-Starter依赖

​    默认情况下MyBatis-Spring-Boot-Starter会进行如下配置：

- 自动检查Spring Boot的数据源配置并构建DataSource对象（**Spring Boot自带数据源，application.properties需要配置数据源信息<username，password，url，Driver>**）
- 通过SqlSessionFactoryBean使用数据源构建并注册SqlSessionFactory对象
- 从SqlSessionFactory中创建并注册一个SqlSessionTemplate实例，其实就是构建一个SqlSession对象
- 自动扫描接口映射器（就是添加@Mapper的类），并将这些映射器与SqlSessionTemplate实例进行关联，同时将它们注册到Spring容器中。（可以依赖注入调用方法）

~~~xml
<!-- 在Spring Boot中集成MyBatis -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
~~~

### 4.2 定义接口映射器（xml映射器）

- 插件会自动扫描使用了@Mapper的接口映射器。

~~~java
@Mapper
public interface AccMapper {
    @Select("select * from account where id = #{id}")
    Account getAccount(@Param("id") long id);
}
~~~

### 4.3 使用接口映射器（xml映射器）

~~~java
@RestController
@RequestMapping("/acc")
public class AccController {
    // 直接通过自动注入的方式使用接口映射器
    @Autowired
    AccMapper accMapper;

    @GetMapping("/{id}")
    @ResponseBody
    public Object acc(@PathVariable("id") long id) {
        return accMapper.getAccount(id);
    }
}
~~~

### 4.4 高级定制

- 定制MyBatis运行时参数

​    在Spring Boot中对MyBatis进行定制是指在Spring Boot的配置文件中（如application.yaml）对MyBatis运行参数进行自定义配置（使用mybatis作为配置参数前缀）。在这里定义了xml映射器位置`mybatis.mapper-locations`，在集成项目里面，既可以使用接口映射器（像上面一样）。

~~~yaml
mybatis:
    check-config-location: true                             # 是否检测MyBatis运行参数配置文件
    config-location: classpath:/mybatis-config.xml          # 指定MyBatis运行参数配置文件位置
    mapper-locations: classpath:/mapper/**/*.xml            # 注册XML映射器
    type-aliases-package: test.springboot.model             # 配置Java类型包名
    type-handlers-package: test.springboot.handlers         # 配置类型处理器包名
    executor-type: SIMPLE                                   # 指定执行器类型
    configuration:
        default-fetch-size: 20
        default-statement-timeout: 30
~~~

# 2 MyBatis-Generator 插件
## 2.1 配置MyBatis Generator

​	[详细文章链接]（https://juejin.im/post/6844903982582743048#heading-20）

​    通过上面的分析，可以推断出使用MyBatis-Generator会生成model、mapper.xml、mapper。然后在Spring Boot启动类中添加`@MapperScan("life.community.mapper")`相当于上面Spring集成原生MyBatis中配置`MapperScannerConfigurer.setBasePackage`类。

## 2.2 ExampleClass 使用方法

​	example class是用来动态构建where子句，example class中包含一个内部静态类Criteria（条件类），这个Criteria中包含一系列可能用到的where子句，example class 可以包含一系列Criteria 对象。

​	Criteria对象可以通过`createCriteria`方法或者是`or`方法创建，第一次创建Criteria时会被自动加到Criteria list中，`or`方法会把所有`Criteria`类加到list中。

​    配置好where子句就可以textMapper.selectByExample(exampleClass)查询。

### 2.2.1 简单查询

~~~java
TestTableExample example = new TestTableExample();
example.createCriteria().andField1EqualTo(5)
// 或者
TestTableExample example = new TestTableExample();
example.or().andField1EqualTo(5);

// 上面创建了where field1 = 5
~~~

### 2.2.2 复杂查询

~~~java
TestTableExample example = new TestTableExample();

example.or()
    .andField1EqualTo(5)
    .andField2IsNull();

example.or()
    .andField3NotEqualTo(9)
    .andField4IsNotNull();

List<Integer> field5Values = new ArrayList<Integer>();
field5Values.add(8);
field5Values.add(11);
field5Values.add(14);
field5Values.add(22);

example.or()
    .andField5In(field5Values);

example.or()
    .andField6Between(3, 7);
~~~

上面的例子创建了下面的where子句

~~~sql
where (field1 = 5 and field2 is null)
    or (field3 <> 9 and field4 is not null)
    or (field5 in (8, 11, 14, 22))
    or (field6 between 3 and 7)
~~~

example.setDistinct(true)即可实现去重。

## F .拓展

### @Param()

~~~java
@Select("select entity from table where userId = ${userId} ")
public int selectEntity(@Param("userId") int userId);
~~~

当使用了使用@Param注解来声明参数时，如果使用 #{} 或 ${} 的方式都可以。当不使用@Param注解来声明参数时，必须使用使用 #{}方式。如果使用 ${} 的方式，会报错，而#{}拿到值之后，拼装sql，会自动对值添加引号。${}则把拿到的值直接拼装进sql，如果需要加单引号，必须手动添加，一般用于动态传入表名或字段名使用，#{}传参能防止sql注入。

### MyBatis Dynamic SQL

[ExampleClassNote](https://mybatis.org/generator/generatedobjects/exampleClassUsage.html)

[MyBatisDynamicSQL](https://mybatis.org/mybatis-dynamic-sql/docs/introduction.html)

