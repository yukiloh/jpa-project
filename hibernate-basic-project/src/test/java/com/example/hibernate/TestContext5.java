package com.example.hibernate;


import com.example.hibernate.domain.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.LockModeType;
import java.util.List;


public class TestContext5 {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();

    }


    //hibernate中的写锁(排他锁)
    @Test
    public void contextLoad1() {
        //方法1:get中使用第三个参数LockMode;sql语句会在最后添加for update
        //Customer cus = session.get(Customer.class, 1, LockMode.PESSIMISTIC_WRITE);

        //方法2:query.setLockMode
        Query query = session.createQuery("from Customer");
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);


        List<Customer> list = query.getResultList();
        for (Customer customer : list ) {
            System.out.println(customer);
        }

        //写锁是读取中的锁,读取中的!读取中的!
        //写入数据时有事务,不需要写锁的接入
    }


    //乐观锁的示范
    @Test
    public void contextLoad2() {
        session.getTransaction().begin();
        Customer customer = session.get(Customer.class, 1);
        customer.setUsername("狗蛋,改");
        //每次更改记录时都会自动更新数据
        //可以通过断点的方式来演示乐观锁的发生,如果事务中version发生改变则会出现以下报错:
        //Row was updated or deleted by another transaction
        //乐观锁是hibernate自己实现的,而悲观锁是通过for update语句数据库自己实现的
        //因为乐观锁是hibernate自身实现的,所以多环境多语言的情况下很容易造成事务冲突,而for update不会

        session.getTransaction().commit();
    }

    @AfterAll
    public static void after() {
        System.out.println("closing...");
        sessionFactory.close();
        session.close();
    }
}

