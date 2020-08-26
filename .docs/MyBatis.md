[Toc]



# 1 MyBatis 映射器

## 1.1 概述

​	映射器是MyBatis中最核心的组件之一，在MyBatis 3之前，只支持xml映射器，即：所有的SQL语句都必须在xml文件中配置。而从MyBatis 3开始，还支持接口映射器，这种映射器方式允许以Java代码的方式注解定义SQL语句。

![](imgs\02-MyBatisMapper.PNG)

## 1.2 XML映射器

​	xml映射器是MyBatis原生支持的方式，最早学习MyBatis就是这种方法。

### 1.2.1 定义xml映射器

​	xml映射器支持将SQL语句编写在xml格式的文件中。在下面xml中mapper的namespace相当于Group的概念，以前使用是将namespace对应于一个model，然后<mapper>里面定义这个model的所需操作的SQL语句

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

### 1.2.2 配置xml映射器

​	配置xml映射器相当于是MyBatis框架知道我们自己定义的xml映射器在哪里。因为MyBatis的运行流程是先要个SqlSessionFactory，再通过SqlSessionFactory获得SqlSession，SqlSession调用xml映射器里面定义的Sql语句，所以MyBatis必须要知道xml映射器在哪里。

​	配置xml映射器（注册xml映射器）分独立使用MyBatis和Spring 集成两种场景

#### 1.2.2.1 独立使用MyBatis

​	独立使用时注册xml映射器只能在MyBatis配置文件中（如：mybatis-config.xml）通过mapper节点实现。此时的mybatis-config.xml 可以配置MyBatis的策略、SqlSessionFactory等等。后面使用xml映射器可以看出这一点。这种方式SqlSessionFactory需要传入mybatis-config.xml new 出来。

~~~xml
<configuration>
    <mappers>
        <!-- 注册xml映射器: 2种方式 -->
        <!-- 方式一: 使用相对于类路径的资源引用 -->
        <mapper resource="org/chench/test/mybatis/mapper/xml/TestMapper.xml"/>

        <!-- 方式二: 使用完全限定资源定位符（URL） -->
        <!--<mapper url="file:///var/config/TestMapper.xml" />-->
    </mappers>
</configuration>
~~~

#### 1.2.2.2 在Spring中集成MyBatis

​	集成Spring后，无需自己传入mybatis-config.xml new SqlSessionFactory，因为集成Spring后SqlSessionFactory需要配置Spring的配置文件中，所以也就不能跟独立使用MyBatis一样传入配置文件（配置文件中写着mapper的位置）new 出来。这个时候必须在Spring 注册SqlSessionFactory的Bean时指定xml 映射器。有下面两种方法。

1. 将xml映射器注册放在MyBatis配置文件中（如：mybatis-config.xml），但是此时必须在SqlSessionFactoryBean中通过属性configLocation指定MyBatis配置文件的位置。

~~~xml
<!-- 配置sqlSessionFactory -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	<property name="dataSource" ref="dataSource"/>
	<!-- 指定MyBatis配置文件（只支持类路径，典型的值为"WEB-INF/mybatis-configuration.xml"） -->
	<property name="configLocation" value="mybatis-config.xml"/>
</bean>
~~~

2. 在SqlSessionFactoryBean中通过属性mapperLocations进行注册xml映射器。

~~~xml
<!-- 配置sqlSessionFactory -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	<property name="dataSource" ref="dataSource"/>
	<!-- 注册xml映射器 -->
	<property name="mapperLocations" value="classpath*:org/chench/test/mybatis/mapper/xml/**/*.xml"/>
</bean>
~~~

### 1.2.3 使用xml映射器

​	分成两种情况，独立使用MyBatis和Spring 集成MyBatis。

1. 独立使用MyBatis

   独立使用时，xml映射器只能使用SqlSession调用。

~~~java
// 从类路径下的xml配置中构建SqlSessionFactory
String resource = "mybatis-config.xml";
InputStream is = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
// 从sqlSessionFactory中获取sqlSession
// SqlSession的作用域最好是请求或方法域，且在使用完毕之后及时释放资源，而且一定要确保资源得到释放
SqlSession sqlSession =	sqlSessionFactory.openSession();
// 从xml映射配置中查询
Test test =	sqlSession.selectOne("org.chench.test.mybatis.mapper.selectOneTest", 1);
sqlSession.close();
~~~

2. 集成Spring

​    集成Spring时，SqlSessionFactory需要给Spring去管理，SqlSession也需要给Spring去管理，所以可以采用依赖注入的方式注入SqlSession，继而使用xml 映射器。

- **使用SqlSession 调用xml映射器**，这个方式与原生方式差不多，但是SqlSession获取方法不一样。

~~~xml
<!-- 在Spring框架中注入SqlSession实例-->
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
	<constructor-arg index="0" ref="sqlSessionFactory"/>
</bean>
~~~

~~~java
// 启动spring容器
ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
// 直接使用spring提供的sqlSession
SqlSession sqlSession = (SqlSession) context.getBean("sqlSession");
Test test =	sqlSession.selectOne("org.chench.test.mybatis.mapper.selectOneTest", 1);
~~~

- **使用接口调用xml映射器**

​    当在Spring框架中集成MyBatis时，对于xml映射器的使用除了可以通过SqlSession实例进行调用，还可以直接通过接口进行调用。

​    注意：此时在定义Java接口和注册xml映射器时需要遵循一定的约定。首先，定义首先，定义的Java接口必须在`org.mybatis.spring.mapper.MapperScannerConfigurer`的属性basePackage指定的包或者子包下。因为集成Spring 所以这个类也必须在Spring配置文件中配置。

​    配置`org.mybatis.spring.mapper.MapperScannerConfigurer`后，basePackage指定下的接口，无需显式注册为Bean（@Repositity、@Component）即可实现依赖注入。

~~~xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <--配置这个类，并且指定basePackage,对应的接口也要定义在下面包里 -->
	<property name="basePackage" value="org.chench.test.mybatis.mapper.impl"/>
</bean>
~~~

​    接口如下所示

~~~java
// Java接口所在包位置
package org.chench.test.mybatis.mapper.impl;
public interface DemoMapper {
    public Demo selectOne(long id);
}
~~~

​    这种方式调用xml映射器，xml映射器的namespace和操作标签的id有限制。namespace必须定义为接口完整类路径名称，id必须和方法一致。

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 将xml映射器的namespace属性设置为完整的接口类名称 -->
<mapper namespace="org.chench.test.mybatis.mapper.impl.DemoMapper">
	<!-- 将操作语句元素的id属性设置为接口方法名称 -->
	<select id="selectOne" resultType="org.chench.test.mybatis.model.Demo">
        SELECT * FROM demo WHERE id=#{id}
    </select>
</mapper>
~~~

​    当完成上述配置后，就可以使用接口调用xml映射器。因为上面我们把接口加上了@Repository注解，所以接口会被注册为Bean，使用时我们就可以直接依赖注入接口，然后调用方法。

~~~java
// 启动spring容器
ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
// 使用接口调用xml映射器，或者直接依赖注入
DemoMapper demoMapper = context.getBean(DemoMapper.class);
Demo demo = demoMapper.selectOne(1);
~~~



## 1.3 接口映射器

​    接口映射器是从MyBatis 3才开始支持的，其实就是**支持在Java接口方法上通过注解方式编写SQL语句，而不再需要xml文件格式的配置**。但请注意：使用注解编写SQL语句这种方式在某些场景下存在一定的限制，特别是处理复杂SQL的时候。虽然其有一定的简洁性，但同时也带来了局限性。通常都是将xml映射器和接口映射器联合使用。

### 1.3.1 定义接口映射器

- 通过注解在Java接口的方法上编写SQL语句，如下所示。

~~~java
// 定义接口映射器
public interface TestMapper {
	// 通过MyBatis的注解在Java接口方法上编写SQL语句
	@Select("select * from test where id = #{id}")
	Test selectOneTest(long id);
}
~~~

### 1.3.2 配置接口映射器

​    配置接口映射器分独立使用MyBatis和集成Spring框架这两种不同的场景。

#### 1.3.2.1 独立使用MyBatis

​    在独立使用MyBatis时，接口映射器只能在MyBatis的配置文件中（如：mybatis-config.xml）通过mapper节点指定，如：

~~~xml
<mappers>
	<!-- 注册接口映射器: 2种方式 -->
	<!-- 方式一: 明确注册每一个接口 -->
	<!--
	<mapper class="org.chench.test.mybatis.mapper.impl.TestMapper" />
	<mapper class="org.chench.test.mybatis.mapper.impl.StudentMapper" />
	-->
	<!-- 方式二: 指定映射器接口所在Java包名称,则该包下的所有映射器接口都会被注册 -->
	<package name="org.chench.test.mybatis.mapper.impl"/>
</mappers>
~~~

#### 1.3.2.2 集成Spring

​	接口映射器只能通过`org.mybatis.spring.mapper.MapperScannerConfigurer`注册，指定其basePackage属性值为需要注册映射器接口所在的包，可以在该包及其子包下定义接口映射器。

~~~xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	<!-- 定义接口映射器所在的Java包 -->
	<property name="basePackage" value="org.chench.test.mybatis.mapper.impl"/>
</bean>
~~~

### 1.3.3 使用接口映射器

- 独立使用MyBatis

~~~java
// 从类路径下的xml配置中构建SqlSessionFactory
String resource = "mybatis-config.xml";
InputStream is = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
// 从sqlSessionFactory中获取sqlSession
// SqlSession的作用域最好是请求或方法域，且在使用完毕之后及时释放资源，而且一定要确保资源得到释放
SqlSession sqlSession =	sqlSessionFactory.openSession();
// 从映射器接口中查询
Test test = sqlSession.getMapper(TestMapper.class).selectOneTest(1);
sqlSession.close();
~~~

- 集成Spring：既可以类似上面先获得到容器中的Bean，再类似于上面调用。也可以把接口映射器注册到容器，然后通过依赖注入的对象调用。

~~~java
// 启动spring容器
ApplicationContext context = new ClassPathXmlApplicationContext("mybatis-spring.xml");
// 从Spring容器中获取接口映射器Bean，这没有使用依赖注入而已。
TestMapper testMapper =	context.getBean(TestMapper.class);
Test test =	testMapper.selectOneTest(1);
~~~