package com.example.demo;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class DemoApplicationTest1 {
    @Autowired
    private UserDao userDao;


    @Test
    void contextLoads() {
        //保存对象
        User user = new User();
        user.setUsername("大柱");
        user.setPassword("251");
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
        //统计
        System.out.println("------------------user count: "+userDao.count());

        //判断id是否存在
        Optional<User> user = userDao.findById(3);
        if (!user.isPresent()) System.out.println("--------------------id not found");

        //另一种判断是否存在的方法
        System.out.println(userDao.existsById(3));
    }


    //get,另一种查询的方式
    @Test
    @Transactional  //开启事务
    void contextLoads2() {
        User user = userDao.getOne(1);  //需要开启事务支持
        System.out.println(user);
        //二者的区别
        //findOne: 立即加载
        //getOne: 延迟加载
    }

}
