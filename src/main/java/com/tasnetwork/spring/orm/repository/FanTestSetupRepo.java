package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DutMasterData;
import com.tasnetwork.spring.orm.model.FanTestSetup;


@Repository
public interface FanTestSetupRepo  extends JpaRepository<FanTestSetup, Long>{

	FanTestSetup findByDutMasterData(DutMasterData dutMasterData);

}
