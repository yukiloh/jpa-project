package com.example.springDataJpa.dao;

import com.example.springDataJpa.domain.User;
import com.example.springDataJpa.domain.QUser;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//Specification:规格,类似于hibernate的criteria查询
//他们的继承关系
//子类|   JpaRepository(jpa操作) < PagingAndSortingRepository(分页排序) < CrudRepository(基础查询)    |父类
//JpaRepository的泛型,1.对象的类型,2.主键的类型
//QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser>: 继承querydsl提供的方法,可以进行querydsl方式的查询
public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User>
        ,QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

    //JPQL的使用
    @Query("from User where username = ?1")
    User findOneByNameWithJPQL(String username);


    //更新数据
    @Query("update User set username = ?2 where id = ?1")
    @Modifying  //表示修改数据
    void updateNameByIdWithJPQL(Integer id,String username);


    //使用sql语句进行查询,通过nativeQuery来区分JPQL
    //可以指定返回对象的类型,而不用Object来转换
    @Query(value = "select * from spring_data_jpa_test_user_table",nativeQuery = true)
    List<User> findAllWithSQL();


    //通过命名规则查询
    //其他案例: https://www.cnblogs.com/chenglc/p/11226693.html  ,搜索"方法名称命名规则查询"可以找到
    List<User> findByUsernameLike(String username);
    List<User> findByUsernameLike(String username, Pageable pageable);
    User findByUsername(String username);

//    /**
//     * 继承querydsl的接口后可以进行自定义方法的查询,原理我也没弄懂
//     */
//    @Override
//    default void customize(
//            QuerydslBindings bindings, QUser root) {
//        bindings.bind(String.class)
//                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
//        bindings.excluding(root.username);
//    }
}
