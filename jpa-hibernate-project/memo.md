#JPA(hibernate)
实现方式:
程序 → JPA → hibernate → 数据库
通过JPA制定的同一的API接口来实现操作数据库
Spring Data JPA则是封装了JPA

#### 添加依赖
junit：测试
hibernate-core：hibernate核心
log4j：日志
mariadb-java-client：mariadb数据库连接驱动


#### jpa的核心配置文件
配置persistence.xml，类似于hibernate.cfg.xml


#### 配置实体类(通过注解)
User.java

#### 通过JPAUtils来创建entityManager

