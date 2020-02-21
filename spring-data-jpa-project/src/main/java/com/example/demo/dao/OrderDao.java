package com.example.demo.dao;

import com.example.demo.domain.Order;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderDao extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order>{

}
