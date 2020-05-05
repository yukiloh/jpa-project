#spring data jpa的入门

### 主要依赖
```spring-boot-starter-data-jpa```
和
```相关数据库的驱动```

### 创建jpa的步骤
### 1.配置application.yml/applicationContext.xml

### 2.配置实体类、dao接口(需要继承JpaRepository等)
关于实体类的用途:
- 简单实体类：```user```
- 一多对：```customer```:```order```
- 多对多：```player```:```role```

设置字段唯一: https://blog.csdn.net/qq_38705025/article/details/86636818

### 3.1 进行简单的crud
基本的查询、保存、删除

测试案例:```DemoApplicationTest1```

### 3.2 复杂查询
##### jpql的查询和更新 & sql查询
测试案例:```DemoApplicationTest2```

##### specification的查询方法
测试案例:```DemoApplicationTest3```


### 3.3 多表操作(通过注解配置)
测试案例:```DemoApplicationTest4```

##### 一对多,客户和订单

##### 级联操作cascade

#####多对多,玩家和角色,player role


### 其他补充

- 使用autowired的类,没有注解component,导致诸如的dao为null---以前从来不知道自动注入的bean一定需要使用component
- 字段取名时,依然需要遵循驼峰,否则会导致命名规则查询失效.另外hibernate会自动转换为a_b_c...
- 关于mariaDB,5.5下未发现dialect报错,但10.x存在,因此需要在yml中设置```(jpa:)database-platform: org.hibernate.dialect.MariaDBDialect```

- 关于date类型
```java
//通过Temporal可以设置数据库的日期类型

//设置为日期类型,2020-05-05
@Temporal(TemporalType.DATE)
private Date date;

//设置为时间类型,17:23:00
@Temporal(TemporalType.TIME)
private Date time;
```