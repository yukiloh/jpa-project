package com.example.demo.dao;

import com.example.demo.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//Specification:规格,类似于hibernate的criteria查询
//他们的继承关系
//子类|   JpaRepository(jpa操作) < PagingAndSortingRepository(分页排序) < CrudRepository(基础查询)    |父类
//JpaRepository的泛型,1.对象的类型,2.主键的类型
public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User>{

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
    List<User> findByUsernameLike(String username);
    List<User> findByUsernameLike(String username, Pageable pageable);
    User findByUsername(String username);

    //其他:https://www.cnblogs.com/chenglc/p/11226693.html
    //搜索:方法名称命名规则查询
}
