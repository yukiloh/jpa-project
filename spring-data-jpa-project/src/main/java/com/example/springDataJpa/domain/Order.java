package com.example.springDataJpa.domain;

import javax.persistence.*;


@Entity
@Table(name = "spring_data_jpa_test_order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;
    @Column(name = "order_name")
    private String orderName;

    //配置一对多的关系
    @ManyToOne(targetEntity = Customer.class)
    //和customer中的一样,name:外键名,referencedColumnName:所对应的主键名
    //foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    @JoinColumn(name = "order_cid",referencedColumnName = "cid")
    private Customer customer;


    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", orderName='" + orderName + '\'' +
                '}';
    }

    public Order() {
    }

    public Order(String orderName) {
        this.orderName = orderName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
