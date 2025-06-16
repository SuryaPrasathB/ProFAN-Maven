package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ConveyorDutSerialNo;

@Repository
public interface ConveyorDutSerialNoRepo extends JpaRepository<ConveyorDutSerialNo, Long>{

}
