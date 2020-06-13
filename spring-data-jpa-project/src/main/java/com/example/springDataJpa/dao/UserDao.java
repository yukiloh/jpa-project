package com.example.springDataJpa.dao;

import com.example.springDataJpa.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//Specification:规格,类似于hibernate的criteria查询
//他们的继承关系
//子类|   JpaRepository(jpa操作) < PagingAndSortingRepository(分页排序) < CrudRepository(基础查询)    |父类
//JpaRepository的泛型,1.对象的类型,2.主键的类型
//QuerydslPredicateExecutor<User>: 让dao可以使用querydsl提供的查询方法(findAll等,spring data的接口)
//QuerydslBinderCustomizer<QUser>: 可以传入自定义的Predicate,在这里进行转义.没深入研究
public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User>
        ,QuerydslPredicateExecutor<User>/*, QuerydslBinderCustomizer<QUser>*/ {

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
//     * 继承QuerydslBinderCustomizer<QUser>时的配置,进行自定义Predicate
//     */
//    @Override
//    default void customize(
//            QuerydslBindings bindings, QUser root) {
//        bindings.bind(String.class)
//                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
//        bindings.excluding(root.username);
//    }
}
