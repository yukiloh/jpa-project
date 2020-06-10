package com.example.springDataJpa.dao;

import com.example.springDataJpa.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerDao extends JpaRepository<Player,Integer>, JpaSpecificationExecutor<Player>{

}
