package com.example.springDataJpa;

import com.example.springDataJpa.dao.UserDao;
import com.example.springDataJpa.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;

@SpringBootTest
class SpringDataJpaApplicationTest1 {
    @Autowired
    private UserDao userDao;


    @Test
    void contextLoads() {
        //保存对象
        User user = new User();
        user.setUsername("绿灯侠");
//        user.setPassword("250");
        userDao.save(user);
        //实现原理:
        //JDKDynamicAopProxy动态代理了对象,该对象也继承了JpaRepository,因此可以实现crud
        //spring data jpa 操作JPA,JPA→Hibernate→jdbc→数据库

        //查找全部
        List<User> list = userDao.findAll();
        System.out.println(list);
    }


    @Test
    void contextLoads1() {
        //统计,简单的聚合
        System.out.println("------------------user count: "+userDao.count()+"------------------");

//        //判断id是否存在
//        Optional<User> user = userDao.findById(3);
//        if (!user.isPresent()) System.out.println("--------------------id not found");
//
//        //另一种判断是否存在的方法
//        System.out.println(userDao.existsById(3));
    }


    //Example.of的查询(2020年5月16日,新增,没有测试)
    @Test
    void contextLoads2() {
        User user = new User();
        user.setUsername("大柱");
        long count = userDao.count(Example.of(user));
        System.out.println(count);
    }




}
