package com.example.springDataJpa.dao;

import com.example.springDataJpa.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderDao extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order>{

}
