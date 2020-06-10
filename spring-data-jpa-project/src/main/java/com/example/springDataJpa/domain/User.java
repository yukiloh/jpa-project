package com.example.springDataJpa.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity
@Table(
        name = "spring_data_jpa_test_user_table"
//        ,uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}        //唯一约束

        //添加索引(未验证)
        ,indexes = {
            //添加单个字段索引.也可以在此处添加唯一约束
            @Index(name = "username",columnList = "username",unique = true)

            //添加复合索引,需要保持name相同即可.存在顺序,与最左匹配原则一致(此处注释,只作为演示)
//            ,@Index(name = "username",columnList = "password")
//            ,@Index(name = "username",columnList = "status")      //未添加该字段,此处作为演示
        }
)

/**
 * 关于@DynamicUpdate 和 @DynamicInsert 的作用
 * 没有开启时的sql语句
 * insert
 * into
 *     spring_data_jpa_test_user_table
 *     (password, username)
 * values
 *     (?, ?)
 *
 * 开启后的sql语句
 * insert
 * into
 *     spring_data_jpa_test_user_table
 *     (username)
 * values
 *     (?)
 *
 * 即,如果开启后,所更新的字段是null,那么便不会执行相应的sql语句
 */
@DynamicUpdate
@DynamicInsert

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
//    private String status;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
