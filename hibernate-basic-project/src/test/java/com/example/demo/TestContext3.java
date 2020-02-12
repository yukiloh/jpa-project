package com.example.demo;


import com.example.demo.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class TestContext3 {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();
    }


    //HQL基本查询示范
    @Test
    public void contextLoad1() {
        //类似于SQL,但HQL是**面向对象查询**,查询的是**对象以及对象中的属性**
        //HQL语法不区分大小写,但类名和属性区分
        //因此,String hql = "from User"; 此时查找的其实是User对象而不是表

        //基础查询,TestContext中演示过了
        Query query1 = session.createQuery("from Customer where id = ?1");
        query1.setParameter(1,1);

        //2个返回obj的结果是相同的
        Customer customer = (Customer) query1.getSingleResult();
        Customer customer1 = (Customer) query1.uniqueResult();
        System.out.println(customer);
        System.out.println(customer1);


        //投影查询,当并非需要查询所有字段时,可以通过一个实体类来接收所需要的字段,此种查询称为投影查询
        //如果不使用投影,将返回Object[],使用很不方便
        Query query = session.createQuery("select c.id,c.username from Customer c where id = 1");
        //Customer customer2 = (Customer) query.uniqueResult(); //错误演示,不可以强转
        List<Object[]> list = query.list();
        Object[] obj = list.get(0);
        System.out.println(obj[0].toString()+obj[1].toString());

        //使用投影查询,提前创建Customer的构造函数
        Query query2 = session.createQuery("select new Customer(c.id,c.username) from Customer c where id = 1");
        System.out.println(query2.getSingleResult());
    }


    //排序，聚合，分组查询
    @Test
    public void contextLoad2() {
        //order by 排序,desc降序,asc升序
        Query query1 = session.createQuery("from Customer order by id desc ");

        //分页查询,之前讲过了

        //聚合查询,需要注意返回结果的类型
        //count
        Query query2 = session.createQuery("select count(c.id) from Customer c");
        Long singleResult1 = (Long) query2.getSingleResult();
        System.out.println("----total count:"+singleResult1);
        //avg
        Query query3 = session.createQuery("select avg (c.id) from Customer c");
        Double singleResult2 = (Double) query3.getSingleResult();
        System.out.println("----total count:"+singleResult2);

        //分组查询 group by
        //hibernate的分组和sql语句不同,因为hibernate是面向对象进行查询,所以group by 依赖的分组是对象,而不是sql中的某个字段
        Query query4 = session.createQuery("select o.customer,count(o) from Order o group by o.customer");
        List<Object[]> resultList = query4.getResultList();
        for (Object[] o : resultList) {
//            System.out.println((User)o);
            System.out.println(o[0].toString()+o[1].toString());
        }
        //结果展示,
/*      Customer{id=1, username='狗蛋', orders=[Order{id=3, orderName='板砖'}, Order{id=4, orderName='铁锅'}]}2
        Customer{id=2, username='二妞', orders=[Order{id=5, orderName='茅草'}, Order{id=6, orderName='石灰'}]}2
        Customer{id=3, username='铁柱', orders=[Order{id=7, orderName='黄沙'}]}1*/
    }


    //交叉连接,隐式内连接,内连接,迫切内连接
    @Test
    public void contextLoad3() {
        //交叉连接/笛卡尔积
//        Query query = session.createQuery("from Customer c,Order o");

        //补充:   显式内连接:取交集;    隐式:查询2张表,但没有inner,通过where决定取哪部分

        //隐式内连接(sql语法中有join)
//        Query query = session.createQuery("from Customer c,Order o where c = o.customer");

        //内连接(inner join),也即非迫切内连接,返回的结果是Object[]
        //谨记hibernate是以对象为查询目标,因此join的不是一张表而是对象!
//        Query query = session.createQuery("from Customer c inner join c.orders");

        //迫切内连接,HQL中添加了fetch,底层依然是内连接
        //将结果集装入父集(Customer)中,封装成一个父集的对象
        //通过obj.getClass会发现他返回的是一个Customer类型的集合
        Query query = session.createQuery("from Customer c inner join fetch c.orders");

        List<Object[]> resultList = query.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj[0]+"---"+obj[1]);
        }
    }


    //外连接,迫切外连接
    @Test
    public void contextLoad4() {
        //左外连接 left outer join;即使右表没有数据(大栓),左表也会输出(但数据为null)
//        Query query = session.createQuery("from Customer c left outer join c.orders");

        //迫切左外连接,与之前的迫切内连接一样,会返回父集类型的结果
        Query query = session.createQuery("from Customer c left outer join fetch c.orders");

        List<Object[]> resultList = query.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj.getClass());
//            System.out.println(obj[0]+"---"+obj[1]);
        }
    }


    //命名查询
    @Test
    public void contextLoad5() {
        //项目打包后成为类文件,此时很难去更改表与对象的关系,因此此时可以通过更改hbm.xml的配置文件来更改映射关系
        //根据scheme,2者都必须写在末尾
        //1.写在实体类配置文件的class标签下的query标签内(局部HQL,使用时需要全类名)
        Query local_hql_1 = session.getNamedQuery("com.example.demo.domain.Customer.local_hql_1");
        local_hql_1.setParameter(1,"狗蛋");
        System.out.println(local_hql_1.getSingleResult().toString());

        //2.写在(实体类配置文件的)hibernate-mapping标签下的query标签内(全局HQL,不需要全类名)
        Query global_hql_1 = session.getNamedQuery("global_hql_1");
        global_hql_1.setParameter(1,"狗蛋");
        System.out.println(global_hql_1.getSingleResult().toString());

        //一般为了方便管理,全局HQL会写在单独的hbm.xml中,而复杂,独有的HQL会写在局部query下
    }

    @AfterAll
    public static void after() {
        System.out.println("closing...");
        sessionFactory.close();
        session.close();
    }
}

