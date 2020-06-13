package com.example.springDataJpa;

import com.example.springDataJpa.dao.UserDao;
import com.example.springDataJpa.domain.QRole;
import com.example.springDataJpa.domain.QUser;
import com.example.springDataJpa.domain.Role;
import com.example.springDataJpa.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author yukiloh
 * @version 0.1
 * @date 2020/6/11 14:07
 * 对querydsl的使用案例
 */
@SpringBootTest
class JPAQuerydslIntegrationTest {

    @Autowired
    private UserDao userDao;


    /**
     * 一个简单的案例,调用类似springDataJpa的接口来执行查询
     */
    @Test
    void simpleQuery() {
        //获取一个qUser对象
        QUser qUser = QUser.user;

        //对qUser的对象进行条件限定
        BooleanExpression john = qUser.username.like("John");

        //调用repo下类似springDataJpa接口进行查询(因为实现了QuerydslPredicateExecutor)
        Iterable<User> users = userDao.findAll(john);

        //遍历打印结果
        users.forEach(user -> System.out.println(user));

        /**
         * 结果:
         * Hibernate:
         *     select
         *         user0_.id as id1_5_,
         *         user0_.password as password2_5_,
         *         user0_.username as username3_5_
         *     from
         *         spring_data_jpa_test_user_table user0_
         *     where
         *         user0_.username like ? escape '!'
         * User{id=45530, username='John', password='123'}
         *
         * 可以看到它并非是先findAll后在jvm中处理结果的
         * 直接向数据库发送了like语句
         */
    }

    /**
     * 单表动态查询
     */
    @Test
    void OneDynamicQuery() {
        QUser qUser = QUser.user;

        //查询一个 id大于1000 且 password like 123 的user
        BooleanExpression expression = qUser.id.gt(1000).and(qUser.password.like("123"));

        //分页查询,根据id降序
        Pageable pageable = PageRequest.of(0,10,Sort.Direction.DESC,"id");

        List<User> userList = userDao.findAll(expression, pageable).toList();
        userList.forEach(user -> System.out.println(user));

        /**
         * 结果:
         * Hibernate:
         *     select
         *         user0_.id as id1_5_,
         *         user0_.password as password2_5_,
         *         user0_.username as username3_5_
         *     from
         *         spring_data_jpa_test_user_table user0_
         *     where
         *         user0_.id>?
         *         and (
         *             user0_.password like ? escape '!'
         *         )
         *     order by
         *         user0_.id desc limit ?
         * User{id=45532, username='Linda', password='123'}
         * User{id=45531, username='Marry', password='123'}
         * User{id=45530, username='John', password='123'}
         */
    }


    @Autowired
    private EntityManager entityManager;

    /**
     * 多表动态查询
     */
    @Test
    void ManyDynamicQuery() {
        //工厂类中需要传入一个em
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        //选择实体类集,然后执行查询语句
        JPAQuery<Tuple> query = jpaQueryFactory.select(QUser.user, QRole.role)
                .from(QUser.user)
                .leftJoin(QRole.role)
                .on(QUser.user.id.eq(QRole.role.rid));

        //再次进行筛选,排除role为null的结果,最后通过fetch()方法向数据库发起查询
        List<Tuple> list = query.where(QRole.role.rid.isNotNull()).fetch();

        //获取的list进行遍历
        list.forEach(tuple -> {
            // System.out.println(tuple);

            //传入Q类对象,从tuple中获取原型对象
            User user = tuple.get(QUser.user);
            Role role = tuple.get(QRole.role);
            System.out.println(user+"  "+role);
        });

        /**
         * 结果:
         * Hibernate:
         *     select
         *         user0_.id as id1_5_0_,
         *         role1_.rid as rid1_4_1_,
         *         user0_.password as password2_5_0_,
         *         user0_.username as username3_5_0_,
         *         role1_.role_name as role_nam2_4_1_
         *     from
         *         spring_data_jpa_test_user_table user0_
         *     left outer join
         *         spring_data_jpa_test_role_table role1_
         *             on (
         *                 user0_.id=role1_.rid
         *             )
         *     where
         *         role1_.rid is not null
         * [User{id=1, username='狗蛋蛋', password='250'}, Role{rid=1, roleName='村民'}]
         * [User{id=2, username='大柱', password='251'}, Role{rid=2, roleName='大爷'}]
         * User{id=1, username='狗蛋蛋', password='250'}  Role{rid=1, roleName='村民'}
         * User{id=2, username='大柱', password='251'}  Role{rid=2, roleName='大爷'}
         */
    }

    /**
     * 子查询(因为表格简单,因此象征性的演示了以下)
     */
    @Test
    void subQuery() {
        // 子查询
//        List<Person> persons = queryFactory.selectFrom(person)
//                .where(person.children.size().eq(
//                        JPAExpressions.select(parent.children.size().max())
//                                .from(parent)))
//                .fetch();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QUser qUser = QUser.user;

        //子查询语句,使用JPAExpressions来创建查询语句
        JPQLQuery<Integer> query = JPAExpressions.select(qUser.id).from(qUser).where(qUser.id.eq(2));

        //selectFrom : return (JPAQuery)this.select((Expression)from).from(from)
        List<User> list = jpaQueryFactory.selectFrom(qUser).where(qUser.id.eq(
                //进行子查询
                query
        )).fetch();

        list.forEach(user -> {
            System.out.println(user);
        });

        /**
         * 结果:
         * Hibernate:
         *     select
         *         user0_.id as id1_5_,
         *         user0_.password as password2_5_,
         *         user0_.username as username3_5_
         *     from
         *         spring_data_jpa_test_user_table user0_
         *     where
         *         user0_.id=(
         *             select
         *                 user1_.id
         *             from
         *                 spring_data_jpa_test_user_table user1_
         *             where
         *                 user1_.id=?
         *         )
         * User{id=2, username='大柱', password='251'}
         */

    }
    /**
     * 投影查询
     */
    @Test
    void projectionQuery() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QUser qUser = QUser.user;

        List<Tuple> list = jpaQueryFactory.select(qUser.id, qUser.username).from(qUser).fetch();


        //获取的list进行遍历
        list.forEach(tuple -> {
            // System.out.println(tuple);

            //传入Q类对象,从tuple中获取原型对象
            Integer id = tuple.get(qUser.id);
            String name = tuple.get(qUser.username);

            System.out.println(id+"  "+name);
        });
        /**
         * 结果:
         * Hibernate:
         *     select
         *         user0_.id as col_0_0_,
         *         user0_.username as col_1_0_
         *     from
         *         spring_data_jpa_test_user_table user0_
         * 45530  John
         * 45532  Linda
         * 45531  Marry
         * 2  大柱
         * 1  狗蛋蛋
         * 45529  绿灯侠
         * 45525  蝙蝠侠
         * 45527  超人
         * 45526  钢铁侠
         */

    }

//    /**
//     * https://www.baeldung.com/rest-api-search-language-spring-data-querydsl
//     * spring官方的案例,需要 SearchCriteria,UserPredicate,UserPredicatesBuilder 三个类的支持
//     * 说实话我也不知道这个案例干啥的,以及怎么实现的,可能因为是2.x的版本的案例有点老
//     */
//    @Test
//    void findUserByPassword() {
//        UserPredicatesBuilder builder = new UserPredicatesBuilder().with("password", ":", "123");
//        BooleanExpression expression = builder.build();
//        Iterable<User> results = userDao.findAll(expression);
//        results.forEach(user -> System.out.println(user));
//    }
}
