package com.example.springDataJpa;

import com.example.springDataJpa.dao.UserDao;
import com.example.springDataJpa.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


/**
 * 用于测试批量操作
 * 可以查阅此处了解详细: https://www.codejava.net/frameworks/hibernate/how-to-execute-batch-insert-update-in-hibernate
 */
@SpringBootTest
class SpringDataJpaApplicationTest99 {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    void init() {
        System.out.println("finished");
    }

    /**
     * 由于maria数据库主键设置为IDENTITY,所以无法使用批量插入(如果mysql类的数据库,jpa中设置主键为Sequence反而会慢很多..)
     * 插入2千个4秒,2万个大概需要38秒,删除差不多
     * 解决方案无,推荐使用spring-jdbc来实现手动插入(兼容性还挺好)
     */

    @Test
    void saveUsers() {
        //创建一个array来保存对象
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            User user = new User("测试用户" + i, String.valueOf(i));
            users.add(user);
        }

        userDao.saveAll(users);
    }

    /**
     * 或者可以通过entityManger来插入. em需要手动开启事务(@Transaction)
     * 感觉无论哪种插入方式速度都差不多,都是一个个对象来执行,而不是批量
     */
    @Test
    @Transactional
    @Rollback(false)
    void test() {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            User user = new User("测试用户" + i, String.valueOf(i));
            users.add(user);
        }

        int i = 0;
        for (User user : users) {
            entityManager.persist(user);
            i++;

            //当第100次或全部插入完毕后,刷新em
            if (i % 100 == 0 || i == users.size()) {
                try {
                    entityManager.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    entityManager.clear();
                }

            }
        }
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
