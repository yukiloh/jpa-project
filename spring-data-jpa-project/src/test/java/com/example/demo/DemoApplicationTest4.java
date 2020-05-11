package com.example.demo;

import com.example.demo.dao.CustomerDao;
import com.example.demo.dao.OrderDao;
import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.RoleDao;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Order;
import com.example.demo.domain.Player;
import com.example.demo.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class DemoApplicationTest4 {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private RoleDao roleDao;


    //一对多的保存
    @Test
    @Transactional
    @Rollback(false)
    void contextLoads() {
        //和hibernate中一样
        Customer customer = new Customer();
        customer.setCustomerName("狗蛋4");

        Order order = new Order();
        order.setOrderName("板砖");

        //让客户拥有订单,建立关系,否则order中的外键为null!
        customer.getOrders().add(order);

        //也可以建立双向关系(让订单属于客户),但容易出现问题
        order.setCustomer(customer);

        //客户拥有订单时,会执行insert,insert,update
        //订单属于客户时,会执行insert,insert,没有update
        //如果让一的一方放弃外键维护则可以只输出2个insert语句

        //注意:如果让customer放弃维护,但此时只执行customer.getOrders().add(order)时,是不会创建外键的

        customerDao.save(customer);
        orderDao.save(order);

    }

    //测试级联删除;当cascade = {CascadeType.ALL}为不包含remove时,执行删除有外键的记录会报错
    @Test
    @Transactional
    @Rollback(false)
    void contextLoad1() {
        Customer customer = customerDao.getOne(3);
        customerDao.delete(customer);   //已设置为All
    }


    //一对多的查询
    @Test
    @Transactional
    void contextLoa3() {
        //因为find是延迟加载,需要在@OneToMany中添加fetch = FetchType.EAGER
        //或者添加事务(本案例选择此项)

        //通过对象导航查询,查询到customer下的order
//        List<Customer> all = customerDao.findAll();
//        for (Customer c : all) {
//            System.out.println(c.getOrders());
//        }

        //如果从多(order)查询一,默认会执行立即加载,sql语句为左外连接(?)
        //如果故意设置FetchType.LAZY则会进行延迟加载,先查询order,再根据id查询customer
        //演示略
        List<Order> allOrder = orderDao.findAll();
        for (Order order : allOrder) {
            System.out.println(order.getCustomer());
        }

        //概括
        //一查多:默认延迟
        //多查一:默认立即
    }


    //测试多对多
    @Test
    @Transactional
    @Rollback(false)
    void contextLoad2() {
        Player player = new Player();
        player.setPlayerName("大柱");

        Role role = new Role();
        role.setRoleName("大爷");

        //添加关系
        player.getRoles().add(role);
        //如果双方都拥有维护权,会导致主键冲突,程序报错
        //通常,让被动的一方放弃维护权(此处选择role)
        role.getPlayers().add(player);

        //保存时也可以设置一方的cascade进行级联操作
        playerDao.save(player);
        roleDao.save(role);
    }

    @Test
    @Transactional
    @Rollback(false)   //原本就是spring的注解,主要就是禁用测试环境中回滚的问题
    void contextLoad3() {
        Customer customer = customerDao.findById(1).isPresent()?customerDao.findById(1).get():null;
        if (customer!=null) {
            customer.setCustomerName("狗蛋蛋");
            customerDao.save(customer);
        }

    }

}
