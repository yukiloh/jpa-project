package com.example.demo;


import com.example.demo.domain.Company;
import com.example.demo.domain.CompanyAddress;
import com.example.demo.domain.Customer;
import com.example.demo.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;


public class TestContext7 {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();

    }


    //证明二级缓存
    @Test
    public void contextLoad1() {
        //查询的对象不要有外键,否则外键无法自动缓存下来
        User user1 = session.get(User.class, 1);
        System.out.println(user1);

        session.clear();

        User user2 = session.get(User.class, 1);
        System.out.println(user2);

        session.close();

        //重开session
        Session session2 = sessionFactory.openSession();
        User user3 = session2.get(User.class, 1);
        System.out.println(user3);
        session2.close();
        
    }

    //测试集合缓存
    @Test
    public void contextLoad2() {
        Customer customer1 = session.get(Customer.class, 1);
        System.out.println(customer1.getOrders());

        session.clear();

        Customer customer2 = session.get(Customer.class, 1);
        System.out.println(customer2.getOrders());

        session.close();

        //重开session
        Session session2 = sessionFactory.openSession();
        Customer customer3 = session2.get(Customer.class, 1);
        System.out.println(customer3.getOrders());
        session2.close();
    }

    //查询缓存(用于缓存HQL)
    @Test
    public void contextLoad3() {
        Query query = session.createQuery("from User");
        //设置允许缓存
        query.setCacheable(true);

        List<Customer> resultList = query.getResultList();
        System.out.println(resultList.toString());

        //此时二级缓存会通过id寻找对象,因此↓不会触发sql语句
        User user = session.get(User.class, 1);
        System.out.println(user);

        //如果关闭session则依然会清除缓存
    }


    @AfterAll
    public static void after() {
        System.out.println("closing...");
        sessionFactory.close();

        if (session.isOpen()) {
            session.close();
        }
    }
}

