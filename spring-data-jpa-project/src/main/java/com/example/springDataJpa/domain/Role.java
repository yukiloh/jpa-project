package com.example.springDataJpa.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//多对多的演示
@Entity
@Table(name = "spring_data_jpa_test_role_table")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;
    @Column(name = "role_name")
    private String roleName;

    //↓放弃维护权
/*    //配置多对多的关系
    @ManyToMany(targetEntity = Player.class)
    //与joinColumn对应,配置中间表
    @JoinTable(
            name = "spring_data_jpa_test_player_to_role_table"
            //当前对象在中间表中所对应的外键,name:中间表中的字段名,referencedColumnName:主表中的字段名
            ,joinColumns = {@JoinColumn(name = "m_rid",referencedColumnName = "rid")}
            //对方对象在中间表中所对应的外键
            ,inverseJoinColumns ={@JoinColumn(name = "m_pid",referencedColumnName = "pid")}
    )*/
    //配置拥有维护权的另一方的属性
    @ManyToMany(mappedBy = "roles")
    private Set<Player> players = new HashSet<>();


    @Override
    public String toString() {
        return "Role{" +
                "rid=" + rid +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
