package com.tasnetwork.spring.orm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DutCommand;

@Repository
public interface DutCommandRepo extends JpaRepository<DutCommand, Long>{

	Optional<DutCommand> findFirstByProjectNameAndTestCaseNameStartingWith(String projectName, String testCaseName);
	Optional<DutCommand> findFirstByProjectNameAndTestAliasId(String projectName, String testAliasId);
	
}
