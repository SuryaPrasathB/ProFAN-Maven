package com.tasnetwork.spring.orm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.model.DutMasterData;
import com.tasnetwork.spring.orm.model.FanTestSetup;
import com.tasnetwork.spring.orm.repository.DutMasterDataRepo;
import com.tasnetwork.spring.orm.repository.FanTestSetupRepo;

@Component
public class FanTestSetupService {
	
	
	@Autowired
	private FanTestSetupRepo fanTestSetupRepo;
	
	
	@Transactional
	public Long saveToDb (FanTestSetup data) {
		FanTestSetup fanTestSetup = fanTestSetupRepo.save(data);
		return fanTestSetup.getId();
	}
	
	
	@Transactional
	public List<FanTestSetup> findAll () {
		List<FanTestSetup> fanTestSetupList = fanTestSetupRepo.findAll();
		return fanTestSetupList;
	}


	public FanTestSetup findByDutMasterData(DutMasterData dutMasterData) {
		return fanTestSetupRepo.findByDutMasterData(dutMasterData);
	}

}
