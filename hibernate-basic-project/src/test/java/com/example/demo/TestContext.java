package com.example.demo;


import com.example.demo.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

public class TestContext {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        //获取核心配置文件
        //通过.configure()来默认加载src/hibernate.cfg.xml,configure()内可以修改具体的路径
        Configuration configure = new Configuration().configure();
        //如果不加则读取prop


        //也可以在此配置实体类的映射文件;通常写在h.c.xml中;不允许重复配置映射文件
        //configure.addResource("com/example/demo/domain/User.hbm.xml");       //直接写路径,路径必须为/
        //configure.addClass(User.class);     //添加实体类,底层会完成路径的拼接


        //创建会话工厂,类似于数据库连接池,也存放数据库连接信息,映射文件,预定义HQL
        //SessionFactory是线程安全的
        sessionFactory = configure.buildSessionFactory();

        //创建会话,openSession为获取一个全新的session
        session = sessionFactory.openSession();

        //getCurrentSession为获取一个与当前线程绑定的session,需要额外在h.c.xml中配置
        /*Session currentSession1 = sessionFactory.getCurrentSession();
        Session currentSession2 = sessionFactory.getCurrentSession();
        if (currentSession1.hashCode() == currentSession2.hashCode()) System.out.println("same session");*/
        //session1和2是相同的
        //与openSession不同的是,事务提交/回滚时底层会自动关闭currentSession
    }


    //基础入门,增删改查的方法
    @Test
    public void contextLoad1() {
//        System.out.println("hello world!");


        //开启事务;多数情况事务交给spring
        Transaction transaction = session.getTransaction();
        transaction.begin();


        //通过session进行crud
        //增
        //save,保存user对象;id为自动生成
        saveUser(session);

        //删,如果没有该对象(主键)会报错
        //deleteUser(session);

        //改;也可以用save/saveOrUpdate,具体在方法内注明
        //setUser(session);

        //查
        //get,直接加载数据;获取不到则为null;
        //load与get类似,延迟加载,查询时只会获取id;获取不到时会报错
        //getUser(session);


        //提交事务,如果在h.c.xml中配置autocommit可以自动提交insert的数据,但del不可
        transaction.commit();
        //transaction.rollback();
    }


    //通过hql语句,使用Query来创建查询对象
    @Test
    public void contextLoad2() {
        //hql语句,面向对象查询语言
        String hql = "from User where username = ?1 and password = ?2";
        Query query1 = session.createQuery(hql);

        //补充:使用like的话setParameter的obj可以使用%占位符,例如"param%"
        //Query query1 = session.createQuery("from User where username like ?1 and password = ?2");

        //idea不推荐从0开始
        query1.setParameter(1,"test2");
        query1.setParameter(2,"1234");

        //执行查询单个结果(unique:单个,独特)
        User user = (User) query1.uniqueResult();
        System.out.println(user);

        //=======================================
        //分页查询
        Query query2 = session.createQuery("from User ");

        query2.setFirstResult(0);   //设置limit的开头和结尾
        query2.setMaxResults(9);    //最大条数

        //返回结果
        List list = query2.list();
        //List list = query2.getResultList();     //或者用此种方式获取list
        //遍历打印
        for (Object o : list) {
            User u = (User)o;
            System.out.println(u.getUsername());
        }
    }


    //通过criteria直接查询对象
    @Test
    public void contextLoad3() {

        //详细: https://www.cnblogs.com/g-smile/p/9177841.html
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        //查询结果所需要的类型
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        //查询所需要的主体类(User)
        Root<User> root = criteriaQuery.from(User.class);


        //过滤条件使用Predicate(谓语)的方式拼接;conjunction:连词;restrictions:限制
        Predicate restrictions = criteriaBuilder.conjunction();

        //过滤条件;criteriaBuilder.xxxx也可分开写
        restrictions = criteriaBuilder.and(
                restrictions
                ,criteriaBuilder.equal(root.get("username"), "test2")   //root.get处应该使用常量
                ,criteriaBuilder.equal(root.get("password"), "1234")
//                ,criteriaBuilder.ge(root.get("age"),11)     //ge:great equal,大于等于; gt:great than,大于   ;le 小于等于,lt 小于
//                ,criteriaBuilder.like(root.get("username"),"%es%")      //like:模糊查询
        );


        //查询符合的结果
        //criteriaQuery.select(root);       //查询符合主体类(User)的结果(全部结果集)
        criteriaQuery.where(restrictions);  //符合restrictions中的结果

        //执行查询,遍历结果
        Query<User> query = session.createQuery(criteriaQuery);
        List<User> userList = query.getResultList();
        query.list();
        if (userList.isEmpty()) System.out.println("list is empty!");   //判空
        for (User u : userList) {
            System.out.println(u);
        }

    }


    //使用sql语句进行查询
    @Test
    public void contextLoad4() {
        NativeQuery sqlQuery = session.createSQLQuery("select * from hibernate_test_table");

        List<Object[]> resultList = sqlQuery.getResultList();   //让其返回一个数组对象
        for (Object[] obj : resultList) {
            //System.out.println(obj[0]);   //可以用获取数组中每个元素的方式来获取值
            //注意,不可以强转为User
            String result = Arrays.toString(obj);
            System.out.println(result);
        }

    }


    //hql和criteria必定会对session进行缓存,但sql不会一定
    @Test
    public void contextLoad5() {
        String hql = "from User";
        Query query1 = session.createQuery(hql);
        System.out.println(query1.list());

//        NativeQuery sqlQuery = session.createSQLQuery("select * from hibernate_test_table");
//        System.out.println(sqlQuery.getResultList());
        //执行sql语句以后,此时还是会再次执行select语句,hql不会


        User user = session.get(User.class, 1);
        System.out.println("user:"+user);

    }

    //persist的用法
    @Test
    public void contextLoad6() {
        //session.getTransaction().begin();

        User user = new User();
        user.setUsername("test");
        user.setPassword("1234");

        //persist在事务外不会执行insert,但事务内与save一样会立刻insert
        //persist不允许指定主键,会报错PersistentObjectException
        //save可以,但会忽视主键;另外,save需要获取主键id所以会立刻执行insert
        //而persist直到事务提交后才会插入
        session.persist(user);

        session.getTransaction().begin();


        session.getTransaction().commit();
    }



    @AfterAll
    public static void after() {
        System.out.println("closing...");
        //关闭工厂
        sessionFactory.close();
        //关闭资源
        session.close();
    }


    private void setUser(Session session) {
        User user = session.get(User.class, 1);
//        User user = new User();   //测试save时使用
//        user.setUid(1);
//        user.setUsername("test");
        user.setPassword("2345");
        //↓也可以不加,会自动进行更新提交
        session.update(user);       //update时必须要有主键
        //session.save(user);         //使用save也可以进行更新,如果通过session获取的obj则update,没有则进行insert
        //session.saveOrUpdate(user);       //若存在主键时进行update,否则insert,不需要从session中获取obj
        System.out.println("user updated");
    }

    private void deleteUser(Session session) {
        //传入一个obj对象,可以从session中获取,此处为了方便直接new
        User user = new User();
        user.setUid(4);
        session.delete(user);   //由此可见是根据主键来删除的
        System.out.println("delete id:4");
    }

    private void getUser(Session session) {
        User user = session.get(User.class, 1);
        System.out.println("get user:" + user);

    }

    private void saveUser(Session session) {
        User user = new User();
        user.setUsername("test4");
        user.setPassword("1234");
        session.save(user);
    }

}

