package com.example.demo.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//多对多的演示
@Entity
@Table(name = "spring_data_jpa_test_player_table")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;
    @Column(name = "player_name")
    private String playerName;

    //配置多对多的关系
    @ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL)
    //与joinColumn对应,配置中间表
    @JoinTable(
            name = "spring_data_jpa_test_player_to_role_table"
            //当前对象在中间表中所对应的外键,name:中间表中的字段名,referencedColumnName:主表中的字段名
            ,joinColumns = {@JoinColumn(name = "m_pid",referencedColumnName = "pid")}
            //对方对象在中间表中所对应的外键
            ,inverseJoinColumns ={@JoinColumn(name = "m_rid",referencedColumnName = "rid")}
            )
    private Set<Role> roles = new HashSet<>();

    @Override
    public String toString() {
        return "Player{" +
                "pid=" + pid +
                ", playerName='" + playerName + '\'' +
                ", roles=" + roles +
                '}';
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
