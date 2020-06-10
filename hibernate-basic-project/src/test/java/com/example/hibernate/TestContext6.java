package com.example.hibernate;


import com.example.hibernate.domain.Company;
import com.example.hibernate.domain.CompanyAddress;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestContext6 {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        System.out.println("initializing...");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();

    }


    //一对一外键示范
    @Test
    public void contextLoad1() {
        session.getTransaction().begin();

        //创建公司和地址
        Company company = new Company();
        company.setCompanyName("C某某公司");

        CompanyAddress companyAddress = new CompanyAddress();
        companyAddress.setAddress("C公司地址");

        //设置多对一中,一和多的关系
        companyAddress.setCompany(company);

        session.save(company);
        session.save(companyAddress);

        session.getTransaction().commit();
    }



    @AfterAll
    public static void after() {
        System.out.println("closing...");
        sessionFactory.close();
        session.close();
    }
}

