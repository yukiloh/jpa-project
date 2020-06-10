package com.example.jpaHibernate;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtils {

    private static EntityManagerFactory managerFactory;
    private static EntityManager entityManager;

    //通过静态代码块创建entityManager
    static {
        System.out.println("Initiating...");
        managerFactory = Persistence.createEntityManagerFactory("myJpa");
        entityManager = managerFactory.createEntityManager();
    }


    public static EntityManager getEntityManager() {
        return entityManager;
    }
}
