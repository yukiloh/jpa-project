package com.example.jpaHibernate;

import com.example.jpaHibernate.domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class TestContext {

    private static EntityManagerFactory managerFactory;
    private static EntityManager entityManager; //线程安全

    @BeforeAll
    public static void before() {
        System.out.println("Initiating...");
        //因为强制了MATA-INF/persistence.xml的原因,可以从此找到unit name
        managerFactory = Persistence.createEntityManagerFactory("myJpa");
        entityManager = managerFactory.createEntityManager();

        //也可以通过自建的工具类
        //entityManager = JPAUtils.getEntityManager();
    }

    //数据插入
    @Test
    public void ContextLoad1(){

        User user = new User();
        user.setUsername("狗蛋");
        user.setPassword("1234");

        entityManager.getTransaction().begin();

        entityManager.persist(user);        //等同于save
        // merge:更新
        // remove:删除
        // find/getReference:查找

        entityManager.getTransaction().commit();
    }


    //查找
    @Test
    public void ContextLoad2(){
        User user = entityManager.find(User.class, 1);      //等同于get
        //User reference = entityManager.getReference(User.class, 1);   //等同于load,支持延迟加载,本质是动态代理对象
        System.out.println(user);
    }

    //删除
    @Test
    public void ContextLoad3(){
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, 4);
        entityManager.remove(user);

        entityManager.getTransaction().commit();
    }

    //更新
    @Test
    public void ContextLoad4(){
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, 1);
        user.setPassword("2345");
//        entityManager.merge(user);

        entityManager.getTransaction().commit();
    }


    //查询全部&排序
    @Test
    public void ContextLoad5(){
        //jpql
//        Query query = entityManager.createQuery("from User ");    //普通查询
        Query query = entityManager.createQuery("from User order by id desc "); //倒序
        List<User> resultList = query.getResultList();

        System.out.println(Arrays.toString(resultList.toArray()));

    }

    //聚合查询
    @Test
    public void ContextLoad6(){
        Query query = entityManager.createQuery("select count(id) from User ");
        Object singleResult = query.getSingleResult();
        System.out.println("------type : "+singleResult.getClass()+"  result: "+singleResult.toString());
    }

    //分页查询
    @Test
    public void ContextLoad7(){
        Query query = entityManager.createQuery("from User ");
        //设置起始数和最大数据数
        query.setFirstResult(0);
        query.setMaxResults(10);

        List<User> resultList = query.getResultList();
        System.out.println(Arrays.toString(resultList.toArray()));
    }


    //条件查询
    @Test
    public void ContextLoad8(){
        Query query = entityManager.createQuery("from User where password like ?1");
        query.setParameter(1,"%1%");
        List<User> resultList = query.getResultList();
        System.out.println(resultList.toString());
    }





    @AfterAll
    public static void after() {
        System.out.println("Closing...");
        if (entityManager.isOpen()) entityManager.close();
        if (managerFactory.isOpen()) managerFactory.close();
    }
}
