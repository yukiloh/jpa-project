package com.example.demo.dao;

import com.example.demo.domain.Player;
import com.example.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleDao extends JpaRepository<Role,Integer>, JpaSpecificationExecutor<Role>{

}
