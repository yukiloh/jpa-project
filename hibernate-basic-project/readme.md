# hibernate 笔记
hibernate是orm框架，实现了JPA标准
orm：对象关系映射框架
所以hibernate自始至终都是围绕着对象而不是记录


#### 配置核心文件: hibernate.cfg.xml


#### 配置实体类: User.java User.hbm.xml


#### hibernate的基础入门,基本api

##### 增删改查的方法

##### 通过hql语句查询对象

##### 通过criteria查询对象
可以参考:
https://www.cnblogs.com/g-smile/p/9177841.html

##### 通过sql语句进行查询结果

##### 关于save和persist的细节和区别
如果对象在session中没有缓存,save()时会执行insert语句,且会忽略原设定的oid(和主键策略有关)
即new一个对象,并设定id,但save后设定的id会变为sql中的主键id
persist笔记在contextLoad6中


#### 实体类
##### 实体类的编写规则
实体类编写的一些策略
1.需要提供无参构造(默认提供);hibernate获取封装数据时会调用无参构造方法
2.需要提供主键字段
3.所有属性需要提供get set方法
4.推荐属性使用包装类
5.不可使用final修饰(将无法生成代理)

PO:persist object 持久化对象

##### 主键生成策略

##### 自然主键和代理主键
如果一个主键符合:不为空 不重复 不改变,则可以作为自然主键,否则需要另外寻找一个属性作为代理主键
例如用户名,如用户名不为空,不重复,不会改变,则可以作为自然主键,如果不满足,则需要另外依赖一个id作为主键,即代理主键

##### hibernate的类型与sql的类型之间,转换时的对应关系
关于hibernate与数据库中字段的对应类型
如果在 entity.hbm.xml 中的 property/iid 没有定义type,则hibernate会根据entity中的属性来定义类型
例如java中的Integer会对应sql中的integer

##### 主键生成策略
笔记在User.hbm.xml中
hibernate通过oid来建立与数据库表中记录的对应关系,对象的oid与主键对应,一般让hibernate去赋值oid而不是开发者

##### 动态插入,动态更新,type的使用
笔记在User.hbm.xml中的;动态内容在class标签内;type在property中

##### 实体类的状态
实体类的状态:
1.瞬时状态:session中无缓存,数据库无记录,oid没有值
2.持久状态:session中有缓存,数据库有记录,oid有值
3.脱管/游离状态:session中无缓存,数据库有记录,oid有值

当对象new后为瞬时状态
save(saveOrUpdate)后改为持久状态,此时get/load都会返回持久状态的对象;如果session有记录则不会再从sql中查询(调用select语句)
当session.close()/session.clear()/session.evict(obj)后,session后改为脱管状态,此时get/load都会返回脱管状态的对象
关于clear(),会将缓冲区内的全部对象清除，但不包括操作中的对象
关于evict(obj),先找到id再进行清除;执行evict之前会执行flush();如果在提交事务前进行evict会报错(这也是与clear不同点)

================================================

#### 一级缓存和快照
一级缓存即session
hibernate获取1条数据后,除了在一级缓存,也会另外在快照中进行备份
当事务提交时会自动将一级缓存与快照中的数据进行比对
若不相同则判断为已发生数据修改,这也即为什么即使setUser()中,即使不加update也会更新数据

可以通过session.flush()进行手动一级缓存刷新

##### hql和criteria必定会对session进行缓存,但sql不会一定
笔记在contextLoad5中

===============================================

#### hibernate的多表关联关系映射

#### 一对多customer和order
##### 入门
笔记在Customer.hbm.xml和Order.hbm.xml中,记得配置h.c.xml中的实体类映射标签mapping
具体实现在TestContext2中的contextLoad1
感觉很神奇

##### 一对多关系中的关系维护(inverse关键词)
笔记在Customer.hbm.xml中的set标签

##### 一对多的查询和删除
笔记在TestContext2中的contextLoad2

##### 级联删除
笔记在TestContext2中的contextLoad2

=========

#### 多对多
实体类student和course
笔记在Student.hbm.xml中;需要先创建2个实体类Student和Course;在h.c.xml中添加mapping标签!

##### 多对多数据保存
笔记在TestContext2中的contextLoad3

##### 类级别的加载和加载策略(lazy=false)
笔记在TestContext2中的contextLoad4

##### set标签中fetch(抓取)属性
笔记在Student.hbm.xml中

=========

#### 多对一的查询和策略
笔记在TestContext2中的contextLoad5


#### 其他补充:批量加载 batch-size batch:批量
笔记在Customer.hbm.xml的set标签上,用的较少

===============================================

#### 各个检索策略的优缺点
概括
                优点                                缺点
即时加载:   可以直接导航至关联对象,减少select语句      费内存
使用场景:   需要立刻访问的数据;使用二级缓存时

延迟加载:   避免多余的select语句,节省内存            代理类实例不一定获取到已初始化的对象
使用场景:    一对多或多对多;不需要立刻使用或不会访问到的对象
            
表连接:    减少select语句,对程序透明                费内存,复杂语句会影响检索性能
使用场景:    多对一,或一对一的关联;需要立即访问的对象;数据库有良好的表连接性能

===============================================

#### HQL
使用hql语句进行的查询
hql的基本查询
排序 聚合 分组
交叉连接/笛卡尔积 迫切/非迫切 内连接 外连接
命名查询
笔记在TestContext3中

===============================================

#### QBC
使用criteria进行条件查询,面向对象的查询方式
基础查询  排序  条件查询  分页(与hql一致)  离线查询
笔记在TestContext4中

===============================================

#### 数据库连接池的配置
笔记在h.c.xml中的hibernate.connection.provider_class处

#### 事务隔离等级
hibernate.connection.isolation


#### 悲观锁
-读锁/共享锁(少用),所有程序都有权获得锁,即使A获取锁后也会被B抢走
 语法:select * from table_name lock in share mode;

-写锁/排他锁(多用),当A程序上锁后其他程序无权进行读取或者上锁;此种上锁可以分为锁表(不推荐)和锁记录(常用)
 语法:select * from table_name for update;(直接锁表了,锁记录时需要在后面添加where再for update)
 
hibernate的中写锁,笔记在TestContext5中的contextLoad1

#### 乐观锁
(在Customer中示范),JavaBean中添加新字段Integer version
同时在实体类配置文件中添加新标签version
具体实现在TestContext5中的contextLoad2和Customer.hbm.xml中

#### log4j的用法
参考:https://blog.csdn.net/u013870094/article/details/79518028
具体略   

===============================================

#### 一对一的配置
需要创建company 和 address 的实体类
##### 通过外键配置一对一
示范在TestContext5中的contextLoad1中
##### 通过主键来实现
address中,地址的主键和公司的id栏明显重复,不需要额外配置字段,因此将主键设置为外键
具体配置在CompanyAddress2.hbm.xml中
注意,已注释address1的mapping,hibernate查询时只会查询address2的表

===============================================

#### 二级缓存
一级数据session级别
二级属于sessionFactory级别,注意,整个应用只会有一个factory
二级缓存适用于:**极少被修改的数据,也不是很重要的数据,并允许出现并发问题**,相反除此以外的数据并不适用于二级缓存

二级缓存可以由**内置缓存**和**外置缓存**来实现
内置缓存:   使用map,对外**只读**
外置缓存:   默认不开启;实现由hibernate提供的接口

二级缓存的并发策略:(类似于事务的四个等级)
事务型:        让缓存支持事务,发生异常时也可回滚;性能低下
读写型:        更新缓存时会锁定数据;适用于大量读,少量写的场景
非严格读写型:   不锁定缓存中的数据,会发生脏读;适合极少被修改的数据
只读型:        只适用于从不写入的数据;性能最高,适用于集群

二级缓存分为四类:
类缓存区
集合缓存区
查询缓存区
时间戳缓存区

本示范使用EHCache,支持读写,非严格读写,只读(只有JBOSS Cache支持事务型);支持集群

##### EHCache的整合
基础配置:   https://www.jianshu.com/p/44f09565f2b6
缓存模式处详细的介绍: https://blog.csdn.net/soul717/article/details/83589180
步骤:
1.maven添加依赖:hibernate-ehcache.jar slf4j-api.jar ehcache-core.jar
2.在hibernate.cfg.xml开启二级缓存
  具体笔记在h.c.xml中
3.编写配置文件ehcache.xml
示范在contextLoad1中,2为集合缓存

实际是根据id来查找,如果都没有则会从数据库中查找


##### eh的查询缓存
二级缓存不会缓存HQL的查询,需要另外开启hibernate.cache.use_query_cache
查询缓存是缓存了hql所查询到的对象(通过id区分)(并不是缓存了hql语句,多次执行hql依然会查询数据库)

##### eh的时间戳缓存
校对上次执行的语句是否进行了非select操作,如果是则跳过查询二级缓存查询,直接查询数据库
演示略

##### ehcache的配置文件讲解
具体见ehcache.xml中的注释


#### 补充:Mariadb中文字符乱码解决
>**错误：**
>[22007][1366] (conn=109) Incorrect string value: '\xE8\x9B\x8B' for column 'orderName' at row 1

```
#查看数据库状态
SHOW CREATE TABLE `hibernate_test_table`;
#修改数据库字符集为uft8（没啥用）
alter database `hibernate-for-test` character set utf8;
#修改表的字符集为utf8（有用）
ALTER TABLE hibernate_test_table CONVERT TO CHARACTER SET utf8;
```
