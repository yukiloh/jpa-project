package com.example.demo;


import com.example.demo.domain.Customer;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

public class TestContext4 {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();
    }


    //QBC,query by criteria
    @Test
    public void contextLoad1() {
        //基础查询,TestContext中写过
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);    //设定结果集
        Root<Customer> root = criteriaQuery.from(Customer.class);       //设定查询的主体,可以返回root来进行条件限制

        //排序
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

        //条件查询
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
                restrictions
                , criteriaBuilder.ge(root.get("id"), 2)     //ge:great equal,大于等于; gt:great than,大于   ;le 小于等于,lt 小于
        );

        criteriaQuery.where(restrictions);  //符合restrictions中的结果

        Query<Customer> query = session.createQuery(criteriaQuery);
        //实现分页,和普通HQL一样
        query.setFirstResult(0);
        query.setMaxResults(10);
        System.out.println(Arrays.toString(query.getResultList().toArray()));

    }




    @Test
    public void contextLoad2() {
        //离线查询,在执行dao前预先添加条件
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Customer.class);
        //在此处添加需要的条件
        detachedCriteria.add(Restrictions.eq("username","狗蛋"));

        //在dc内塞入session进行查询
        Criteria executableCriteria = detachedCriteria.getExecutableCriteria(session);
        Customer customer = (Customer) executableCriteria.uniqueResult();
        System.out.println(customer);


    }

    @AfterAll
    public static void after() {
        System.out.println("closing...");
        sessionFactory.close();
        session.close();
    }
}

