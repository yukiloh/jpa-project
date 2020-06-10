package com.example.springDataJpa.dao;

import com.example.springDataJpa.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerDao extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer>{

}
