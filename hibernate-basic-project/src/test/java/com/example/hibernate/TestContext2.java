package com.example.hibernate;


import com.example.hibernate.domain.Course;
import com.example.hibernate.domain.Customer;
import com.example.hibernate.domain.Order;
import com.example.hibernate.domain.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class TestContext2 {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();
    }


    //一对多的入门，数据的创建
    @Test
    public void contextLoad1() {
        session.getTransaction().begin();

        //创建客户
        Customer customer = new Customer();
        customer.setUsername("goudan");

        //创建订单
        Order order1 = new Order();
        order1.setOrderName("gou");
        Order order2 = new Order();
        order2.setOrderName("dan");

        //维护订单与客户的关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);

        //维护客户与订单的关系（让客户拥有订单）
        customer.getOrders().add(order1);
        customer.getOrders().add(order2);

        //提交数据（需要先保存客户,因为订单要获取客户的id）
        session.save(customer);
        session.save(order1);
        session.save(order2);

        session.getTransaction().commit();
    }


    //一对多的查询和删除
    @Test
    public void contextLoad2() {
        //查询客户
        Customer customer = session.get(Customer.class, 2);
        System.out.println("==customer name: "+customer.getUsername());

        //查询该客户的订单
        Set<Order> orders = customer.getOrders();
        for (Order o : orders) {
            System.out.println("==order: "+o.getOrderName());
        }
        //执行customer.getOrders()时,hibernate会进行第二次的外键查询
        //也可以使用hql进行查询,以后讲(2020年2月10日)


        //一对多的删除
        //需要在Customer.hbm.xml中的set标签设置级联cascade,值为save-update;注意,级联需要设置在一对多的"一"中
        //当删除customer时会自动将order中的订单记录设为null
        //cascade的属性为delete时会直接删除外键关联的订单(未测试)
        session.getTransaction().begin();       //删除操作需要开启事务

        //session.delete(customer);     //删除客户

        orders.clear();     //孤儿删除,删除子记录(order);也可以迭代出来一个个remove;

        session.getTransaction().commit();


        //级联cascade的补充
        //cascade可以设置多个属性
        //当开启级联(save-update)后,保存save customer时会自动保存order的数据(不需要手动save)
        //delete-orphan:孤儿删除;开启后允许删除子记录(否则则使外键为null)
        //all-delete-orphan = save-update,delete,delete-orphan ,三个整合
        //all = save-update,delete
    }


    //多对多,插入数据
    @Test
    public void contextLoad3() {
        //创建学生
        Student student1 = new Student();
        student1.setStudentName("stu1");
        Student student2 = new Student();
        student2.setStudentName("stu2");

        //创建课程
        Course course1 = new Course();
        course1.setCourseName("course1");
        Course course2 = new Course();
        course2.setCourseName("course2");

        //让学生绑定课程
        student1.getCourses().add(course1);
        student2.getCourses().add(course1);
        student1.getCourses().add(course2);
        student2.getCourses().add(course2);


        session.getTransaction().begin();   //保存需要开启事务

        //保存数据;需要给student.hbm.xml添加级联才能保存课程
        session.save(student1);
        session.save(student2);
        //stu表2条,course表2条,中间表4条数据,总共8条

        //补充,如果inverse,则由外键即course来维护中间表,此时course需要开启级联,并且保存的对象使course1和2

        session.getTransaction().commit();



    }


    //多对多,加载数据
    @Test
    public void contextLoad4() {
        //当Student.hbm.xml中,class标签下的lazy设置为false,load不会延迟加载
        //lazy=true的时候:先---,再select stu_table,再select mom_table
        //lazy=false时,先select stu_table,再---,再select mom_table
        Student student = session.load(Student.class, 1);
        System.out.println("----------------------------------");
        System.out.println(student);    //注意toString选中的元素,循环嵌套后会栈溢出

        //特别情况,如果只加载id时( student.getSid() ),不会触发sql语句
    }


    //多对一,加载数据
    @Test
    public void contextLoad5() {
        Order order = session.load(Order.class, 3);
        Customer customer = order.getCustomer();
        System.out.println(order.getOrderName()+"-------"+customer.getUsername());
        //多对一也可以设置是否延迟加载(Order.hbm.xml中的many-to-one标签中的lazy属性,false proxy no-proxy)
        /*当为proxy时,即时/延迟加载的效果与class中的lazy mto中的fetch都有关系如下图
        * Order.hbm.xml中:
        * class:lazy   mto:lazy   mto:fetch
        *   false       proxy       join        多和一都立即加载,单条sql语句
        *   false       proxy       select      多(Order)为即时,一为延迟,多条sql
        *   true        proxy       join        多为延迟,一为即时
        *   true        proxy       select      多和一都是延迟加载
        * */

    }


    //测试批量加载(具体配置在Customer.hbm.xml中)
    @Test
    public void contextLoad6() {
        List<Customer> customers = session.createQuery("from Customer").getResultList();
        for (Customer c : customers) {
            System.out.println(c);
        }
    }








    @AfterAll
    public static void after() {
        System.out.println("closing...");
        sessionFactory.close();
        session.close();
    }
}

