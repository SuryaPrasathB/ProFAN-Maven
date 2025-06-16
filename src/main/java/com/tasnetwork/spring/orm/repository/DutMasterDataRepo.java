package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DutMasterData;


@Repository
public interface DutMasterDataRepo extends JpaRepository<DutMasterData, Long>{

	// Spring Data JPA will auto-implement this based on method name
    DutMasterData findByModelName(String modelName);

    void deleteByModelName(String modelName); // This works if modelName is unique
		
	

}
