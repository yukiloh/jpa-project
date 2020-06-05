package com.example.demo.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//一对多的演示
@Entity
@Table(name = "spring_data_jpa_test_customer_table")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cid;
    @Column(name = "customer_name")
    private String customerName;

    //一般应交由多的一方来维护,因此↓注释
/*    //一对多的配置;
    @OneToMany(targetEntity = Order.class)
    //配置外键与主表(customer表)的关系,name为外键的字段名,referencedColumnName为所对应主表的字段名
    @JoinColumn(name = "order_cid",referencedColumnName = "cid")*/

    //一方放弃外键维护,mappedBy 代表另一方(多表)中的属性的名称
    //cascade:级联操作;merge:会执行合并操作,persist:保存,remove:删除,all:全执行
    @OneToMany(mappedBy = "customer",cascade = {CascadeType.ALL})
    private Set<Order> orders = new HashSet<>();
//    private Set<Order> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "cid=" + cid +
                ", customerName='" + customerName + '\'' +
                ", orders=" + orders +
                '}';
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
