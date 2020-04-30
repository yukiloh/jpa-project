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

