#spring data jpa的入门

## 主要依赖

`spring-boot-starter-data-jpa`和`相关数据库的驱动`

## 创建jpa的步骤

### 1.配置application.yml/applicationContext.xml

### 2.配置实体类、dao接口(需要继承JpaRepository等)

关于实体类的用途:
- 简单实体类：`user`
- 一多对：`customer`:`order`
- 多对多：`player`:`role`

设置字段唯一: https://blog.csdn.net/qq_38705025/article/details/86636818

### 3.1 进行简单的crud

基本的查询、保存、删除  
测试案例:`DemoApplicationTest1`

### 3.2 复杂查询

#### jpql的查询和更新 & sql查询

测试案例:`DemoApplicationTest2`

##### specification的查询方法

测试案例:`DemoApplicationTest3`


### 3.3 多表操作(通过注解配置)

测试案例:`DemoApplicationTest4`

##### 关于外键

JPA中的多表操作绕不开外键(不使用外键会非常奇怪)  
外键主要的作用是交给数据库去维护多表之间的关系  
如果不采用外键那么则需要开发人员来维护  

##### 一对多,客户和订单

一般性由多来维护外键(就好比作业是学生交上去而不是老师来强收)  
如果选择多来维护,那么在更新(save)前需要让多来建立和一的关系  
例如:` order.setCustomer(customer);`  
反之也是一样,但尽量不要两者都来建立,浪费性能,且有未知隐患  

##### 级联操作cascade

#####多对多,玩家和角色,player role

### 其他补充

- 使用autowired的类,没有注解component,导致诸如的dao为null---以前从来不知道自动注入的bean一定需要使用component  

- 字段取名时,依然需要遵循驼峰,否则会导致命名规则查询失效.另外hibernate会自动转换为a_b_c...  

- 关于mariaDB,5.5下未发现dialect报错,但10.x存在,因此需要在yml中设置`(jpa:)database-platform: org.hibernate.dialect.MariaDBDialect`  

- 关于jpa中的date类型
  ```java
  //通过Temporal可以设置数据库的日期类型
  
  //设置为日期类型,2020-05-05
  @Temporal(TemporalType.DATE)
  private Date date;
  
  //设置为时间类型,17:23:00
  @Temporal(TemporalType.TIME)
  private Date time;
  ```
  
- 也可以直接设置为sql类型,他们之间的映射关系为
  - DATE ：等于java.sql.Date
  - TIME ：等于java.sql.Time
  - TIMESTAMP ：等于java.sql.Timestamp 
  
---

### 追加了querydsl的用法

#### 关于querydsl是啥

在jpa之上的查询框架,通过兼容jpa的api(findAll)来执行查询  
好处就是比jpa更加直观,例如下面需要查找`username like John`  

```java
QUser qUser = QUser.user;
BooleanExpression john = qUser.username.like("John");
Iterable<User> users = userDao.findAll(john);
users.forEach(user -> System.out.println(user));
```

我觉得写criteria真的是要了命了,非常不习惯

具体的代码演示在测试类`JPAQuerydslIntegrationTest`中

#### 依赖

```xml
<dependencies>
  <dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-apt</artifactId>
  </dependency>
  
  <dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
  </dependency>
</dependencies>

```

还有Q类生成插件

```xml
<!-- https://github.com/querydsl/apt-maven-plugin -->
<plugin>
<groupId>com.mysema.maven</groupId>
<artifactId>apt-maven-plugin</artifactId>
<version>1.1.3</version>
<executions>
  <execution>
    <goals>
      <goal>process</goal>
    </goals>
    <configuration>
      <!--
          指定输出路径,官方写法推荐生成在target目录下
          因为问题比较多,不建议修改,更不建议手动移动到开发路径下
          如果出现找不到类或者重复的类的错误,建议mvn:clean mvn:compile
      -->
      <outputDirectory>target/generated-sources/java</outputDirectory>
      <!-- 类生成器,不需要修改 -->
      <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
    </configuration>
  </execution>
</executions>
</plugin>
```

### 一些很好的参考资料

这次的案例基本都是知乎学来的  
知乎: https://zhuanlan.zhihu.com/p/24778422

v2列举了spring官网的方法,中文更贴切  
v2ex: https://www.v2ex.com/t/350737#reply9
