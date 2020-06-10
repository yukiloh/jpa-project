package com.example.springDataJpa;

import com.example.springDataJpa.dao.UserDao;
import com.example.springDataJpa.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SpringDataJpaApplicationTest3 {
    @Autowired
    private UserDao userDao;

    //Specification,动态查询的基础
    @Test
    void contextLoads5() {
        Specification<User> userSpecification = (Specification<User>) (root, criteriaQuery, criteriaBuilder) -> {
            //root:需要查询的对象的条件
            //criteriaBuilder:构造查询的条件
            //criteriaQuery:本质是一个容器,通过add添加条件限制来查询对象;本项目中没有进行演示
            Path username = root.get("username");
            Path password = root.get("password");
            Predicate predicate1 = criteriaBuilder.equal(username, "狗蛋蛋");
            Predicate predicate2 = criteriaBuilder.equal(password, "250");

            //当需要查询多个条件时
            Predicate predicate = criteriaBuilder.and(predicate1, predicate2);
            //或者也可以用or
            //criteriaBuilder.or()
            return predicate;
        };
        Optional<User> one = userDao.findOne(userSpecification);
        one.ifPresent(System.out::println);
    }


    //Specification,模糊查询
    @Test
    void contextLoads6() {

        Specification<User> userSpecification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path username = root.get("username");
                //username.as(String.class):匹配的username需要以String类型进行匹配,非必须
                Predicate predicate = criteriaBuilder.like(username.as(String.class), "狗蛋%");
                return predicate;
            }
        };

        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        //模糊查出来的可能是多个,所以需要用findAll()
        List<User> list = userDao.findAll(userSpecification,sort);
        if (!list.isEmpty() ) System.out.println(Arrays.toString(list.toArray()));
    }


    //分页查询
    @Test
    void contextLoads7() {
        //Pageable是接口,PageRequest是其实现类,可以alt+ctrl+B查看到接口的实现类
        Pageable pageable = PageRequest.of(0,10);
        Page<User> list = userDao.findAll(pageable);

        //常用的三条语句
        List<User> content = list.getContent();         //获取结果集
        long totalElements = list.getTotalElements();   //数据库的总条数
        int totalPages = list.getTotalPages();          //可获得的总页数

        System.out.println(content);
        System.out.println("all records: "+totalElements);
        System.out.println("all pages: "+totalPages);

        System.out.println("=====================");

        //如果是pageable和其他限制条件结合,此处列举通过命名规则查询来限制username的方式
        List<User> userList = userDao.findByUsernameLike("%%", pageable);
        userList.forEach(user -> System.out.println(user));

    }



}
