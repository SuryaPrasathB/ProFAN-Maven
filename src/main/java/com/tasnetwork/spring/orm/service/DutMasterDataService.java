package com.tasnetwork.spring.orm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.model.DutMasterData;
import com.tasnetwork.spring.orm.repository.DutMasterDataRepo;

@Component
public class DutMasterDataService {

	@Autowired
	private DutMasterDataRepo dutMasterDataRepo;
	
	
	@Transactional
	public Long saveToDb (DutMasterData data) {
		DutMasterData dutMasterData = dutMasterDataRepo.save(data);
		return dutMasterData.getId();
	}
	
	@Transactional
    public void deleteByModelName(String modelName) {
        DutMasterData data = dutMasterDataRepo.findByModelName(modelName);
        if (data != null) {
            dutMasterDataRepo.delete(data);
        }
    }
	
	@Transactional
	public DutMasterData findByModelName(String modelName) {
	    return dutMasterDataRepo.findByModelName(modelName);
	}
	
	@Transactional
	public List<DutMasterData> findAll () {
		List<DutMasterData> dutMasterDataList = dutMasterDataRepo.findAll();
		return dutMasterDataList;
	}
}
