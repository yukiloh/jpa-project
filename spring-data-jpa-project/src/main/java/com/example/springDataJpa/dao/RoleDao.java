package com.example.springDataJpa.dao;

import com.example.springDataJpa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleDao extends JpaRepository<Role,Integer>, JpaSpecificationExecutor<Role>{

}
