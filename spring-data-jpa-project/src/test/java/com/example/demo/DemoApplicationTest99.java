package com.example.demo;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 用于测试批量操作
 * 可以查阅此处了解详细: https://www.codejava.net/frameworks/hibernate/how-to-execute-batch-insert-update-in-hibernate
 */
@SpringBootTest
class DemoApplicationTest99 {
    @Autowired
    private UserDao userDao;

    @Test
    void init() {
        System.out.println("finished");
    }

    /**
     * 由于maria数据库主键设置为IDENTITY,所以无法使用批量插入
     * 插入2万个大概需要38秒,删除差不多
     * 解决方案无,推荐使用spring-jdbc来实现手动插入(兼容性还挺好)
     */

    @Test
    void saveUsers() {
        //创建一个array来保存对象
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < 20000; i++) {
            User user = new User("测试用户" + i, String.valueOf(i));
            users.add(user);
        }

        userDao.saveAll(users);
    }

    /**
     * 清除测试用户
     */
    @Test
    void clearDatabase() {
        List<User> list = userDao.findByUsernameLike("%测试用户%");
        userDao.deleteAll(list);
    }
}
