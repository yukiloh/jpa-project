package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

public class Customer {
    private Integer id;
    private String username;
    private Integer version;

    //一个客户可以有多个订单
    private Set<Order> orders = new HashSet<>();

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }



    public Customer() {
    }

    public Customer(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", orders=" + orders +
                '}';
    }

    //依靠hibernate的生成表格时必须要有get set
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

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
