package com.example.demo.init;

import com.example.demo.dao.CustomerDao;
import com.example.demo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


//用于测试开发环境不会进行回滚操作
//@Component
public class MyAppRunnable implements ApplicationRunner {


    @Autowired
    CustomerDao customerDao;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        doTest();
    }

    private void doTest() {
        Customer customer = customerDao.findById(1).isPresent()?customerDao.findById(1).get():null;
        if (customer!=null) {
            customer.setCustomerName("狗蛋蛋");
            customerDao.save(customer);
        }
    }
}
